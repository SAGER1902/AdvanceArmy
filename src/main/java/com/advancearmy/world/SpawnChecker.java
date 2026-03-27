package advancearmy.world;

import advancearmy.entity.EntitySA_SoldierBase;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.IServerWorld;

import java.util.Random;
import net.minecraft.entity.CreatureEntity;
public class SpawnChecker {

	public static boolean canNightSpawn(EntityType<? extends CreatureEntity> livingType, IServerWorld worldIn,
			SpawnReason reason, BlockPos pos, Random rand) {
		return checkSpawnNight(livingType, worldIn, reason, pos, rand);
	}
	public static boolean canNormalSpawn(EntityType<? extends CreatureEntity> livingType, IWorld worldIn,
			SpawnReason reason, BlockPos pos, Random rand) {
		return checkSpawnNormal(livingType, worldIn, reason, pos, rand);
	}
	public static boolean canSunSpawn(EntityType<? extends CreatureEntity> livingType, IWorld worldIn,
			SpawnReason reason, BlockPos pos, Random rand) {
		return checkSpawnSun(livingType, worldIn, reason, pos, rand);
	}

	public static boolean canHardSpawn(EntityType<? extends CreatureEntity> livingType, IServerWorld worldIn,
										 SpawnReason reason, BlockPos pos, Random rand) {
		return worldIn.getDifficulty() == Difficulty.HARD && checkSpawnNight(livingType, worldIn, reason, pos, rand);
	}
	
	public static boolean isDarkEnoughToSpawn(IServerWorld worldIn, BlockPos pos, Random rand) {
		if (worldIn.getBrightness(LightType.SKY, pos) > rand.nextInt(32)) {
			return false;
		} else {
			int i = worldIn.getLevel().isThundering() ? worldIn.getMaxLocalRawBrightness(pos, 10) : worldIn.getMaxLocalRawBrightness(pos);
			return i <= rand.nextInt(8);
		}
	}

	private static boolean checkSpawnNight(EntityType<? extends CreatureEntity> livingType, IServerWorld worldIn, SpawnReason reason, BlockPos pos, Random rand) {
		/*boolean isFlatWorld = worldIn.getLevel().getChunkSource().getGenerator() instanceof FlatChunkGenerator;
		if (isFlatWorld) {
			return rand.nextInt(8)==0 && MobEntity.checkMobSpawnRules(livingType, worldIn, reason, pos, rand);
		} else */{
			return isDarkEnoughToSpawn(worldIn, pos, rand) && MobEntity.checkMobSpawnRules(livingType, worldIn, reason, pos, rand);
		}
	}
	
	private static boolean checkSpawnNormal(EntityType<? extends CreatureEntity> livingType, IWorld worldIn, SpawnReason reason, BlockPos pos, Random rand) {
		return worldIn.getRawBrightness(pos, 0) < 8 && MobEntity.checkMobSpawnRules(livingType, worldIn, reason, pos, rand);
	}
	
	private static boolean checkSpawnSun(EntityType<? extends CreatureEntity> livingType, IWorld worldIn, SpawnReason reason, BlockPos pos, Random rand) {
		return worldIn.getRawBrightness(pos, 0) > 7 && MobEntity.checkMobSpawnRules(livingType, worldIn, reason, pos, rand);
	}
}
