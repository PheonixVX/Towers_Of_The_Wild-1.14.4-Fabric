package io.github.ender.towersofthewild.util;

import io.github.ender.towersofthewild.TowersOfTheWild;
import io.github.ender.towersofthewild.world.structures.*;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Locale;

public class RegistryHandler {

	public static StructureFeature<DefaultFeatureConfig> TOWER = Registry.register(
		Registry.FEATURE,
		"tower",
		new TowerStructure(DefaultFeatureConfig::deserialize)
	);

	public static final StructureFeature<?> TOWER_STRUCTURE = Registry.register(
		Registry.STRUCTURE_FEATURE,
		"tower",
		TOWER
	);

	public static void initialize() {
		TowersOfTheWild.LOGGER.info("Registering structure");
		Feature.STRUCTURES.put("Tower".toLowerCase(Locale.ROOT), TOWER);
	}

	// Structure Pieces
	public static final StructurePieceType TOWER_PIECE = registerPiece("tower", TowerPieces.Piece::new);
	public static final StructurePieceType JUNGLE_TOWER_PIECE = registerPiece("jungle_tower", JungleTowerPieces.Piece::new);
	public static final StructurePieceType ICE_TOWER_PIECE = registerPiece("ice_tower", IceTowerPieces.Piece::new);
	public static final StructurePieceType DERELICT_TOWER_PIECE = registerPiece("derelict_tower", DerelictTowerPieces.Piece::new);
	public static final StructurePieceType DERELICT_TOWER_GRASS_PIECE = registerPiece("derelict_tower_grass", DerelictTowerGrassPieces.Piece::new);

	private static StructurePieceType registerPiece(String key, StructurePieceType type) {
		TowersOfTheWild.LOGGER.info(key + " structure piece registered");
		return Registry.register(Registry.STRUCTURE_PIECE, new Identifier(TowersOfTheWild.MOD_ID, key), type);
	}
}
