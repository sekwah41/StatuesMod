package com.shynieke.statues.util;

import com.shynieke.statues.recipe.LootRecipe;
import com.shynieke.statues.recipe.StatuesRecipes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class LootHelper {

	public static ItemStack getFloodBucket() {
		ItemStack floodBucket = new ItemStack(Items.WATER_BUCKET);
		floodBucket.set(DataComponents.CUSTOM_NAME, Component.literal("The Flood").withStyle(ChatFormatting.BLUE));

		return floodBucket;
	}

	public static RecipeHolder<LootRecipe> getMatchingLoot(Level level, ItemStack stack) {
		return level.getRecipeManager().getRecipeFor(StatuesRecipes.LOOT_RECIPE.get(), new SingleRecipeInput(stack), level).orElse(null);
	}

	public static boolean hasLoot(Level level, ItemStack stack) {
		return getMatchingLoot(level, stack) != null;
	}
}
