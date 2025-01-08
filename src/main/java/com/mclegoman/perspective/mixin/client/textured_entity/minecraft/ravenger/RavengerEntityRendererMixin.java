/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.ravenger;

import com.mclegoman.perspective.client.entity.TexturedEntity;
import net.minecraft.client.render.entity.RavagerEntityRenderer;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 100, value = RavagerEntityRenderer.class)
public class RavengerEntityRendererMixin {
	@Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/mob/RavagerEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
	private void perspective$getTexture(RavagerEntity entity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(TexturedEntity.getTexture(entity, cir.getReturnValue()));
	}
}