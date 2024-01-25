/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.common.config.ConfigProvider;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.perspective.common.util.Pair;
import com.mclegoman.simplefabriclibs.simple_config.SimpleConfig;

public class WarningsConfig {
	protected static final String ID = Data.VERSION.getID() + "-warnings";
	protected static SimpleConfig CONFIG;
	protected static ConfigProvider CONFIG_PROVIDER;
	protected static boolean PHOTOSENSITIVITY;
	protected static boolean PRANK;

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
		CONFIG_PROVIDER.add(new Pair<>("photosensitivity", false));
		CONFIG_PROVIDER.add(new Pair<>("prank", false));
	}

	protected static void assign() {
		PHOTOSENSITIVITY = CONFIG.getOrDefault("photosensitivity", false);
		PRANK = CONFIG.getOrDefault("prank", false);
	}

	protected static void save() {
		Data.VERSION.getLogger().info("{} Writing warning config to file.", Data.VERSION.getLoggerPrefix());
		CONFIG_PROVIDER.setConfig("photosensitivity", PHOTOSENSITIVITY);
		CONFIG_PROVIDER.setConfig("prank", PRANK);
		CONFIG_PROVIDER.saveConfig(ID);
	}
}