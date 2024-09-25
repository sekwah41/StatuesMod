package com.shynieke.statues.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.shynieke.statues.blockentities.PlayerBlockEntity;
import com.shynieke.statues.blocks.statues.PlayerStatueBlock;
import com.shynieke.statues.client.ClientHandler;
import com.shynieke.statues.client.model.StatuePlayerTileModel;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class PlayerBER implements BlockEntityRenderer<PlayerBlockEntity> {
	private final StatuePlayerTileModel model;
	private final StatuePlayerTileModel slimModel;
	public boolean isSlim = false;

	public static final ResourceLocation defaultTexture = DefaultPlayerSkin.getDefaultTexture();

	public PlayerBER(BlockEntityRendererProvider.Context context) {
		this.model = new StatuePlayerTileModel(context.bakeLayer(ClientHandler.PLAYER_STATUE), false);
		this.slimModel = new StatuePlayerTileModel(context.bakeLayer(ClientHandler.PLAYER_STATUE_SLIM), true);
	}

	@Override
	public void render(PlayerBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
		BlockState blockstate = blockEntity.getBlockState();
		boolean flag = blockstate.getBlock() instanceof PlayerStatueBlock;
		Direction direction = flag ? blockstate.getValue(PlayerStatueBlock.FACING) : Direction.UP;
		ResolvableProfile resolvableProfile = blockEntity.getPlayerProfile();

		if (resolvableProfile != null) {
			SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
			if (isSlim != skinmanager.getInsecureSkin(resolvableProfile.gameProfile()).model().id().equals("slim"))
				isSlim = !isSlim;
		}

		render(direction, resolvableProfile, poseStack, bufferSource, combinedLightIn, partialTicks);
	}

	public void render(@Nullable Direction direction, @Nullable ResolvableProfile profile, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, float partialTicks) {
		poseStack.translate(0.5D, 0.25D, 0.5D);
		poseStack.pushPose();
		if (direction != null) {
			switch (direction) {
				case NORTH:
					break;
				case SOUTH:
					poseStack.mulPose(Axis.YP.rotationDegrees(180));
					break;
				case WEST:
					poseStack.mulPose(Axis.YP.rotationDegrees(90));
					break;
				default:
					poseStack.mulPose(Axis.YP.rotationDegrees(270));
			}
		}
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.translate(0.0D, -1.25D, 0.0D);

		boolean isSupporter = false;
		if (profile != null) {
			final String s = ChatFormatting.stripFormatting(profile.name().orElse("Steve"));
			if ("Dinnerbone".equalsIgnoreCase(s) || "Grumm".equalsIgnoreCase(s)) {
				poseStack.translate(0.0D, (double) (1.85F), 0.0D);
				poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
			}
			isSupporter = ClientHandler.SUPPORTER.contains(profile.id().orElse(Util.NIL_UUID));
		}

		int light = isSupporter ? 15728880 : combinedLight;
		VertexConsumer vertexConsumer = bufferSource.getBuffer(getRenderType(profile));
		StatuePlayerTileModel playerModel = isSlim ? slimModel : model;

		playerModel.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);

		poseStack.popPose();
	}

	public static RenderType getRenderType(@Nullable ResolvableProfile resolvableProfile) {
		if (resolvableProfile == null)
			return RenderType.entityTranslucent(defaultTexture);
		SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
		return RenderType.entityTranslucent(skinmanager.getInsecureSkin(resolvableProfile.gameProfile()).texture());
	}
}
