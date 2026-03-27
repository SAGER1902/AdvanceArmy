package advancearmy.world;

import advancearmy.AdvanceArmy;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import advancearmy.AAConfig;
public class EntitySpawnRegister {
	public static void addEntitySpawnToBiome(BiomeLoadingEvent event, RegistryKey<Biome> biomeKey) {
		if(AAConfig.COMMON.spawn.spawn_army.get()){
			if(AAConfig.COMMON.spawn.ranger_count.get()>0)event.getSpawns().addSpawn(EntityClassification.CREATURE, new Spawners(AdvanceArmy.ENTITY_SOLDIER, AAConfig.COMMON.spawn.ranger_count.get(), 1, 1));
			if(AAConfig.COMMON.spawn.conscript_count.get()>0)event.getSpawns().addSpawn(EntityClassification.CREATURE, new Spawners(AdvanceArmy.ENTITY_CONS, AAConfig.COMMON.spawn.conscript_count.get(), 1, 1));
		}
		if(AAConfig.COMMON.spawn.spawn_eromob.get()){
			if(AAConfig.COMMON.spawn.ero_zombie_count.get()>0)event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(AdvanceArmy.ENTITY_EZOMBIE, AAConfig.COMMON.spawn.ero_zombie_count.get(), 1, 1));
			if(AAConfig.COMMON.spawn.ero_skeleton_count.get()>0)event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(AdvanceArmy.ENTITY_SKELETON, AAConfig.COMMON.spawn.ero_skeleton_count.get(), 1, 5));
			if(AAConfig.COMMON.spawn.ero_creeper_count.get()>0)event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(AdvanceArmy.ENTITY_CREEPER, AAConfig.COMMON.spawn.ero_creeper_count.get(), 1, 3));
			if(AAConfig.COMMON.spawn.ero_reb_count.get()>0){
				event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(AdvanceArmy.ENTITY_REB, AAConfig.COMMON.spawn.ero_reb_count.get(), 1, 1));
				event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(AdvanceArmy.ENTITY_BIKE, AAConfig.COMMON.spawn.ero_reb_count.get(), 1, 5));
			}
			if(AAConfig.COMMON.spawn.ero_pillager_count.get()>0)event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(AdvanceArmy.ENTITY_PI, AAConfig.COMMON.spawn.ero_pillager_count.get(), 1, 1));
			if(AAConfig.COMMON.spawn.ero_ghost_count.get()>0)event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(AdvanceArmy.ENTITY_GST, AAConfig.COMMON.spawn.ero_ghost_count.get(), 1, 1));
			if(AAConfig.COMMON.spawn.ero_phantom_count.get()>0)event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(AdvanceArmy.ENTITY_PHA, AAConfig.COMMON.spawn.ero_phantom_count.get(), 1, 6));
			if(AAConfig.COMMON.spawn.ero_husk_count.get()>0)event.getSpawns().addSpawn(EntityClassification.MONSTER, new Spawners(AdvanceArmy.ENTITY_EHUSK, AAConfig.COMMON.spawn.ero_husk_count.get(), 1, 6));
		}
	}
}
