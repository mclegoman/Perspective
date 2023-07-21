/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: CC-BY 4.0
*/

package com.mclegoman.perspective.client.config;

import com.mclegoman.perspective.client.translation.PerspectiveTranslation;
import com.mclegoman.perspective.common.data.PerspectiveData;
import com.mclegoman.simplefabric.fabric_simplelibs.simple_config.SimpleConfig;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class PerspectiveConfigProvider implements SimpleConfig.DefaultConfig {
    private String CONTENTS = "";
    private List<Pair<String, ?>> CONFIG_LIST = new ArrayList<>();
    public void add(Pair<String, ?> keyValuePair) {
        try {
            CONFIG_LIST.add(keyValuePair);
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to add {} config value: {}", keyValuePair, error);
        }
    }
    @Override
    public String get(String namespace) {
        return CONTENTS;
    }
    public void setContents(String ID) {
        StringBuilder contents = new StringBuilder(PerspectiveTranslation.getString("#{}.properties file\n", ID));
        for (Pair<String, ?> option : CONFIG_LIST) {
            contents.append(option.getFirst()).append("=").append(option.getSecond()).append("\n");
        }
        CONTENTS = contents.toString();
    }
    public void setConfig(String KEY_NAME, Object KEY_VALUE) {
        try {
            List<Pair<String, ?>> NEW_CONFIG_LIST = this.CONFIG_LIST;
            for (Pair<String, ?> key : NEW_CONFIG_LIST) {
                String KEY_FIRST = key.getFirst();
                int KEY_INDEX = NEW_CONFIG_LIST.indexOf(key);
                if (KEY_FIRST.equals(KEY_NAME)) {
                    NEW_CONFIG_LIST.set(KEY_INDEX, new Pair<>(KEY_NAME, KEY_VALUE));
                }
            }
            CONFIG_LIST = NEW_CONFIG_LIST;
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to set {} config value: {}", KEY_NAME, error);
        }
    }
    public void saveConfig(String ID) {
        try {
            setContents(ID);
            PrintWriter writer = new PrintWriter(FabricLoader.getInstance().getConfigDir().resolve( ID + ".properties").toFile(), StandardCharsets.UTF_8);
            writer.write(CONTENTS);
            writer.close();
        } catch (Exception error) {
            PerspectiveData.LOGGER.warn(PerspectiveData.PREFIX + "Failed to save {} config: {}", ID, error);
        }
    }
}