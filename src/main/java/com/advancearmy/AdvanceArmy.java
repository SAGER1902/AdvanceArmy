package advancearmy;
import advancearmy.entity.land.EntitySA_BMP2;
import advancearmy.entity.land.EntitySA_BMPT;
import advancearmy.entity.land.EntitySA_M109;
import advancearmy.entity.land.EntitySA_M2A2;
import advancearmy.entity.land.EntitySA_M2A2AA;
import advancearmy.entity.land.EntitySA_Bike;
import advancearmy.entity.land.EntitySA_Car;
import advancearmy.entity.land.EntitySA_T55;
import advancearmy.entity.land.EntitySA_FTK;
import advancearmy.entity.land.EntitySA_Hmmwv;
import advancearmy.entity.land.EntitySA_99G;
import advancearmy.entity.land.EntitySA_FTK_H;
import advancearmy.entity.land.EntitySA_LaserAA;
import advancearmy.entity.land.EntitySA_MASTDOM;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.land.EntitySA_Reaper;
import advancearmy.entity.land.EntitySA_LAV;
import advancearmy.entity.land.EntitySA_LAVAA;
import advancearmy.entity.land.EntitySA_Ember;
import advancearmy.entity.land.EntitySA_Sickle;
import advancearmy.entity.land.EntitySA_Tank;
import advancearmy.entity.land.EntitySA_Prism;
import advancearmy.entity.land.EntitySA_MMTank;
import advancearmy.entity.land.EntitySA_Mirage;
import advancearmy.entity.land.EntitySA_Tesla;
import advancearmy.entity.land.EntitySA_APAGAT;
import advancearmy.entity.soldier.EntitySA_GAT;
import advancearmy.entity.map.CreatureRespawn;
import advancearmy.entity.map.ArmyMovePoint;
import advancearmy.entity.map.VehicleRespawn;
import advancearmy.entity.map.SupportPoint;
import advancearmy.entity.map.RewardBox;
import advancearmy.entity.map.DefencePoint;
import advancearmy.entity.building.SandBag;
import advancearmy.render.SupportRenderer;
import advancearmy.render.DefenceRenderer;

import advancearmy.entity.building.SoldierMachine;
import advancearmy.entity.building.VehicleMachine;
import advancearmy.render.DefaultRender;
import advancearmy.render.RenderReaper;
import advancearmy.render.BoxRenderer;
import advancearmy.render.RenderRADS;
import advancearmy.render.Render99;
import advancearmy.render.RenderMirage;
import advancearmy.render.RenderGAT;
import advancearmy.entity.soldier.EntitySA_OFG;
import advancearmy.entity.soldier.EntitySA_RADS;

import advancearmy.entity.turret.EntitySA_Mortar;

import advancearmy.entity.turret.EntitySA_TOW;
import advancearmy.entity.turret.EntitySA_STIN;

import advancearmy.entity.turret.EntitySA_M2hb;
import advancearmy.entity.turret.EntitySA_Kord;

import advancearmy.entity.air.EntitySA_AH6;
import advancearmy.entity.air.EntitySA_MI24;
import advancearmy.entity.air.EntitySA_AH1Z;
import advancearmy.entity.air.EntitySA_Plane;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.entity.air.EntitySA_Fw020;
import advancearmy.entity.air.EntitySA_Lapear;
import advancearmy.entity.air.EntitySA_YouHun;
import advancearmy.entity.air.EntitySA_Yw010;

import advancearmy.entity.air.EntitySA_A10a;
import advancearmy.entity.air.EntitySA_A10c;

import advancearmy.entity.air.EntitySA_F35;
import advancearmy.entity.air.EntitySA_SU33;
import advancearmy.entity.air.EntitySA_Helicopter;

import advancearmy.entity.sea.EntitySA_BattleShip;
import advancearmy.entity.soldier.EntitySA_GI;
import advancearmy.entity.soldier.EntitySA_Conscript;
import advancearmy.entity.soldier.EntitySA_ConscriptX;
import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.entity.soldier.EntitySA_Swun;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.mob.EntityAohuan;
import advancearmy.entity.mob.ERO_Pillager;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Husk;
import advancearmy.entity.mob.ERO_Skeleton;
import advancearmy.entity.mob.ERO_Creeper;
import advancearmy.entity.mob.DragonTurret;
import advancearmy.entity.mob.ERO_REB;
import advancearmy.entity.mob.ERO_Phantom;
import advancearmy.entity.mob.ERO_Ghast;
import advancearmy.entity.mob.EvilPortal;

import advancearmy.render.RenderAH6;
import advancearmy.render.RenderOFG;
import advancearmy.render.RenderTurretBase;

import advancearmy.render.RenderAohuan;
import advancearmy.render.RenderEmber;
import advancearmy.render.RenderSwun;
import advancearmy.render.RenderGI;
import advancearmy.render.RenderYw010;

import advancearmy.render.ZombieRenderer;
import advancearmy.render.SkeletonRenderer;
import advancearmy.render.CreeperRenderer;
import advancearmy.render.EvilPhantomRenderer;
import advancearmy.render.EvilGhastRenderer;
import advancearmy.render.PortalRenderer;

import advancearmy.render.RenderREB;
import advancearmy.render.RenderPillager;

import advancearmy.render.RenderDragonTurret;
import advancearmy.render.RenderBike;
import advancearmy.render.RenderFTK_H;
import advancearmy.render.RenderLAV;
import advancearmy.render.RenderBattleShip;
import advancearmy.render.RenderSeat;

import advancearmy.render.MachineRendererV;
import advancearmy.render.MachineRendererS;
import advancearmy.render.RenderMASTDOM;
import advancearmy.render.RenderTankBase;
import advancearmy.render.RenderSoldier;
import advancearmy.render.RenderTank;
import advancearmy.render.RenderSickle;
import advancearmy.render.RenderHeliBase;
import advancearmy.render.RenderAirBase;

import advancearmy.render.RenderConscript;
import advancearmy.render.RenderConscriptX;
import advancearmy.item.ItemSpawn;
import advancearmy.item.ItemSpawnMob;
import advancearmy.item.ItemSupport;
import advancearmy.item.ItemRemove;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.eventbus.api.EventPriority;
import advancearmy.world.BiomeRegister;
import advancearmy.world.SpawnChecker;
import advancearmy.event.SAEntityEvent;
import advancearmy.event.SASoundEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wmlib.client.obj.SAObjModel;

import advancearmy.item.ItemGun_Target;
import advancearmy.item.ItemDefence;
import wmlib.common.item.ItemGun;
import wmlib.common.item.ItemGun_Custom;

import wmlib.client.obj.SAObjModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;

import net.minecraft.world.gen.Heightmap;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;

@Mod(AdvanceArmy.MOD_ID)
public class AdvanceArmy {
    public static final String MOD_ID = "advancearmy";
	public static final Logger LOGGER = LogManager.getLogger(AdvanceArmy.MOD_ID);
    public static final ItemGroup GROUP_UNIT = new ItemGroup(MOD_ID)
    {
        @Override
        public ItemStack makeIcon()
        {
            ItemStack stack = new ItemStack(AdvanceArmy.item_spawn_tank);
            return stack;
        }
        @Override
        public void fillItemList(NonNullList<ItemStack> items)
        {
            super.fillItemList(items);
        }
    };
    public static final ItemGroup GROUP_ITEM = new ItemGroup(MOD_ID)
    {
        @Override
        public ItemStack makeIcon()
        {
            ItemStack stack = new ItemStack(AdvanceArmy.support_f35bomb);
            return stack;
        }
        @Override
        public void fillItemList(NonNullList<ItemStack> items)
        {
            super.fillItemList(items);
        }
    };
    public static EntityType<ERO_Phantom> ENTITY_PHA;
	public static EntityType<EvilPortal> ENTITY_POR;
    public static EntityType<ERO_Ghast> ENTITY_GST;
    public static EntityType<DragonTurret> ENTITY_DT;
    public static EntityType<EntitySA_Soldier> ENTITY_SOLDIER;
    public static EntityType<ERO_REB> ENTITY_REB;
	public static EntityType<ERO_Pillager> ENTITY_PI;
	public static EntityType<ERO_Zombie> ENTITY_EZOMBIE;
	public static EntityType<ERO_Husk> ENTITY_EHUSK;
	public static EntityType<ERO_Skeleton> ENTITY_SKELETON;
	public static EntityType<ERO_Creeper> ENTITY_CREEPER;
	public static EntityType<DefencePoint> ENTITY_DPT;
	
    public static EntityType<EntitySA_Conscript> ENTITY_CONS;
	public static EntityType<EntitySA_ConscriptX> ENTITY_CONSX;
	public static EntityType<EntitySA_Seat> ENTITY_SEAT;
    public static EntityType<EntitySA_Helicopter> ENTITY_HELI;
	public static EntityType<EntitySA_Plane> ENTITY_PLANE;
	public static EntityType<EntitySA_Plane1> ENTITY_PLANE1;
	public static EntityType<EntitySA_Plane2> ENTITY_PLANE2;
	
	public static EntityType<EntitySA_A10a> ENTITY_A10A;
	public static EntityType<EntitySA_A10c> ENTITY_A10C;
	
	public static EntityType<EntitySA_F35> ENTITY_F35;
	public static EntityType<EntitySA_SU33> ENTITY_SU33;
	public static EntityType<EntitySA_LaserAA> ENTITY_LAA;
	public static EntityType<EntitySA_MASTDOM> ENTITY_MAST;
	public static EntityType<EntitySA_Tank> ENTITY_TANK;
	
	public static EntityType<EntitySA_YouHun> ENTITY_YOUHUN;
	public static EntityType<EntitySA_Ember> ENTITY_EMBER;
	public static EntityType<EntitySA_Yw010> ENTITY_YW010;
	public static EntityType<EntitySA_Fw020> ENTITY_FW020;
	public static EntityType<EntitySA_Lapear> ENTITY_LAPEAR;
	public static EntityType<EntityAohuan> ENTITY_AOHUAN;
	public static EntityType<EntitySA_Swun> ENTITY_SWUN;
	public static EntityType<EntitySA_GI> ENTITY_GI;
	public static EntityType<EntitySA_RADS> ENTITY_RADS;
	
	public static EntityType<EntitySA_BMP2> ENTITY_BMP2;
	public static EntityType<EntitySA_BMPT> ENTITY_BMPT;
	public static EntityType<EntitySA_M109> ENTITY_M109;
	public static EntityType<EntitySA_M2A2> ENTITY_M2A2;
	public static EntityType<EntitySA_M2A2AA> ENTITY_M2A2AA;
	public static EntityType<EntitySA_Bike> ENTITY_BIKE;
	public static EntityType<EntitySA_Car> ENTITY_CAR;
	public static EntityType<EntitySA_T55> ENTITY_T55;
	public static EntityType<EntitySA_FTK> ENTITY_FTK;
	public static EntityType<EntitySA_Hmmwv> ENTITY_HMMWV;
	public static EntityType<EntitySA_99G> ENTITY_99G;
	public static EntityType<EntitySA_FTK_H> ENTITY_FTK_H;
	public static EntityType<EntitySA_AH1Z> ENTITY_AH1Z;
	
	public static EntityType<CreatureRespawn> ENTITY_CRES;
	public static EntityType<SupportPoint> ENTITY_SPT;
	public static EntityType<RewardBox> ENTITY_RBOX;
	public static EntityType<ArmyMovePoint> ENTITY_MOVEP;
	public static EntityType<VehicleRespawn> ENTITY_VRES;
	public static EntityType<SandBag> ENTITY_SANDBAG;
	
	public static EntityType<SoldierMachine> ENTITY_SMAC;
	public static EntityType<VehicleMachine> ENTITY_VMAC;
	
	public static EntityType<EntitySA_AH6> ENTITY_AH6;
	public static EntityType<EntitySA_MI24> ENTITY_MI24;
	public static EntityType<EntitySA_OFG> ENTITY_OFG;
	public static EntityType<EntitySA_Mortar> ENTITY_MORTAR;
	public static EntityType<EntitySA_TOW> ENTITY_TOW;
	public static EntityType<EntitySA_STIN> ENTITY_STIN;
	public static EntityType<EntitySA_M2hb> ENTITY_M2HB;
	public static EntityType<EntitySA_Kord> ENTITY_KORD;	
	
	public static EntityType<EntitySA_T72> ENTITY_T72;
	public static EntityType<EntitySA_T90> ENTITY_T90;
	
