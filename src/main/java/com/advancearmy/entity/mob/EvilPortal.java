package advancearmy.entity.mob;

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
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import net.minecraft.item.ArmorItem;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.pathfinding.GroundPathNavigator;

import net.minecraftforge.fml.ModList;
import net.minecraft.entity.CreatureEntity;

import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.land.EntitySA_T55;
import advancearmy.entity.land.EntitySA_Car;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;

import advancearmy.entity.EntitySA_LandBase;
import safx.SagerFX;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.BossInfo;
import advancearmy.event.SASoundEvent;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import net.minecraft.entity.player.ServerPlayerEntity;
import wmlib.api.ITool;
import net.minecraft.world.Explosion;
public class EvilPortal extends CreatureEntity implements IMob,IEnemy{
	private static final DataParameter<Integer> ID_TYPE = EntityDataManager.defineId(EvilPortal.class, DataSerializers.INT);
	protected void defineSynchedData() {
	  super.defineSynchedData();
	  this.getEntityData().define(ID_TYPE, 0);
	}
	
	public void setPortalType(int p_203034_1_) {
	  this.entityData.set(ID_TYPE, p_203034_1_);
	}
	public int getPortalType() {
	  return this.entityData.get(ID_TYPE);
	}
	
	/*@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
	  p_213386_4_ = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
	  this.setPortalType(this.random.nextInt(3));
	  return p_213386_4_;
	}*/
   
   
	public EvilPortal(EntityType<? extends EvilPortal> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.xpReward = 100;
	}
	private final ServerBossInfo bossEvent = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenScreen(true);
	public EvilPortal(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_POR, worldIn);
	}
	public void checkDespawn() {
	}
	protected void registerGoals() {
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, MobEntity.class, 10, 25F, false, false, (attackentity) -> {
			return this.CanAttack(attackentity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 25F, false, false, (attackentity) -> {
			return true;
		}));
	}
    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F){
			if(!(entity instanceof IMob)&&!(entity instanceof ITool)||entity==this.getTarget()){
				return true;
			}else{
				return false;
			}
    	}else{
			return false;
		}
    }


	protected void tickDeath() {
	  ++this.deathTime;
	  if (this.deathTime == 1){
		  this.playSound(SASoundEvent.wreck_explosion.get(), 6F, 1.0F);
		  this.level.explode(null, this.getX(), this.getY(), this.getZ(), 6F, false, Explosion.Mode.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("LargeExplosionFire", null, this.getX(), this.getY(), this.getZ(), 0,0,0,2);
	  }
	  if (this.deathTime >= 120) {
		 this.remove(); //Forge keep data until we revive player
		 this.playSound(SASoundEvent.artillery_impact.get(), 12F, 1.0F);
		 this.level.explode(null, this.getX(), this.getY(), this.getZ(), 12F, false, Explosion.Mode.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("BigMissileExplosion", null, this.getX(), this.getY(), this.getZ(), 0,0,0,2);
	  }
	}


	protected int getExperienceReward(PlayerEntity p_70693_1_) {
	  return super.getExperienceReward(p_70693_1_);
	}
	
	public void startSeenByPlayer(ServerPlayerEntity p_184178_1_) {
	  super.startSeenByPlayer(p_184178_1_);
	  this.bossEvent.addPlayer(p_184178_1_);
	}
	public void stopSeenByPlayer(ServerPlayerEntity p_184203_1_) {
	  super.stopSeenByPlayer(p_184203_1_);
	  this.bossEvent.removePlayer(p_184203_1_);
	}
	public void setCustomName(@Nullable ITextComponent p_200203_1_) {
	  super.setCustomName(p_200203_1_);
	  this.bossEvent.setName(this.getDisplayName());
	}   
	public int summontime = 0;
	public float cooltime6 = 0;
	
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	
	public float startTime = 0;
	public float rote = 0;
	public void aiStep() {
		if(rote<360){
			++rote;
		}else{
			rote=0;
		}
		if(startTime<120)++startTime;
		if(startTime==1)this.setPortalType(this.random.nextInt(3));
		if(startTime<100)return;
    	if(this.setx == 0) {
    		this.setx=((int)this.getX());
    		this.sety=((int)this.getY());
    		this.setz=((int)this.getZ());
    	}
    	{
			BlockPos blockpos = new BlockPos(this.setx + 0.5,this.sety - 1,this.setz + 0.5);
			BlockState iblockstate = this.level.getBlockState(blockpos);
			if (this.setx != 0 && !iblockstate.isAir(this.level, blockpos)){
				this.moveTo(this.setx,this.sety,this.setz);
			}else{
				this.moveTo(this.setx,this.getY(), this.setz);
			}
    	}
		if (this.isAlive()){
			this.bossEvent.setPercent(this.getHealth() / this.getMaxHealth());
			if(cooltime6<50)++cooltime6;
			if (!(this.level instanceof ServerWorld)) {
				//return false;
			} else {
			ServerWorld serverworld = (ServerWorld)this.level;
			LivingEntity livingentity = this.getTarget();
			int tx =0;
			int ty =0;
			int tz =0;
			if (livingentity == null && this.getTarget() instanceof LivingEntity) {
			livingentity = (LivingEntity)this.getTarget();
			}
			if(livingentity != null){
			tx = (int)livingentity.getX() + MathHelper.nextInt(this.random, 2, 10) * MathHelper.nextInt(this.random, -1, 1);
			ty = (int)livingentity.getY() + MathHelper.nextInt(this.random, 2, 10);
			tz = (int)livingentity.getZ() + MathHelper.nextInt(this.random, 2, 10) * MathHelper.nextInt(this.random, -1, 1);
			}
			int i = MathHelper.floor(this.getX());
			int j = MathHelper.floor(this.getY());
			int k = MathHelper.floor(this.getZ());
			if(summontime<100)++summontime;
			if(summontime>20){//
				int count = 0;
				int ghost = 0;
				int ve = 0;
				int i1 = i + MathHelper.nextInt(this.random, 2, 10) * MathHelper.nextInt(this.random, -1, 1);
				int j1 = j + MathHelper.nextInt(this.random, 2, 10);
				int k1 = k + MathHelper.nextInt(this.random, 2, 10) * MathHelper.nextInt(this.random, -1, 1);
				List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(34D, 13.0D, 34D));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					if(ent instanceof ERO_Zombie && ((CreatureEntity)ent).getHealth()>0){
						CreatureEntity friend = (CreatureEntity)ent;
						++count;
						if (livingentity != null && friend.getTarget()==null){
							friend.setTarget(livingentity);
							friend.getNavigation().moveTo(livingentity.getX()+i1, livingentity.getY(), livingentity.getZ()+k1, 1.2F);
						}
					}
					if(ent instanceof ERO_REB && ((CreatureEntity)ent).getHealth()>0){
						++count;
						CreatureEntity friend = (CreatureEntity)ent;
						if (livingentity != null && friend.getTarget()==null){
							friend.setTarget(livingentity);
							friend.getNavigation().moveTo(livingentity.getX()+i1, livingentity.getY(), livingentity.getZ()+k1, 1.2F);
						}
					}
					if(ent instanceof ERO_Ghast && ((LivingEntity)ent).getHealth()>0){
						++ghost;
					}
					if((ent instanceof EntitySA_T72||ent instanceof EntitySA_T90) && ((LivingEntity)ent).getHealth()>0){
						++ve;
					}
					if(ent instanceof EntitySA_Plane1 && ((LivingEntity)ent).getHealth()>0){
						++ve;
					}
					if(ent instanceof EntitySA_Plane2 && ((LivingEntity)ent).getHealth()>0){
						++ve;
					}
				}
				if(count<12){
					if(summontime>50+count*2){
						for(int l = 0; l < 50; ++l) {
						   BlockPos blockpos = new BlockPos(i1, j1, k1);
						   EntityType<?> entitytype = AdvanceArmy.ENTITY_EZOMBIE;
						   ERO_Zombie army = new ERO_Zombie(AdvanceArmy.ENTITY_EZOMBIE, serverworld);
						   //EntitySpawnPlacementRegistry.PlacementType entityspawnplacementregistry$placementtype = EntitySpawnPlacementRegistry.getPlacementType(entitytype);
						   /*if (WorldEntitySpawner.isSpawnPositionOk(entityspawnplacementregistry$placementtype, this.level, blockpos, entitytype) /*&& EntitySpawnPlacementRegistry.checkSpawnRules(entitytype, serverworld, SpawnReason.REINFORCEMENT, blockpos, this.level.random)*)*/ {
								army.setPos((double)i1, (double)j1, (double)k1);
								if(this.level.random.nextInt(11)==1){
									army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
								}else if(this.level.random.nextInt(11)==2){
									army.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
								}else if(this.level.random.nextInt(11)==3){
									army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
									army.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
								}else if(this.level.random.nextInt(11)==4){
									army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
									army.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));
								}else if(this.level.random.nextInt(11)==5){
									army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
									army.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
								}else if(this.level.random.nextInt(11)==6){
									army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
									army.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
								}else if(this.level.random.nextInt(11)==7){
									army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
									army.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
								}else if(this.level.random.nextInt(11)==8){
									army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
									army.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
								}else if(this.level.random.nextInt(11)==9){
									army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
									army.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
								}else if(this.level.random.nextInt(11)==10){
									army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
									army.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
								}
								/*if (!this.level.hasNearbyAlivePlayer((double)i1, (double)j1, (double)k1, 7.0D) && this.level.isUnobstructed(army) && this.level.noCollision(army) && !this.level.containsAnyLiquid(army.getBoundingBox()))*/{
									if(ghost<5){
										if(this.level.random.nextInt(4)==4){
											ERO_Ghast enemy = new ERO_Ghast(AdvanceArmy.ENTITY_GST, serverworld);
											enemy.setPos((double)i1, (double)j1+50, (double)k1);
											enemy.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(enemy.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
											serverworld.addFreshEntityWithPassengers(enemy);
										}
									}
									if(this.level.random.nextInt(4)==1){
										ERO_Phantom enemy = new ERO_Phantom(AdvanceArmy.ENTITY_PHA, serverworld);
										enemy.setPos((double)i1, (double)j1+40, (double)k1);
										enemy.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(enemy.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
										serverworld.addFreshEntityWithPassengers(enemy);
									}
									if(this.level.random.nextInt(8)==1 && ve<3){
										EntitySA_Car enemy = new EntitySA_Car(AdvanceArmy.ENTITY_CAR, serverworld);
										enemy.setTargetType(2);
										enemy.setPos((double)i1, (double)j1, (double)k1);
										serverworld.addFreshEntity(enemy);
										if (livingentity != null){
											enemy.setTarget(livingentity);
											enemy.setMoveType(2);
											enemy.setMovePosX(tx);
											enemy.setMovePosY(ty);
											enemy.setMovePosZ(tz);
										}
										{
											ERO_REB reb = new ERO_REB(AdvanceArmy.ENTITY_REB, serverworld);
											reb.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(reb);
											enemy.catchPassenger(reb);
										}
									}
									if(this.level.random.nextInt(10)==1 && ve<3){
										EntitySA_T55 enemy = new EntitySA_T55(AdvanceArmy.ENTITY_T55, serverworld);
										enemy.setTargetType(2);
										enemy.setPos((double)i1, (double)j1, (double)k1);
										serverworld.addFreshEntity(enemy);
										if (livingentity != null){
											enemy.setTarget(livingentity);
											enemy.setMoveType(2);
											enemy.setMovePosX(tx);
											enemy.setMovePosY(ty);
											enemy.setMovePosZ(tz);
										}
										{
											ERO_REB reb = new ERO_REB(AdvanceArmy.ENTITY_REB, serverworld);
											reb.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(reb);
											enemy.catchPassenger(reb);
										}
									}
									if(this.level.random.nextInt(12)==1 && ve<3){
										EntitySA_T72 enemy = new EntitySA_T72(AdvanceArmy.ENTITY_T72, serverworld);
										enemy.setTargetType(2);
										enemy.setPos((double)i1, (double)j1, (double)k1);
										serverworld.addFreshEntity(enemy);
										if (livingentity != null){
											enemy.setTarget(livingentity);
											enemy.setMoveType(2);
											enemy.setMovePosX(tx);
											enemy.setMovePosY(ty);
											enemy.setMovePosZ(tz);
										}
										{
											ERO_REB reb = new ERO_REB(AdvanceArmy.ENTITY_REB, serverworld);
											reb.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(reb);
											enemy.catchPassenger(reb);
										}
									}
									if(this.level.random.nextInt(15)==1 && ve<2){
										EntitySA_T90 enemy = new EntitySA_T90(AdvanceArmy.ENTITY_T90, serverworld);
										enemy.setTargetType(2);
										enemy.setPos((double)i1, (double)j1, (double)k1);
										serverworld.addFreshEntity(enemy);
										if (livingentity != null){
											enemy.setTarget(livingentity);
											enemy.setMoveType(2);
											enemy.setMovePosX(tx);
											enemy.setMovePosY(ty);
											enemy.setMovePosZ(tz);
										}
										{
											ERO_REB reb = new ERO_REB(AdvanceArmy.ENTITY_REB, serverworld);
											reb.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(reb);
											enemy.catchPassenger(reb);
										}
									}
									if(this.level.random.nextInt(7)==1 && ve<4){
										EntitySA_Plane1 enemy = new EntitySA_Plane1(AdvanceArmy.ENTITY_PLANE1, serverworld);
										enemy.movePower=enemy.throttleMax-2;
										enemy.throttle=enemy.throttleMax-2;
										enemy.setTargetType(2);
										enemy.setPos((double)i1, (double)j1+25, (double)k1);
										serverworld.addFreshEntity(enemy);
										if (livingentity != null){
											enemy.setTarget(livingentity);
											enemy.setMoveType(2);
											enemy.setMovePosX(tx);
											enemy.setMovePosY(ty);
											enemy.setMovePosZ(tz);
										}
										{
											ERO_Pillager reb = new ERO_Pillager(AdvanceArmy.ENTITY_PI, serverworld);
											reb.setPos((double)i1, (double)j1+25, (double)k1);
											serverworld.addFreshEntity(reb);
											enemy.catchPassenger(reb);
										}
									}
									if(this.level.random.nextInt(8)==1 && ve<3){
										EntitySA_Plane2 enemy = new EntitySA_Plane2(AdvanceArmy.ENTITY_PLANE2, serverworld);
										enemy.movePower=enemy.throttleMax-2;
										enemy.throttle=enemy.throttleMax-2;
										enemy.setTargetType(2);
										enemy.setPos((double)i1, (double)j1+25, (double)k1);
										serverworld.addFreshEntity(enemy);
										if (livingentity != null){
											enemy.setTarget(livingentity);
											enemy.setMoveType(2);
											enemy.setMovePosX(tx);
											enemy.setMovePosY(ty);
											enemy.setMovePosZ(tz);
										}
										{
											ERO_Pillager reb = new ERO_Pillager(AdvanceArmy.ENTITY_PI, serverworld);
											reb.setPos((double)i1, (double)j1+25, (double)k1);
											serverworld.addFreshEntity(reb);
											enemy.catchPassenger(reb);
										}
									}
									if(this.level.random.nextInt(4)==1){
										ERO_REB enemy = new ERO_REB(AdvanceArmy.ENTITY_REB, serverworld);
										enemy.setPos((double)i1, (double)j1, (double)k1);
										serverworld.addFreshEntity(enemy);
									}
									if(this.level.random.nextInt(5)==2){
										ERO_Creeper enemy = new ERO_Creeper(AdvanceArmy.ENTITY_CREEPER, serverworld);
										enemy.setPos((double)i1, (double)j1, (double)k1);
										serverworld.addFreshEntity(enemy);
									}
									if(this.level.random.nextInt(6)==2){
										ERO_Pillager enemy = new ERO_Pillager(AdvanceArmy.ENTITY_PI, serverworld);
										enemy.setPos((double)i1, (double)j1, (double)k1);
										serverworld.addFreshEntity(enemy);
									}
									if(this.level.random.nextInt(6)==7){
										ERO_Skeleton enemy = new ERO_Skeleton(AdvanceArmy.ENTITY_SKELETON, serverworld);
										enemy.setPos((double)i1, (double)j1, (double)k1);
										serverworld.addFreshEntity(enemy);
									}
									if (livingentity != null)army.setTarget(livingentity);
									serverworld.addFreshEntity(army);//addFreshEntityWithPassengers
									break;
								}
						   }
						}
						this.summontime = 0;
					}
				}
			}
		}
      }
      super.aiStep();
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.WITHER_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.WITHER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.WITHER_DEATH;
   }
}
