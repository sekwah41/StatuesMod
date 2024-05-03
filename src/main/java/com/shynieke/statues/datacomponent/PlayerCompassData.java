package com.shynieke.statues.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record PlayerCompassData(BlockPos pos, String name) {
	public static final Codec<PlayerCompassData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					BlockPos.CODEC.fieldOf("pos").forGetter(PlayerCompassData::pos),
					Codec.STRING.fieldOf("name").forGetter(PlayerCompassData::name))
			.apply(inst, PlayerCompassData::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCompassData> STREAM_CODEC = StreamCodec.of(
			PlayerCompassData::toNetwork, PlayerCompassData::fromNetwork
	);

	private static PlayerCompassData fromNetwork(RegistryFriendlyByteBuf byteBuf) {
		BlockPos pos = BlockPos.STREAM_CODEC.decode(byteBuf);
		String name = byteBuf.readUtf(32767);
		return new PlayerCompassData(pos, name);
	}

	private static void toNetwork(RegistryFriendlyByteBuf byteBuf, PlayerCompassData playerCompassData) {
		BlockPos.STREAM_CODEC.encode(byteBuf, playerCompassData.pos());
		byteBuf.writeUtf(playerCompassData.name());
	}
}
