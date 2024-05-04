package com.shynieke.statues.blocks.statues;

import com.mojang.authlib.GameProfile;
import com.shynieke.statues.blockentities.PlayerBlockEntity;
import com.shynieke.statues.blocks.AbstractBaseBlock;
import com.shynieke.statues.config.StatuesConfig;
import com.shynieke.statues.datacomponent.PlayerCompassData;
import com.shynieke.statues.entity.PlayerStatue;
import com.shynieke.statues.items.PlayerStatueSpawnItem;
import com.shynieke.statues.registry.StatueBlockEntities;
import com.shynieke.statues.registry.StatueDataComponents;
import com.shynieke.statues.registry.StatueRegistry;
import com.shynieke.statues.registry.StatueTags;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerStatueBlock extends AbstractBaseBlock {
	private static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	public static final BooleanProperty ONLINE = BooleanProperty.create("online");

	public PlayerStatueBlock(Properties builder) {
		super(builder.sound(SoundType.STONE));
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE).setValue(ONLINE, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlayerBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createStatueTicker(level, blockEntityType, StatueBlockEntities.PLAYER.get());
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createStatueTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends PlayerBlockEntity> blockEntityType1) {
		return level.isClientSide ? null : createTickerHelper(blockEntityType, blockEntityType1, PlayerBlockEntity::serverTick);
	}

	private PlayerBlockEntity getBE(Level level, BlockPos pos) {
		return (PlayerBlockEntity) level.getBlockEntity(pos);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity be, ItemStack stack) {
		if (be instanceof PlayerBlockEntity blockEntity && ((Nameable) be).hasCustomName()) {
			player.causeFoodExhaustion(0.005F);

			if (level.isClientSide)
				return;

			if (this == Blocks.AIR)
				return;

			ItemStack itemstack = new ItemStack(this);
			itemstack.set(DataComponents.CUSTOM_NAME, ((Nameable) blockEntity).getName());

			blockEntity.saveToItem(itemstack, level.registryAccess());

			popResource(level, pos, itemstack);

			if (blockEntity.getComparatorApplied()) {
				popResource(level, pos, new ItemStack(Blocks.COMPARATOR.asItem()));
			}
		} else {
			super.playerDestroy(level, player, pos, state, null, stack);
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean p_196243_5_) {
		if (state.hasBlockEntity() && newState.getBlock() != StatueRegistry.PLAYER_STATUE.get()) {
			level.removeBlockEntity(pos);
		}
	}

//	@Override
//	public ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
//		BlockEntity blockEntity = level.getBlockEntity(pos);
//		if (blockEntity instanceof PlayerBlockEntity) {
//			return getStatueWithName(level, pos, state);
//		} else {
//			return new ItemStack(state.getBlock());
//		}
//	}

//	@Override
//	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
//		BlockEntity blockEntity = level.getBlockEntity(pos);
//		if (blockEntity instanceof PlayerBlockEntity) {
//			return getStatueWithName(level, pos, state);
//		} else {
//			return super.getCloneItemStack(state, target, level, pos, player);
//		}
//	}
//
//	private ItemStack getStatueWithName(BlockGetter level, BlockPos pos, BlockState state) {
//		BlockEntity blockEntity = level.getBlockEntity(pos);
//		if (blockEntity instanceof PlayerBlockEntity playerBlockEntity) {
//			ItemStack stack = new ItemStack(state.getBlock());
//
//			ResolvableProfile resolvableProfile = playerBlockEntity.getPlayerProfile();
//			if (resolvableProfile != null) {
//				CompoundTag tag = new CompoundTag();
//
//				if (!StringUtil.isNullOrEmpty(resolvableProfile.name())) {
//					PlayerBlockEntity.resolveGameProfile(tag, resolvableProfile.getName());
//				}
//				stack.setTag(tag);
//			}
//
//			return stack.setHoverName(playerBlockEntity.getName());
//		} else {
//			return new ItemStack(state.getBlock());
//		}
//	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state.setValue(ONLINE, false), placer, stack);

		if (!level.isClientSide && getBE(level, pos) != null) {
			PlayerBlockEntity playerBlockEntity = getBE(level, pos);
			if (stack.has(DataComponents.CUSTOM_NAME)) {
				String stackName = stack.getHoverName().getString();
				boolean spaceFlag = stackName.contains(" ");
				boolean emptyFlag = stackName.isEmpty();

				if (!spaceFlag && !emptyFlag) {
					GameProfile newProfile = new GameProfile(Util.NIL_UUID, stackName);

					if (stack.has(DataComponents.PROFILE)) {
						ResolvableProfile profile = stack.get(DataComponents.PROFILE);
						if (profile != null && profile.name().isPresent() && profile.name().get().equalsIgnoreCase(stackName)) {
							newProfile = profile.gameProfile();
						}
					}

					playerBlockEntity.setPlayerProfile(new ResolvableProfile(newProfile));
				}
			} else {
				if (placer instanceof Player player) {
					playerBlockEntity.setPlayerProfile(new ResolvableProfile(player.getGameProfile()));
				} else {
					playerBlockEntity.setPlayerProfile(new ResolvableProfile(new GameProfile(Util.NIL_UUID, "steve")));
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
		if (Screen.hasShiftDown()) {
			MutableComponent userComponent = Component.literal("Username: ").withStyle(ChatFormatting.GOLD);
			userComponent.append(stack.getHoverName().plainCopy().withStyle(ChatFormatting.WHITE));
			components.add(userComponent);

			if (stack.has(DataComponents.PROFILE)) {
				ResolvableProfile profile = stack.get(DataComponents.PROFILE);
				profile.id().ifPresent((id) -> {
					MutableComponent UUIDComponent = Component.literal("UUID: ").withStyle(ChatFormatting.GOLD);
					UUIDComponent.append(Component.literal(id.toString()).withStyle(ChatFormatting.WHITE));
					components.add(UUIDComponent);
				});
			}
		}
		super.appendHoverText(stack, context, components, flag);
	}

	@Override
	public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
		if (blockState.getValue(ONLINE))
			return 15;
		return 0;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return state.getValue(ONLINE);
	}

	@Override
	public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> blockStateBuilder) {
		blockStateBuilder.add(FACING, WATERLOGGED, ONLINE);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player playerIn, InteractionHand hand, BlockHitResult result) {
		ResolvableProfile tileProfile = getBE(level, pos).getPlayerProfile();
		PlayerBlockEntity playerBlockEntity = getBE(level, pos);
		if (!level.isClientSide && playerBlockEntity != null && tileProfile != null) {
			String playerName = tileProfile.name().orElse("Unknown");
			UUID id = tileProfile.id().orElse(null);
			boolean onlineFlag = id != null && level.getPlayerByUUID(tileProfile.id().get()) != null;

			if (playerIn.isShiftKeyDown()) {
				if (playerBlockEntity.getComparatorApplied()) {
					playerBlockEntity.setComparatorApplied(false);
					ItemStack comparatorStack = new ItemStack(Items.COMPARATOR);
					if (!playerIn.addItem(comparatorStack)) {
						level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 0.5, pos.getZ(), comparatorStack));
					}
				}
				return ItemInteractionResult.SUCCESS;
			} else {
				if (StatuesConfig.COMMON.playerCompass.get()) {
					if (stack.getItem() == Items.COMPASS || stack.getItem() == StatueRegistry.PLAYER_COMPASS.get()) {
						boolean isPlayerCompass = stack.getItem() == StatueRegistry.PLAYER_COMPASS.get();
						if (onlineFlag) {
							ItemStack playerCompass = isPlayerCompass ? stack : new ItemStack(StatueRegistry.PLAYER_COMPASS.get());
							CompoundTag locationTag = new CompoundTag();

							Player player = level.getPlayerByUUID(id);
							if (player != null && player.level().dimension().location().equals(playerIn.level().dimension().location())) {
								BlockPos playerPos = player.blockPosition();
								playerCompass.set(StatueDataComponents.PLAYER_COMPASS_DATA.get(), new PlayerCompassData(playerPos, tileProfile.name().orElse("Unknown")));

								if (!isPlayerCompass) {
									if (!playerIn.getAbilities().instabuild) {
										stack.shrink(1);
									}
									if (stack.isEmpty()) {
										playerIn.setItemInHand(hand, playerCompass);
									} else if (!playerIn.getInventory().add(playerCompass)) {
										playerIn.drop(playerCompass, false);
									}
								}
							} else {
								playerIn.sendSystemMessage(Component.translatable("statues.player.compass.dimension.failure", ChatFormatting.GOLD + playerName));
							}

						} else {
							playerIn.sendSystemMessage(Component.translatable("statues.player.compass.offline", ChatFormatting.GOLD + playerName));
						}
						return ItemInteractionResult.SUCCESS;
					}
					if (stack.getItem() == Items.COMPARATOR) {
						if (!playerBlockEntity.getComparatorApplied()) {
							if (!playerIn.getAbilities().instabuild) {
								stack.shrink(1);
							}
							playerBlockEntity.setComparatorApplied(true);
							playerBlockEntity.updateOnline();
							return ItemInteractionResult.SUCCESS;
						}
					}
					if (stack.is(StatueTags.PLAYER_UPGRADE_ITEM)) {
						if (level instanceof ServerLevel serverLevel) {
							Consumer<PlayerStatue> consumer = EntityType.appendCustomEntityStackConfig((p_263581_) -> {
							}, serverLevel, stack, playerIn);
							PlayerStatue playerStatueEntity = StatueRegistry.PLAYER_STATUE_ENTITY.get().create(serverLevel, consumer, pos, MobSpawnType.SPAWN_EGG, true, true);
							if (playerStatueEntity == null) {
								return ItemInteractionResult.FAIL;
							}

							serverLevel.addFreshEntityWithPassengers(playerStatueEntity);
							float f = (float) Mth.floor((Mth.wrapDegrees(playerIn.getYRot() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
							playerStatueEntity.setGameProfile(playerBlockEntity.getPlayerProfile());
							playerStatueEntity.moveTo(playerStatueEntity.getX(), playerStatueEntity.getY(), playerStatueEntity.getZ(), f, 0.0F);
							PlayerStatueSpawnItem.applyRandomRotations(playerStatueEntity, level.random);
							level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
							level.addFreshEntity(playerStatueEntity);
							level.playSound((Player) null, playerStatueEntity.getX(), playerStatueEntity.getY(), playerStatueEntity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);

							if (!playerIn.getAbilities().instabuild) {
								stack.shrink(1);
							}
						}
					}
				}
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING))).setValue(ONLINE, false);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING))).setValue(ONLINE, false);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
		super.animateTick(state, level, pos, randomSource);

		if (level.isClientSide) {
			PlayerBlockEntity playerBlockEntity = getBE(level, pos);
			if (playerBlockEntity != null && playerBlockEntity.getPlayerProfile() != null &&
					com.shynieke.statues.client.ClientHandler.TRANSLATORS.contains(playerBlockEntity.getPlayerProfile().id().orElse(null))) {
				level.addParticle(ParticleTypes.ENCHANT,
						(double) pos.getX() + 0.5D, (double) pos.getY() + 2.0D, (double) pos.getZ() + 0.5D,
						(double) ((float) (level.random.nextFloat() - 0.5) * 3 + randomSource.nextFloat()) - 0.5D,
						(double) ((float) (level.random.nextFloat() - 0.5) * 3 - randomSource.nextFloat() - 1.0F),
						(double) ((float) (level.random.nextFloat() - 0.5) * 3 + randomSource.nextFloat()) - 0.5D);
			}
		}
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
}