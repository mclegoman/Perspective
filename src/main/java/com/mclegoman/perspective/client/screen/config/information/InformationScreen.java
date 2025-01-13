/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.information;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.screen.config.AbstractConfigScreen;
import com.mclegoman.perspective.client.screen.config.LinkScreen;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.util.Identifier;

public class InformationScreen extends AbstractConfigScreen {
	public InformationScreen(Screen parentScreen, boolean refresh) {
		super(parentScreen, refresh, 1);
	}
	public void init() {
		try {
			super.init();
			if (this.page == 1) this.gridAdder.add(createInformation());
			postInit();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize zoom config screen: {}", error));
			ClientData.minecraft.setScreen(this.parentScreen);
		}
	}
	private GridWidget createInformation() {
		GridWidget infoGrid = new GridWidget();
		infoGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder infoGridAdder = infoGrid.createAdder(1);
		ButtonWidget documentationButton = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information.documentation"), button -> ClientData.minecraft.setScreen(new LinkScreen(ClientData.minecraft.currentScreen, "https://mclegoman.com/Perspective", true))).width(304).tooltip(Tooltip.of(Translation.getConfigTranslation(Data.version.getID(), "information.documentation", true))).build();
		documentationButton.active = false;
		infoGridAdder.add(documentationButton, 1);
		infoGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information.source_code"), button -> ClientData.minecraft.setScreen(new LinkScreen(ClientData.minecraft.currentScreen, "https://github.com/MCLegoMan/Perspective", true))).width(304).build(), 1);
		infoGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information.report"), button -> ClientData.minecraft.setScreen(new LinkScreen(ClientData.minecraft.currentScreen, "https://github.com/MCLegoMan/Perspective/issues", true))).width(304).build(), 1);
		infoGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "information.credits"), button -> ClientData.minecraft.setScreen(new CreditsAttributionScreen(ClientData.minecraft.currentScreen, Identifier.of(Data.version.getID(), "texts/credits.json")))).width(304).build(), 1);
		return infoGrid;
	}
	protected GridWidget createFooter() {
		GridWidget footerGrid = new GridWidget();
		footerGrid.getMainPositioner().alignHorizontalCenter().margin(2);
		GridWidget.Adder footerGridAdder = footerGrid.createAdder(this.getMaxPage() > 1 ? 2 : 1);
		footerGridAdder.add(ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "back"), (button) -> {
			if (this.page <= 1) {
				this.shouldClose = true;
			}
			else {
				this.page -= 1;
				this.refresh = true;
			}
		}).build());
		if (this.getMaxPage() > 1) {
			ButtonWidget nextButtonWidget = ButtonWidget.builder(Translation.getConfigTranslation(Data.version.getID(), "next"), (button) -> {
				if (!(this.page >= getMaxPage())) {
					this.page += 1;
					this.refresh = true;
				}
			}).build();
			if (this.page >= getMaxPage()) nextButtonWidget.active = false;
			footerGridAdder.add(nextButtonWidget);
		}
		return footerGrid;
	}
	public String getPageId() {
		return "information";
	}
	public Screen getRefreshScreen() {
		return new InformationScreen(this.parentScreen, false);
	}
}