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
import net.minecraft.entity.ai.goal.BreakBlockGoal;
import net.minecraft.entity.ai.goal.BreakDoorGoal;
import net.minecraft.entity.ai.goal.OpenDoorGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ArmorItem;

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

import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;

import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.MobEntity;

import advancearmy.AdvanceArmy;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;


import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;

import net.minecraft.pathfinding.GroundPathNavigator;

import net.minecraftforge.fml.ModList;
import net.minecraft.entity.CreatureEntity;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import advancearmy.entity.EntitySA_LandBase;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.entity.ai.ZombieAttackGoalSA;
import net.minecraft.entity.ai.goal.SwimGoal;
import wmlib.api.ITool;
public class ERO_Zombie extends CreatureEntity implements IMob,IEnemy{
	private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (p_213697_0_) -> {
	  return true;
	};
	private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE);
	private final OpenDoorGoal opDoorGoal = new OpenDoorGoal(this, true);
	private boolean canBreakDoors;
	private boolean canSummon=false;
	public ERO_Zombie(EntityType<? extends ERO_Zombie> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.xpReward = 2;
	  ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
	}
	/*public void checkDespawn() {
		
	}*/
	public ERO_Zombie(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_EZOMBIE, worldIn);
		//this.setCanPickUpLoot(true);
	}
	protected float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
	protected void registerGoals() {
	  this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
	  this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
	  this.addBehaviourGoals();
	}

	protected void addBehaviourGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new ZombieAttackGoalSA(this, 1.0D, false));
		this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglinEntity.class));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, MobEntity.class, 10, 10F, false, false, (attackentity) -> {
			return this.CanAttack(attackentity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 10F, false, false, (attackentity) -> {
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

	public boolean canBreakDoors() {
	  return this.canBreakDoors;
	}
	
	public void setCanBreakDoors(boolean flag) {
	  if (this.supportsBreakDoorGoal() && GroundPathHelper.hasGroundPathNavigation(this)) {
		 if (this.canBreakDoors != flag) {
			this.canBreakDoors = flag;
			((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(flag);
			if (flag) {
				this.goalSelector.addGoal(1, this.opDoorGoal);
			   this.goalSelector.addGoal(1, this.breakDoorGoal);
			} else {
			   this.goalSelector.removeGoal(this.breakDoorGoal);
			   this.goalSelector.removeGoal(this.opDoorGoal);
			}
		 }
	  } else if (this.canBreakDoors) {
		 this.goalSelector.removeGoal(this.breakDoorGoal);
		 this.canBreakDoors = false;
	  }
	}

	protected boolean supportsBreakDoorGoal() {
	  return true;
	}

	public void onSyncedDataUpdated(DataParameter<?> p_184206_1_) {
	  super.onSyncedDataUpdated(p_184206_1_);
	}
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof IEnemy){
				return false;
			}else{
				return super.hurt(source, par2);
			}
		}else{
			return super.hurt(source, par2);
		}
	}
	public float move_type = 0;
	public float movecool = 0;
	public float summontime = 0;
	public float cooltime6 = 0;
	public Vector3d motions = this.getDeltaMovement();
   public void aiStep() {
      if (this.isAlive()) {
		if(this.getHealth()<this.getMaxHealth() && this.cooltime6>45 && canSummon){
			this.setHealth(this.getHealth()+1);
			this.cooltime6 = 0;
		}
		if(cooltime6<50)++cooltime6;
		if(movecool<100)++movecool;
		this.updateSwingTime();
		if (!(this.level instanceof ServerWorld)) {
			//return false;
		} else{
			ServerWorld serverworld = (ServerWorld)this.level;
			LivingEntity livingentity = this.getTarget();
			if (livingentity == null && this.getTarget() instanceof LivingEntity) {
				livingentity = (LivingEntity)this.getTarget();
			}
			if(movecool>99){
				this.move_type=this.level.random.nextInt(3);
				movecool = 0;
			}
			int i = MathHelper.floor(this.getX());
			int j = MathHelper.floor(this.getY());
			int k = MathHelper.floor(this.getZ());
			if(summontime<500)++summontime;
			if(summontime>99 && canSummon){
				int count = 0;
				int husk = 0;
				int ve = 0;
				List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(40D, 50.0D, 40D));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity friend = list.get(k2);
					if(friend instanceof ERO_Zombie && ((LivingEntity)friend).getHealth()>0){
						++count;
					}
					if(friend instanceof ERO_Husk && ((LivingEntity)friend).getHealth()>0){
						++husk;
					}
					if(friend instanceof EntitySA_T72 && ((LivingEntity)friend).getHealth()>0){
						++ve;
					}
					if(friend instanceof EntitySA_Plane1 && ((LivingEntity)friend).getHealth()>0){
						++ve;
					}
					if(friend instanceof EntitySA_Plane2 && ((LivingEntity)friend).getHealth()>0){
						++ve;
					}
				}
				if(count<25){
					if(summontime>450){
						for(int l = 0; l < 50; ++l) {
							int i1 = i + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
							int j1 = j + MathHelper.nextInt(this.random, 1, 3);
							int k1 = k + MathHelper.nextInt(this.random, 7, 40) * MathHelper.nextInt(this.random, -1, 1);
							BlockPos blockpos = new BlockPos(i1, j1, k1);
							EntityType<?> entitytype = AdvanceArmy.ENTITY_EHUSK;
							ERO_Husk army = new ERO_Husk(AdvanceArmy.ENTITY_EHUSK, serverworld);
							EntitySpawnPlacementRegistry.PlacementType entityspawnplacementregistry$placementtype = EntitySpawnPlacementRegistry.getPlacementType(entitytype);
							/*if (WorldEntitySpawner.isSpawnPositionOk(entityspawnplacementregistry$placementtype, this.level, blockpos, entitytype))*/{
								/*if (!this.level.hasNearbyAlivePlayer((double)i1, (double)j1, (double)k1, 7.0D) && this.level.isUnobstructed(army) && this.level.noCollision(army) && !this.level.containsAnyLiquid(army.getBoundingBox()))*/{
									army.setPos((double)i1, (double)j1, (double)k1);
									if(this.level.random.nextInt(5)==1){
										army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
									}else if(this.level.random.nextInt(5)==2){
										army.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
									}else if(this.level.random.nextInt(5)==2){
										army.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
										army.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
									}
									if (livingentity != null)army.setTarget(livingentity);
									army.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(army.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
									serverworld.addFreshEntity(army);
									if(this.level.random.nextInt(10)==1 && count<25){
										ERO_Phantom pha = new ERO_Phantom(AdvanceArmy.ENTITY_PHA, serverworld);
										pha.setPos((double)i1, (double)j+30, (double)k1);
										pha.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(pha.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
										serverworld.addFreshEntity(pha);
										if (livingentity != null)pha.setTarget(livingentity);
									}
									if(count>10){
										if(this.level.random.nextInt(8)==1 && ve<1){
											EntitySA_T72 pha = new EntitySA_T72(AdvanceArmy.ENTITY_T72, serverworld);
											pha.setTargetType(2);
											pha.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
											{
												ERO_REB reb = new ERO_REB(AdvanceArmy.ENTITY_REB, serverworld);
												reb.setPos((double)i1, (double)j1, (double)k1);
												reb.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(reb.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
												serverworld.addFreshEntity(reb);
												pha.catchPassenger(reb);
											}
										}
										if(this.level.random.nextInt(7)==1 && ve<4){
											EntitySA_Plane1 pha = new EntitySA_Plane1(AdvanceArmy.ENTITY_PLANE1, serverworld);
											pha.setTargetType(2);
											pha.setPos((double)i1, (double)j1+25, (double)k1);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
											{
												ERO_Pillager reb = new ERO_Pillager(AdvanceArmy.ENTITY_PI, serverworld);
												reb.setPos((double)i1, (double)j1+25, (double)k1);
												reb.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(reb.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
												serverworld.addFreshEntity(reb);
												pha.catchPassenger(reb);
											}
										}
										if(this.level.random.nextInt(8)==1 && ve<3){
											EntitySA_Plane2 pha = new EntitySA_Plane2(AdvanceArmy.ENTITY_PLANE2, serverworld);
											pha.setTargetType(2);
											pha.setPos((double)i1, (double)j1+25, (double)k1);
											serverworld.addFreshEntity(pha);
											if (livingentity != null)pha.setTarget(livingentity);
											{
												ERO_Pillager reb = new ERO_Pillager(AdvanceArmy.ENTITY_PI, serverworld);
												reb.setPos((double)i1, (double)j1+25, (double)k1);
												reb.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(reb.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
												serverworld.addFreshEntity(reb);
												pha.catchPassenger(reb);
											}
										}
										if(this.level.random.nextInt(5)==4){
											ERO_Skeleton pha = new ERO_Skeleton(AdvanceArmy.ENTITY_SKELETON, serverworld);
											pha.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
											pha.setPos((double)i1, (double)j1, (double)k1);
											pha.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(pha.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
											serverworld.addFreshEntityWithPassengers(pha);
											if (livingentity != null)pha.setTarget(livingentity);
										}
										if(this.level.random.nextInt(5)==3){
											ERO_Creeper pha = new ERO_Creeper(AdvanceArmy.ENTITY_CREEPER, serverworld);
											pha.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
											pha.setPos((double)i1, (double)j1, (double)k1);
											pha.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(pha.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
											serverworld.addFreshEntityWithPassengers(pha);
											if (livingentity != null)pha.setTarget(livingentity);
										}
										if(this.level.random.nextInt(5)==2){
											ERO_Pillager pha = new ERO_Pillager(AdvanceArmy.ENTITY_PI, serverworld);
											pha.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
											pha.setPos((double)i1, (double)j1, (double)k1);
											pha.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(pha.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
											serverworld.addFreshEntityWithPassengers(pha);
											if (livingentity != null)pha.setTarget(livingentity);
										}
										if(this.level.random.nextInt(4)==1){
											ERO_REB pha = new ERO_REB(AdvanceArmy.ENTITY_REB, serverworld);
											pha.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
											pha.setPos((double)i1, (double)j1, (double)k1);
											pha.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(pha.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
											serverworld.addFreshEntityWithPassengers(pha);
											if (livingentity != null)pha.setTarget(livingentity);
										}
										if(husk>25){
											if(this.level.random.nextInt(4)==1){
												ERO_Zombie pha = new ERO_Zombie(AdvanceArmy.ENTITY_EZOMBIE, serverworld);
												pha.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
												pha.canSummon=true;
												pha.setPos((double)i1, (double)j1, (double)k1);
												pha.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(pha.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
												serverworld.addFreshEntityWithPassengers(pha);
												if (livingentity != null)pha.setTarget(livingentity);
											}
										}
									}
									break;
								}
							}
						}
						this.summontime = 0;
					}
				}
			}
		}
		float sp = 0.20F;//移速
		this.moveway(this, sp, 25, 25);//看目标+移动
      }
      super.aiStep();
   }

	public void moveway(ERO_Zombie entity, float sp, double max, double range1) {
		boolean ta = false;
		{//索敌
			if (entity.getTarget() != null) {
				if (!entity.getTarget().isInvisible()) {
					if (entity.getTarget().getHealth() > 0.0F) {
						{
							{//
								if(entity.move_type==1 && entity.cooltime6>40){//跳跃模式
									Vector3d vector3d = entity.getDeltaMovement();
									entity.setDeltaMovement(1.5F*vector3d.x, 0.3D+entity.level.random.nextInt(2)*0.1D, 1.5F*vector3d.z);
									entity.cooltime6 = 0;
								}
								if(entity.move_type!=0){//高速模式
									entity.getNavigation().moveTo(entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), 3.5);
								}
							}
						}
					}
				}
			}
		}
	}

   protected boolean isSunSensitive() {
      return true;
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.ZOMBIE_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.ZOMBIE_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.ZOMBIE_DEATH;
   }

   protected SoundEvent getStepSound() {
      return SoundEvents.ZOMBIE_STEP;
   }

   protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
      this.playSound(this.getStepSound(), 0.15F, 1.0F);
   }

   public CreatureAttribute getMobType() {
      return CreatureAttribute.UNDEAD;
   }

   public void addAdditionalSaveData(CompoundNBT nbt) {
      super.addAdditionalSaveData(nbt);
      nbt.putBoolean("CanBreakDoors", this.canBreakDoors());
	  nbt.putBoolean("CanSummon", this.canSummon);
   }
   public void readAdditionalSaveData(CompoundNBT nbt) {
      super.readAdditionalSaveData(nbt);
	  //this.setCanBreakDoors(nbt.getBoolean("CanBreakDoors"));
	  this.setCanBreakDoors(true);
      this.canSummon=nbt.getBoolean("CanSummon");
   }
	int destroyCount = 0;
	public void killed(ServerWorld p_241847_1_, LivingEntity p_241847_2_) {
		super.killed(p_241847_1_, p_241847_2_);
		++destroyCount;
		if(destroyCount==5){
			/*this.setCanBreakDoors(true);
			this.setCanPickUpLoot(true);*/
			this.populateDefaultEquipmentSlots(null);
		}
		if(destroyCount==10){
			canSummon=true;
			if (this.random.nextFloat() < 0.1F){
				this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.TOTEM_OF_UNDYING));
				this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHealth()*2);
			}else{
				this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SOUL_TORCH));
				this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHealth()*1.5F);
			}
		}
	}


   protected void populateDefaultEquipmentSlots(DifficultyInstance diff) {
      if (this.random.nextFloat() < 0.5F) {
         int i = this.random.nextInt(3);
         if (i == 0) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
			this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SHIELD));
         } else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
         }
      }
      if (this.random.nextFloat() < 0.55F) {
         int i = this.random.nextInt(2);
         float f = 0.5F;
         if (this.random.nextFloat() < 0.095F)++i;
         if (this.random.nextFloat() < 0.095F)++i;
         if (this.random.nextFloat() < 0.095F)++i;
         boolean flag = true;
         for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
            if (equipmentslottype.getType() == EquipmentSlotType.Group.ARMOR) {
               ItemStack itemstack = this.getItemBySlot(equipmentslottype);
               if (!flag && this.random.nextFloat() < f) {
                  break;
               }
               flag = false;
               if (itemstack.isEmpty()) {
                  Item item = getEquipmentForSlot(equipmentslottype, i);
                  if (item != null) {
                     this.setItemSlot(equipmentslottype, new ItemStack(item));
                  }
               }
            }
         }
      }
   }

   protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
      return this.isBaby() ? 0.93F : 1.74F;
   }

   public boolean canHoldItem(ItemStack stack) {
      return stack.getItem() == Items.EGG && this.isBaby() && this.isPassenger() ? false : super.canHoldItem(stack);
   }

   /*@Nullable
   public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance diff, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
      return data;
   }*/

   public double getMyRidingOffset() {
      return this.isBaby() ? 0.0D : -0.45D;
   }

   protected ItemStack getSkull() {
      return new ItemStack(Items.ZOMBIE_HEAD);
   }

   public static class GroupData implements ILivingEntityData {
      public final boolean isBaby;
      public final boolean canSpawnJockey;
      public GroupData(boolean p_i231567_1_, boolean p_i231567_2_) {
         this.isBaby = p_i231567_1_;
         this.canSpawnJockey = p_i231567_2_;
      }
   }
}
