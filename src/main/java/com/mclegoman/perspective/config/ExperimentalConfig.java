/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.config;

import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Couple;
import com.mclegoman.releasetypeutils.common.version.Helper;
import net.darktree.simplelibs.config.SimpleConfig;

public class ExperimentalConfig {
	protected static final String ID = Data.VERSION.getID() + "-experimental";
	protected static SimpleConfig CONFIG;
	protected static ConfigProvider CONFIG_PROVIDER;
	protected static boolean improvedShaderRenderer;

	protected static void init() {
		try {
			CONFIG_PROVIDER = new ConfigProvider();
			create();
			CONFIG = SimpleConfig.of(ID).provider(CONFIG_PROVIDER).request();
			assign();
		} catch (Exception error) {
			Data.VERSION.getLogger().warn("{} Failed to initialize {} config: {}", Data.VERSION.getLoggerPrefix(), ID, error);
		}
	}

	protected static void create() {
		CONFIG_PROVIDER.add(new Couple<>("improved_shader_renderer", false));
	}

	protected static void assign() {
		improvedShaderRenderer = CONFIG.getOrDefault("improved_shader_renderer", false);
	}

	protected static void save() {
		if (ConfigHelper.EXPERIMENTS_AVAILABLE) {
			Data.VERSION.sendToLog(Helper.LogType.INFO,"Writing experimental config to file.");
			CONFIG_PROVIDER.setConfig("improved_shader_renderer", improvedShaderRenderer);
			CONFIG_PROVIDER.saveConfig(ID);
		}
	}
}