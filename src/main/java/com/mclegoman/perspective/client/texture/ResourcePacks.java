/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.texture;

import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;

public class ResourcePacks extends com.mclegoman.luminance.client.texture.ResourcePacks {
	/**
	 * To add a resource pack to this project, please follow these guidelines:
	 * 1. When registering your resource pack, ensure you include the resource pack's name, and the contributor(s) in the following format:
	 * - Resource Pack Name
	 * - Contributor(s): _________
	 * - Licence: _________
	 * You only need to include the licence in your comment if it is not GNU LGPLv3.
	 */
	public static void init() {
		try {
		/*
            Perspective: Default
            Contributor(s): dannytaylor
            Attribution(s): Phantazap ('Jester' Giant Textured Entity)
        */
			register(Identifier.of("perspective_default"), Data.version.getModContainer(), Translation.getTranslation(Data.version.getID(), "resource_pack.perspective_default"), ResourcePackActivationType.DEFAULT_ENABLED);
		/*
            Perspective: Extended
            Contributor(s): dannytaylor
        */
			register(Identifier.of("perspective_extended"), Data.version.getModContainer(), Translation.getTranslation(Data.version.getID(), "resource_pack.perspective_extended"), ResourcePackActivationType.NORMAL);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to init resource packs: {}", error));
		}
	}
}