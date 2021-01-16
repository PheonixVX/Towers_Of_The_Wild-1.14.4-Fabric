package io.github.ender.towersofthewild.world.structures;

import com.mojang.datafixers.Dynamic;
import io.github.ender.towersofthewild.TowersOfTheWild;
import io.github.ender.towersofthewild.config.TOTWConfig;
import io.github.ender.towersofthewild.util.RegistryHandler;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;
import java.util.function.Function;

public class TowerStructure extends AbstractTempleFeature<DefaultFeatureConfig> {

	public static final String NAME = TowersOfTheWild.MOD_ID + ":tower";
	private static int FEATURE_DISTANCE;
	private static int FEATURE_SEPARATION = 5;

	public TowerStructure(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configFactory) {
		super(configFactory);
		TOTWConfig config = AutoConfig.getConfigHolder(TOTWConfig.class).getConfig();
		FEATURE_DISTANCE = config.rarity;
	}

	@Override
	public int getRadius() {
		return 3;
	}

	@Override
	protected ChunkPos getStart (ChunkGenerator<?> generator, Random random, int chunkX, int chunkZ, int offsetX, int offsetZ) {
		int chunkPosX = chunkX + FEATURE_DISTANCE * offsetX;
		int chunkPosZ = chunkZ + FEATURE_DISTANCE * offsetZ;
		int chunkPosX1 = chunkPosX < 0 ? chunkPosX - FEATURE_DISTANCE + 1 : chunkPosX;
		int chunkPosZ1 = chunkPosZ < 0 ? chunkPosZ - FEATURE_DISTANCE + 1 : chunkPosZ;
		int lvt_13_1_ = chunkPosX1 / FEATURE_DISTANCE;
		int lvt_14_1_ = chunkPosZ1 / FEATURE_DISTANCE;
		((ChunkRandom)random).setStructureSeed(generator.getSeed(), lvt_13_1_, lvt_14_1_, 16897777);
		lvt_13_1_ *= FEATURE_DISTANCE;
		lvt_14_1_ *= FEATURE_DISTANCE;
		lvt_13_1_ += random.nextInt(FEATURE_DISTANCE - FEATURE_SEPARATION);
		lvt_14_1_ += random.nextInt(FEATURE_DISTANCE - FEATURE_SEPARATION);
		return new ChunkPos(lvt_13_1_, lvt_14_1_);
	}

	@Override
	public StructureStartFactory getStructureStartFactory () {
		return Start::new;
	}

	@Override
	public String getName () {
		return NAME;
	}

	@Override
	public boolean generate (IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		return super.generate(world, generator, random, pos, config);
	}

	protected int getSeedModifier() {
		return 16897777;
	}

	public static class Start extends StructureStart {
		public Start(StructureFeature<?> structure, int chunkX, int chunkY, Biome biome, BlockBox boundingBox, int reference, long seed) {
			super(structure, chunkX, chunkY, biome, boundingBox, reference, seed);
		}

		@Override
		public void initialize (ChunkGenerator<?> generator, StructureManager structureManager, int x, int z, Biome biome) {
			DefaultFeatureConfig nofeatureconfig = (DefaultFeatureConfig)generator.getStructureConfig(biome, RegistryHandler.TOWER);
			int i = getChunkX() * 16;
			int j = getChunkZ() * 16;
			BlockPos blockpos = new BlockPos(i + 3, 90, j + 3);
			BlockRotation rotation = BlockRotation.NONE;

			if (biome.getCategory() == Biome.Category.JUNGLE) {
				JungleTowerPieces.addPieces(structureManager, blockpos, rotation, this.children, this.random, nofeatureconfig);
			} else if (biome.getCategory() == Biome.Category.ICY) {
				IceTowerPieces.addPieces(structureManager, blockpos, rotation, this.children, this.random, nofeatureconfig);
			} else {
				TOTWConfig config = AutoConfig.getConfigHolder(TOTWConfig.class).getConfig();
				if (this.random.nextInt(100) < config.derelictTowerProportion) {
					blockpos = new BlockPos(i, 90, j);
					if (biome.getCategory() == Biome.Category.PLAINS
						    || biome.getCategory() == Biome.Category.FOREST
						    || biome.getCategory() == Biome.Category.TAIGA
						    || biome.getCategory() == Biome.Category.SAVANNA
						    || biome.getCategory() == Biome.Category.EXTREME_HILLS) {
						DerelictTowerGrassPieces.addPieces(structureManager, blockpos, rotation, this.children, this.random, nofeatureconfig);
					} else {
						DerelictTowerPieces.addPieces(structureManager, blockpos, rotation, this.children, this.random, nofeatureconfig);
					}
				} else {
					TowerPieces.addPieces(structureManager, blockpos, rotation, this.children, this.random, nofeatureconfig);
				}
			}
			this.setBoundingBoxFromChildren();
		}
	}
}
