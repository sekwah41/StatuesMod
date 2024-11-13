package com.shynieke.statues.compat.jei.category;

import com.shynieke.statues.compat.jei.JEIPlugin;
import com.shynieke.statues.datacomponent.StatueStats;
import com.shynieke.statues.recipe.UpgradeRecipe;
import com.shynieke.statues.recipe.UpgradeType;
import com.shynieke.statues.registry.StatueDataComponents;
import com.shynieke.statues.registry.StatueRegistry;
import com.shynieke.statues.registry.StatueTags;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UpgradeCategory implements IRecipeCategory<UpgradeRecipe> {
	private final IDrawable background;
	private final IDrawable icon;
	private final Component localizedName;

	public UpgradeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(JEIPlugin.UPGRADE_BACKGROUND, 0, 0, 148, 62);
		this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(StatueRegistry.STATUE_TABLE.get()));
		this.localizedName = Component.translatable("statues.gui.jei.category.upgrade");
	}

	@Override
	public RecipeType<UpgradeRecipe> getRecipeType() {
		return JEIPlugin.UPGRADE_TYPE;
	}

	@SuppressWarnings("removal")
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, UpgradeRecipe recipe, IFocusGroup focuses) {
		UpgradeTooltipCallback callback = new UpgradeTooltipCallback(recipe);
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		if (level == null) {
			throw new NullPointerException("level must not be null.");
		}
		RegistryAccess registryAccess = level.registryAccess();

		final Optional<IFocus<?>> focused = focuses.getAllFocuses().stream().findFirst();
		final int tier = recipe.getTier();
		if (focused.isPresent() && focused.get().getTypedValue().getIngredient() instanceof ItemStack focusStack &&
				recipe.getCenter().test(focusStack)) {
			ItemStack centerStack = focusStack.copy();
			//Make sure if the center is focused that it doesn't scroll through more stacks
			fillInTag(centerStack, recipe);
			if (recipe.getUpgradeType() == UpgradeType.UNGLOWING) {
				UpgradeType.GLOWING.apply(centerStack, -1);
			}
			if (tier > -1) {
				for (int i = 0; i < tier; i++) {
					recipe.getUpgradeType().apply(centerStack, i);
				}
			}
			if (recipe.getUpgradeType() == UpgradeType.UPGRADE) {
				setUpgradeSlots(centerStack, 0);
			} else {
				setUpgradeSlots(centerStack, 1);
			}
			builder.addSlot(RecipeIngredientRole.INPUT, 73, 23).addItemStack(centerStack);

			if (recipe.getResultItem(registryAccess).isEmpty()) {
				ItemStack outputStack = centerStack.copy();
				recipe.getUpgradeType().apply(outputStack, tier);
				if (recipe.getUpgradeType() == UpgradeType.UPGRADE) {
					setUpgradeSlots(outputStack, 0);
				} else {
					setUpgradeSlots(outputStack, 1);
				}
				builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 23).addItemStack(outputStack).addRichTooltipCallback(callback);
			} else {
				builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 23).addItemStack(recipe.getResultItem(registryAccess)).addRichTooltipCallback(callback);
			}
		} else {
			List<ItemStack> centerList = new ArrayList<>();
			for (ItemStack centerStack : recipe.getCenter().getItems()) {
				ItemStack stack = centerStack.copy();
				fillInTag(stack, recipe);
				if (recipe.getUpgradeType() == UpgradeType.UNGLOWING) {
					UpgradeType.GLOWING.apply(stack, -1);
				}
				if (tier > -1) {
					for (int i = 0; i < tier; i++) {
						recipe.getUpgradeType().apply(stack, i);
					}
				}
				if (recipe.getUpgradeType() == UpgradeType.UPGRADE) {
					setUpgradeSlots(stack, 0);
				} else {
					setUpgradeSlots(stack, 1);
				}
				centerList.add(stack);
			}
			builder.addSlot(RecipeIngredientRole.INPUT, 73, 23).addItemStacks(centerList);

			if (recipe.getResultItem(registryAccess).isEmpty()) {
				List<ItemStack> stackList = new ArrayList<>();
				for (ItemStack centerStack : centerList) {
					ItemStack stack = centerStack.copy();
					recipe.getUpgradeType().apply(stack, tier);
					if (recipe.getUpgradeType() == UpgradeType.UPGRADE) {
						setUpgradeSlots(stack, 1);
					} else {
						setUpgradeSlots(stack, 0);
					}
					stackList.add(stack);
				}
				builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 23).addItemStacks(stackList).addRichTooltipCallback(callback);
			} else {
				builder.addSlot(RecipeIngredientRole.OUTPUT, 127, 23).addItemStack(recipe.getResultItem(registryAccess)).addRichTooltipCallback(callback);
			}
		}

		builder.addSlot(RecipeIngredientRole.CATALYST, 1, 41).addIngredients(recipe.requiresCore() ? Ingredient.of(StatueTags.STATUE_CORE) : Ingredient.EMPTY);
		List<Ingredient> catalysts = recipe.getCatalysts();
		if (catalysts.size() > 0) {
			builder.addSlot(RecipeIngredientRole.CATALYST, 55, 5).addIngredients(catalysts.get(0));
			if (catalysts.size() > 1) {
				builder.addSlot(RecipeIngredientRole.CATALYST, 91, 5).addIngredients(catalysts.get(1));
				if (catalysts.size() > 2) {
					builder.addSlot(RecipeIngredientRole.CATALYST, 55, 41).addIngredients(catalysts.get(2));
					if (catalysts.size() > 3) {
						builder.addSlot(RecipeIngredientRole.CATALYST, 91, 41).addIngredients(catalysts.get(3));
					}
				}
			}
		}
	}

	private void fillInTag(ItemStack stack, UpgradeRecipe recipe) {
		int tier = recipe.getTier();
		int actualTier = tier == -1 ? recipe.getUpgradeType() == UpgradeType.UPGRADE ? 0 : 1 : tier + 1;
		StatueStats stats = new StatueStats(actualTier, actualTier * 10, 20);
		stack.set(StatueDataComponents.STATS, stats);
		stack.set(StatueDataComponents.UPGRADED, true);
	}

	private void setUpgradeSlots(ItemStack stack, int count) {
		StatueStats stats = stack.getOrDefault(StatueDataComponents.STATS, StatueStats.empty());
		stack.set(StatueDataComponents.STATS, new StatueStats(stats.level(), count, stats.killCount()));
	}

	@Override
	public void draw(UpgradeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
	}

	public static class UpgradeTooltipCallback implements IRecipeSlotRichTooltipCallback {
		public final MutableComponent component;

		public UpgradeTooltipCallback(UpgradeRecipe recipe) {
			this.component = recipe.getUpgradeName();
		}

		@Override
		public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
			tooltip.add(component);
		}
	}

	@Override
	public Component getTitle() {
		return localizedName;
	}
}
