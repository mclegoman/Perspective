/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.entity_renderer;

import com.mclegoman.perspective.client.textured_entity.PerspectiveTexturedEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(priority = 10000, value = BoatEntityRenderer.class)
public class PerspectiveBoatEntityRenderer {
    @Inject(at = @At("RETURN"), method = "getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    private void perspective$getTexture(Entity entity, CallbackInfoReturnable<Identifier> cir) {
        if (entity instanceof ChestBoatEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:chest_boat", "", cir.getReturnValue()));
        else if (entity instanceof BoatEntity) cir.setReturnValue(PerspectiveTexturedEntity.getTexture(entity, "minecraft:boat", "", cir.getReturnValue()));
    }
}