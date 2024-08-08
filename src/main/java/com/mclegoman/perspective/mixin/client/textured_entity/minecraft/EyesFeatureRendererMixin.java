/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = EyesFeatureRenderer.class)
public class EyesFeatureRendererMixin {
	@Unique
	private Entity entity;
	@Inject(method = "render", at = @At("HEAD"))
	public void perspective$getEntity(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci) {
		this.entity = entity;
	}
	@ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/EyesFeatureRenderer;getEyesTexture()Lnet/minecraft/client/render/RenderLayer;"))
	public RenderLayer perspective$render(RenderLayer renderLayer) {
		if (this.entity instanceof EndermanEntity) {
			return RenderLayer.getEyes(TexturedEntity.getTexture(this.entity, "minecraft", "enderman", TexturedEntity.Affix.SUFFIX, "_eyes", Identifier.of("textures/entity/enderman/enderman_eyes.png")));
		}
		else if (this.entity instanceof SpiderEntity) {
			return RenderLayer.getEyes(TexturedEntity.getTexture(this.entity, "minecraft", "spider", TexturedEntity.Affix.SUFFIX, "_eyes", Identifier.of("textures/entity/spider_eyes.png")));
		}
		else if (this.entity instanceof PhantomEntity) {
			return RenderLayer.getEyes(TexturedEntity.getTexture(this.entity, "minecraft", "phantom", TexturedEntity.Affix.SUFFIX, "_eyes", Identifier.of("textures/entity/phantom_eyes.png")));
		}
		return renderLayer;
	}
}