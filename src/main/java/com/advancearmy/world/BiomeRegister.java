package advancearmy.world;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import advancearmy.AdvanceArmy;
import java.util.Objects;
import net.minecraft.util.RegistryKey;
public class BiomeRegister {
	public static RegistryKey<Biome> getKey(final Biome biome) {
		return RegistryKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome), "Biome registry name was null"));
	}
    public static void biomeModification(final BiomeLoadingEvent event) {
    	Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());
		if(biome != null) {
			if(event.getCategory() == Biome.Category.NONE||event.getCategory() == Biome.Category.TAIGA
			||event.getCategory() == Biome.Category.JUNGLE||event.getCategory() == Biome.Category.EXTREME_HILLS
			||event.getCategory() == Biome.Category.PLAINS||event.getCategory() == Biome.Category.MESA
			||event.getCategory() == Biome.Category.ICY||event.getCategory() == Biome.Category.SAVANNA
			||event.getCategory() == Biome.Category.BEACH||event.getCategory() == Biome.Category.FOREST
			||event.getCategory() == Biome.Category.DESERT||event.getCategory() == Biome.Category.SWAMP
			||event.getCategory() == Biome.Category.MUSHROOM){
				final RegistryKey<Biome> biomeKey = getKey(biome);
    	        EntitySpawnRegister.addEntitySpawnToBiome(event, biomeKey);
			}
    	}
    }
}
