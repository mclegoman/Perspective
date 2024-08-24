/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.hold_perspective;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.AbstractConfigScreen;
import com.mclegoman.perspective.client.screen.widget.ConfigSliderWidget;
import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EmptyWidget;
import net.minecraft.client.gui.widget.GridWidget;

public class HoldPerspectiveConfigScreen extends AbstractConfigScreen {
	public HoldPerspectiveConfigScreen(Screen parentScreen, boolean refresh, boolean saveOnClose, int page) {
		super(parentScreen, refresh, saveOnClose, page);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createPageOne());
			else shouldClose = true;
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize zoom config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createPageOne() {
		GridWidget holdPerspectiveGrid = new GridWidget();
		holdPerspectiveGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder holdPerspectiveGridAdder = holdPerspectiveGrid.createAdder(2);
		holdPerspectiveGridAdder.add(new ConfigSliderWidget(holdPerspectiveGridAdder.getGridWidget().getX(), holdPerspectiveGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "hold_perspective.back.multiplier", new Object[]{String.format("%.2f", (double)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_multiplier"))}, false), (((double) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_multiplier") - 0.5F) / 3.5F)) {
			@Override
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective.back.multiplier", new Object[]{String.format("%.2f", (double)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_multiplier"))}, false));
			}
			@Override
			protected void applyValue() {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_multiplier", Double.valueOf(String.format("%.2f", ((value * 3.5D) + 0.5D))));
			}
		}).setTooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective.back.multiplier", true)));
		holdPerspectiveGridAdder.add(new ConfigSliderWidget(holdPerspectiveGridAdder.getGridWidget().getX(), holdPerspectiveGridAdder.getGridWidget().getY(), 150, 20, Translation.getConfigTranslation(Data.version.getID(), "hold_perspective.front.multiplier", new Object[]{String.format("%.2f", (double)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_multiplier"))}, false), (((double) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_multiplier") - 0.5F) / 3.5F)) {
			@Override
			protected void updateMessage() {
				setMessage(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective.front.multiplier", new Object[]{String.format("%.2f", (double)ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_multiplier"))}, false));
			}
			@Override
			protected void applyValue() {
				ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_multiplier", Double.valueOf(String.format("%.2f", ((value * 3.5D) + 0.5D))));
			}
		}).setTooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective.front.multiplier", true)));
		holdPerspectiveGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective.back.hide_hud", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_hide_hud"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_hide_hud", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_back_hide_hud"));
			this.refresh = true;
		}).build());
		holdPerspectiveGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "hold_perspective.front.hide_hud", new Object[]{Translation.getVariableTranslation(Data.version.getID(), (boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_hide_hud"), Translation.Type.ONFF)}), (button) -> {
			ConfigHelper.setConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_hide_hud", !(boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "hold_perspective_front_hide_hud"));
			this.refresh = true;
		}).build());
		holdPerspectiveGridAdder.add(new EmptyWidget(20, 20), 2);
		holdPerspectiveGridAdder.add(new EmptyWidget(20, 20), 2);
		return holdPerspectiveGrid;
	}
	public Screen getRefreshScreen() {
		return new HoldPerspectiveConfigScreen(this.parentScreen, false, false, this.page);
	}
	public String getPageId() {
		return "hold_perspective";
	}
}