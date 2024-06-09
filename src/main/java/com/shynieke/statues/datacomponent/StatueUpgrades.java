package com.shynieke.statues.datacomponent;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record StatueUpgrades(Map<String, Short> upgradeMap) {
	public static final StatueUpgrades EMPTY = new StatueUpgrades(new HashMap<>());
	public static final Codec<StatueUpgrades> CODEC = Codec.unboundedMap(Codec.STRING, Codec.SHORT)
			.xmap(StatueUpgrades::new, StatueUpgrades::upgradeMap);
	public static final StreamCodec<RegistryFriendlyByteBuf, StatueUpgrades> STREAM_CODEC = StreamCodec.of(
			StatueUpgrades::toNetwork, StatueUpgrades::fromNetwork
	);

	public static StatueUpgrades empty() {
		return new StatueUpgrades(Map.of());
	}

	private static StatueUpgrades fromNetwork(RegistryFriendlyByteBuf byteBuf) {
		return new StatueUpgrades(byteBuf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readShort));
	}

	private static void toNetwork(RegistryFriendlyByteBuf byteBuf, StatueUpgrades playerCompassData) {
		Map<String, Short> upgrades = playerCompassData.upgradeMap();
		byteBuf.writeVarInt(upgrades.size());
		upgrades.forEach((string, level) -> {
			byteBuf.writeUtf(string);
			byteBuf.writeShort(level);
		});
	}

	public Map<String, Short> withUpgrade(String id) {
		Map<String, Short> newMap = new HashMap<>(upgradeMap);
		short level = newMap.getOrDefault(id, (short) 0);
		newMap.put(id, (short) (level + 1));
		return newMap;
	}

	public Map<String, Short> withDowngrade(String id) {
		Map<String, Short> newMap = new HashMap<>(upgradeMap);
		short level = newMap.getOrDefault(id, (short) 0);
		if (level > 0) {
			newMap.put(id, (short) (level - 1));
		}
		return newMap;
	}

	public int getUpgradeLevel(String id) {
		return upgradeMap == null ? -1 : upgradeMap.getOrDefault(id, (short) 0);
	}

	public MutableComponent getUpgradeName(String id, int level) {
		String descriptionID = "statues.upgrade." + id + ".name";
		MutableComponent mutablecomponent = Component.translatable(descriptionID).withStyle(ChatFormatting.GRAY);

		if (level > 0) {
			mutablecomponent.append(" ").append(Component.translatable("enchantment.level." + level));
		}

		return mutablecomponent;
	}

	public List<Component> getUpgradeNames() {
		List<Component> components = new ArrayList<>();
		for (Map.Entry<String, Short> entry : upgradeMap.entrySet()) {
			components.add(getUpgradeName(entry.getKey(), (int) entry.getValue()));
		}

		return components;
	}
}
