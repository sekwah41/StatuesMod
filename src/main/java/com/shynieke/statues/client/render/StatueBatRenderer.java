package com.shynieke.statues.client.render;

import com.shynieke.statues.Reference;
import com.shynieke.statues.client.model.StatueBatModel;
import com.shynieke.statues.entity.StatueBatEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class StatueBatRenderer extends MobRenderer<StatueBatEntity, StatueBatModel> {
	private static final ResourceLocation BAT_TEXTURES = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/entity/statue_bat.png");

	public StatueBatRenderer(Context context) {
		super(context, new StatueBatModel(context.bakeLayer(ModelLayers.BAT)), 0.25F);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	@Override
	public ResourceLocation getTextureLocation(StatueBatEntity statueBat) {
		return BAT_TEXTURES;
	}
}
