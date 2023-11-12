/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.zoom;

import com.mclegoman.perspective.client.config.ConfigDataLoader;
import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.overlays.HUDOverlays;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Zoom {
	public static double fov;
	private static boolean zoomUpdated;
	public static double prevZoomMultiplier;
	public static double zoomMultiplier;
	public static void updateZoomMultiplier() {
		float f = getZoomMultiplier();
		prevZoomMultiplier = zoomMultiplier;
		zoomMultiplier += (f - zoomMultiplier) * 0.5F;
	}

	public static float getZoomMultiplier() {
		return isZooming() ? 1 - ((float) getZoomLevel() / 100) : 1;
	}
	public static double limitFov(double fov) {
		return Math.max(Math.max(0.1, fov), Math.min(fov, 110));
	}

	public static boolean SET_ZOOM;
	public static boolean isZooming() {
		return ClientData.CLIENT.player != null && (Keybindings.HOLD_ZOOM.isPressed() || SET_ZOOM);
	}
	public static void tick(MinecraftClient client) {
		try {
			updateZoomMultiplier();
			if (Keybindings.TOGGLE_ZOOM.wasPressed()) SET_ZOOM = !SET_ZOOM;
			if (!isZooming() && zoomUpdated) {
				ConfigHelper.saveConfig(true);
				zoomUpdated = false;
			}
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick zoom: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
	public static int getZoomLevel() {
		return (int) ConfigHelper.getConfig("zoom_level");
	}
	public static void zoom(boolean in, int amount) {
		try {
			boolean updated = false;
			for (int i = 0; i < amount; i++){
				if (in) {
					if (!(getZoomLevel() >= 100)) {
						ConfigHelper.setConfig("zoom_level", getZoomLevel() + 1);
						updated = true;
						zoomUpdated = true;
					}
				}
				else {
					if (!(getZoomLevel() <= -50)) {
						ConfigHelper.setConfig("zoom_level", getZoomLevel() - 1);
						updated = true;
						zoomUpdated = true;
					}
				}
			}
			if (updated) setOverlay();
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to set zoom level: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
	public static void reset(MinecraftClient client) {
		try {
			if ((int) ConfigHelper.getConfig("zoom_level") != ConfigDataLoader.ZOOM_LEVEL) {
				ConfigHelper.setConfig("zoom_level", ConfigDataLoader.ZOOM_LEVEL);
				setOverlay();
				ConfigHelper.saveConfig(true);
			}
		} catch (Exception error) {
			Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to reset zoom level: {}", Data.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
		}
	}
	private static void setOverlay(){
		if ((boolean) ConfigHelper.getConfig("zoom_show_percentage")) HUDOverlays.setOverlay(Text.translatable("gui.perspective.message.zoom_level", Text.literal((int) ConfigHelper.getConfig("zoom_level") + "%")).formatted(Formatting.GOLD));
	}
	public static void cycleZoomTransitions() {
		if (ConfigHelper.getConfig("zoom_transition").equals("smooth")) ConfigHelper.setConfig("zoom_transition", "instant");
		else if (ConfigHelper.getConfig("zoom_transition").equals("instant")) ConfigHelper.setConfig("zoom_transition", "smooth");
		else ConfigHelper.setConfig("zoom_transition", ConfigDataLoader.ZOOM_TRANSITION);
	}
	public static void cycleZoomCameraModes() {
		if (ConfigHelper.getConfig("zoom_camera_mode").equals("default")) ConfigHelper.setConfig("zoom_camera_mode", "spyglass");
		else if (ConfigHelper.getConfig("zoom_camera_mode").equals("spyglass")) ConfigHelper.setConfig("zoom_camera_mode", "default");
		else ConfigHelper.setConfig("zoom_camera_mode", ConfigDataLoader.ZOOM_CAMERA_MODE);
	}
}