	public static EntityType<EntitySA_LAV> ENTITY_LAV;
	public static EntityType<EntitySA_LAVAA> ENTITY_LAVAA;
	public static EntityType<EntitySA_BattleShip> ENTITY_BSHIP;
	public static EntityType<EntitySA_Sickle> ENTITY_SICKLE;
	public static EntityType<EntitySA_Prism> ENTITY_PRISM;
	public static EntityType<EntitySA_Reaper> ENTITY_REAPER;
	public static EntityType<EntitySA_APAGAT> ENTITY_APAGAT;
	public static EntityType<EntitySA_MMTank> ENTITY_MMTANK;
	public static EntityType<EntitySA_GAT> ENTITY_GAT;
	
	public static EntityType<EntitySA_Mirage> ENTITY_MIRAGE;
	public static EntityType<EntitySA_Tesla> ENTITY_TESLA;
	
    public AdvanceArmy() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AAConfig.commonSpec);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new SAEntityEvent());
		modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::initClient);
		SASoundEvent.REGISTER.register(modEventBus);
		forgeBus.addListener(EventPriority.HIGH, BiomeRegister::biomeModification);
	}

	public void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			EntitySpawnPlacementRegistry.register(
				ENTITY_SOLDIER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canSunSpawn);

			EntitySpawnPlacementRegistry.register(
				ENTITY_CONS, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canSunSpawn);
				
			EntitySpawnPlacementRegistry.register(
				ENTITY_EHUSK, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
				
			EntitySpawnPlacementRegistry.register(
				ENTITY_EZOMBIE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canNightSpawn);

			EntitySpawnPlacementRegistry.register(
				ENTITY_CREEPER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canNightSpawn);

			EntitySpawnPlacementRegistry.register(
				ENTITY_SKELETON, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canNightSpawn);

			EntitySpawnPlacementRegistry.register(
				ENTITY_REB, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canNightSpawn);
				
			EntitySpawnPlacementRegistry.register(
				ENTITY_BIKE, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canNightSpawn);
				
			EntitySpawnPlacementRegistry.register(
				ENTITY_PI, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canNightSpawn);

			EntitySpawnPlacementRegistry.register(
				ENTITY_GST, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canNightSpawn);

			EntitySpawnPlacementRegistry.register(
				ENTITY_PHA, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, SpawnChecker::canNightSpawn);
		});
	}

    private void initClient(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_SMAC, MachineRendererS::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_VMAC, MachineRendererV::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_CRES, DefaultRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_RBOX, BoxRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_SPT, SupportRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_DPT, DefenceRenderer::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_MOVEP, DefaultRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_VRES, DefaultRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_SANDBAG, DefaultRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_SEAT, RenderSeat::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_SOLDIER, RenderSoldier::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_DT, RenderDragonTurret::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_POR, PortalRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_PHA, EvilPhantomRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_REB, RenderREB::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_PI, RenderPillager::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_CONS, RenderConscript::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_CONSX, RenderConscriptX::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_GST, EvilGhastRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_EZOMBIE, ZombieRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_EHUSK, ZombieRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_SKELETON, SkeletonRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_CREEPER, CreeperRenderer::new);
		
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_RADS, RenderRADS::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_SWUN, RenderSwun::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_AOHUAN, RenderAohuan::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_GI, RenderGI::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_YW010, RenderYw010::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_YOUHUN, RenderHeliBase::new);
		
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_EMBER, RenderEmber::new);
		
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_LAPEAR, RenderAirBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_FW020, RenderAirBase::new);
		
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_GAT, RenderGAT::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_APAGAT, RenderTankBase::new);
		
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_MMTANK, RenderTankBase::new);
		
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_MIRAGE, RenderMirage::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_TESLA, RenderTankBase::new);
		
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_BMP2, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_BMPT, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_M109, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_M2A2, RenderTankBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_M2A2AA, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_CAR, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_T55, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_T72, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_T90, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_FTK, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_HMMWV, RenderTankBase::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_99G, Render99::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_AH1Z, RenderHeliBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_HELI, RenderHeliBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_AH6, RenderAH6::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_MI24, RenderHeliBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_OFG, RenderOFG::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_MORTAR, RenderTurretBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_TOW, RenderTurretBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_STIN, RenderTurretBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_M2HB, RenderTurretBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_KORD, RenderTurretBase::new);
		
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_PLANE, RenderAirBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_PLANE1, RenderAirBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_PLANE2, RenderAirBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_F35, RenderAirBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_SU33, RenderAirBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_A10A, RenderAirBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_A10C, RenderAirBase::new);
		
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_FTK_H, RenderFTK_H::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_BIKE, RenderBike::new);
    	RenderingRegistry.registerEntityRenderingHandler(ENTITY_TANK, RenderTank::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_LAV, RenderLAV::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_LAVAA, RenderLAV::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_BSHIP, RenderBattleShip::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_SICKLE, RenderSickle::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_REAPER, RenderReaper::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_LAA, RenderTankBase::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_MAST, RenderMASTDOM::new);
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_PRISM, RenderTankBase::new);
    }
	
	public static Item item_spawn_wrench;
    public static Item item_spawn_soldier;
    public static Item item_spawn_tank;
    public static Item item_spawn_gltk;
    public static Item item_spawn_conscript;
	public static Item item_spawn_conscript_big;
    public static Item item_spawn_gi;
    public static Item item_spawn_heli;
    public static Item item_spawn_egal;
	public static Item item_spawn_bmpt;
	public static Item item_spawn_bmp2;
	public static Item item_spawn_t72b3;
	public static Item item_spawn_t90;
	public static Item item_spawn_lavaa;
	public static Item item_spawn_skyfire;
	public static Item item_spawn_sickle;
	public static Item item_spawn_mast;
	public static Item item_spawn_reaper;
	public static Item item_spawn_sandbag;
	public static Item item_spawn_sandbag2;
	public static Item item_spawn_sandbag3;
	
	public static Item item_spawn_gattank;
	public static Item item_spawn_mmtank;
	public static Item item_spawn_mirage;
	public static Item item_spawn_tesla;
	
	public static Item item_spawn_m2a2;
	public static Item item_spawn_99g;
	public static Item item_spawn_m109;
	public static Item item_spawn_t55;
	public static Item item_spawn_ftk;
	public static Item item_spawn_ftk_heavy;
	public static Item item_spawn_ah1z;
	public static Item item_spawn_car;
	public static Item item_spawn_hmmwv;
	public static Item item_spawn_f35;
	public static Item item_spawn_ah6;
	public static Item item_spawn_mi24;
	public static Item item_spawn_su33;
	public static Item item_spawn_ofg;
	public static Item item_spawn_mortar;
	public static Item item_spawn_tow;
	public static Item item_spawn_stin;
	public static Item item_spawn_m2hb;
	public static Item item_spawn_kord;
	public static Item item_spawn_bike;
	
	public static Item item_spawn_trident;
	public static Item item_spawn_m6aa;
	public static Item item_spawn_lav;
	public static Item item_spawn_minigunner;
	public static Item item_spawn_a10a;
	public static Item item_spawn_a10c;
	public static Item item_spawn_rads;
	
	public static Item mob_spawn_aohuan;
	public static Item mob_spawn_evilportal;
	public static Item mob_spawn_zombie;
	public static Item mob_spawn_skeleton;
	public static Item mob_spawn_pillager;
	public static Item mob_spawn_phantom;
	public static Item mob_spawn_ghost;
	public static Item mob_spawn_creeper;
	public static Item mob_spawn_dragonturret;
	public static Item mob_spawn_reb;
	
	public static Item maptool_respawnc;
	public static Item maptool_respawnv;
	public static Item maptool_movepoint;
	public static Item unit_remove;
	public static Item targetgun;

	public static Item support_swun;
	public static Item support_fw020;
	public static Item support_youhun;
	public static Item support_ember;
	public static Item support_ftkh;
	public static Item support_a10a;
	public static Item support_f35bomb;
	public static Item support_a10ax3;
	public static Item support_f35bombx3;
	
	public static Item challenge_mob;
	public static Item challenge_reb;
	public static Item challenge_pillager;
	public static Item challenge_tank;
	public static Item challenge_mobair;
	public static Item challenge_air;
	//public static Item challenge_sea;
	public static Item challenge_aohuan;
	public static Item challenge_portal;
	
	public static Item portal_star;
	
	public static Item support_155;
	public static Item support_kh29l;
	public static Item support_3m22;
	public static Item support_agm158;
	public static Item support_kh58;
	public static Item support_nuke;
	
	public static Item support_trident;
	public static Item support_cons;
	
	public static Item soldier_machine;
	public static Item vehicle_machine;
	
    static int id = 0;
	static int mobid = 0;
	static int spid = 0;
	static int dfid = 0;
    @EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
    	@SubscribeEvent
        public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event) {
    		ENTITY_SOLDIER = EntityType.Builder.<EntitySA_Soldier>of(EntitySA_Soldier::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Soldier::new).sized(0.5F, 1.8F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitysoldier");
    		GlobalEntityTypeAttributes.put(ENTITY_SOLDIER, 
    				EntitySA_Soldier.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ARMOR, (double) 4D).build());
    		ENTITY_SOLDIER.setRegistryName(new ResourceLocation("advancearmy", "entitysoldier"));
			
    		ENTITY_REB = EntityType.Builder.<ERO_REB>of(ERO_REB::new,EntityClassification.MONSTER)
    				.setCustomClientFactory(ERO_REB::new).sized(0.5F, 1.8F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:ero_reb");
    		GlobalEntityTypeAttributes.put(ENTITY_REB, 
    				ERO_REB.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ARMOR, (double) 2D).build());
    		ENTITY_REB.setRegistryName(new ResourceLocation("advancearmy", "ero_reb"));
			
    		ENTITY_PI = EntityType.Builder.<ERO_Pillager>of(ERO_Pillager::new,EntityClassification.MONSTER)
    				.setCustomClientFactory(ERO_Pillager::new).sized(0.5F, 1.8F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:ero_pillager");
    		GlobalEntityTypeAttributes.put(ENTITY_PI, 
    				ERO_Pillager.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 40.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
    		ENTITY_PI.setRegistryName(new ResourceLocation("advancearmy", "ero_pillager"));

    		ENTITY_CONS = EntityType.Builder.<EntitySA_Conscript>of(EntitySA_Conscript::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Conscript::new).sized(0.5F, 1.8F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityconscript");
    		GlobalEntityTypeAttributes.put(ENTITY_CONS, 
    				EntitySA_Conscript.createMobAttributes().add(Attributes.MAX_HEALTH, 45.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ARMOR, (double) 6D).build());
    		ENTITY_CONS.setRegistryName(new ResourceLocation("advancearmy", "entityconscript"));

    		ENTITY_RADS = EntityType.Builder.<EntitySA_RADS>of(EntitySA_RADS::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_RADS::new).sized(0.5F, 1.8F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityrads");
    		GlobalEntityTypeAttributes.put(ENTITY_RADS, 
    				EntitySA_RADS.createMobAttributes().add(Attributes.MAX_HEALTH, 80.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 45.0D)
					.add(Attributes.ARMOR, (double) 6D).build());
    		ENTITY_RADS.setRegistryName(new ResourceLocation("advancearmy", "entityrads"));

    		ENTITY_CONSX = EntityType.Builder.<EntitySA_ConscriptX>of(EntitySA_ConscriptX::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_ConscriptX::new).sized(1.5F, 3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityconscriptx");
    		GlobalEntityTypeAttributes.put(ENTITY_CONSX, 
    				EntitySA_ConscriptX.createMobAttributes().add(Attributes.MAX_HEALTH, 500.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.5F)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 50D).build());
    		ENTITY_CONSX.setRegistryName(new ResourceLocation("advancearmy", "entityconscriptx"));

    		ENTITY_SEAT = EntityType.Builder.<EntitySA_Seat>of(EntitySA_Seat::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Seat::new).sized(0.5F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityseat");
			GlobalEntityTypeAttributes.put(ENTITY_SEAT, EntitySA_Seat.createMobAttributes().build());
    		ENTITY_SEAT.setRegistryName(new ResourceLocation("advancearmy", "entityseat"));
			
    		ENTITY_SPT = EntityType.Builder.<SupportPoint>of(SupportPoint::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(SupportPoint::new).setTrackingRange(120).sized(0.5F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitysupport");
			GlobalEntityTypeAttributes.put(ENTITY_SPT, SupportPoint.createMobAttributes().build());
    		ENTITY_SPT.setRegistryName(new ResourceLocation("advancearmy", "entitysupport"));
			
    		ENTITY_RBOX = EntityType.Builder.<RewardBox>of(RewardBox::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(RewardBox::new).sized(0.5F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityrewardbox");
			GlobalEntityTypeAttributes.put(ENTITY_RBOX, RewardBox.createMobAttributes().add(Attributes.MAX_HEALTH, 1000.0D).build());
    		ENTITY_RBOX.setRegistryName(new ResourceLocation("advancearmy", "entityrewardbox"));
			
    		ENTITY_DPT = EntityType.Builder.<DefencePoint>of(DefencePoint::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(DefencePoint::new).setTrackingRange(120).sized(1F, 3.2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitydefence");
			GlobalEntityTypeAttributes.put(ENTITY_DPT, DefencePoint.createMobAttributes().add(Attributes.MAX_HEALTH, 1000.0D).add(Attributes.ARMOR, (double) 20D).build());
    		ENTITY_DPT.setRegistryName(new ResourceLocation("advancearmy", "entitydefence"));
			
    		ENTITY_CRES = EntityType.Builder.<CreatureRespawn>of(CreatureRespawn::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(CreatureRespawn::new).setTrackingRange(120).sized(0.5F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityrespawnc");
			GlobalEntityTypeAttributes.put(ENTITY_CRES, CreatureRespawn.createMobAttributes().build());
    		ENTITY_CRES.setRegistryName(new ResourceLocation("advancearmy", "entityrespawnc"));
    		ENTITY_MOVEP = EntityType.Builder.<ArmyMovePoint>of(ArmyMovePoint::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(ArmyMovePoint::new).setTrackingRange(120).sized(0.5F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitymovep");
			GlobalEntityTypeAttributes.put(ENTITY_MOVEP, ArmyMovePoint.createMobAttributes().build());
    		ENTITY_MOVEP.setRegistryName(new ResourceLocation("advancearmy", "entitymovep"));
    		ENTITY_VRES = EntityType.Builder.<VehicleRespawn>of(VehicleRespawn::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(VehicleRespawn::new).setTrackingRange(120).sized(0.5F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityrespawnv");
			GlobalEntityTypeAttributes.put(ENTITY_VRES, VehicleRespawn.createMobAttributes().build());
    		ENTITY_VRES.setRegistryName(new ResourceLocation("advancearmy", "entityrespawnv"));
			
    		ENTITY_SANDBAG = EntityType.Builder.<SandBag>of(SandBag::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(SandBag::new).sized(1F, 0.9F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitysandbag");
			GlobalEntityTypeAttributes.put(ENTITY_SANDBAG, SandBag.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.KNOCKBACK_RESISTANCE, (double) 10F).build());
    		ENTITY_SANDBAG.setRegistryName(new ResourceLocation("advancearmy", "entitysandbag"));
			
    		ENTITY_VMAC = EntityType.Builder.<VehicleMachine>of(VehicleMachine::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(VehicleMachine::new).setTrackingRange(120).sized(2F, 2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitymachinev");
			GlobalEntityTypeAttributes.put(ENTITY_VMAC, VehicleMachine.createMobAttributes().add(Attributes.MAX_HEALTH, 500.0D).add(Attributes.ARMOR, (double) 5D).build());
    		ENTITY_VMAC.setRegistryName(new ResourceLocation("advancearmy", "entitymachinev"));
			
    		ENTITY_SMAC = EntityType.Builder.<SoldierMachine>of(SoldierMachine::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(SoldierMachine::new).setTrackingRange(120).sized(2F, 2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitymachines");
			GlobalEntityTypeAttributes.put(ENTITY_SMAC, SoldierMachine.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.ARMOR, (double) 5D).build());
    		ENTITY_SMAC.setRegistryName(new ResourceLocation("advancearmy", "entitymachines"));
			
    		ENTITY_DT = EntityType.Builder.<DragonTurret>of(DragonTurret::new,EntityClassification.MONSTER)
    				.setCustomClientFactory(DragonTurret::new).sized(3F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:dragonturret");
    		GlobalEntityTypeAttributes.put(ENTITY_DT, 
    				DragonTurret.createMobAttributes().add(Attributes.MAX_HEALTH, 400.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.FOLLOW_RANGE, 60.0D)
					.add(Attributes.ARMOR, (double) 10D).build());
    		ENTITY_DT.setRegistryName(new ResourceLocation("advancearmy", "dragonturret"));
			
    		ENTITY_PHA = EntityType.Builder.<ERO_Phantom>of(ERO_Phantom::new,EntityClassification.MONSTER)
    				.setCustomClientFactory(ERO_Phantom::new).sized(0.9F, 0.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:ero_phantom");
    		GlobalEntityTypeAttributes.put(ENTITY_PHA, 
    				ERO_Phantom.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)4F).add(Attributes.ATTACK_DAMAGE, 8.0D)
					.add(Attributes.FOLLOW_RANGE, 80.0D)
					.add(Attributes.ARMOR, (double) 5D).build());
    		ENTITY_PHA.setRegistryName(new ResourceLocation("advancearmy", "ero_phantom"));
			
    		ENTITY_POR = EntityType.Builder.<EvilPortal>of(EvilPortal::new,EntityClassification.MONSTER).sized(2F, 2F)
    				//.setCustomClientFactory(EvilPortal::new).sized(1F, 0.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:evilportal");
    		GlobalEntityTypeAttributes.put(ENTITY_POR, 
    				EvilPortal.createMobAttributes().add(Attributes.MAX_HEALTH, 1000.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 50.0F)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 10D).build());
    		ENTITY_POR.setRegistryName(new ResourceLocation("advancearmy", "evilportal"));
			
    		ENTITY_GST = EntityType.Builder.<ERO_Ghast>of(ERO_Ghast::new,EntityClassification.MONSTER)
    				.setCustomClientFactory(ERO_Ghast::new).sized(4F, 4F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:ero_ghast");
    		GlobalEntityTypeAttributes.put(ENTITY_GST, 
    				ERO_Ghast.createMobAttributes().add(Attributes.MAX_HEALTH, 240.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)4F).add(Attributes.ATTACK_DAMAGE, 8.0D)
					.add(Attributes.FOLLOW_RANGE, 80.0D)
					.add(Attributes.ARMOR, (double) 5D).build());
    		ENTITY_GST.setRegistryName(new ResourceLocation("advancearmy", "ero_ghast"));
			
    		ENTITY_EZOMBIE = EntityType.Builder.<ERO_Zombie>of(ERO_Zombie::new,EntityClassification.MONSTER)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:ero_zombie");
    		GlobalEntityTypeAttributes.put(ENTITY_EZOMBIE, 
    				ERO_Zombie.createMobAttributes().add(Attributes.FOLLOW_RANGE, 100.0D).add(Attributes.MAX_HEALTH, 25.0D)
					.add(Attributes.MOVEMENT_SPEED, (double)0.12F).add(Attributes.ATTACK_DAMAGE, 3.0D)
					.add(Attributes.ARMOR, 2.0D).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE).build());
    		ENTITY_EZOMBIE.setRegistryName(new ResourceLocation("advancearmy", "ero_zombie"));
			
    		ENTITY_EHUSK = EntityType.Builder.<ERO_Husk>of(ERO_Husk::new,EntityClassification.MONSTER)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:ero_husk");
    		GlobalEntityTypeAttributes.put(ENTITY_EHUSK, 
    				ERO_Husk.createMobAttributes().add(Attributes.FOLLOW_RANGE, 100.0D).add(Attributes.MAX_HEALTH, 60.0D)
					.add(Attributes.MOVEMENT_SPEED, (double)0.15F).add(Attributes.ATTACK_DAMAGE, 3.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.ARMOR, 3.0D).build());
    		ENTITY_EHUSK.setRegistryName(new ResourceLocation("advancearmy", "ero_husk"));
			
    		ENTITY_SKELETON = EntityType.Builder.<ERO_Skeleton>of(ERO_Skeleton::new,EntityClassification.MONSTER)
    				//.setCustomClientFactory(ERO_Skeleton::new).sized(0.5F, 2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:ero_skeleton");
    		GlobalEntityTypeAttributes.put(ENTITY_SKELETON, 
    				ERO_Skeleton.createMobAttributes().add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ATTACK_DAMAGE, 3.0D)
					.add(Attributes.ARMOR, (double) 4D).build());
    		ENTITY_SKELETON.setRegistryName(new ResourceLocation("advancearmy", "ero_skeleton"));
			
    		ENTITY_CREEPER = EntityType.Builder.<ERO_Creeper>of(ERO_Creeper::new,EntityClassification.MONSTER)
    				//.setCustomClientFactory(ERO_Creeper::new).sized(0.5F, 1.8F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:ero_creeper");
    		GlobalEntityTypeAttributes.put(ENTITY_CREEPER, 
    				ERO_Creeper.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
					.add(Attributes.MOVEMENT_SPEED, (double)0.2F)
					.add(Attributes.FOLLOW_RANGE, 35.0D)
					.add(Attributes.ATTACK_DAMAGE, 3.0D)
					.add(Attributes.ARMOR, (double) 4D).build());
    		ENTITY_CREEPER.setRegistryName(new ResourceLocation("advancearmy", "ero_creeper"));
			
    		ENTITY_TANK = EntityType.Builder.<EntitySA_Tank>of(EntitySA_Tank::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Tank::new).sized(4F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitytank");
    		GlobalEntityTypeAttributes.put(ENTITY_TANK, EntitySA_Tank.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 600.0D)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 16D).build());
    		ENTITY_TANK.setRegistryName(new ResourceLocation("advancearmy", "entitytank"));
			
    		ENTITY_T90 = EntityType.Builder.<EntitySA_T90>of(EntitySA_T90::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_T90::new).sized(4F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityt90");
    		GlobalEntityTypeAttributes.put(ENTITY_T90, EntitySA_T90.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 650.0D)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 18D).build());
    		ENTITY_T90.setRegistryName(new ResourceLocation("advancearmy", "entityt90"));
			
    		ENTITY_T72 = EntityType.Builder.<EntitySA_T72>of(EntitySA_T72::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_T72::new).sized(4F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityt72");
    		GlobalEntityTypeAttributes.put(ENTITY_T72, EntitySA_T72.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 500.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 15D).build());
    		ENTITY_T72.setRegistryName(new ResourceLocation("advancearmy", "entityt72"));
			
    		ENTITY_MAST = EntityType.Builder.<EntitySA_MASTDOM>of(EntitySA_MASTDOM::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_MASTDOM::new).sized(7F, 3.2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitymastdom");
    		GlobalEntityTypeAttributes.put(ENTITY_MAST, 
    				EntitySA_MASTDOM.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 1200.0D)
					.add(Attributes.FOLLOW_RANGE, 65.0D)
					.add(Attributes.ARMOR, (double) 25D).build());
    		ENTITY_MAST.setRegistryName(new ResourceLocation("advancearmy", "entitymastdom"));	
			
    		ENTITY_LAV = EntityType.Builder.<EntitySA_LAV>of(EntitySA_LAV::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_LAV::new).sized(3F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitylav");
    		GlobalEntityTypeAttributes.put(ENTITY_LAV, 
    				EntitySA_LAV.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 60.0D)
					.add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_LAV.setRegistryName(new ResourceLocation("advancearmy", "entitylav"));
			
			ENTITY_LAVAA = EntityType.Builder.<EntitySA_LAVAA>of(EntitySA_LAVAA::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_LAVAA::new).sized(3F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitylavaa");
    		GlobalEntityTypeAttributes.put(ENTITY_LAVAA, 
    				EntitySA_LAVAA.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_LAVAA.setRegistryName(new ResourceLocation("advancearmy", "entitylavaa"));
			
    		ENTITY_BSHIP = EntityType.Builder.<EntitySA_BattleShip>of(EntitySA_BattleShip::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_BattleShip::new).setTrackingRange(120).sized(6F, 6F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitybattleship");
    		GlobalEntityTypeAttributes.put(ENTITY_BSHIP, 
    				EntitySA_BattleShip.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 6000.0D)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 100D).build());
    		ENTITY_BSHIP.setRegistryName(new ResourceLocation("advancearmy", "entitybattleship"));
			
    		ENTITY_SICKLE = EntityType.Builder.<EntitySA_Sickle>of(EntitySA_Sickle::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Sickle::new).sized(3F, 2.25F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitysickle");
    		GlobalEntityTypeAttributes.put(ENTITY_SICKLE, 
    				EntitySA_Sickle.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_SICKLE.setRegistryName(new ResourceLocation("advancearmy", "entitysickle"));
			
    		ENTITY_REAPER = EntityType.Builder.<EntitySA_Reaper>of(EntitySA_Reaper::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Reaper::new).sized(3F, 4F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityreaper");
    		GlobalEntityTypeAttributes.put(ENTITY_REAPER, 
    				EntitySA_Reaper.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 350.0D)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 12D).build());
    		ENTITY_REAPER.setRegistryName(new ResourceLocation("advancearmy", "entityreaper"));
			
    		ENTITY_HELI = EntityType.Builder.<EntitySA_Helicopter>of(EntitySA_Helicopter::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Helicopter::new).setTrackingRange(120).sized(3.2F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityheli");
    		GlobalEntityTypeAttributes.put(ENTITY_HELI, 
    				EntitySA_Helicopter.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 80.0D)
					.add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_HELI.setRegistryName(new ResourceLocation("advancearmy", "entityheli"));

    		ENTITY_AH1Z = EntityType.Builder.<EntitySA_AH1Z>of(EntitySA_AH1Z::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_AH1Z::new).setTrackingRange(120).sized(3.2F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityah1z");
    		GlobalEntityTypeAttributes.put(ENTITY_AH1Z, 
    				EntitySA_AH1Z.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 200.0D)
					.add(Attributes.FOLLOW_RANGE, 75.0D)
					.add(Attributes.ARMOR, (double) 5D).build());
    		ENTITY_AH1Z.setRegistryName(new ResourceLocation("advancearmy", "entityah1z"));

    		ENTITY_AH6 = EntityType.Builder.<EntitySA_AH6>of(EntitySA_AH6::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_AH6::new).setTrackingRange(120).sized(2.5F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityah6");
    		GlobalEntityTypeAttributes.put(ENTITY_AH6, 
    				EntitySA_AH6.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 120.0D)
					.add(Attributes.FOLLOW_RANGE, 75.0D)
					.add(Attributes.ARMOR, (double) 4D).build());
    		ENTITY_AH6.setRegistryName(new ResourceLocation("advancearmy", "entityah6"));
			
    		ENTITY_MI24 = EntityType.Builder.<EntitySA_MI24>of(EntitySA_MI24::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_MI24::new).setTrackingRange(120).sized(3.2F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitymi24");
    		GlobalEntityTypeAttributes.put(ENTITY_MI24, 
    				EntitySA_MI24.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 350.0D)
					.add(Attributes.FOLLOW_RANGE, 75.0D)
					.add(Attributes.ARMOR, (double) 9D).build());
    		ENTITY_MI24.setRegistryName(new ResourceLocation("advancearmy", "entitymi24"));

    		ENTITY_MORTAR = EntityType.Builder.<EntitySA_Mortar>of(EntitySA_Mortar::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Mortar::new).setTrackingRange(90).sized(1F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitymortar");
    		GlobalEntityTypeAttributes.put(ENTITY_MORTAR, 
    				EntitySA_Mortar.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 90.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
    		ENTITY_MORTAR.setRegistryName(new ResourceLocation("advancearmy", "entitymortar"));

    		ENTITY_TOW = EntityType.Builder.<EntitySA_TOW>of(EntitySA_TOW::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_TOW::new).sized(1F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitytow");
    		GlobalEntityTypeAttributes.put(ENTITY_TOW, 
    				EntitySA_TOW.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 75.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
    		ENTITY_TOW.setRegistryName(new ResourceLocation("advancearmy", "entitytow"));

    		ENTITY_STIN = EntityType.Builder.<EntitySA_STIN>of(EntitySA_STIN::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_STIN::new).sized(1F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitystin");
    		GlobalEntityTypeAttributes.put(ENTITY_STIN, 
    				EntitySA_STIN.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 90.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
    		ENTITY_STIN.setRegistryName(new ResourceLocation("advancearmy", "entitystin"));

    		ENTITY_M2HB = EntityType.Builder.<EntitySA_M2hb>of(EntitySA_M2hb::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_M2hb::new).sized(1F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitym2hb");
    		GlobalEntityTypeAttributes.put(ENTITY_M2HB, 
    				EntitySA_M2hb.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
    		ENTITY_M2HB.setRegistryName(new ResourceLocation("advancearmy", "entitym2hb"));
			
    		ENTITY_KORD = EntityType.Builder.<EntitySA_Kord>of(EntitySA_Kord::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Kord::new).sized(1F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitykord");
    		GlobalEntityTypeAttributes.put(ENTITY_KORD, 
    				EntitySA_Kord.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
    		ENTITY_KORD.setRegistryName(new ResourceLocation("advancearmy", "entitykord"));

    		ENTITY_PLANE = EntityType.Builder.<EntitySA_Plane>of(EntitySA_Plane::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Plane::new).setTrackingRange(100).sized(4F, 3.3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityplane");
    		GlobalEntityTypeAttributes.put(ENTITY_PLANE, 
    				EntitySA_Plane.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 200.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 6D).build());
    		ENTITY_PLANE.setRegistryName(new ResourceLocation("advancearmy", "entityplane"));
			
    		ENTITY_PLANE1 = EntityType.Builder.<EntitySA_Plane1>of(EntitySA_Plane1::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Plane1::new).setTrackingRange(100).sized(3F, 3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityplane1");
    		GlobalEntityTypeAttributes.put(ENTITY_PLANE1, 
    				EntitySA_Plane1.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
    		ENTITY_PLANE1.setRegistryName(new ResourceLocation("advancearmy", "entityplane1"));
			
    		ENTITY_PLANE2 = EntityType.Builder.<EntitySA_Plane2>of(EntitySA_Plane2::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Plane2::new).setTrackingRange(100).sized(3F, 3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityplane2");
    		GlobalEntityTypeAttributes.put(ENTITY_PLANE2, 
    				EntitySA_Plane2.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 100.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 4D).build());
    		ENTITY_PLANE2.setRegistryName(new ResourceLocation("advancearmy", "entityplane2"));
			
    		ENTITY_A10A = EntityType.Builder.<EntitySA_A10a>of(EntitySA_A10a::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_A10a::new).setTrackingRange(100).sized(3F, 3.6F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitya10a");
    		GlobalEntityTypeAttributes.put(ENTITY_A10A, 
    				EntitySA_A10a.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_A10A.setRegistryName(new ResourceLocation("advancearmy", "entitya10a"));
			
    		ENTITY_A10C = EntityType.Builder.<EntitySA_A10c>of(EntitySA_A10c::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_A10c::new).setTrackingRange(100).sized(3F, 3.6F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitya10c");
    		GlobalEntityTypeAttributes.put(ENTITY_A10C, 
    				EntitySA_A10c.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_A10C.setRegistryName(new ResourceLocation("advancearmy", "entitya10c"));
			
    		ENTITY_F35 = EntityType.Builder.<EntitySA_F35>of(EntitySA_F35::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_F35::new).setTrackingRange(100).sized(4F, 3.3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityf35");
    		GlobalEntityTypeAttributes.put(ENTITY_F35, 
    				EntitySA_F35.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 250.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 5D).build());
    		ENTITY_F35.setRegistryName(new ResourceLocation("advancearmy", "entityf35"));
			
    		ENTITY_SU33 = EntityType.Builder.<EntitySA_SU33>of(EntitySA_SU33::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_SU33::new).setTrackingRange(100).sized(4F, 3.2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitysu33");
    		GlobalEntityTypeAttributes.put(ENTITY_SU33, 
    				EntitySA_SU33.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 240.0D)
					.add(Attributes.FOLLOW_RANGE, 200.0D)
					.add(Attributes.ARMOR, (double) 5D).build());
    		ENTITY_SU33.setRegistryName(new ResourceLocation("advancearmy", "entitysu33"));	
			
    		ENTITY_LAA = EntityType.Builder.<EntitySA_LaserAA>of(EntitySA_LaserAA::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_LaserAA::new).sized(3F, 2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityskyfire");
    		GlobalEntityTypeAttributes.put(ENTITY_LAA, 
    				EntitySA_LaserAA.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 200.0D)
					.add(Attributes.FOLLOW_RANGE, 120.0D)
					.add(Attributes.ARMOR, (double) 10D).build());
    		ENTITY_LAA.setRegistryName(new ResourceLocation("advancearmy", "entityskyfire"));		
			
    		ENTITY_PRISM = EntityType.Builder.<EntitySA_Prism>of(EntitySA_Prism::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Prism::new).sized(3.5F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityprism");
    		GlobalEntityTypeAttributes.put(ENTITY_PRISM, 
    				EntitySA_Prism.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 200.0D)
					.add(Attributes.FOLLOW_RANGE, 80.0D)
					.add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_PRISM.setRegistryName(new ResourceLocation("advancearmy", "entityprism"));
			
    		ENTITY_AOHUAN = EntityType.Builder.<EntityAohuan>of(EntityAohuan::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntityAohuan::new).sized(0.5F, 1.8F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entityaohuan");
    		GlobalEntityTypeAttributes.put(ENTITY_AOHUAN, EntityAohuan.createMobAttributes().add(Attributes.MAX_HEALTH, 80.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F).add(Attributes.MOVEMENT_SPEED, (double)0.2F).add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.ARMOR, (double) 12D).build());
    		ENTITY_AOHUAN.setRegistryName(new ResourceLocation("advancearmy", "entityaohuan"));
			
    		ENTITY_SWUN = EntityType.Builder.<EntitySA_Swun>of(EntitySA_Swun::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Swun::new).sized(0.5F, 1.8F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entityswun");
    		GlobalEntityTypeAttributes.put(ENTITY_SWUN, EntitySA_Swun.createMobAttributes().add(Attributes.MAX_HEALTH, 80.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F).add(Attributes.MOVEMENT_SPEED, (double)0.2F).add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.ARMOR, (double) 12D).build());
    		ENTITY_SWUN.setRegistryName(new ResourceLocation("advancearmy", "entityswun"));
			
    		ENTITY_OFG = EntityType.Builder.<EntitySA_OFG>of(EntitySA_OFG::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_OFG::new).sized(0.5F, 1.8F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entityofg");
    		GlobalEntityTypeAttributes.put(ENTITY_OFG, EntitySA_OFG.createMobAttributes().add(Attributes.MAX_HEALTH, 70.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D).add(Attributes.MOVEMENT_SPEED, (double)0.2F).add(Attributes.FOLLOW_RANGE, 55.0D).add(Attributes.ARMOR, (double) 14D).build());
    		ENTITY_OFG.setRegistryName(new ResourceLocation("advancearmy", "entityofg"));
			
    		ENTITY_GI = EntityType.Builder.<EntitySA_GI>of(EntitySA_GI::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_GI::new).sized(0.5F, 1.8F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entitygi");
    		GlobalEntityTypeAttributes.put(ENTITY_GI, EntitySA_GI.createMobAttributes().add(Attributes.MAX_HEALTH, 60.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F).add(Attributes.MOVEMENT_SPEED, (double)0.2F).add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.ARMOR, (double) 12D).build());
    		ENTITY_GI.setRegistryName(new ResourceLocation("advancearmy", "entitygi"));
			
    		ENTITY_GAT = EntityType.Builder.<EntitySA_GAT>of(EntitySA_GAT::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_GAT::new).sized(0.5F, 1.8F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entityminigunner");
    		GlobalEntityTypeAttributes.put(ENTITY_GAT, EntitySA_GAT.createMobAttributes().add(Attributes.MAX_HEALTH, 65.0D)
					.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F).add(Attributes.MOVEMENT_SPEED, (double)0.2F).add(Attributes.FOLLOW_RANGE, 110.0D).add(Attributes.ARMOR, (double) 13D).build());
    		ENTITY_GAT.setRegistryName(new ResourceLocation("advancearmy", "entityminigunner"));
			
    		ENTITY_YOUHUN = EntityType.Builder.<EntitySA_YouHun>of(EntitySA_YouHun::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_YouHun::new).setTrackingRange(100).sized(3.2F, 2.5F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entityyouhun");
    		GlobalEntityTypeAttributes.put(ENTITY_YOUHUN, 
    				EntitySA_YouHun.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 400.0D).add(Attributes.FOLLOW_RANGE, 75.0D).add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_YOUHUN.setRegistryName(new ResourceLocation("advancearmy", "entityyouhun"));
			
    		ENTITY_YW010 = EntityType.Builder.<EntitySA_Yw010>of(EntitySA_Yw010::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Yw010::new).setTrackingRange(100).sized(3.2F, 2.5F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entityyw010");
    		GlobalEntityTypeAttributes.put(ENTITY_YW010, 
    				EntitySA_Yw010.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D).add(Attributes.MAX_HEALTH, 600.0D).add(Attributes.FOLLOW_RANGE, 75.0D).add(Attributes.ARMOR, (double) 10D).build());
    		ENTITY_YW010.setRegistryName(new ResourceLocation("advancearmy", "entityyw010"));
			
    		ENTITY_LAPEAR = EntityType.Builder.<EntitySA_Lapear>of(EntitySA_Lapear::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Lapear::new).setTrackingRange(100).sized(4F, 2.5F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entitylapear");
    		GlobalEntityTypeAttributes.put(ENTITY_LAPEAR, EntitySA_Lapear.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 400.0D).add(Attributes.FOLLOW_RANGE, 200.0D).add(Attributes.ARMOR, (double) 7D).build());
    		ENTITY_LAPEAR.setRegistryName(new ResourceLocation("advancearmy", "entitylapear"));
			
    		ENTITY_FW020 = EntityType.Builder.<EntitySA_Fw020>of(EntitySA_Fw020::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Fw020::new).setTrackingRange(100).sized(4F, 2.5F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entityfw020");
    		GlobalEntityTypeAttributes.put(ENTITY_FW020, EntitySA_Fw020.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 500.0D).add(Attributes.FOLLOW_RANGE, 200.0D).add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_FW020.setRegistryName(new ResourceLocation("advancearmy", "entityfw020"));
			
    		ENTITY_EMBER = EntityType.Builder.<EntitySA_Ember>of(EntitySA_Ember::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Ember::new).sized(5F, 9F).setShouldReceiveVelocityUpdates(true).build("advancearmy:entityember");
    		GlobalEntityTypeAttributes.put(ENTITY_EMBER, EntitySA_Ember.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 1500.0D).add(Attributes.FOLLOW_RANGE, 50.0D).add(Attributes.ARMOR, (double) 20D).build());
    		ENTITY_EMBER.setRegistryName(new ResourceLocation("advancearmy", "entityember"));
			
    		ENTITY_BMPT = EntityType.Builder.<EntitySA_BMPT>of(EntitySA_BMPT::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_BMPT::new).sized(4F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitybmpt");
    		GlobalEntityTypeAttributes.put(ENTITY_BMPT, EntitySA_BMPT.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 500.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 15D).build());
    		ENTITY_BMPT.setRegistryName(new ResourceLocation("advancearmy", "entitybmpt"));

    		ENTITY_M109 = EntityType.Builder.<EntitySA_M109>of(EntitySA_M109::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_M109::new).setTrackingRange(100).sized(3F, 3.8F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitym109");
    		GlobalEntityTypeAttributes.put(ENTITY_M109, EntitySA_M109.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 100.0D)
					.add(Attributes.FOLLOW_RANGE, 150.0D)
					.add(Attributes.ARMOR, (double) 5D).build());
    		ENTITY_M109.setRegistryName(new ResourceLocation("advancearmy", "entitym109"));
			
    		ENTITY_M2A2 = EntityType.Builder.<EntitySA_M2A2>of(EntitySA_M2A2::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_M2A2::new).sized(3F, 2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitym2a2");
    		GlobalEntityTypeAttributes.put(ENTITY_M2A2, EntitySA_M2A2.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 200.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 8D).build());
    		ENTITY_M2A2.setRegistryName(new ResourceLocation("advancearmy", "entitym2a2"));
			
    		ENTITY_M2A2AA = EntityType.Builder.<EntitySA_M2A2AA>of(EntitySA_M2A2AA::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_M2A2AA::new).sized(3F, 2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitym6aa");
    		GlobalEntityTypeAttributes.put(ENTITY_M2A2AA, EntitySA_M2A2AA.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 200.0D)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 7D).build());
    		ENTITY_M2A2AA.setRegistryName(new ResourceLocation("advancearmy", "entitym6aa"));
			
    		ENTITY_APAGAT = EntityType.Builder.<EntitySA_APAGAT>of(EntitySA_APAGAT::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_APAGAT::new).sized(3F, 3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityapagat");
    		GlobalEntityTypeAttributes.put(ENTITY_APAGAT, EntitySA_APAGAT.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 250.0D)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 10D).build());
    		ENTITY_APAGAT.setRegistryName(new ResourceLocation("advancearmy", "entityapagat"));
			
			
    		ENTITY_MIRAGE = EntityType.Builder.<EntitySA_Mirage>of(EntitySA_Mirage::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Mirage::new).sized(3F, 3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitymirage");
    		GlobalEntityTypeAttributes.put(ENTITY_MIRAGE, EntitySA_Mirage.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 350.0D)
					.add(Attributes.FOLLOW_RANGE, 55.0D)
					.add(Attributes.ARMOR, (double) 12D).build());
    		ENTITY_MIRAGE.setRegistryName(new ResourceLocation("advancearmy", "entitymirage"));
			
    		ENTITY_TESLA = EntityType.Builder.<EntitySA_Tesla>of(EntitySA_Tesla::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Tesla::new).sized(3F, 3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitytesla");
    		GlobalEntityTypeAttributes.put(ENTITY_TESLA, EntitySA_Tesla.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 450.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 14D).build());
    		ENTITY_TESLA.setRegistryName(new ResourceLocation("advancearmy", "entitytesla"));
			
			
    		ENTITY_MMTANK = EntityType.Builder.<EntitySA_MMTank>of(EntitySA_MMTank::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_MMTank::new).sized(4F, 3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitymmtank");
    		GlobalEntityTypeAttributes.put(ENTITY_MMTANK, EntitySA_MMTank.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 650.0D)
					.add(Attributes.FOLLOW_RANGE, 100.0D)
					.add(Attributes.ARMOR, (double) 22D).build());
    		ENTITY_MMTANK.setRegistryName(new ResourceLocation("advancearmy", "entitymmtank"));
			
    		ENTITY_BIKE = EntityType.Builder.<EntitySA_Bike>of(EntitySA_Bike::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Bike::new).sized(1.5F, 1F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitybike");
    		GlobalEntityTypeAttributes.put(ENTITY_BIKE, EntitySA_Bike.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 50.0D)
					.add(Attributes.ARMOR, (double) 2D).build());
    		ENTITY_BIKE.setRegistryName(new ResourceLocation("advancearmy", "entitybike"));
			
    		ENTITY_FTK = EntityType.Builder.<EntitySA_FTK>of(EntitySA_FTK::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_FTK::new).sized(4F, 2.5F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityftk");
    		GlobalEntityTypeAttributes.put(ENTITY_FTK, EntitySA_FTK.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 550.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 16D).build());
    		ENTITY_FTK.setRegistryName(new ResourceLocation("advancearmy", "entityftk"));

    		ENTITY_FTK_H = EntityType.Builder.<EntitySA_FTK_H>of(EntitySA_FTK_H::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_FTK_H::new).setTrackingRange(120).sized(8F, 4F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityftkh");
    		GlobalEntityTypeAttributes.put(ENTITY_FTK_H, EntitySA_FTK_H.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 3000.0D)
					.add(Attributes.FOLLOW_RANGE, 60.0D)
					.add(Attributes.ARMOR, (double) 50D).build());
    		ENTITY_FTK_H.setRegistryName(new ResourceLocation("advancearmy", "entityftkh"));
			
    		ENTITY_99G = EntityType.Builder.<EntitySA_99G>of(EntitySA_99G::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_99G::new).sized(4.2F, 3F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entity99g");
    		GlobalEntityTypeAttributes.put(ENTITY_99G, EntitySA_99G.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 800.0D)
					.add(Attributes.FOLLOW_RANGE, 60.0D)
					.add(Attributes.ARMOR, (double) 25D).build());
    		ENTITY_99G.setRegistryName(new ResourceLocation("advancearmy", "entity99g"));
			
			ENTITY_CAR = EntityType.Builder.<EntitySA_Car>of(EntitySA_Car::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Car::new).sized(2.5F, 2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitycar");
    		GlobalEntityTypeAttributes.put(ENTITY_CAR, EntitySA_Car.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 100.0D)
					.add(Attributes.FOLLOW_RANGE, 30.0D)
					.add(Attributes.ARMOR, (double) 3D).build());
    		ENTITY_CAR.setRegistryName(new ResourceLocation("advancearmy", "entitycar"));
			
    		ENTITY_HMMWV = EntityType.Builder.<EntitySA_Hmmwv>of(EntitySA_Hmmwv::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_Hmmwv::new).sized(2.5F, 2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityhmmwv");
    		GlobalEntityTypeAttributes.put(ENTITY_HMMWV, EntitySA_Hmmwv.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 120.0D)
					.add(Attributes.FOLLOW_RANGE, 30.0D)
					.add(Attributes.ARMOR, (double) 4D).build());
    		ENTITY_HMMWV.setRegistryName(new ResourceLocation("advancearmy", "entityhmmwv"));
			
    		ENTITY_T55 = EntityType.Builder.<EntitySA_T55>of(EntitySA_T55::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_T55::new).sized(3.5F, 2.4F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entityt55");
    		GlobalEntityTypeAttributes.put(ENTITY_T55, EntitySA_T55.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 300.0D)
					.add(Attributes.FOLLOW_RANGE, 45.0D)
					.add(Attributes.ARMOR, (double) 10D).build());
    		ENTITY_T55.setRegistryName(new ResourceLocation("advancearmy", "entityt55"));
			
    		ENTITY_BMP2 = EntityType.Builder.<EntitySA_BMP2>of(EntitySA_BMP2::new,EntityClassification.CREATURE)
    				.setCustomClientFactory(EntitySA_BMP2::new).sized(3F, 2F)
        			.setShouldReceiveVelocityUpdates(true).build("advancearmy:entitybmp2");
    		GlobalEntityTypeAttributes.put(ENTITY_BMP2, EntitySA_BMP2.createMobAttributes().add(Attributes.KNOCKBACK_RESISTANCE, (double) 10.0D)
					.add(Attributes.MAX_HEALTH, 250.0D)
					.add(Attributes.FOLLOW_RANGE, 50.0D)
					.add(Attributes.ARMOR, (double) 7D).build());
    		ENTITY_BMP2.setRegistryName(new ResourceLocation("advancearmy", "entitybmp2"));
			
			event.getRegistry().register(ENTITY_MIRAGE);
			event.getRegistry().register(ENTITY_TESLA);
			event.getRegistry().register(ENTITY_BIKE);
			event.getRegistry().register(ENTITY_BMP2);
			event.getRegistry().register(ENTITY_BMPT);
			event.getRegistry().register(ENTITY_M109);
			event.getRegistry().register(ENTITY_M2A2);
			event.getRegistry().register(ENTITY_M2A2AA);
            event.getRegistry().register(ENTITY_T55);
			event.getRegistry().register(ENTITY_FTK);
			event.getRegistry().register(ENTITY_CONSX);
            event.getRegistry().register(ENTITY_99G);
            event.getRegistry().register(ENTITY_FTK_H);
			event.getRegistry().register(ENTITY_CAR);
			event.getRegistry().register(ENTITY_HMMWV);
			event.getRegistry().register(ENTITY_AH1Z);
			event.getRegistry().register(ENTITY_BSHIP);
			event.getRegistry().register(ENTITY_HELI);
			event.getRegistry().register(ENTITY_PLANE);
			
			event.getRegistry().register(ENTITY_GAT);
			event.getRegistry().register(ENTITY_APAGAT);
			event.getRegistry().register(ENTITY_MMTANK);
			
            event.getRegistry().register(ENTITY_TANK);
            event.getRegistry().register(ENTITY_T90);
            event.getRegistry().register(ENTITY_T72);
			event.getRegistry().register(ENTITY_PRISM);
            event.getRegistry().register(ENTITY_SOLDIER);
            event.getRegistry().register(ENTITY_CONS);
            event.getRegistry().register(ENTITY_REB);
			event.getRegistry().register(ENTITY_PI);
			event.getRegistry().register(ENTITY_PLANE1);
			event.getRegistry().register(ENTITY_PLANE2);
			event.getRegistry().register(ENTITY_A10A);
			event.getRegistry().register(ENTITY_A10C);
			event.getRegistry().register(ENTITY_SEAT);
			event.getRegistry().register(ENTITY_SICKLE);
			event.getRegistry().register(ENTITY_REAPER);
			event.getRegistry().register(ENTITY_DT);
            event.getRegistry().register(ENTITY_EZOMBIE);
			event.getRegistry().register(ENTITY_EHUSK);
			
			event.getRegistry().register(ENTITY_SKELETON);
			event.getRegistry().register(ENTITY_CREEPER);
			event.getRegistry().register(ENTITY_PHA);
			event.getRegistry().register(ENTITY_POR);
			event.getRegistry().register(ENTITY_GST);
			event.getRegistry().register(ENTITY_LAV);
			event.getRegistry().register(ENTITY_LAVAA);
			event.getRegistry().register(ENTITY_F35);
			event.getRegistry().register(ENTITY_SU33);
			event.getRegistry().register(ENTITY_LAA);
			event.getRegistry().register(ENTITY_MAST);

			event.getRegistry().register(ENTITY_YOUHUN);
			event.getRegistry().register(ENTITY_EMBER);
			event.getRegistry().register(ENTITY_YW010);
			event.getRegistry().register(ENTITY_FW020);
			event.getRegistry().register(ENTITY_LAPEAR);
			event.getRegistry().register(ENTITY_AOHUAN);
			event.getRegistry().register(ENTITY_SWUN);
			event.getRegistry().register(ENTITY_GI);

			event.getRegistry().register(ENTITY_AH6);
			event.getRegistry().register(ENTITY_MI24);
			event.getRegistry().register(ENTITY_OFG);
			event.getRegistry().register(ENTITY_MORTAR);
			event.getRegistry().register(ENTITY_TOW);
			event.getRegistry().register(ENTITY_STIN);
			event.getRegistry().register(ENTITY_M2HB);
			event.getRegistry().register(ENTITY_KORD);
			
			event.getRegistry().register(ENTITY_RADS);
			event.getRegistry().register(ENTITY_CRES);
			event.getRegistry().register(ENTITY_SPT);
			event.getRegistry().register(ENTITY_RBOX);
			event.getRegistry().register(ENTITY_DPT);
			event.getRegistry().register(ENTITY_MOVEP);
			event.getRegistry().register(ENTITY_SANDBAG);
			event.getRegistry().register(ENTITY_VRES);
			event.getRegistry().register(ENTITY_SMAC);
			event.getRegistry().register(ENTITY_VMAC);
        	event.getRegistry().registerAll(ENTITY_SOLDIER,ENTITY_DT, ENTITY_F35,ENTITY_CONS,ENTITY_REB,ENTITY_LAA, ENTITY_AH6, ENTITY_MI24, ENTITY_GAT, ENTITY_APAGAT,ENTITY_MIRAGE,ENTITY_TESLA,
			ENTITY_OFG, ENTITY_MORTAR,ENTITY_TOW,ENTITY_STIN,ENTITY_SMAC,ENTITY_VMAC,ENTITY_SPT,ENTITY_CONSX,ENTITY_EHUSK,ENTITY_DPT,ENTITY_RBOX,ENTITY_RADS, ENTITY_MMTANK, ENTITY_SANDBAG,
			ENTITY_T90, ENTITY_T72,ENTITY_BIKE,ENTITY_A10A,ENTITY_A10C,ENTITY_YOUHUN,ENTITY_EMBER,ENTITY_SWUN,ENTITY_GI, ENTITY_M2HB, ENTITY_KORD, ENTITY_SU33,ENTITY_CRES,ENTITY_MOVEP,ENTITY_VRES,
			ENTITY_BMP2,ENTITY_BMPT,ENTITY_M109,ENTITY_M2A2,ENTITY_M2A2AA,ENTITY_T55,ENTITY_FTK,ENTITY_99G,ENTITY_FTK_H,ENTITY_AH1Z,ENTITY_HMMWV,ENTITY_CAR,ENTITY_YW010,ENTITY_FW020,ENTITY_LAPEAR,ENTITY_AOHUAN, ENTITY_REAPER,
			ENTITY_TANK, ENTITY_BSHIP, ENTITY_PHA, ENTITY_LAV, ENTITY_LAVAA, ENTITY_POR, ENTITY_GST, ENTITY_HELI, ENTITY_PLANE, ENTITY_PI, ENTITY_PLANE1,ENTITY_PLANE2, ENTITY_EZOMBIE, ENTITY_SKELETON, ENTITY_CREEPER,ENTITY_PRISM, ENTITY_SEAT, ENTITY_SICKLE, ENTITY_MAST);
        }
    	@SubscribeEvent
    	public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
			{
				targetgun = new ItemGun_Target(new Item.Properties().stacksTo(1).tab(GROUP_ITEM).durability(200)).setRegistryName(new ResourceLocation(MOD_ID,"targetgun"));
				((ItemGun_Target)targetgun).obj_model = new SAObjModel("advancearmy:textures/gun/targetgun.obj");
				((ItemGun_Target)targetgun).obj_tex = new ResourceLocation("advancearmy:textures/gun/gun.png");
				((ItemGun_Target)targetgun).arm_l_posx=0;
				((ItemGun_Target)targetgun).arm_l_posy=-0.5F;
				((ItemGun_Target)targetgun).arm_l_posz=-1.5F;
				event.getRegistry().register(targetgun);
			}
    		{
    			item_spawn_soldier =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				1,300,200).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_soldier"));
    			((ItemSpawn)item_spawn_soldier).infor1="advancearmy.infor.item1.desc";
				((ItemSpawn)item_spawn_soldier).infor2="右键兵种方块来设置职业";
				((ItemSpawn)item_spawn_soldier).infor3="为突击兵职业时能够额外装备榴弹发射器";
				event.getRegistry().register(item_spawn_soldier);
    		}
    		{
    			item_spawn_tank =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1000,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_tank"));
				event.getRegistry().register(item_spawn_tank);
				((ItemSpawn)item_spawn_tank).infor1="advancearmy.infor.item2.desc";
				((ItemSpawn)item_spawn_tank).obj_model = new SAObjModel("advancearmy:textures/mob/m1tank.obj");
    		}
			{
    			item_spawn_conscript =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				1,200,150).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_conscript"));
				((ItemSpawn)item_spawn_conscript).infor1="advancearmy.infor.item1.desc";
				((ItemSpawn)item_spawn_conscript).infor3="动员兵中的精锐";
    			event.getRegistry().register(item_spawn_conscript);
    		}
			{
    			item_spawn_ftk =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,900,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_ftk"));
    			event.getRegistry().register(item_spawn_ftk);
				((ItemSpawn)item_spawn_ftk).infor1="advancearmy.infor.item2.desc";
				((ItemSpawn)item_spawn_ftk).infor3="加强了装甲，暂时没有配装磁能炮弹";
				((ItemSpawn)item_spawn_ftk).obj_model = new SAObjModel("advancearmy:textures/mob/ftk_new.obj");
    		}
			{
    			item_spawn_t55 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,600,500).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_t55"));
    			event.getRegistry().register(item_spawn_t55);
				((ItemSpawn)item_spawn_t55).obj_model = new SAObjModel("advancearmy:textures/mob/t55.obj");
				((ItemSpawn)item_spawn_t55).infor1="advancearmy.infor.item2.desc";
    		}
    		{
    			item_spawn_gltk =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1000,800).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_gltk"));
    			event.getRegistry().register(item_spawn_gltk);
				((ItemSpawn)item_spawn_gltk).obj_model = new SAObjModel("advancearmy:textures/mob/gltk.obj");
				((ItemSpawn)item_spawn_gltk).infor1="advancearmy.infor.item13.desc";
    		}
			{
    			item_spawn_heli =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1000,800).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_heli"));
				((ItemSpawn)item_spawn_heli).obj_model = new SAObjModel("advancearmy:textures/mob/ah64.obj");
    			event.getRegistry().register(item_spawn_heli);
				((ItemSpawn)item_spawn_heli).infor1="advancearmy.infor.item4.desc";
    		}
			{
    			item_spawn_egal =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1000,800).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_egal"));
    			event.getRegistry().register(item_spawn_egal);
				((ItemSpawn)item_spawn_egal).infor1="advancearmy.infor.item3.desc";
				((ItemSpawn)item_spawn_egal).obj_model = new SAObjModel("advancearmy:textures/mob/egal.obj");
    		}
			{
    			item_spawn_t72b3 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,900,600).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_t72b3"));
    			event.getRegistry().register(item_spawn_t72b3);
				((ItemSpawn)item_spawn_t72b3).infor1="advancearmy.infor.item2.desc";
				((ItemSpawn)item_spawn_t72b3).obj_model = new SAObjModel("advancearmy:textures/mob/t72b3.obj");
    		}
			{
    			item_spawn_t90 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1050,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_t90"));
    			event.getRegistry().register(item_spawn_t90);
				((ItemSpawn)item_spawn_t90).infor1="advancearmy.infor.item2.desc";
				((ItemSpawn)item_spawn_t90).obj_model = new SAObjModel("advancearmy:textures/mob/t90.obj");
    		}
			{
    			item_spawn_lavaa =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,800,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_lavaa"));
    			event.getRegistry().register(item_spawn_lavaa);
				((ItemSpawn)item_spawn_lavaa).infor1="advancearmy.infor.item12.desc";
				((ItemSpawn)item_spawn_lavaa).obj_model = new SAObjModel("advancearmy:textures/mob/lav25aa.obj");
    		}
			{
    			item_spawn_skyfire =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1200,800).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_skyfire"));
    			event.getRegistry().register(item_spawn_skyfire);
				((ItemSpawn)item_spawn_skyfire).infor1="advancearmy.infor.item12.desc";
				((ItemSpawn)item_spawn_skyfire).infor3="经过改进后，武器角度可以对地";
				((ItemSpawn)item_spawn_skyfire).obj_model = new SAObjModel("advancearmy:textures/mob/skyfire.obj");
    		}
			{
    			item_spawn_sickle =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,800,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_sickle"));
    			event.getRegistry().register(item_spawn_sickle);
				((ItemSpawn)item_spawn_sickle).infor1="四足机甲";
				((ItemSpawn)item_spawn_sickle).infor2="经过SWUN军团的改造";
				((ItemSpawn)item_spawn_sickle).infor3="装甲和火力都获得了升级，武器角度可以对空";
				((ItemSpawn)item_spawn_sickle).obj_model = new SAObjModel("advancearmy:textures/mob/sickle.obj");
    		}
			{
    			item_spawn_mast =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,2000,1000).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_mast"));
    			event.getRegistry().register(item_spawn_mast);
				((ItemSpawn)item_spawn_mast).infor1="超重型坦克";
				((ItemSpawn)item_spawn_mast).infor3="经过改进后，可以选择在短暂蓄力后快速开火";
				((ItemSpawn)item_spawn_mast).obj_model = new SAObjModel("advancearmy:textures/mob/mast.obj");
    		}
			
			{
    			item_spawn_m2a2 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,800,800).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_m2a2"));
    			event.getRegistry().register(item_spawn_m2a2);
				((ItemSpawn)item_spawn_m2a2).infor1="advancearmy.infor.item11.desc";
				((ItemSpawn)item_spawn_m2a2).obj_model = new SAObjModel("advancearmy:textures/mob/m2a2.obj");
    		}
			{
    			item_spawn_99g =  new ItemSpawn(new Item.Properties().tab(GROUP_UNIT), ++id,
				0,1800,1200).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_99g"));
    			event.getRegistry().register(item_spawn_99g);
				((ItemSpawn)item_spawn_99g).infor1="advancearmy.infor.item2.desc";
				((ItemSpawn)item_spawn_99g).obj_model = new SAObjModel("advancearmy:textures/mob/99.obj");
    		}
			{
    			item_spawn_m109 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1000,1000).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_m109"));
    			event.getRegistry().register(item_spawn_m109);
				((ItemSpawn)item_spawn_m109).infor1="advancearmy.infor.item13.desc";
				((ItemSpawn)item_spawn_m109).obj_model = new SAObjModel("advancearmy:textures/mob/m109.obj");
    		}
			{
    			item_spawn_ftk_heavy =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,4000,2000).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_ftk_heavy"));
    			event.getRegistry().register(item_spawn_ftk_heavy);
				((ItemSpawn)item_spawn_ftk_heavy).infor1="超重型坦克";
				((ItemSpawn)item_spawn_ftk_heavy).infor3="这辆犀牛坦克经过了十分疯狂的改造";
				((ItemSpawn)item_spawn_ftk_heavy).obj_model = new SAObjModel("advancearmy:textures/mob/ftk_heavy.obj");
    		}
			{
    			item_spawn_ah1z =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,900,1200).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_ah1z"));
    			event.getRegistry().register(item_spawn_ah1z);
				((ItemSpawn)item_spawn_ah1z).infor1="advancearmy.infor.item4.desc";
				((ItemSpawn)item_spawn_ah1z).obj_model = new SAObjModel("advancearmy:textures/mob/ah1z.obj");
    		}
			{
    			item_spawn_car =  new ItemSpawn(new Item.Properties().tab(GROUP_UNIT), ++id,
				0,500,500).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_car"));
    			event.getRegistry().register(item_spawn_car);
				((ItemSpawn)item_spawn_car).infor1="advancearmy.infor.item14.desc";
				((ItemSpawn)item_spawn_car).obj_model = new SAObjModel("advancearmy:textures/mob/car.obj");
    		}
			{
    			item_spawn_hmmwv =  new ItemSpawn(new Item.Properties().tab(GROUP_UNIT), ++id,
				0,600,600).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_hmmwv"));
    			event.getRegistry().register(item_spawn_hmmwv);
				((ItemSpawn)item_spawn_hmmwv).infor1="advancearmy.infor.item14.desc";
				((ItemSpawn)item_spawn_hmmwv).obj_model = new SAObjModel("advancearmy:textures/mob/hmmwv.obj");
    		}
			{
    			item_spawn_f35 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,2000,1000).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_f35"));
    			event.getRegistry().register(item_spawn_f35);
				((ItemSpawn)item_spawn_f35).infor1="advancearmy.infor.item5.desc";
				((ItemSpawn)item_spawn_f35).obj_model = new SAObjModel("advancearmy:textures/mob/f35.obj");
    		}
			{
    			item_spawn_trident =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,3000,2000).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_trident"));
    			event.getRegistry().register(item_spawn_trident);
				((ItemSpawn)item_spawn_trident).infor1="advancearmy.infor.item15.desc";
				((ItemSpawn)item_spawn_trident).obj_model = new SAObjModel("advancearmy:textures/mob/battleship.obj");
    		}
			{
    			item_spawn_m6aa =  new ItemSpawn(new Item.Properties().tab(GROUP_UNIT), ++id,
				0,800,800).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_m6aa"));
    			event.getRegistry().register(item_spawn_m6aa);
				((ItemSpawn)item_spawn_m6aa).infor1="advancearmy.infor.item12.desc";
				((ItemSpawn)item_spawn_m6aa).obj_model = new SAObjModel("advancearmy:textures/mob/m2a2aa.obj");
    		}
			{
    			item_spawn_lav =  new ItemSpawn(new Item.Properties().tab(GROUP_UNIT), ++id,
				0,700,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_lav"));
    			event.getRegistry().register(item_spawn_lav);
				((ItemSpawn)item_spawn_lav).infor1="advancearmy.infor.item11.desc";
				((ItemSpawn)item_spawn_lav).obj_model = new SAObjModel("advancearmy:textures/mob/lav25.obj");
    		}
			{
    			item_spawn_gattank =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,900,600).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_gattank"));
				((ItemSpawn)item_spawn_gattank).infor1="advancearmy.infor.item12.desc";
				((ItemSpawn)item_spawn_gattank).obj_model = new SAObjModel("advancearmy:textures/mob/apagat.obj");
    			event.getRegistry().register(item_spawn_gattank);
    		}
			{
    			item_spawn_bmp2 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,700,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_bmp2"));
    			event.getRegistry().register(item_spawn_bmp2);
				((ItemSpawn)item_spawn_bmp2).infor1="advancearmy.infor.item11.desc";
				((ItemSpawn)item_spawn_bmp2).obj_model = new SAObjModel("advancearmy:textures/mob/bmp2.obj");
    		}
			{
    			item_spawn_bmpt =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1200,900).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_bmpt"));
    			event.getRegistry().register(item_spawn_bmpt);
				((ItemSpawn)item_spawn_bmpt).infor1="坦克支援车";
				((ItemSpawn)item_spawn_bmpt).obj_model = new SAObjModel("advancearmy:textures/mob/bmpt.obj");
    		}
			{
    			item_spawn_a10a =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1200,900).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_a10a"));
    			event.getRegistry().register(item_spawn_a10a);
				((ItemSpawn)item_spawn_a10a).infor1="advancearmy.infor.item3.desc";
				((ItemSpawn)item_spawn_a10a).obj_model = new SAObjModel("advancearmy:textures/mob/a10a.obj");
    		}
			{
    			item_spawn_a10c =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1500,900).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_a10c"));
    			event.getRegistry().register(item_spawn_a10c);
				((ItemSpawn)item_spawn_a10c).infor1="advancearmy.infor.item3.desc";
				((ItemSpawn)item_spawn_a10c).obj_model = new SAObjModel("advancearmy:textures/mob/a10c.obj");
    		}
			{
    			item_spawn_ah6 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,700,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_ah6"));
    			event.getRegistry().register(item_spawn_ah6);
				((ItemSpawn)item_spawn_ah6).infor1="advancearmy.infor.item4.desc";
				((ItemSpawn)item_spawn_ah6).obj_model = new SAObjModel("advancearmy:textures/mob/ah6.obj");
    		}
			{
    			item_spawn_mi24 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1100,900).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_mi24"));
    			event.getRegistry().register(item_spawn_mi24);
				((ItemSpawn)item_spawn_mi24).infor1="advancearmy.infor.item4.desc";
				((ItemSpawn)item_spawn_mi24).obj_model = new SAObjModel("advancearmy:textures/mob/mi24.obj");
    		}
			{
    			item_spawn_su33 =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1600,900).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_su33"));
    			event.getRegistry().register(item_spawn_su33);
				((ItemSpawn)item_spawn_su33).infor1="advancearmy.infor.item5.desc";
				((ItemSpawn)item_spawn_su33).obj_model = new SAObjModel("advancearmy:textures/mob/su33.obj");
    		}
			{
    			item_spawn_ofg =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				1,1200,900).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_ofg"));
    			event.getRegistry().register(item_spawn_ofg);
				((ItemSpawn)item_spawn_ofg).infor1="专业突击兵";
				((ItemSpawn)item_spawn_ofg).infor3="装备高科技热光步枪";
    		}
			{
    			item_spawn_mortar =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,700,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_mortar"));
    			event.getRegistry().register(item_spawn_mortar);
				((ItemSpawn)item_spawn_mortar).infor1="advancearmy.infor.item10.desc";
				((ItemSpawn)item_spawn_mortar).obj_model = new SAObjModel("advancearmy:textures/mob/mortar.obj");
    		}
			{
    			item_spawn_m2hb =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,500,500).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_m2hb"));
    			event.getRegistry().register(item_spawn_m2hb);
				((ItemSpawn)item_spawn_m2hb).infor1="advancearmy.infor.item7.desc";
				((ItemSpawn)item_spawn_m2hb).obj_model = new SAObjModel("advancearmy:textures/mob/m2hb_t.obj");
    		}
			{
    			item_spawn_kord =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,500,500).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_kord"));
    			event.getRegistry().register(item_spawn_kord);
				((ItemSpawn)item_spawn_kord).infor1="advancearmy.infor.item7.desc";
				((ItemSpawn)item_spawn_kord).obj_model = new SAObjModel("advancearmy:textures/mob/kord_t.obj");
    		}
			{
    			item_spawn_gi =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				1,320,320).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_gi"));
				((ItemSpawn)item_spawn_gi).infor1="专业支援兵";
				((ItemSpawn)item_spawn_gi).infor3="防守状态下能够部署沙袋架设轻机枪";
    			event.getRegistry().register(item_spawn_gi);
    		}
			{
    			item_spawn_conscript_big =  new ItemSpawn(new Item.Properties().stacksTo(1)/*.tab(GROUP_UNIT)*/, ++id,
				1,1500,1000).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_conscript_big"));
    			((ItemSpawn)item_spawn_conscript_big).infor1="特种兵";
				((ItemSpawn)item_spawn_conscript_big).infor3="看起来十分强壮的动员兵，没有人知道他们经历了什么";
				event.getRegistry().register(item_spawn_conscript_big);
    		}
			{
    			item_spawn_tow =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,700,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_tow"));
    			event.getRegistry().register(item_spawn_tow);
				((ItemSpawn)item_spawn_tow).infor1="advancearmy.infor.item8.desc";
				((ItemSpawn)item_spawn_tow).obj_model = new SAObjModel("advancearmy:textures/mob/tow.obj");
    		}
			{
    			item_spawn_stin =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,700,700).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_stin"));
    			event.getRegistry().register(item_spawn_stin);
				((ItemSpawn)item_spawn_stin).infor1="advancearmy.infor.item9.desc";
				((ItemSpawn)item_spawn_stin).obj_model = new SAObjModel("advancearmy:textures/mob/stin.obj");
    		}
			{
    			soldier_machine =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				3,-1,0).setRegistryName(new ResourceLocation(MOD_ID,"soldier_machine"));
    			event.getRegistry().register(soldier_machine);
				((ItemSpawn)soldier_machine).infor1="advancearmy.infor.item6.desc";
				((ItemSpawn)soldier_machine).infor3="advancearmy.infor.building1.desc";
				((ItemSpawn)soldier_machine).infor4="advancearmy.infor.pickaxe_fix.desc";
				((ItemSpawn)soldier_machine).infor5="advancearmy.infor.building_recycle.desc";
				((ItemSpawn)soldier_machine).infor6="advancearmy.infor.building_rote.desc";
    		}
			{
    			vehicle_machine =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				3,-1,0).setRegistryName(new ResourceLocation(MOD_ID,"vehicle_machine"));
    			event.getRegistry().register(vehicle_machine);
				((ItemSpawn)vehicle_machine).infor1="advancearmy.infor.item6.desc";
				((ItemSpawn)vehicle_machine).infor3="advancearmy.infor.building2.desc";
				((ItemSpawn)vehicle_machine).infor4="advancearmy.infor.pickaxe_fix.desc";
				((ItemSpawn)vehicle_machine).infor5="advancearmy.infor.building_recycle.desc";
				((ItemSpawn)vehicle_machine).infor6="advancearmy.infor.building_rote.desc";
    		}
			{
    			item_spawn_rads =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				1,600,500).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_rads"));
				((ItemSpawn)item_spawn_rads).infor1="专业工程兵";
				((ItemSpawn)item_spawn_rads).infor3="防守状态下能够部署制造大范围辐射场";
    			event.getRegistry().register(item_spawn_rads);
    		}
			{
    			item_spawn_minigunner =  new ItemSpawn(new Item.Properties().tab(GROUP_UNIT), ++id,
				1,450,350).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_minigunner"));
    			event.getRegistry().register(item_spawn_minigunner);
				((ItemSpawn)item_spawn_minigunner).infor1="专业支援兵";
    		}
			{
    			item_spawn_reaper =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1100,900).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_reaper"));
    			event.getRegistry().register(item_spawn_reaper);
				((ItemSpawn)item_spawn_reaper).infor1="四足机甲";
				((ItemSpawn)item_spawn_reaper).infor2="换装穿甲主炮与猛犸牙导弹";
				((ItemSpawn)item_spawn_reaper).infor3="由于设备造价提高,现在禁止进行跳跃";
				((ItemSpawn)item_spawn_reaper).obj_model = new SAObjModel("advancearmy:textures/mob/reaper.obj");
    		}
			
			{
    			item_spawn_mmtank =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1400,1000).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_mmtank"));
    			event.getRegistry().register(item_spawn_mmtank);
				((ItemSpawn)item_spawn_mmtank).infor1="重型坦克";
				((ItemSpawn)item_spawn_mmtank).infor2="翻新后的老旧坦克";
				((ItemSpawn)item_spawn_mmtank).obj_model = new SAObjModel("advancearmy:textures/mob/mmtank.obj");
    		}
			
			{
    			item_spawn_sandbag =  new ItemSpawn(new Item.Properties().stacksTo(64).tab(GROUP_UNIT), ++id,
				3,-1,0).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_sandbag"));
    			event.getRegistry().register(item_spawn_sandbag);
				((ItemSpawn)item_spawn_sandbag).infor1="防御建筑";
				((ItemSpawn)item_spawn_sandbag).infor2="对低威力子弹抗性高,可以让友军子弹穿过";
				((ItemSpawn)item_spawn_sandbag).infor3="可以蹲下右键搬起";
				((ItemSpawn)item_spawn_sandbag).infor4="advancearmy.infor.pickaxe_fix.desc";
				((ItemSpawn)item_spawn_sandbag).infor5="advancearmy.infor.building_recycle.desc";
    		}
			{
    			item_spawn_sandbag2 =  new ItemSpawn(new Item.Properties().stacksTo(64).tab(GROUP_UNIT), ++id,
				3,-1,0).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_sandbag2"));
    			event.getRegistry().register(item_spawn_sandbag2);
				((ItemSpawn)item_spawn_sandbag2).infor1="防御建筑";
				((ItemSpawn)item_spawn_sandbag2).infor2="对低威力子弹抗性高,可以让友军子弹穿过";
				((ItemSpawn)item_spawn_sandbag2).infor3="可以蹲下右键搬起";
				((ItemSpawn)item_spawn_sandbag2).infor4="advancearmy.infor.pickaxe_fix.desc";
				((ItemSpawn)item_spawn_sandbag2).infor5="advancearmy.infor.building_recycle.desc";
    		}
			{
    			item_spawn_sandbag3 =  new ItemSpawn(new Item.Properties().stacksTo(64).tab(GROUP_UNIT), ++id,
				3,-1,0).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_sandbag3"));
    			event.getRegistry().register(item_spawn_sandbag3);
				((ItemSpawn)item_spawn_sandbag3).infor1="防御建筑";
				((ItemSpawn)item_spawn_sandbag3).infor2="对低威力子弹抗性高,可以让友军子弹穿过";
				((ItemSpawn)item_spawn_sandbag3).infor3="可以蹲下右键搬起";
				((ItemSpawn)item_spawn_sandbag3).infor4="advancearmy.infor.pickaxe_fix.desc";
				((ItemSpawn)item_spawn_sandbag3).infor5="advancearmy.infor.building_recycle.desc";
    		}
			{
    			item_spawn_mirage =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1600,1000).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_mirage"));
    			event.getRegistry().register(item_spawn_mirage);
				((ItemSpawn)item_spawn_mirage).infor1="advancearmy.infor.item2.desc";
				((ItemSpawn)item_spawn_mirage).infor2="改进型幻影突击坦克";
				((ItemSpawn)item_spawn_mirage).infor3="将裂缝发射器换成了防御护盾";
				((ItemSpawn)item_spawn_mirage).obj_model = new SAObjModel("advancearmy:textures/mob/mirage.obj");
    		}
			{
    			item_spawn_tesla =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,1400,1000).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_tesla"));
    			event.getRegistry().register(item_spawn_tesla);
				((ItemSpawn)item_spawn_tesla).infor1="advancearmy.infor.item2.desc";
				((ItemSpawn)item_spawn_tesla).infor2="先进的改进型号";
				((ItemSpawn)item_spawn_tesla).infor3="可以短暂进入高频模式进行大范围攻击";
				((ItemSpawn)item_spawn_tesla).obj_model = new SAObjModel("advancearmy:textures/mob/tesla.obj");
    		}
			{
    			item_spawn_bike =  new ItemSpawn(new Item.Properties().stacksTo(1).tab(GROUP_UNIT), ++id,
				0,200,100).setRegistryName(new ResourceLocation(MOD_ID,"item_spawn_bike"));
    			event.getRegistry().register(item_spawn_bike);
				((ItemSpawn)item_spawn_bike).obj_model = new SAObjModel("advancearmy:textures/mob/bike.obj");
    		}
			
			challenge_mob =  new ItemDefence(new Item.Properties().tab(GROUP_ITEM), ++dfid,"挑战1-怪物之围").setRegistryName(new ResourceLocation(MOD_ID,"challenge_mob"));
			event.getRegistry().register(challenge_mob);
			challenge_reb =  new ItemDefence(new Item.Properties().tab(GROUP_ITEM), ++dfid,"挑战2-GLA入侵").setRegistryName(new ResourceLocation(MOD_ID,"challenge_reb"));
			event.getRegistry().register(challenge_reb);
			challenge_pillager =  new ItemDefence(new Item.Properties().tab(GROUP_ITEM), ++dfid,"挑战3-灾厄入侵").setRegistryName(new ResourceLocation(MOD_ID,"challenge_pillager"));
			event.getRegistry().register(challenge_pillager);
			challenge_tank =  new ItemDefence(new Item.Properties().tab(GROUP_ITEM), ++dfid,"挑战4-GLA装甲部队入侵").setRegistryName(new ResourceLocation(MOD_ID,"challenge_tank"));
			event.getRegistry().register(challenge_tank);
			challenge_mobair =  new ItemDefence(new Item.Properties().tab(GROUP_ITEM), ++dfid,"挑战5-铺天盖地").setRegistryName(new ResourceLocation(MOD_ID,"challenge_mobair"));
			event.getRegistry().register(challenge_mobair);
			challenge_air =  new ItemDefence(new Item.Properties().tab(GROUP_ITEM), ++dfid,"挑战6-GLA空军入侵").setRegistryName(new ResourceLocation(MOD_ID,"challenge_air"));
			event.getRegistry().register(challenge_air);
			//challenge_sea =  new ItemDefence(new Item.Properties()/*.tab(GROUP_ITEM)*/, ++dfid,"挑战7-守卫战舰").setRegistryName(new ResourceLocation(MOD_ID,"challenge_sea"));
			//event.getRegistry().register(challenge_sea);
			challenge_aohuan =  new ItemDefence(new Item.Properties().tab(GROUP_ITEM), ++dfid,"挑战8-太空船入侵").setRegistryName(new ResourceLocation(MOD_ID,"challenge_aohuan"));
			event.getRegistry().register(challenge_aohuan);
			challenge_portal =  new ItemDefence(new Item.Properties().tab(GROUP_ITEM), ++dfid,"挑战9-邪恶传送门").setRegistryName(new ResourceLocation(MOD_ID,"challenge_portal"));
			event.getRegistry().register(challenge_portal);
			
			
			support_swun =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,1,0,0).setRegistryName(new ResourceLocation(MOD_ID,"support_swun"));
			event.getRegistry().register(support_swun);
			support_fw020 =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,1,0,0).setRegistryName(new ResourceLocation(MOD_ID,"support_fw020"));
			event.getRegistry().register(support_fw020);
			support_youhun =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,1,0,0).setRegistryName(new ResourceLocation(MOD_ID,"support_youhun"));
			event.getRegistry().register(support_youhun);
			support_ember =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,1,0,0).setRegistryName(new ResourceLocation(MOD_ID,"support_ember"));
			event.getRegistry().register(support_ember);
			((ItemSupport)support_ember).enc=true;
			support_ftkh =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,1,0,0).setRegistryName(new ResourceLocation(MOD_ID,"support_ftkh"));
			event.getRegistry().register(support_ftkh);
			((ItemSupport)support_ftkh).enc=true;
			
			support_a10a =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,0,300,400).setRegistryName(new ResourceLocation(MOD_ID,"support_a10a"));
			event.getRegistry().register(support_a10a);
			support_f35bomb =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,0,400,500).setRegistryName(new ResourceLocation(MOD_ID,"support_f35bomb"));
			event.getRegistry().register(support_f35bomb);
			support_a10ax3 =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,0,600,500).setRegistryName(new ResourceLocation(MOD_ID,"support_a10ax3"));
			event.getRegistry().register(support_a10ax3);
			support_f35bombx3 =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,0,600,600).setRegistryName(new ResourceLocation(MOD_ID,"support_f35bombx3"));
			event.getRegistry().register(support_f35bombx3);
			support_155 =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,0,400,700).setRegistryName(new ResourceLocation(MOD_ID,"support_155"));
			event.getRegistry().register(support_155);//10
			
			support_kh29l =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,0,800,900).setRegistryName(new ResourceLocation(MOD_ID,"support_kh29l"));
			event.getRegistry().register(support_kh29l);
			support_3m22 =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,0,1200,1200).setRegistryName(new ResourceLocation(MOD_ID,"support_3m22"));
			event.getRegistry().register(support_3m22);
			support_agm158 =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,0,1300,1500).setRegistryName(new ResourceLocation(MOD_ID,"support_agm158"));
			event.getRegistry().register(support_agm158);
			support_kh58 =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,0,1600,2500).setRegistryName(new ResourceLocation(MOD_ID,"support_kh58"));
			event.getRegistry().register(support_kh58);
			support_nuke =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,1,0,0).setRegistryName(new ResourceLocation(MOD_ID,"support_nuke"));
			event.getRegistry().register(support_nuke);
			((ItemSupport)support_nuke).enc=true;
			
			support_trident =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid,1,0,0).setRegistryName(new ResourceLocation(MOD_ID,"support_trident"));
			event.getRegistry().register(support_trident);
			((ItemSupport)support_trident).enc=true;
			/*support_cons =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), ++spid).setRegistryName(new ResourceLocation(MOD_ID,"support_cons"));
			event.getRegistry().register(support_cons);*/
			
			portal_star =  new ItemSupport(new Item.Properties().tab(GROUP_ITEM), 0,0,0,0).setRegistryName(new ResourceLocation(MOD_ID,"portal_star"));
			event.getRegistry().register(portal_star);
			((ItemSupport)portal_star).isSummon=true;
			((ItemSupport)portal_star).enc=true;
			((ItemSupport)portal_star).infor1="advancearmy.infor.portal_star.desc";
			
			maptool_respawnc =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"maptool_respawnc"));
			((ItemSpawnMob)maptool_respawnc).infor1="advancearmy.infor.maptool.desc";
			((ItemSpawnMob)maptool_respawnc).infor2="advancearmy.infor.maptool_remove.desc";
			((ItemSpawnMob)maptool_respawnc).infor3="advancearmy.infor.spawns1.desc";
			((ItemSpawnMob)maptool_respawnc).infor4="advancearmy.infor.spawns2.desc";
			((ItemSpawnMob)maptool_respawnc).infor5="advancearmy.infor.spawns3.desc";
			((ItemSpawnMob)maptool_respawnc).infor7="advancearmy.infor.maptool_show.desc";
			event.getRegistry().register(maptool_respawnc);
			
			
			maptool_respawnv =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"maptool_respawnv"));
			((ItemSpawnMob)maptool_respawnv).infor1="advancearmy.infor.maptool.desc";
			((ItemSpawnMob)maptool_respawnv).infor2="advancearmy.infor.maptool_remove.desc";
			((ItemSpawnMob)maptool_respawnv).infor3="advancearmy.infor.spawnv1.desc";
			((ItemSpawnMob)maptool_respawnv).infor4="advancearmy.infor.spawnv2.desc";
			((ItemSpawnMob)maptool_respawnv).infor5="advancearmy.infor.spawnv3.desc";
			((ItemSpawnMob)maptool_respawnv).infor7="advancearmy.infor.maptool_show.desc";
			event.getRegistry().register(maptool_respawnv);
			
			maptool_movepoint =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"maptool_movepoint"));
			((ItemSpawnMob)maptool_movepoint).infor1="advancearmy.infor.maptool.desc";
			((ItemSpawnMob)maptool_movepoint).infor2="advancearmy.infor.maptool_remove.desc";
			((ItemSpawnMob)maptool_movepoint).infor3="advancearmy.infor.movep1.desc";
			((ItemSpawnMob)maptool_movepoint).infor4="advancearmy.infor.movep2.desc";
			((ItemSpawnMob)maptool_movepoint).infor5="advancearmy.infor.movep3.desc";
			((ItemSpawnMob)maptool_movepoint).infor6="advancearmy.infor.movep4.desc";
			((ItemSpawnMob)maptool_movepoint).infor7="advancearmy.infor.maptool_show.desc";
			event.getRegistry().register(maptool_movepoint);
			
			mob_spawn_skeleton =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_skeleton"));
			event.getRegistry().register(mob_spawn_skeleton);
			mob_spawn_pillager =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_pillager"));
			event.getRegistry().register(mob_spawn_pillager);
			mob_spawn_phantom =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_phantom"));
			event.getRegistry().register(mob_spawn_phantom);
			mob_spawn_ghost =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_ghost"));
			event.getRegistry().register(mob_spawn_ghost);
			mob_spawn_creeper =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_creeper"));
			event.getRegistry().register(mob_spawn_creeper);
			mob_spawn_dragonturret =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_dragonturret"));
			event.getRegistry().register(mob_spawn_dragonturret);
			mob_spawn_reb =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_reb"));
			event.getRegistry().register(mob_spawn_reb);
			mob_spawn_aohuan =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_aohuan"));
			event.getRegistry().register(mob_spawn_aohuan);
			mob_spawn_evilportal =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_evilportal"));
			event.getRegistry().register(mob_spawn_evilportal);
			mob_spawn_zombie =  new ItemSpawnMob(new Item.Properties().tab(GROUP_ITEM), ++mobid).setRegistryName(new ResourceLocation(MOD_ID,"mob_spawn_zombie"));
			event.getRegistry().register(mob_spawn_zombie);
			
			unit_remove =  new ItemRemove(new Item.Properties().tab(GROUP_ITEM)).setRegistryName(new ResourceLocation(MOD_ID,"unit_remove"));
			event.getRegistry().register(unit_remove);
    	}
    }
}//end