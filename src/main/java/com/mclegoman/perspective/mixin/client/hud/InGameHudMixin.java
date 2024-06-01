/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.mixin.client.hud;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.hud.DebugOverlay;
import com.mclegoman.perspective.client.hud.HUDHelper;
import com.mclegoman.perspective.client.hud.Overlays;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.config.ConfigHelper;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 100, value = InGameHud.class)
public abstract class InGameHudMixin {
	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void perspective$render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (HUDHelper.shouldHideHUD()) ci.cancel();
	}

	@Inject(at = @At("RETURN"), method = "render")
	private void perspective$renderOverlays(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (!ClientData.minecraft.getDebugHud().shouldShowDebugHud() && !ClientData.minecraft.options.hudHidden && !HUDHelper.shouldHideHUD()) {
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "version_overlay"))
				context.drawTextWithShadow(ClientData.minecraft.textRenderer, Translation.getTranslation(Data.version.getID(), "version_overlay", new Object[]{SharedConstants.getGameVersion().getName()}), 2, 2, 0xffffff);
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "position_overlay")) Overlays.renderPositionOverlay(context);
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "day_overlay")) Overlays.renderDayOverlay(context);
			if (!DebugOverlay.debugType.equals(DebugOverlay.Type.none)) {
				DebugOverlay.renderDebugHUD(context);
			}
		}
	}
}