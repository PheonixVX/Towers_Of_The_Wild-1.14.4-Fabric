package io.github.ender.towersofthewild.world.structures;

import com.google.common.collect.ImmutableMap;
import io.github.ender.towersofthewild.TowersOfTheWild;
import io.github.ender.towersofthewild.util.RegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.List;
import java.util.Random;

public class TowerPieces {
	private static final Identifier TOWER_TOP = new Identifier(TowersOfTheWild.MOD_ID, "tower_top");
	private static final Identifier TOWER_BOTTOM = new Identifier(TowersOfTheWild.MOD_ID, "tower_bottom");

	private static final Identifier TOWER_CHEST = new Identifier(TowersOfTheWild.MOD_ID, "chests/tower_chest");
	private static final ImmutableMap<Identifier, BlockPos> CENTER_TOP_OFFSETS = ImmutableMap.of(TOWER_TOP, new BlockPos(6, 28, 6), TOWER_BOTTOM, new BlockPos(3, 31, 3));
	private static final ImmutableMap<Identifier, BlockPos> CORNER_RELATIVE_POSITIONS = ImmutableMap.of(TOWER_TOP, new BlockPos(-3, 31, -3), TOWER_BOTTOM, BlockPos.ORIGIN);

	public static void addPieces(StructureManager structureManager, BlockPos absolutePos, BlockRotation rotation, List<StructurePiece> pieces, Random random, DefaultFeatureConfig config) {
		pieces.add(new Piece(structureManager, TOWER_BOTTOM, absolutePos, rotation));
		pieces.add(new Piece(structureManager, TOWER_TOP, absolutePos, rotation));
	}

	public static class Piece extends SimpleStructurePiece {
		private final Identifier structurePart;
		private final BlockRotation rotation;

		public Piece(StructureManager structureManager, Identifier structurePart, BlockPos absolutePos, BlockRotation rotation) {
			super(RegistryHandler.TOWER_PIECE, 0);
			this.structurePart = structurePart;
			BlockPos relativePos = (BlockPos) CORNER_RELATIVE_POSITIONS.get(structurePart);
			this.pos = absolutePos.add(relativePos.getX(), relativePos.getY(), relativePos.getZ());
			this.rotation = rotation;
			this.initializeStructureData(structureManager);
		}

		public Piece(StructureManager structureManager, CompoundTag tag) {
			super(RegistryHandler.TOWER_PIECE, tag);
			this.structurePart = new Identifier(tag.getString("Template"));
			this.rotation = BlockRotation.valueOf(tag.getString("Rot"));
			this.initializeStructureData(structureManager);
		}
		
		private void initializeStructureData(StructureManager structureManager) {
			Structure structure = structureManager.getStructureOrBlank(this.structurePart);
			StructurePlacementData placementData = (new StructurePlacementData()).setRotation(this.rotation).setMirrored(BlockMirror.NONE).setPosition(CENTER_TOP_OFFSETS.get(this.structurePart));
			this.setStructureData(structure, this.pos, placementData);
		}

		/**
		 * (abstract) Helper method to read subclass data from NBT
		 */
		protected void toNbt(CompoundTag tagCompound) {
			super.toNbt(tagCompound);
			tagCompound.putString("Template", this.structurePart.toString());
			tagCompound.putString("Rot", this.rotation.name());
		}

		protected void handleMetadata(String function, BlockPos pos, IWorld world, Random rand, BlockBox sbb) {
			if ("chest".equals(function)) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
				BlockEntity blockentity = world.getBlockEntity(pos.down());
				if (blockentity instanceof ChestBlockEntity) {
					((ChestBlockEntity) blockentity).setLootTable(TOWER_CHEST, rand.nextLong());
				}
			}
		}

		@Override
		public boolean generate (IWorld world, Random random, BlockBox boundingBox, ChunkPos pos) {
			StructurePlacementData placementsettings = (new StructurePlacementData().setRotation(BlockRotation.NONE).setMirrored(BlockMirror.NONE).setPosition(CENTER_TOP_OFFSETS.get(this.structurePart)));
			BlockPos relativePos = CORNER_RELATIVE_POSITIONS.get(this.structurePart);

			if (this.structurePart.equals(TOWER_BOTTOM)) {
				BlockPos blockpos1 = this.pos;

				// setting spawn height
				int height;
				int minHeight = Integer.MAX_VALUE;
				for (int i=0; i<5; ++i) {
					for (int j=0; j<5; ++j) {
						height = world.getTop(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX() + i, blockpos1.getZ() + j);
						if (height < minHeight) {
							minHeight = height;
						}
					}
				}

				// replacing dirt or water blocks beneath tower by grass
				for (int i=-1; i<6; ++i) {
					for (int j=-1; j<6; ++j) {
						BlockPos grassPos = new BlockPos(blockpos1.getX() + i, minHeight -1, blockpos1.getZ() + j);
						BlockState blockstate = world.getBlockState(grassPos);
						if (blockstate.getBlock() == Blocks.DIRT) {
							world.setBlockState(grassPos, Blocks.GRASS_BLOCK.getDefaultState(), 3);
						}

						if (!((i == -1 ||  i == 5) && (j == -1 || j == 5))) {
							if (blockstate.getBlock() == Blocks.WATER) {
								world.setBlockState(grassPos, Blocks.GRASS_BLOCK.getDefaultState(), 3);
							}
						}
					}
				}
				this.pos = this.pos.add(0, minHeight - 90, 0);
			} else if (this.structurePart.equals(TOWER_TOP)) {
				BlockPos blockpos1 = this.pos;
				int height;
				int minHeight = Integer.MAX_VALUE;
				for (int i=0; i<5; ++i) {
					for (int j=0; j<5; ++j) {
						height = world.getTop(Heightmap.Type.WORLD_SURFACE_WG, blockpos1.getX() + 3 + i, blockpos1.getZ() + 3 + j);
						if (height < minHeight) {
							minHeight = height;
						}
					}
				}
				this.pos = this.pos.add(0, minHeight - 90, 0);
			}

				return super.generate(world, random, boundingBox, pos);
		}
	}
}
