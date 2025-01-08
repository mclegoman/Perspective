/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.client.texture.TextureHelper;
import com.mclegoman.perspective.common.data.Data;
import com.mclegoman.luminance.common.util.IdentifierHelper;
import com.mclegoman.perspective.config.ConfigHelper;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TexturedEntity {
	private static final List<Identifier> forbiddenEntities = new ArrayList<>();
	public static List<Identifier> getForbiddenEntities() {
		return forbiddenEntities;
	}
	public static void addForbiddenEntity(Identifier entityId) {
		forbiddenEntities.add(entityId);
	}
	public static boolean isForbiddenEntity(Identifier entityId) {
		return forbiddenEntities.contains(entityId);
	}
	public static Identifier getEntityTypeId(EntityType<?> entityType) {
		return Registries.ENTITY_TYPE.getId(entityType);
	}
	private static void addDefaultForbiddenEntities() {
		try {
			// This prevents users from trying to use textured entity features on players. Use appearance instead.
			addForbiddenEntity(Registries.ENTITY_TYPE.getId(EntityType.PLAYER));
			// The dragon is simply just not compatible, if it ever becomes compatible, this can be removed.
			addForbiddenEntity(Registries.ENTITY_TYPE.getId(EntityType.ENDER_DRAGON));
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to add default forbidden textured entities: {}", error));
		}
	}
	public static void init() {
		try {
			addDefaultForbiddenEntities();
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new TexturedEntityDataLoader());
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to initialize textured entity: {}", error));
		}
	}
	private static Identifier getOverrideTexture(String prefix, String suffix, JsonArray overrides, Identifier fallback) {
		if (!overrides.isEmpty()) {
			for (JsonElement element : overrides) {
				String entityPrefix = JsonHelper.getString((JsonObject) element, "prefix", "");
				String entitySuffix = JsonHelper.getString((JsonObject) element, "suffix", "");
				String entityTexture = JsonHelper.getString((JsonObject) element, "texture", IdentifierHelper.stringFromIdentifier(fallback));
				String entityTextureNamespace = entityTexture.contains(":") ? entityTexture.substring(0, entityTexture.lastIndexOf(":")) : "minecraft";
				String entityTexturePath = entityTexture.contains(":") ? entityTexture.substring(entityTexture.lastIndexOf(":") + 1) : entityTexture;
				if (prefix.equals(entityPrefix) && suffix.equals(entitySuffix)) return Identifier.of(entityTextureNamespace, entityTexturePath.endsWith(".png") ? entityTexturePath : entityTexturePath + ".png");
			}
		}
		return fallback;
	}
	private static Identifier getOverrideTexture(JsonArray overrides, Identifier fallback) {
		return getOverrideTexture("", "", overrides, fallback);
	}
	public static Identifier getTexture(Entity entity, Identifier fallback) {
		return getTexture(entity, "", "", "", fallback);
	}
	public static Identifier getTexture(Entity entity, String overrideNamespace, Identifier fallback) {
		return getTexture(entity, overrideNamespace, "", "", fallback);
	}
	public static Identifier getTexture(Entity entity, String prefix, String suffix, Identifier fallback) {
		return getTexture(entity, "", prefix, suffix, fallback);
	}
	public static Identifier getTexture(Entity entity, String overrideNamespace, String prefix, String suffix, Identifier fallback) {
		try {
			if (TexturedEntityDataLoader.isReady) {
				Identifier entityType = Registries.ENTITY_TYPE.getId(entity.getType());
				String namespace = fallback.getNamespace();
				if (!overrideNamespace.isEmpty()) namespace = overrideNamespace;
				Optional<TexturedEntityData> entityData = getEntity(entity);
				if (entityData.isPresent()) {
					boolean shouldReplaceTexture = true;
						if (entity instanceof LivingEntity) {
							JsonObject entitySpecific = entityData.get().getEntitySpecific();
							if (entitySpecific != null) {
								if (entitySpecific.has("ages")) {
									JsonObject ages = JsonHelper.getObject(entitySpecific, "ages", new JsonObject());
									if (((LivingEntity)entity).isBaby()) {
										if (ages.has("baby")) {
											JsonObject typeRegistry = JsonHelper.getObject(ages, "baby", new JsonObject());
											shouldReplaceTexture = JsonHelper.getBoolean(typeRegistry, "enabled", true);
										}
									} else {
										if (ages.has("adult")) {
											JsonObject typeRegistry = JsonHelper.getObject(ages, "adult", new JsonObject());
											shouldReplaceTexture = JsonHelper.getBoolean(typeRegistry, "enabled", true);
										}
									}
								}
							}
						}
					if (shouldReplaceTexture) return TextureHelper.getTexture(getOverrideTexture(prefix, suffix, entityData.get().getOverrides(), Identifier.of(namespace, "textures/textured_entity/" + entityType.getNamespace() + "/" + entityType.getPath() + "/" + (prefix + entityData.get().getName().toLowerCase() + suffix) + ".png")), fallback);
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to set textured entity texture: {}", error));
		}
		return fallback;
	}
	private static List<TexturedEntityData> getRegistry(String namespace, String entity_type) {
		List<TexturedEntityData> entityRegistry = new ArrayList<>();
		try {
			for (TexturedEntityData registry : TexturedEntityDataLoader.getRegistry()) {
				if (registry.getNamespace().equals(namespace) && registry.getType().equals(entity_type)) entityRegistry.add(registry);
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to get textured entity string registry: {}", error));
		}
		return entityRegistry;
	}
	public static Optional<TexturedEntityData> getEntity(Entity entity) {
		return getEntity(entity, getEntityTypeId(entity.getType()));
	}
	private static Optional<String> getEntityName(Entity entity) {
		if (entity.getCustomName() != null) return Optional.of(entity.getCustomName().getString());
		else return Optional.of("default");
	}
	private static Optional<TexturedEntityData> getEntityData(List<TexturedEntityData> registry, String entityName) {
		for (TexturedEntityData entityData : registry) {
			if (entityName.equals(entityData.getName())) {
				if (entityData.getEnabled()) return Optional.of(entityData);
			}
		}
		return Optional.empty();
	}
	public static Optional<Identifier> getEntitySpecificModel(Entity entity) {
		if (entity != null) {
			Optional<TexturedEntityData> entityData = getEntity(entity);
			if (entityData.isPresent()) {
				JsonObject entitySpecific = entityData.get().getEntitySpecific();
				if (entitySpecific != null) {
					if (entitySpecific.has("model")) {
						return Optional.of(Identifier.of(JsonHelper.getString(entitySpecific, "model").toLowerCase()));
					}
				}
			}
		}
		return Optional.of(Identifier.of(Data.version.getID(), "default"));
	}
	private static Optional<TexturedEntityData> getEntity(Entity entity, Identifier entityId) {
		try {
			if (!isForbiddenEntity(getEntityTypeId(entity.getType()))) {
				List<TexturedEntityData> registry = getRegistry(IdentifierHelper.getStringPart(IdentifierHelper.Type.NAMESPACE, IdentifierHelper.stringFromIdentifier(entityId)), IdentifierHelper.getStringPart(IdentifierHelper.Type.KEY, IdentifierHelper.stringFromIdentifier(entityId)));
				if (TexturedEntityDataLoader.isReady && !registry.isEmpty()) {
					Optional<String> entityName = getEntityName(entity);
					if (entityName.isPresent()) {
						if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_named_entity")) {
							Optional<TexturedEntityData> entityData = getEntityData(registry, entityName.get());
							if (entityData.isPresent()) return entityData;
						}
						if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_random_entity")) {
							TexturedEntityData entityData = registry.get(Math.floorMod(entity.getUuid().getLeastSignificantBits(), registry.size()));
							if (entityData.getEnabled()) return Optional.of(entityData);
						}
						if ((boolean) ConfigHelper.getConfig(ConfigHelper.ConfigType.normal, "textured_named_entity")) {
							// If the entity texture isn't replaced by the previous checks, it doesn't have a valid textured entity,
							// so we return the default textured entity if it exists.
							if (!entityName.get().equalsIgnoreCase("default")) {
								Optional<TexturedEntityData> entityData = getEntityData(registry, "default");
								if (entityData.isPresent()) return entityData;
							}
						}
					}
				}
			}
		} catch (Exception error) {
			Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to get textured entity entity specific data: {}", error));
		}
		return Optional.empty();
	}
}