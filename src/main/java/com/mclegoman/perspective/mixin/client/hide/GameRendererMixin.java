/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hide;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.hud.HUD;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(priority = 10000, value = GameRenderer.class)
public abstract class GameRendererMixin {
	@Inject(at = @At("HEAD"), method = "shouldRenderBlockOutline", cancellable = true)
	private void perspective$renderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
		if ((HUD.shouldHideHUD()) || ((boolean) ConfigHelper.getConfig("hide_block_outline")))
			cir.setReturnValue(false);
	}
}