package com.shynieke.statues.lootmodifiers;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.shynieke.statues.registry.StatueRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class SnifferLootModifier extends LootModifier {
	public static final Supplier<Codec<SnifferLootModifier>> CODEC = Suppliers.memoize(() ->
			RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, SnifferLootModifier::new)));

	public SnifferLootModifier(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@NotNull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (context.getRandom().nextInt(10) == 0)
			generatedLoot.add(new ItemStack(StatueRegistry.CORE_FLOWER_SEED.get()));

		return generatedLoot;
	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}