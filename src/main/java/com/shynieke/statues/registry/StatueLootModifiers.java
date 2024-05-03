package com.shynieke.statues.registry;

import com.mojang.serialization.MapCodec;
import com.shynieke.statues.Reference;
import com.shynieke.statues.lootmodifiers.CityStatuesLootModifier;
import com.shynieke.statues.lootmodifiers.SherdLootModifier;
import com.shynieke.statues.lootmodifiers.SnifferLootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class StatueLootModifiers {
	public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Reference.MOD_ID);

	public static final Supplier<MapCodec<? extends IGlobalLootModifier>> STATUES_CITY_LOOT = GLM.register("statues_city_loot", CityStatuesLootModifier.CODEC);
	public static final Supplier<MapCodec<? extends IGlobalLootModifier>> STATUES_SHERD = GLM.register("statues_sherd", SherdLootModifier.CODEC);
	public static final Supplier<MapCodec<? extends IGlobalLootModifier>> STATUES_CORE_FLOWER = GLM.register("statues_core_flower", SnifferLootModifier.CODEC);
}
