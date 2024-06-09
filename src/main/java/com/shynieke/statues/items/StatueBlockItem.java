package com.shynieke.statues.items;

import com.shynieke.statues.blocks.AbstractStatueBase;
import com.shynieke.statues.datacomponent.StatueStats;
import com.shynieke.statues.datacomponent.StatueUpgrades;
import com.shynieke.statues.registry.StatueDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class StatueBlockItem extends BlockItem {

	public StatueBlockItem(Block blockIn, Item.Properties builder) {
		super(blockIn, builder);
	}

	@Nullable
	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	public boolean isBaby() {
		return this.getBlock() instanceof AbstractStatueBase statueBase && statueBase.isBaby();
	}

	public boolean isHiddenStatue() {
		return this.getBlock() instanceof AbstractStatueBase statueBase && statueBase.isHiddenStatue();
	}

	public EntityType<?> getEntity() {
		return this.getBlock() instanceof AbstractStatueBase statueBase ? statueBase.getEntity() : null;
	}

	public boolean matchesEntity(LivingEntity livingEntity) {
		return getEntity() != null && getEntity() == livingEntity.getType() && livingEntity.isBaby() == isBaby();
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext context) {
		BlockState state = super.getPlacementState(context);
		ItemStack stack = context.getItemInHand();
		if (state != null && state.getBlock() instanceof AbstractStatueBase) {
			if (stack.has(StatueDataComponents.UPGRADED)) {
				if (stack.get(StatueDataComponents.UPGRADED)) {
					state = state.setValue(AbstractStatueBase.INTERACTIVE, true);
				}
			}
		}
		return state;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, components, tooltipFlag);
		if (stack.has(StatueDataComponents.STATS)) {
			StatueStats stats = stack.get(StatueDataComponents.STATS);
			components.add(Component.translatable("statues.info.level").withStyle(ChatFormatting.GOLD)
					.append(" ").append(
							Component.literal(String.valueOf(stats.level())).withStyle(ChatFormatting.YELLOW)
					)
			);
			components.add(Component.translatable("statues.info.kills").withStyle(ChatFormatting.GOLD)
					.append(" ").append(
							Component.literal(String.valueOf(stats.killCount())).withStyle(ChatFormatting.YELLOW)
					)
			);
			components.add(Component.translatable("statues.info.upgrade_slots").withStyle(ChatFormatting.GOLD)
					.append(" ").append(
							Component.literal(String.valueOf(stats.upgradeSlots())).withStyle(ChatFormatting.YELLOW)
					)
			);
		}


		StatueUpgrades upgrades = stack.getOrDefault(StatueDataComponents.UPGRADES, StatueUpgrades.empty());
		Map<String, Short> upgradeMap = upgrades.upgradeMap();
		if (!upgradeMap.isEmpty()) {
			components.addAll(upgrades.getUpgradeNames());
		}
	}
}
