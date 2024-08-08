/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.mooshroom;

import com.google.gson.JsonObject;
import com.mclegoman.perspective.client.entity.TexturedEntity;
import com.mclegoman.perspective.client.entity.TexturedEntityData;
import com.mclegoman.perspective.client.entity.TexturedEntityModels;
import com.mclegoman.perspective.client.entity.renderer.feature.MooshroomOverlayFeatureRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.MooshroomEntityRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(priority = 100, value = MooshroomEntityRenderer.class)
public abstract class MooshroomEntityRendererMixin extends MobEntityRenderer<MooshroomEntity, CowEntityModel<MooshroomEntity>> {
	@Shadow @Final
	private static Map<MooshroomEntity.Type, Identifier> TEXTURES;
	public MooshroomEntityRendererMixin(EntityRendererFactory.Context context, CowEntityModel<MooshroomEntity> entityModel, float f) {
		super(context, entityModel, f);
	}
	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)V", at = @At("TAIL"))
	private void perspective$init(EntityRendererFactory.Context context, CallbackInfo ci) {
		this.addFeature(new MooshroomOverlayFeatureRenderer<>(this, new CowEntityModel<>(context.getPart(TexturedEntityModels.mooshroomOverlay))));
	}
	@Inject(method = "getTexture", at = @At("RETURN"), cancellable = true)
	public void perspective$getTexture(MooshroomEntity mooshroomEntity, CallbackInfoReturnable<Identifier> cir) {
		boolean isTexturedEntity = true;
		TexturedEntityData entityData = TexturedEntity.getEntity(mooshroomEntity, "minecraft", "mooshroom");
		if (entityData != null) {
			JsonObject entitySpecific = entityData.getEntitySpecific();
			if (entitySpecific != null) {
				if (entitySpecific.has("variants")) {
					JsonObject variants = JsonHelper.getObject(entitySpecific, "variants");
					if (variants != null) {
						if (entitySpecific.has(mooshroomEntity.getVariant().asString().toLowerCase())) {
							JsonObject typeRegistry = JsonHelper.getObject(variants, mooshroomEntity.getVariant().asString().toLowerCase());
							if (typeRegistry != null) {
								isTexturedEntity = JsonHelper.getBoolean(typeRegistry, "enabled", true);
							}
						}
					}
				}
			}
			if (isTexturedEntity) {
				String variant = mooshroomEntity.getVariant() != null ? mooshroomEntity.getVariant().asString().toLowerCase() + "_" : "";
				cir.setReturnValue(TexturedEntity.getTexture(mooshroomEntity, "minecraft:mooshroom", TexturedEntity.Affix.PREFIX, variant, TEXTURES.get(mooshroomEntity.getVariant())));
			}
		}
	}
}