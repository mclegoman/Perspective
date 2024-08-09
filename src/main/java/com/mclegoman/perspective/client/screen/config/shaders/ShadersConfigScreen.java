/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.luminance.client.events.Events;
import com.mclegoman.luminance.client.shaders.Shaders;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.screen.AbstractConfigScreen;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.ScreenHelper;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ShadersConfigScreen extends AbstractConfigScreen {
	private boolean reverse;
	private Formatting[] formattings;
	public ShadersConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, Formatting[] formattings) {
		super(parentScreen, refresh, saveOnClose);
		this.formattings = formattings;
	}
	public void init() {
		try {
			super.init();
			this.gridAdder.add(ScreenHelper.createTitle(ClientData.minecraft, getRefreshScreen(), "zoom", false, true));
			this.gridAdder.add(createShaders());
			this.gridAdder.add(createShaderOptions());
			this.gridAdder.add(new EmptyWidget(20, 20));
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize shaders config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createShaders() {
		GridWidget shadersGrid = new GridWidget();
		shadersGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder shadersGridAdder = shadersGrid.createAdder(3);
		ButtonWidget cycleShaders = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.cycle", new Object[]{Events.ShaderRender.Shaders.exists(com.mclegoman.perspective.client.shaders.Shaders.superSecretSettingsId, "main") ?  Shaders.getShaderName(Shaders.getShaderIndex((String)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_shader"))) : Translation.getShaderTranslation(Data.version.getID(), "shader.not_loaded")}, formattings), (button) -> {
			Shader.cycle(true, !this.reverse, true, false, false);
			this.formattings = new Formatting[]{Shader.getRandomColor()};
			this.refresh = true;
		}).width(256).build();
		cycleShaders.active = Shader.isShaderButtonsEnabled();
		shadersGridAdder.add(cycleShaders);
		ButtonWidget listShaders = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.list"), (button) -> ClientData.minecraft.setScreen(new ShaderSelectionConfigScreen(getRefreshScreen(), new Formatting[]{Shader.getRandomColor()}, -1, (boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_selection_blur")))).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.list", true))).width(20).build();
		listShaders.active = Shader.isShaderButtonsEnabled();
		shadersGridAdder.add(listShaders);
		ButtonWidget randomShader = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.random"), (button) -> {
			Shader.random(true, false, false);
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.random", true))).width(20).build();
		randomShader.active = Shader.isShaderButtonsEnabled();
		shadersGridAdder.add(randomShader);
		return shadersGrid;
	}
	private GridWidget createShaderOptions() {
		GridWidget shaderOptionsGrid = new GridWidget();
		shaderOptionsGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder shaderOptionsGridAdder = shaderOptionsGrid.createAdder(2);
		shaderOptionsGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.mode", new Object[]{Translation.getShaderModeTranslation(Data.version.getID(), (String) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode")), ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode").equals("screen") ? Translation.getVariableTranslation(Data.version.getID(), (boolean) Shaders.get(Shader.superSecretSettingsIndex).getDisableGameRendertype(), Translation.Type.DISABLE_SCREEN_MODE) : ""}), (button) -> {
			Shader.cycleShaderModes();
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.mode", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "shaders.mode." + ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_mode"), true)}, true))).build());
		shaderOptionsGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.play_sound", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_sound"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_sound", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_sound"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.play_sound", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "shaders.play_sound." + ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_sound") ? "on" : "off"), true)}, true))).build());
		shaderOptionsGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.show_name", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_show_name"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_show_name", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_show_name"));
			this.refresh = true;
		}).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "shaders.show_name", new Object[]{Translation.getConfigTranslation(Data.version.getID(), "shaders.show_name." + ((boolean)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_show_name") ? "on" : "off"), true)}, true))).build());
		shaderOptionsGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "shaders.toggle", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "super_secret_settings_enabled"), Translation.Type.ENDISABLE)}), (button) -> {
			Shader.toggle(true, false, false, false);
			this.refresh = true;
		}).build());
		return shaderOptionsGrid;
	}
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) this.reverse = true;
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) this.reverse = false;
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	public Screen getRefreshScreen() {
		return new ShadersConfigScreen(parentScreen, false, saveOnClose, formattings);
	}
	public Text getPageTitle() {
		return Translation.getConfigTranslation(Data.version.getID(), "shaders");
	}
}