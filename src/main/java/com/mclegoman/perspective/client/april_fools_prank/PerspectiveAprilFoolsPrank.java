/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.perspective.client.april_fools_prank;

import com.mclegoman.perspective.client.config.PerspectiveConfigHelper;
import com.mclegoman.perspective.common.data.PerspectiveData;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

import java.time.LocalDate;
import java.time.Month;
import java.util.TimeZone;

public class PerspectiveAprilFoolsPrank {
    public static void init() {
        try {
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PerspectiveAprilFoolsPrankDataLoader());
        } catch (Exception error) {
            PerspectiveData.PERSPECTIVE_VERSION.getLogger().warn("{} Failed to initialize april fools prank: {}", PerspectiveData.PERSPECTIVE_VERSION.getLoggerPrefix(), error);
        }
    }
    public static boolean isAprilFools() {
        if ((boolean)PerspectiveConfigHelper.getConfig("force_april_fools")) return true;
        else {
            LocalDate date = LocalDate.now(TimeZone.getTimeZone("GMT+12").toZoneId());
            return date.getMonth() == Month.APRIL && date.getDayOfMonth() <= 2;
        }
    }
    public static boolean isPrankEnabled() {
        return (boolean)PerspectiveConfigHelper.getConfig("allow_april_fools");
    }
}