package io.github.ender.towersofthewild.world;

import io.github.ender.towersofthewild.TowersOfTheWild;
import io.github.ender.towersofthewild.config.TOTWConfig;
import io.github.ender.towersofthewild.util.RegistryHandler;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class WorldInit {

	public static void setup() {
		for (Biome biome : Biome.BIOMES) {
			TOTWConfig config = AutoConfig.getConfigHolder(TOTWConfig.class).getConfig();
			if (!config.biomeBlackList.contains(biome.getName().toString())) {
				addSurfaceStructure(biome, RegistryHandler.TOWER);
			}
		}
	}

	private static void addSurfaceStructure(Biome biome, StructureFeature<DefaultFeatureConfig> structure) {
		biome.addStructureFeature(structure, new DefaultFeatureConfig());
		biome.addFeature(
			GenerationStep.Feature.SURFACE_STRUCTURES,
			Biome.configureFeature(
				structure,
				FeatureConfig.DEFAULT, // FeatureConfig.DEFAULT?
				Decorator.NOPE,
				DecoratorConfig.DEFAULT
			)
		);
	}
}
