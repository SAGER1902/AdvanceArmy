package advancearmy.util;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.GroundPathHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.MobEntity;
import advancearmy.AdvanceArmy;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.item.Item;

import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraft.entity.CreatureEntity;

import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.BossInfo;

import wmlib.api.IEnemy;
import net.minecraft.entity.player.ServerPlayerEntity;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.air.EntitySA_Plane;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;

import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.entity.soldier.EntitySA_Conscript;
import advancearmy.entity.soldier.EntitySA_GI;

import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.sea.EntitySA_BattleShip;
import advancearmy.entity.land.EntitySA_FTK;
import advancearmy.entity.air.EntitySA_Fw020;
import advancearmy.entity.land.EntitySA_Ember;
import advancearmy.entity.air.EntitySA_YouHun;
import advancearmy.entity.air.EntitySA_Yw010;
import advancearmy.entity.air.EntitySA_F35;
import advancearmy.entity.air.EntitySA_Helicopter;
import advancearmy.entity.air.EntitySA_AH1Z;
import advancearmy.entity.air.EntitySA_AH6;
import advancearmy.entity.air.EntitySA_A10a;
import net.minecraft.entity.passive.TameableEntity;
import wmlib.common.living.EntityWMVehicleBase;
import net.minecraft.entity.AgeableEntity;
import wmlib.api.ITool;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;
import advancearmy.entity.soldier.EntitySA_Swun;
import advancearmy.entity.land.EntitySA_T55;
import advancearmy.entity.land.EntitySA_Tank;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.land.EntitySA_BMP2;
import advancearmy.entity.air.EntitySA_Lapear;
import advancearmy.entity.land.EntitySA_LaserAA;
import advancearmy.entity.land.EntitySA_Prism;
import advancearmy.entity.land.EntitySA_LAV;
import advancearmy.entity.land.EntitySA_LAVAA;
import advancearmy.entity.land.EntitySA_Bike;
import advancearmy.entity.air.EntitySA_MI24;
import advancearmy.entity.land.EntitySA_M2A2AA;
import advancearmy.entity.land.EntitySA_M2A2;
import advancearmy.entity.land.EntitySA_MMTank;
import advancearmy.entity.land.EntitySA_Reaper;

import advancearmy.entity.land.EntitySA_Car;
import advancearmy.entity.land.EntitySA_Hmmwv;

import advancearmy.entity.air.EntitySA_SU33;
import advancearmy.entity.mob.ERO_Husk;
import advancearmy.entity.mob.EntityAohuan;
import advancearmy.entity.mob.ERO_Pillager;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Phantom;
import advancearmy.entity.mob.ERO_Ghast;
import advancearmy.entity.mob.EvilPortal;
import advancearmy.entity.mob.ERO_REB;
import advancearmy.entity.mob.ERO_Creeper;
import advancearmy.entity.mob.EntityMobSquadBase;

import advancearmy.entity.EntitySA_LandBase;
import advancearmy.event.SASoundEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;

