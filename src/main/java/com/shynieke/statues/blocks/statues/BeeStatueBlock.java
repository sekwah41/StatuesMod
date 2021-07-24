package com.shynieke.statues.blocks.statues;

import com.shynieke.statues.recipes.StatueLootList;
import com.shynieke.statues.tiles.StatueBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BeeStatueBlock extends AngryBeeStatueBlock {
	private static final VoxelShape SHAPE = Block.box(4.0D, 6.0D, 4.0D, 12.0D, 11D, 12.0D);

	public BeeStatueBlock(Properties builder) {
		super(builder);
	}

	@Override
	public void executeStatueBehavior(StatueBlockEntity tile, BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult result) {
		tile.giveItem(StatueLootList.getLootInfo(getLootName()).getLoot(), playerIn);
//		tile.FakeMobs(new FakeZombie(worldIn), worldIn, pos, true);
	}

	@Override
	public String getLootName() {
		return "bee";
	}

	@Override
	public EntityType<?> getEntity() {
		return EntityType.BEE;
	}

	@Override
	public SoundEvent getSound(BlockState state) {
		return SoundEvents.BEE_LOOP;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
