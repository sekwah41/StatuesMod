package com.shynieke.statues.blockentities;

import com.shynieke.statues.registry.StatueBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class TropicalFishBlockEntity extends StatueBlockEntity {
	private int MAIN_COLOR;
	private int SECONDARY_COLOR;

	public TropicalFishBlockEntity(BlockPos pos, BlockState state) {
		super(StatueBlockEntities.TROPICAL_FISH.get(), pos, state);
		this.MAIN_COLOR = 0;
		this.SECONDARY_COLOR = 0;
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		this.MAIN_COLOR = compound.getInt("MainColor");
		this.SECONDARY_COLOR = compound.getInt("SecondaryColor");
	}

	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.putInt("MainColor", MAIN_COLOR);
		compound.putInt("SecondaryColor", SECONDARY_COLOR);
	}

	public void scrambleColors() {
		if (level != null) {
			this.MAIN_COLOR = level.random.nextInt(16);
			this.SECONDARY_COLOR = level.random.nextInt(16);
		}
		setChanged();
	}

	public int getMainColor() {
		return MAIN_COLOR;
	}

	public int getSecondaryColor() {
		return SECONDARY_COLOR;
	}
}
