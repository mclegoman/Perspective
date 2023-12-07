/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.screen.config.shaders;

import com.mclegoman.perspective.client.data.ClientData;
import com.mclegoman.perspective.client.shaders.ShaderDataLoader;
import com.mclegoman.perspective.client.shaders.ShaderRegistryValue;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.translation.TranslationType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

public class ShaderListEntry extends AlwaysSelectedEntryListWidget.Entry<ShaderListEntry> {
	public final int shader;
	public ShaderListEntry(int shader) {
		this.shader = shader;
	}
	@Override
	public void render(DrawContext context, int index, int y, int x, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float delta) {
		context.drawCenteredTextWithShadow(ClientData.CLIENT.textRenderer, Translation.getConfigTranslation("shaders.shader.list", new Object[]{ShaderDataLoader.getShaderName(this.shader), Translation.getVariableTranslation((boolean) ShaderDataLoader.get(this.shader, ShaderRegistryValue.DISABLE_SCREEN_MODE), TranslationType.DISABLE_SCREEN_MODE)}), ClientData.CLIENT.getWindow().getScaledWidth() / 2, y + (rowHeight / 2) - (9 / 2), 0xFFFFFF);
	}
	@Override
	public Text getNarration() {
		return Text.literal("");
	}
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isMouseOver(mouseX, mouseY)) {
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
