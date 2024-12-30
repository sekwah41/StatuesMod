package com.shynieke.statues.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.shynieke.statues.client.ClientHandler;
import com.shynieke.statues.client.model.PlayerStatueModel;
import com.shynieke.statues.entity.PlayerStatue;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.component.ResolvableProfile;

public class PlayerStatueRenderer extends LivingEntityRenderer<PlayerStatue, PlayerStatueModel> {
	private final PlayerStatueModel playerModel;
	private final PlayerStatueModel slimPlayerModel;
	public static final ResourceLocation defaultTexture = DefaultPlayerSkin.getDefaultTexture();
	public boolean isSlim = false;

	public PlayerStatueRenderer(EntityRendererProvider.Context context) {
		this(context, false);
	}

	public PlayerStatueRenderer(EntityRendererProvider.Context context, boolean slim) {
		super(context, new PlayerStatueModel(context.bakeLayer(ModelLayers.PLAYER), slim), 0.0F);
		this.playerModel = new PlayerStatueModel(context.bakeLayer(ModelLayers.PLAYER), false);
		this.slimPlayerModel = new PlayerStatueModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);

		this.addLayer(new HumanoidArmorLayer<>(this,
				new HumanoidModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_INNER_ARMOR : ModelLayers.PLAYER_INNER_ARMOR)),
				new HumanoidModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM_OUTER_ARMOR : ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		this.addLayer(new ElytraLayer<>(this, context.getModelSet()));
		this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(PlayerStatue playerStatue) {
		return playerStatue.getGameProfile()
				.map(this::getSkin)
				.orElse(defaultTexture);
	}

	private ResourceLocation getSkin(ResolvableProfile resolvableProfile) {
		SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
		if (resolvableProfile != null)
			return skinmanager.getInsecureSkin(resolvableProfile.gameProfile()).texture();
		else
			return defaultTexture;
	}

	@Override
	public void render(PlayerStatue playerStatue, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
		SkinManager skinmanager = Minecraft.getInstance().getSkinManager();
		if (playerStatue.getGameProfile().isPresent()) {
			if (isSlim != skinmanager.getInsecureSkin(playerStatue.getGameProfile().get().gameProfile()).model().id().equals("slim"))
				isSlim = !isSlim;
		}

		this.model = isSlim ? this.slimPlayerModel : playerModel;
		poseStack.translate(0, playerStatue.getYOffsetData(), 0);
		if (playerStatue.clientLock > 0) {
			playerStatue.xRotO = playerStatue.yBodyRot;
			playerStatue.yHeadRotO = playerStatue.yHeadRot;
		}
		super.render(playerStatue, entityYaw, partialTicks, poseStack, bufferSource, isSupporter(playerStatue) ? 15728880 : packedLightIn);
	}

	@Override
	protected boolean shouldShowName(PlayerStatue playerStatue) {
		return playerStatue.isCustomNameVisible();
	}

	@Override
	protected void setupRotations(PlayerStatue playerStatue, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, float scale) {
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
		float f = (float) (playerStatue.level().getGameTime() - playerStatue.punchCooldown) + partialTicks;
		if (f < 5.0F) {
			poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f / 1.5F * (float) Math.PI) * 3.0F));
		}

		if (isPlayerUpsideDown(playerStatue)) {
			poseStack.translate(0.0D, (double) (playerStatue.getBbHeight() + 0.1F), 0.0D);
			poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
		}
	}

	@Override
	protected void scale(PlayerStatue playerStatue, PoseStack poseStack, float partialTickTime) {
		float f = 0.9375F;
		poseStack.scale(f, f, f);
	}

	public static boolean isPlayerUpsideDown(PlayerStatue playerStatue) {
		if (playerStatue.getGameProfile().isPresent()) {
			ResolvableProfile profile = playerStatue.getGameProfile().get();
			String s = ChatFormatting.stripFormatting(profile.name().orElse("steve"));
			return "Dinnerbone".equals(s) || "Grumm".equals(s);
		}

		return false;
	}

	public static boolean isSupporter(PlayerStatue playerStatue) {
		if (playerStatue.getGameProfile().isPresent()) {
			ResolvableProfile profile = playerStatue.getGameProfile().get();
			return ClientHandler.SUPPORTER.contains(profile.id().orElse(Util.NIL_UUID));
		}

		return false;
	}
}
