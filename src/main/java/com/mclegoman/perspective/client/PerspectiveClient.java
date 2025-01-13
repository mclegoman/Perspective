/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.appearance.Appearance;
import com.mclegoman.perspective.client.entity.Entity;
import com.mclegoman.perspective.client.events.AprilFoolsPrank;
import com.mclegoman.perspective.client.contributor.Contributor;
import com.mclegoman.perspective.client.hide.Hide;
import com.mclegoman.perspective.client.hud.Overlays;
import com.mclegoman.perspective.client.item.ItemGroup;
import com.mclegoman.perspective.client.panorama.Panorama;
import com.mclegoman.perspective.client.shaders.Shaders;
import com.mclegoman.perspective.client.texture.TextureHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.ui.UIBackground;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.logo.PerspectiveLogo;
import com.mclegoman.perspective.client.util.Tick;
import com.mclegoman.perspective.client.update.Update;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.client.config.ConfigHelper;
import net.fabricmc.api.ClientModInitializer;

public class PerspectiveClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		try {
			Data.version.sendToLog(LogType.INFO, Translation.getString("Initializing {}", Data.version.getName()));
			TextureHelper.init();
			AprilFoolsPrank.init();
			Appearance.init();
			UIBackground.init();
			Overlays.init();
			Zoom.init();
			Contributor.init();
			Hide.init();
			Keybindings.init();
			Panorama.init();
			PerspectiveLogo.init();
			Entity.init();
			Tick.init();
			ConfigHelper.init();
			ItemGroup.init();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to run onInitializeClient: {}", error));
		}
	}
	public static void afterInitializeConfig() {
		try {
			Data.version.sendToLog(LogType.INFO, Translation.getString("AfterConfiging {}", Data.version.getName()));
			Update.checkForUpdates(Data.version);
			Shaders.init();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to run afterInitializeConfig: {}", error));
		}
	}
}