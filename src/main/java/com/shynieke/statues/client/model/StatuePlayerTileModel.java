package com.shynieke.statues.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class StatuePlayerTileModel<T extends LivingEntity> extends PlayerModel<T> {
	public StatuePlayerTileModel(ModelPart part, boolean slim) {
		super(part, slim);
		this.hat.setPos(0.0F, -1.75F, 0.0F);
		this.rightSleeve.setPos(-5.0F, 2.0F, 0.0F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int p_102036_, int p_102037_, int p_350361_) {
		this.setAllVisible(true);
		this.hat.visible = true;
		this.jacket.visible = true;
		this.leftPants.visible = true;
		this.rightPants.visible = true;
		this.leftSleeve.visible = true;
		this.rightSleeve.visible = true;
		super.renderToBuffer(poseStack, vertexConsumer, p_102036_, p_102037_, p_350361_);
	}
}
