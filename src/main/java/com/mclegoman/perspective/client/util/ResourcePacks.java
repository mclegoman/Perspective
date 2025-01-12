/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;

public class ResourcePacks {
	/**
	 * To add a resource pack to this project, please follow these guidelines:
	 * 1. When registering your resource pack, ensure you include the resource pack's name, and the contributor(s) in the following format:
	 * - Resource Pack Name
	 * - Contributor(s): _________
	 * 2. Your resource pack must use the GNU LGPLv3 licence.
	 * - This only applies to resource packs that are included with Perspective.
	 **/
	public static void init() {
        /*
            Perspective: Default
            Contributor(s): MCLegoMan
        */
		ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("perspective_default"), Data.version.getModContainer(), Translation.getTranslation(Data.version.getID(), "resource_pack.perspective_default"), ResourcePackActivationType.DEFAULT_ENABLED);
		ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("super_secret_settings"), Data.version.getModContainer(), Translation.getTranslation(Data.version.getID(), "resource_pack.super_secret_settings"), ResourcePackActivationType.DEFAULT_ENABLED);
	}
}