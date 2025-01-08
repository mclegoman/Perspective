/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.contributor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.luminance.common.util.IdentifierHelper;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContributorDataLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
	public static final List<ContributorData> registry = new ArrayList<>();
	public static final String id = "contributors";
	public ContributorDataLoader() {
		super(new Gson(), id);
	}
	private void add(List<JsonElement> inputIds, String uuid, boolean shouldFlipUpsideDown, boolean shouldReplaceCape, String capeTexture, boolean shouldRenderHeadItem, String headItem, boolean shouldRenderOverlay, String overlayTexture, boolean isOverlayEmissive) {
		try {
			ContributorLockData lockData = Contributor.getUuid(uuid);
			if (lockData != null) {
				registry.removeIf(contributorData -> contributorData.getUuid().equals(uuid));
				List<String> outputIds = new ArrayList<>();
				for (JsonElement id : inputIds) outputIds.add(id.getAsString());
				registry.add(ContributorData.builder(uuid).id(outputIds).type(lockData.getType().getName()).shouldFlipUpsideDown(shouldFlipUpsideDown).shouldReplaceCape(shouldReplaceCape).capeTexture(IdentifierHelper.identifierFromString(capeTexture)).shouldRenderHeadItem(shouldRenderHeadItem).headItem(IdentifierHelper.identifierFromString(headItem)).shouldRenderOverlay(shouldRenderOverlay).overlayTexture(IdentifierHelper.identifierFromString(overlayTexture), isOverlayEmissive).build());
			} else {
				Data.version.sendToLog(LogType.WARN, Translation.getString("{} is not permitted to use contributor dataloader!", uuid));
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to add contributor to contributor registry: {}", error));
		}
	}
	private void reset() {
		try {
			registry.clear();
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to reset contributor registry: {}", error));
		}
	}
	@Override
	public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		try {
			reset();
			prepared.forEach(this::layout$perspective);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to apply contributor dataloader: {}", error));
		}
	}
	private void layout$perspective(Identifier identifier, JsonElement jsonElement) {
		try {
			JsonObject reader = jsonElement.getAsJsonObject();
			JsonArray defaultIds = new JsonArray();
			defaultIds.add(identifier.getPath());
			JsonArray ids = JsonHelper.getArray(reader, "ids", defaultIds);
			List<JsonElement> id = ids.asList();
			JsonArray uuids = JsonHelper.getArray(reader, "uuids");
			boolean shouldFlipUpsideDown = JsonHelper.getBoolean(reader, "shouldFlipUpsideDown", false);
			boolean shouldReplaceCape = JsonHelper.getBoolean(reader, "shouldReplaceCape", false);
			String capeTexture = JsonHelper.getString(reader, "capeTexture", "perspective:textures/contributors/cape/dev_oneyear");
			boolean shouldRenderHeadItem = JsonHelper.getBoolean(reader, "shouldRenderHeadItem", false);
			String headItem = JsonHelper.getString(reader, "headItem", "minecraft:air");
			boolean shouldRenderOverlay = JsonHelper.getBoolean(reader, "shouldRenderOverlay", false);
			String overlayTexture = JsonHelper.getString(reader, "overlayTexture", "none");
			boolean isOverlayEmissive = JsonHelper.getBoolean(reader, "isOverlayEmissive", false);
			for (JsonElement uuid : uuids) add(id, uuid.getAsString(), shouldFlipUpsideDown, shouldReplaceCape, capeTexture, shouldRenderHeadItem, headItem, shouldRenderOverlay, overlayTexture, isOverlayEmissive);
		} catch (Exception error) {
			Data.version.sendToLog(LogType.WARN, Translation.getString("Failed to load contributor from dataloader: {}", error));
		}
	}
	@Override
	public Identifier getFabricId() {
		return Identifier.of(Data.version.getID(), id);
	}
}