import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import wmlib.api.IHealthBar;
import wmlib.api.IArmy;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.Direction;
public class SummonEntity{
	public static void wildSummon(World world1, double ix, double iy, double iz, int id, boolean isEnemy, Team team, int c) {
		if(!world1.isClientSide()&&world1.getGameTime()>500){
			BlockPos pos = new BlockPos(ix,iy,iz);
			if (world1.canSeeSky(pos)){
				if(id>0&&id<28){
					WeaponVehicleBase vehicle=null;
					if(id == 1) {
						vehicle = new EntitySA_Tank(AdvanceArmy.ENTITY_TANK, world1);
					}else if(id == 2){
						vehicle = new EntitySA_AH1Z(AdvanceArmy.ENTITY_AH1Z, world1);
					}else if(id == 3){
						vehicle = new EntitySA_FTK(AdvanceArmy.ENTITY_FTK, world1);
					}else if(id == 4){
						vehicle = new EntitySA_MI24(AdvanceArmy.ENTITY_MI24, world1);
					}else if(id == 5){
						vehicle = new EntitySA_SU33(AdvanceArmy.ENTITY_SU33, world1);
					}else if(id == 6){
						vehicle = new EntitySA_A10a(AdvanceArmy.ENTITY_A10A, world1);
					}else if(id == 7){
						vehicle = new EntitySA_F35(AdvanceArmy.ENTITY_F35, world1);
					}else if(id == 8){
						vehicle = new EntitySA_Prism(AdvanceArmy.ENTITY_PRISM, world1);
					}else if(id == 9){
						vehicle = new EntitySA_M2A2AA(AdvanceArmy.ENTITY_M2A2AA, world1);
					}else if(id == 10){
						vehicle = new EntitySA_T55(AdvanceArmy.ENTITY_T55, world1);
					}else if(id == 11){
						vehicle = new EntitySA_T72(AdvanceArmy.ENTITY_T72, world1);
					}else if(id == 12){
						vehicle = new EntitySA_T90(AdvanceArmy.ENTITY_T90, world1);
					}else if(id == 13){
						vehicle = new EntitySA_BMP2(AdvanceArmy.ENTITY_BMP2, world1);
					}else if(id == 14){
						vehicle = new EntitySA_Lapear(AdvanceArmy.ENTITY_LAPEAR, world1);
					}else if(id == 15){
						vehicle = new EntitySA_Plane1(AdvanceArmy.ENTITY_PLANE1, world1);
					}else if(id == 16){
						vehicle = new EntitySA_Plane2(AdvanceArmy.ENTITY_PLANE2, world1);
					}else if(id == 17){
						vehicle = new EntitySA_AH6(AdvanceArmy.ENTITY_AH6, world1);
					}else if(id == 18){
						vehicle = new EntitySA_Plane(AdvanceArmy.ENTITY_PLANE, world1);
					}else if(id == 19){
						vehicle = new EntitySA_LAV(AdvanceArmy.ENTITY_LAV, world1);
					}else if(id == 20){
						vehicle = new EntitySA_Helicopter(AdvanceArmy.ENTITY_HELI, world1);
					}else if(id == 21){
						vehicle = new EntitySA_LaserAA(AdvanceArmy.ENTITY_LAA, world1);
					}else if(id == 23){
						vehicle = new EntitySA_Car(AdvanceArmy.ENTITY_CAR, world1);
					}else if(id == 24){
						vehicle = new EntitySA_Hmmwv(AdvanceArmy.ENTITY_HMMWV, world1);
					}else if(id == 25){
						vehicle = new EntitySA_M2A2(AdvanceArmy.ENTITY_M2A2, world1);
					}else if(id == 26){
						vehicle = new EntitySA_M2A2AA(AdvanceArmy.ENTITY_M2A2AA, world1);
					}else if(id == 27){
						vehicle = new EntitySA_LAVAA(AdvanceArmy.ENTITY_LAVAA, world1);
					}
					if(vehicle!=null){
						vehicle.moveTo(ix, iy, iz, 0, 0);
						world1.addFreshEntity(vehicle);
						vehicle.setMoveType(1);
						/*vehicle.setMovePosX((int)ix);
						vehicle.setMovePosY((int)iy);
						vehicle.setMovePosZ((int)iz);*/
					}
				}
				if(id==31||id==32){
					for(int k2 = 0; k2 < c; ++k2){
						EntitySA_SquadBase soldier = null;
						if(id==31){
							soldier = new EntitySA_Soldier(AdvanceArmy.ENTITY_SOLDIER, world1);
						}else if(id==32){
							soldier = new EntitySA_Conscript(AdvanceArmy.ENTITY_CONS, world1);
						}
						if(soldier!=null){
							soldier.canPara = true;
							soldier.setPos(ix+k2, iy, iz);
							world1.addFreshEntity(soldier);
						}
					}
				}
				if(id==33||id==34){
					for(int k2 = 0; k2 < c; ++k2){
						EntityMobSquadBase mobs = null;
						if(id==33){
							mobs = new ERO_REB(AdvanceArmy.ENTITY_REB, world1);
						}else if(id==34){
							mobs = new ERO_Pillager(AdvanceArmy.ENTITY_PI, world1);
						}
						if(mobs!=null){
							mobs.canPara = true;
							mobs.setPos(ix+k2, iy, iz);
							world1.addFreshEntity(mobs);
						}
					}
				}
			}
		}
	}
}
