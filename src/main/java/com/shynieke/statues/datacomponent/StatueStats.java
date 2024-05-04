package com.shynieke.statues.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public final class StatueStats {
	public static final Codec<StatueStats> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					Codec.INT.optionalFieldOf("level", 0).forGetter(StatueStats::level),
					Codec.INT.optionalFieldOf("killCount", 0).forGetter(StatueStats::killCount),
					Codec.INT.optionalFieldOf("upgradeSlots", 0).forGetter(StatueStats::upgradeSlots))
			.apply(inst, StatueStats::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, StatueStats> STREAM_CODEC = StreamCodec.of(
			StatueStats::toNetwork, StatueStats::fromNetwork
	);
	private int level;
	private int killCount;
	private int upgradeSlots;

	public StatueStats(int level, int killCount, int upgradeSlots) {
		this.level = level;
		this.killCount = killCount;
		this.upgradeSlots = upgradeSlots;
	}

	public static StatueStats empty() {
		return new StatueStats(0, 0, 0);
	}

	private static StatueStats fromNetwork(RegistryFriendlyByteBuf byteBuf) {
		int level = byteBuf.readInt();
		int killCount = byteBuf.readInt();
		int upgradeSlots = byteBuf.readInt();
		return new StatueStats(level, killCount, upgradeSlots);
	}

	private static void toNetwork(RegistryFriendlyByteBuf byteBuf, StatueStats playerCompassData) {
		byteBuf.writeInt(playerCompassData.level());
		byteBuf.writeInt(playerCompassData.killCount());
		byteBuf.writeInt(playerCompassData.upgradeSlots());
	}

	public int level() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int killCount() {
		return killCount;
	}

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	public int upgradeSlots() {
		return upgradeSlots;
	}

	public void setUpgradeSlots(int upgradeSlots) {
		this.upgradeSlots = upgradeSlots;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (StatueStats) obj;
		return this.level == that.level &&
				this.killCount == that.killCount &&
				this.upgradeSlots == that.upgradeSlots;
	}

	@Override
	public int hashCode() {
		return Objects.hash(level, killCount, upgradeSlots);
	}

	@Override
	public String toString() {
		return "StatueStats[" +
				"level=" + level + ", " +
				"killCount=" + killCount + ", " +
				"upgradeSlots=" + upgradeSlots + ']';
	}

}
