package com.shynieke.statues.items;

import com.shynieke.statues.registry.StatueRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class StatueBeeItem extends StatueBlockItem {
	public StatueBeeItem(Block blockIn, Properties builder) {
		super(blockIn, builder);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entityIn, int itemSlot, boolean isSelected) {
		if (stack.has(DataComponents.CUSTOM_NAME) && !level.isClientSide) {
			final String name = stack.getHoverName().getString();
			if (entityIn instanceof Player) {
				if (name.equalsIgnoreCase("Trans Bee")) {
					((Player) entityIn).getInventory().setItem(itemSlot, new ItemStack(StatueRegistry.TRANS_BEE_STATUE.toStack().getItemHolder(), stack.getCount(), stack.getComponentsPatch()));
				} else if (name.equalsIgnoreCase("Tropibee")) {
					((Player) entityIn).getInventory().setItem(itemSlot, new ItemStack(StatueRegistry.TROPIBEE.toStack().getItemHolder(), stack.getCount(), stack.getComponentsPatch()));
				}
			}
		}
	}
}
