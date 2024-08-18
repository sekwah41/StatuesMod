package com.shynieke.statues.client.model;

import com.shynieke.statues.entity.StatueBatEntity;
import net.minecraft.client.animation.definitions.BatAnimation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;

/**
 * Because vanilla BatModel doesn't allow any other class than BatEntity
 */
public class StatueBatModel extends HierarchicalModel<StatueBatEntity> {
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightWing;
	private final ModelPart leftWing;
	private final ModelPart rightWingTip;
	private final ModelPart leftWingTip;
	private final ModelPart feet;

	public StatueBatModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.root = root;
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.rightWing = this.body.getChild("right_wing");
		this.rightWingTip = this.rightWing.getChild("right_wing_tip");
		this.leftWing = this.body.getChild("left_wing");
		this.leftWingTip = this.leftWing.getChild("left_wing_tip");
		this.feet = this.body.getChild("feet");
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	/**
	 * Sets this entity's model rotation angles
	 */
	@Override
	public void setupAnim(StatueBatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if (entity.isResting()) {
			this.applyHeadRotation(netHeadYaw);
		}

		this.animate(entity.flyAnimationState, BatAnimation.BAT_FLYING, ageInTicks, 1.0F);
		this.animate(entity.restAnimationState, BatAnimation.BAT_RESTING, ageInTicks, 1.0F);
	}

	private void applyHeadRotation(float headRotation) {
		this.head.yRot = headRotation * (float) (Math.PI / 180.0);
	}
}
