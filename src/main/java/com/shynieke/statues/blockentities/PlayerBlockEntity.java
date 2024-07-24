package com.shynieke.statues.blockentities;

import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.shynieke.statues.Statues;
import com.shynieke.statues.blocks.statues.PlayerStatueBlock;
import com.shynieke.statues.registry.StatueBlockEntities;
import com.shynieke.statues.registry.StatueRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.Services;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;

public class PlayerBlockEntity extends BlockEntity implements Nameable {
	@Nullable
	private static GameProfileCache profileCache;
	@Nullable
	private static MinecraftSessionService sessionService;
	@Nullable
	private static Executor mainThreadExecutor;
	private static final Executor CHECKED_MAIN_THREAD_EXECUTOR = runnable -> {
		Executor executor = mainThreadExecutor;
		if (executor != null) {
			executor.execute(runnable);
		}
	};

	@Nullable
	private ResolvableProfile playerProfile;
	private boolean comparatorApplied;
	private boolean onlineChecking;
	private int checkerCooldown;

	public PlayerBlockEntity(BlockPos pos, BlockState state) {
		super(StatueBlockEntities.PLAYER.get(), pos, state);
		this.comparatorApplied = false;
		this.checkerCooldown = 0;
		this.onlineChecking = false;
	}

	public static void setup(GameProfileCache gameProfileCache, MinecraftSessionService service, Executor executor) {
		profileCache = gameProfileCache;
		sessionService = service;
		mainThreadExecutor = executor;
	}

	public static void setup(Services services, Executor executor) {
		setup(services.profileCache(), services.sessionService(), executor);
	}

	public static void clear() {
		profileCache = null;
		sessionService = null;
		mainThreadExecutor = null;
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider lookupProvider) {
		super.loadAdditional(compound, lookupProvider);

		if (compound.contains("profile")) {
			ResolvableProfile.CODEC
					.parse(NbtOps.INSTANCE, compound.get("profile"))
					.resultOrPartial(p_332637_ -> Statues.LOGGER.error("Failed to load profile from player statue: {}", p_332637_))
					.ifPresent(this::setPlayerProfile);
		}

		comparatorApplied = compound.getBoolean("comparatorApplied");
		onlineChecking = compound.getBoolean("OnlineChecking");
		checkerCooldown = compound.getInt("checkerCooldown");
	}

	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider lookupProvider) {
		super.saveAdditional(compound, lookupProvider);
		if (this.playerProfile != null) {
			ResolvableProfile.CODEC.encodeStart(NbtOps.INSTANCE, this.playerProfile)
					.resultOrPartial(Statues.LOGGER::error)
					.ifPresent(profile -> compound.put("profile", profile));
		}
		compound.putBoolean("comparatorApplied", comparatorApplied);
		compound.putBoolean("OnlineChecking", onlineChecking);
		compound.putInt("checkerCooldown", checkerCooldown);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
		CompoundTag compoundNBT = pkt.getTag();
		handleUpdateTag(compoundNBT, lookupProvider);
	}

	@Override
	public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
		super.handleUpdateTag(tag, lookupProvider);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider lookupProvider) {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt, lookupProvider);
		return nbt;
	}

	@Override
	public CompoundTag getPersistentData() {
		CompoundTag nbt = new CompoundTag();
		this.saveAdditional(nbt, VanillaRegistries.createLookup());
		return nbt;
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public boolean hasCustomName() {
		return this.playerProfile != null && !this.playerProfile.name().isEmpty();
	}

	@Nullable
	public ResolvableProfile getPlayerProfile() {
		return this.playerProfile;
	}

	public void setPlayerProfile(@Nullable ResolvableProfile profile) {
		synchronized (this) {
			this.playerProfile = profile;
		}

		this.updateOwnerProfile();
	}

	private void updateOwnerProfile() {
		if (this.playerProfile != null && !this.playerProfile.isResolved()) {
			this.playerProfile.resolve().thenAcceptAsync(profile -> {
				this.playerProfile = profile;
				this.setChanged();
			}, CHECKED_MAIN_THREAD_EXECUTOR);
		} else {
			this.setChanged();
		}
	}

	public void updateOnline() {
		BlockState state = getBlockState();
		boolean isStateOnline = state.getValue(PlayerStatueBlock.ONLINE);
		boolean checkAnswer = level.getPlayerByUUID(this.playerProfile.id().orElse(Util.NIL_UUID)) != null;
		if (isStateOnline != checkAnswer) {
			BlockState newState = state.setValue(PlayerStatueBlock.ONLINE, checkAnswer);
			level.setBlockAndUpdate(getBlockPos(), newState);
			level.sendBlockUpdated(getBlockPos(), state, newState, 3);
		}
	}


	public void setComparatorApplied(boolean comparatorApplied) {
		this.comparatorApplied = comparatorApplied;
		if (!comparatorApplied) {
			BlockState state = getBlockState();
			BlockState newState = state.setValue(PlayerStatueBlock.ONLINE, false);
			level.setBlockAndUpdate(getBlockPos(), newState);
			level.sendBlockUpdated(getBlockPos(), state, newState, 3);
		}
		this.setChanged();
	}

	public boolean getComparatorApplied() {
		return comparatorApplied;
	}

	public int getCooldown() {
		return this.checkerCooldown;
	}

	public void setOnlineChecking(boolean onlineChecking) {
		this.onlineChecking = onlineChecking;
		this.setChanged();
	}

	@Override
	public Component getName() {
		return this.hasCustomName() ? Component.literal(this.playerProfile != null ?
				playerProfile.name().orElse("") : "") :
				Component.translatable("entity.statues.player_statue");
	}

	@Nullable
	@Override
	public Component getCustomName() {
		return null;
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, PlayerBlockEntity blockEntity) {
		if (level != null) {
			if (state.is(StatueRegistry.PLAYER_STATUE.get()) && blockEntity.comparatorApplied) {
				if (!blockEntity.onlineChecking) {
					blockEntity.checkerCooldown++;
					blockEntity.setChanged();
					if (blockEntity.checkerCooldown == 0)
						blockEntity.checkerCooldown = 200;

					if (blockEntity.checkerCooldown >= 200) {
						blockEntity.checkerCooldown = 0;
						blockEntity.setOnlineChecking(true);
					}
				} else {
					blockEntity.updateOnline();
					blockEntity.setOnlineChecking(false);
				}
			}
		}
	}

	@Override
	protected void applyImplicitComponents(BlockEntity.DataComponentInput input) {
		super.applyImplicitComponents(input);
		this.setPlayerProfile(input.get(DataComponents.PROFILE));
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(DataComponents.PROFILE, this.playerProfile);
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		super.removeComponentsFromTag(tag);
		tag.remove("tag");
	}
}
