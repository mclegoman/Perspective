/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.perspective.client.config.ConfigHelper;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.ConfigScreenHelper;
import com.mclegoman.perspective.client.shaders.Shader;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import com.mclegoman.perspective.client.util.Keybindings;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class ShadersConfigScreen extends Screen {
    private final Screen PARENT_SCREEN;
    private final GridWidget GRID;
    private boolean SHOULD_CLOSE;
    private final boolean SAVE_ON_CLOSE;
    private boolean REFRESH;
    private boolean CYCLE_DIRECTION = true;
    public ShadersConfigScreen(Screen PARENT, boolean SAVE_ON_CLOSE, boolean REFRESH) {
        super(Text.literal(""));
        this.GRID = new GridWidget();
        this.PARENT_SCREEN = PARENT;
        this.SAVE_ON_CLOSE = SAVE_ON_CLOSE;
        this.REFRESH = REFRESH;
    }
    public void init() {
        try {
            GRID.getMainPositioner().alignHorizontalCenter().margin(0);
            GridWidget.Adder GRID_ADDER = GRID.createAdder(1);
            GRID_ADDER.add(ConfigScreenHelper.createTitle(ClientData.CLIENT, new ShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, true), true, "shaders"));
            GRID_ADDER.add(createShaders());
            GRID_ADDER.add(createShaderOptions());
            GRID_ADDER.add(new EmptyWidget(4, 4));
            GRID_ADDER.add(createFooter());
            GRID.refreshPositions();
            GRID.forEachChild(this::addDrawableChild);
            initTabNavigation();
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize config>shaders screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }

    public void tick() {
        try {
            if (this.REFRESH) {
                ClientData.CLIENT.setScreen(new ShadersConfigScreen(PARENT_SCREEN, SAVE_ON_CLOSE, false));
            }
            if (this.SHOULD_CLOSE) {
                if (this.SAVE_ON_CLOSE) ConfigHelper.saveConfig(false);
                ClientData.CLIENT.setScreen(PARENT_SCREEN);
            }
        } catch (Exception error) {
            Data.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to tick perspective$config$shaders screen: {}", Data.PERSPECTIVE_VERSION.getID(), error);
        }
    }
    private GridWidget createShaders() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.shader", new Object[]{ShaderDataLoader.getShaderName((int) ConfigHelper.getConfig("super_secret_settings"))}, new Formatting[]{Shader.getRandomColor()}), (button) -> {
            Shader.cycle(ClientData.CLIENT, CYCLE_DIRECTION, true, false);
            this.REFRESH = true;
        }).width(280).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("shaders.shader", true)));
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.random"), (button) -> {
            Shader.random(true, false);
            this.REFRESH = true;
        }).width(20).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("shaders.random", true)));
        return GRID;
    }
    private GridWidget createShaderOptions() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.mode", new Object[]{Translation.getShaderModeTranslation((String) ConfigHelper.getConfig("super_secret_settings_mode")), Translation.getVariableTranslation((boolean)Shader.getShaderData(ShaderRegistryValue.DISABLE_SCREEN_MODE), TranslationType.DISABLE_SCREEN_MODE)}), (button) -> {
            Shader.cycleShaderModes();
            this.REFRESH = true;
        }).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("shaders.mode", true)));
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.toggle", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("super_secret_settings_enabled"), TranslationType.ENDISABLE)}), (button) -> {
            Shader.toggle(ClientData.CLIENT, true, false);
            this.REFRESH = true;
        }).build()).setTooltip(Tooltip.of(Translation.getConfigTranslation("shaders.toggle", true)));
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("shaders.overlay_message", new Object[]{Translation.getVariableTranslation((boolean) ConfigHelper.getConfig("super_secret_settings_overlay_message"), TranslationType.ONFF)}), (button) -> {
            ConfigHelper.setConfig("super_secret_settings_overlay_message", !(boolean) ConfigHelper.getConfig("super_secret_settings_overlay_message"));
            this.REFRESH = true;
        }).width(304).build(), 2);
        return GRID;
    }
    private GridWidget createFooter() {
        GridWidget GRID = new GridWidget();
        GRID.getMainPositioner().alignHorizontalCenter().margin(2);
        GridWidget.Adder GRID_ADDER = GRID.createAdder(2);
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("reset"), (button) -> {
            ConfigHelper.resetConfig();
            this.REFRESH = true;
        }).build());
        GRID_ADDER.add(ButtonWidget.builder(Translation.getConfigTranslation("back"), (button) -> this.SHOULD_CLOSE = true).build());
        return GRID;
    }

    public void initTabNavigation() {
        SimplePositioningWidget.setPos(GRID, getNavigationFocus());
    }
    public Text getNarratedTitle() {
        return ScreenTexts.joinSentences();
    }
    public boolean shouldCloseOnEsc() {
        return false;
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == KeyBindingHelper.getBoundKeyOf(Keybindings.OPEN_CONFIG).getCode()) this.SHOULD_CLOSE = true;
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) this.CYCLE_DIRECTION = false;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT) this.CYCLE_DIRECTION = true;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
}