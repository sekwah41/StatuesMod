package com.shynieke.statues.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class StatueCoreItem extends Item {

	public StatueCoreItem(Item.Properties builder) {
		super(builder);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
	}
}
