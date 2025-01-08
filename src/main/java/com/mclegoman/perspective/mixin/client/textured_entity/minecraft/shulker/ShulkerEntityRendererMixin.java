/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.shulker;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.entity.ShulkerEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = ShulkerEntityRenderer.class)
public class ShulkerEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/mob/ShulkerEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(ShulkerEntity entity, CallbackInfoReturnable<Identifier> cir) {
		// TODO: Add coloured shulkers to entity specific
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}