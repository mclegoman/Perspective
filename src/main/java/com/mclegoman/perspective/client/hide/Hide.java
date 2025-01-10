/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.hide;

import com.mclegoman.luminance.client.util.MessageOverlay;
import com.mclegoman.luminance.common.util.Triple;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.perspective.Perspective;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.keybindings.Keybindings;
import com.mclegoman.perspective.client.zoom.Zoom;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Hide {
	public static final String[] zoomHideHudModes = new String[]{"true", "hand", "false"};
	public static final String[] hideCrosshairModes = new String[]{"vanilla", "dynamic", "hidden"};
	public static float rainbowTime = 0.0F;
	public static void init() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HideArmorDataLoader());
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HideNameTagsDataLoader());
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new HidePlayerDataLoader());
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new DynamicCrosshairItemsDataLoader());
	}
	public static void tick() {
		if (Keybindings.toggleArmour.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hide_armor", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_armor"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.armor", Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_armor"), Translation.Type.ENDISABLE)).formatted(Formatting.GOLD));
		}
		if (Keybindings.toggleBlockOutline.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hide_block_outline", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_block_outline"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.block_outline", Translation.getVariableTranslation(Data.version.getID(), !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_block_outline"), Translation.Type.ENDISABLE)).formatted(Formatting.GOLD));
		}
		if (Keybindings.cycleCrosshair.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "crosshair_type", nextCrosshairMode());
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.crosshair", Translation.getCrosshairTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "crosshair_type"))).formatted(Formatting.GOLD));
		}
		if (Keybindings.toggleNametags.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hide_nametags", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_nametags"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.nametags", Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_nametags"), Translation.Type.ENDISABLE)).formatted(Formatting.GOLD));
		}
		if (Keybindings.togglePlayers.wasPressed()) {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hide_players", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_players"));
			if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_show_message"))
				MessageOverlay.setOverlay(Text.translatable("gui.perspective.message.hide.players", Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_nametags"), Translation.Type.ENDISABLE)).formatted(Formatting.GOLD));
		}
		rainbowTime += 1.0F % 20.0F;
	}
	public static boolean shouldHidePlayer(UUID uuid) {
		if (ClientData.minecraft.player != null) {
			if (!uuid.equals(ClientData.minecraft.player.getGameProfile().getId()))
				return (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_players") || HidePlayerDataLoader.REGISTRY.contains(String.valueOf(uuid));
		}
		return false;
	}
	public static String nextZoomHideHudMode() {
		List<String> modes = Arrays.stream(zoomHideHudModes).toList();
		return modes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_hide_hud")) ? zoomHideHudModes[(modes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_hide_hud")) + 1) % zoomHideHudModes.length] : zoomHideHudModes[0];
	}
	public static String nextCrosshairMode() {
		List<String> modes = Arrays.stream(hideCrosshairModes).toList();
		return modes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "crosshair_type")) ? hideCrosshairModes[(modes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "crosshair_type")) + 1) % hideCrosshairModes.length] : hideCrosshairModes[0];
	}
	public static boolean shouldHideHand(HideHudTypes type) {
		if (type == HideHudTypes.zoom) return Zoom.isZooming() && ((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_hide_hud")).equalsIgnoreCase("hand");
		return false;
	}
	public static boolean shouldHideHud(HideHudTypes type) {
		switch (type) {
			case zoom -> {
				return Zoom.isZooming() && ((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_hide_hud")).equalsIgnoreCase("true");
			}
			case holdPerspectiveBack -> {return Perspective.isHoldingPerspectiveBack() && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_hide_hud");}
			case holdPerspectiveFront -> {return Perspective.isHoldingPerspectiveFront() && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_hide_hud");}
			default -> {return false;}
		}
	}
	public static boolean shouldHideArmor(UUID uuid) {
		return (boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_armor") || HideArmorDataLoader.registry.contains(String.valueOf(uuid));
	}
	public static int getBlockOutlineLevel() {
		return ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "block_outline"));
	}
	public static boolean getRainbowBlockOutline() {
		return (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "rainbow_block_outline");
	}
	public static int getRainbowOutline() {
		return Color.getHSBColor(rainbowTime / 20.0F, 1.0F, 1.0F).getRGB();
	}
	public static int getARGB(int color, int alpha) {
		return (Math.clamp(alpha, 0, 255) << 24) | (color & 0x00FFFFFF);
	}
}