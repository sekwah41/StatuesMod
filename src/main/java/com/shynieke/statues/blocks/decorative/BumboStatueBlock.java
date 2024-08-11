package com.shynieke.statues.blocks.decorative;

import com.shynieke.statues.blocks.AbstractBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BumboStatueBlock extends AbstractBaseBlock implements IDecorativeStatue {

	private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 16, 12);

	public BumboStatueBlock(Properties properties) {
		super(properties.sound(SoundType.STONE));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
