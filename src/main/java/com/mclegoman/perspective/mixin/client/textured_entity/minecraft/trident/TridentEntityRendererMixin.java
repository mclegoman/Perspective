/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.textured_entity.minecraft.trident;

import com.mclegoman.perspective.client.textured_entity.TexturedEntity;
import net.minecraft.client.render.entity.TridentEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 10000, value = TridentEntityRenderer.class)
public class TridentEntityRendererMixin {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof TridentEntity) cir.setReturnValue(TexturedEntity.getTexture(entity, "minecraft:trident", "", cir.getReturnValue()));
    }
}