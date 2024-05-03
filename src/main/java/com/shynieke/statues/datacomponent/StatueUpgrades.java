package com.shynieke.statues.datacomponent;

import com.mojang.serialization.Codec;
import com.shynieke.statues.util.UpgradeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record StatueUpgrades(Map<String, Short> upgradeMap) {
	public static final StatueUpgrades EMPTY = new StatueUpgrades(Map.of());
	public static final Codec<StatueUpgrades> CODEC = Codec.unboundedMap(Codec.STRING, Codec.SHORT)
			.xmap(StatueUpgrades::new, StatueUpgrades::upgradeMap);
	public static final StreamCodec<RegistryFriendlyByteBuf, StatueUpgrades> STREAM_CODEC = StreamCodec.of(
			StatueUpgrades::toNetwork, StatueUpgrades::fromNetwork
	);

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

	public void upgrade(String id) {
		short level = upgradeMap.getOrDefault(id, (short) 0);
		upgradeMap.put(id, (short) (level + 1));
	}

	public void downgrade(String id) {
		short level = upgradeMap.getOrDefault(id, (short) 0);
		if (level > 0) {
			upgradeMap.put(id, (short) (level - 1));
		}
	}

	public int getUpgradeLevel(ItemStack stack, String id) {
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
