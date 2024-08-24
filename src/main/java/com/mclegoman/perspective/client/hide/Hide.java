/*
    Perspective
    Contributor(s): MCLegoMan
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

public class Hide {
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
	public static boolean shouldHidePlayer(PlayerEntity player) {
		if (ClientData.minecraft.player != null) {
			if (!player.getGameProfile().getId().equals(ClientData.minecraft.player.getGameProfile().getId()))
				return (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_players") || HidePlayerDataLoader.REGISTRY.contains(String.valueOf(player.getGameProfile().getId()));
		}
		return false;
	}
	public static String nextCrosshairMode() {
		List<String> crosshairModes = Arrays.stream(hideCrosshairModes).toList();
		return crosshairModes.contains((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "crosshair_type")) ? hideCrosshairModes[(crosshairModes.indexOf((String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "crosshair_type")) + 1) % hideCrosshairModes.length] : hideCrosshairModes[0];
	}
	public static boolean shouldHideHud(HideHudTypes type) {
		switch (type) {
			case zoom -> {return Zoom.isZooming() && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "zoom_hide_hud");}
			case holdPerspectiveBack -> {return Perspective.isHoldingPerspectiveBack() && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_hide_hud");}
			case holdPerspectiveFront -> {return Perspective.isHoldingPerspectiveFront() && (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_hide_hud");}
			default -> {return false;}
		}
	}
	public static boolean shouldHideArmor(PlayerEntity player) {
		return (boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hide_armor") || HideArmorDataLoader.registry.contains(String.valueOf(player.getGameProfile().getId()));
	}
	public static float getBlockOutlineLevel() {
		return ((int) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "block_outline")) / 100.0F;
	}
	public static boolean getRainbowBlockOutline() {
		return (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "rainbow_block_outline");
	}
	public static Triple<Float, Float, Float> getRainbowOutline() {
		Color color = Color.getHSBColor(rainbowTime / 20.0F, 1.0F, 1.0F);
		return new Triple<>(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F);
	}
}