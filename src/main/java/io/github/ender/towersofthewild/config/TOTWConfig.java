package io.github.ender.towersofthewild.config;

import com.google.common.collect.Lists;
import io.github.ender.towersofthewild.TowersOfTheWild;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

import java.util.List;

@Config(name = TowersOfTheWild.MOD_ID)
public class TOTWConfig implements ConfigData {
	public static int rarity = 35;
	public static List<String> biomeBlackList = Lists.newArrayList(
		"minecraft:ocean",
		"minecraft:deep_ocean",
		"minecraft:frozen_ocean",
		"minecraft:deep_frozen_ocean",
		"minecraft:cold_ocean",
		"minecraft:deep_cold_ocean",
		"minecraft:lukewarm_ocean",
		"minecraft:deep_lukewarm_ocean",
		"minecraft:warm_ocean",
		"minecraft:deep_warm_ocean",
		"minecraft:river",
		"minecraft:frozen_river",
		"minecraft:beach",
		"minecraft:stone_shore",
		"minecraft:snowy_beach",
		"minecraft:nether",
		"minecraft:the_end",
		"minecraft:small_end_islands",
		"minecraft:end_midlands",
		"minecraft:end_highlands",
		"minecraft:end_barrens",
		"minecraft:the_void"
	);
	public static int derelictTowerProportion = 15;
}
