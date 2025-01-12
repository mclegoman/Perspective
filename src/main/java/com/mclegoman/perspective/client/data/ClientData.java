/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.data;

import com.mclegoman.perspective.config.ConfigHelper;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.TimeZone;

public class ClientData {
	public static final MinecraftClient minecraft = MinecraftClient.getInstance();
	public static final String[] PRIDE_LOGOS = new String[]{"pride", "trans", "bi", "pan", "ace", "aro", "aroace", "gay", "lesbian"};
	public static final int PRIDE_LOGO = new Random().nextInt(PRIDE_LOGOS.length);
	private static boolean finishedInitializing = false;
	public static Identifier getLogo() {
		return Data.version.isDevelopmentBuild() ? getLogoType(Data.version.getID(), true, isPride()) : getLogoType(Data.version.getID(), false, isPride());
	}
	public static Identifier getLogoType(String namespace, boolean development, boolean pride) {
		return development ? new Identifier(namespace, (getLogoPath(pride) + "development.png")) : new Identifier(namespace, (getLogoPath(pride) + "release.png"));
	}
	public static String getPrideLogoType() {
		return ((boolean) ConfigHelper.getConfig("force_pride_type")) ? PRIDE_LOGOS[(int) ConfigHelper.getConfig("force_pride_type_index")] : PRIDE_LOGOS[PRIDE_LOGO];
	}
	public static String getLogoPath(boolean pride) {
		return pride ? "textures/gui/logo/pride/" + getPrideLogoType() + "/" : "textures/gui/logo/normal/";
	}
	public static boolean isPride() {
		if ((boolean) ConfigHelper.getConfig("force_pride")) return true;
		else {
			LocalDate date = LocalDate.now(TimeZone.getTimeZone("GMT+12").toZoneId());
			return date.getMonth() == Month.JUNE || date.getMonth() == Month.JULY && date.getDayOfMonth() <= 2;
		}
	}
	public static void finishedInitializing() {
		finishedInitializing = true;
	}
	public static boolean isFinishedInitializing() {
		return minecraft.isFinishedLoading() && finishedInitializing;
	}
}