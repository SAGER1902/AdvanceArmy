package advancearmy.entity.mob;

import java.util.EnumSet;
import java.util.Random;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.Explosion;
import wmlib.common.bullet.EntityShell;
import advancearmy.AdvanceArmy;
import wmlib.api.IEnemy;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.DifficultyInstance;
import javax.annotation.Nullable;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.world.server.ServerWorld;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import wmlib.api.ITool;
import net.minecraftforge.fml.network.FMLPlayMessages;

import net.minecraft.world.gen.Heightmap;
public class ERO_Ghast extends CreatureEntity implements IMob,IEnemy{
   //private static final DataParameter<Boolean> DATA_IS_CHARGING = EntityDataManager.defineId(ERO_Ghast.class, DataSerializers.BOOLEAN);
   private int explosionPower = 5;
   private static final DataParameter<Integer> ID_SIZE = EntityDataManager.defineId(ERO_Ghast.class, DataSerializers.INT);
   public ERO_Ghast(EntityType<? extends ERO_Ghast> p_i50206_1_, World p_i50206_2_) {
      super(p_i50206_1_, p_i50206_2_);
      this.xpReward = 10;
      this.moveControl = new ERO_Ghast.MoveHelperController(this);
   }
	public ERO_Ghast(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_GST, worldIn);
	}
   public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
      return false;
   }
   
   public boolean fireImmune() {
		return true;
	}
   
   protected void checkFallDamage(double p_184231_1_, boolean p_184231_3_, BlockState p_184231_4_, BlockPos p_184231_5_) {
   }
   
   public void travel(Vector3d p_213352_1_) {
      if (this.isInWater()) {
         this.moveRelative(0.02F, p_213352_1_);
         this.move(MoverType.SELF, this.getDeltaMovement());
         this.setDeltaMovement(this.getDeltaMovement().scale((double)0.8F));
      } else if (this.isInLava()) {
         this.moveRelative(0.02F, p_213352_1_);
         this.move(MoverType.SELF, this.getDeltaMovement());
         this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
      } else {
         BlockPos ground = new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ());
         float f = 0.91F;
         if (this.onGround) {
            f = this.level.getBlockState(ground).getSlipperiness(this.level, ground, this) * 0.91F;
         }
         float f1 = 0.16277137F / (f * f * f);
         f = 0.91F;
         if (this.onGround) {
            f = this.level.getBlockState(ground).getSlipperiness(this.level, ground, this) * 0.91F;
         }
         this.moveRelative(this.onGround ? 0.1F * f1 : 0.02F, p_213352_1_);
         this.move(MoverType.SELF, this.getDeltaMovement());
         this.setDeltaMovement(this.getDeltaMovement().scale((double)f));
      }
      this.calculateEntityAnimation(this, false);
   }

   public boolean onClimbable() {
      return false;
   }
   
	protected float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
	protected void registerGoals() {
	  this.goalSelector.addGoal(5, new ERO_Ghast.RandomFlyGoal(this));
	  this.goalSelector.addGoal(7, new ERO_Ghast.LookAroundGoal(this));
	  this.goalSelector.addGoal(7, new ERO_Ghast.FireballAttackGoal(this));
	  /*this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (p_213812_1_) -> {
		 return Math.abs(p_213812_1_.getY() - this.getY()) <= 4.0D;
	  }));*/
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, MobEntity.class, 10, 60F, false, false, (attackentity) -> {
			return this.CanAttack(attackentity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 60F, false, false, (attackentity) -> {
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

	boolean ischarge = false;
   @OnlyIn(Dist.CLIENT)
   public boolean isCharging() {
      //return this.entityData.get(DATA_IS_CHARGING);
	  return this.isAggressive();
   }

   public void setCharging(boolean p_175454_1_) {
      //this.entityData.set(DATA_IS_CHARGING, p_175454_1_);
	  this.ischarge=true;
   }

   /*protected boolean shouldDespawnInPeaceful() {
      return true;
   }*/

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

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(ID_SIZE, 0);
      //this.entityData.define(DATA_IS_CHARGING, false);
   }
   public void setAIType(int p_203034_1_) {
      this.entityData.set(ID_SIZE, MathHelper.clamp(p_203034_1_, 0, 64));
   }
   public int getAIType() {
      return this.entityData.get(ID_SIZE);
   }
   
   public static AttributeModifierMap.MutableAttribute createAttributes() {
      return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.FOLLOW_RANGE, 100.0D);
   }

   public SoundCategory getSoundSource() {
      return SoundCategory.HOSTILE;
   }

   protected SoundEvent getAmbientSound() {
      return SoundEvents.GHAST_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.GHAST_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.GHAST_DEATH;
   }

   protected float getSoundVolume() {
      return 5.0F;
   }

	/*public void checkDespawn() {
		
	}*/

   public static boolean checkGhastSpawnRules(EntityType<ERO_Ghast> p_223368_0_, IWorld p_223368_1_, SpawnReason p_223368_2_, BlockPos p_223368_3_, Random p_223368_4_) {
      //return p_223368_1_.getDifficulty() != Difficulty.PEACEFUL && p_223368_4_.nextInt(20) == 0 && checkMobSpawnRules(p_223368_0_, p_223368_1_, p_223368_2_, p_223368_3_, p_223368_4_);
		return true;
   }

   public int getMaxSpawnClusterSize() {
      return 1;
   }

   public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
      //this.anchorPoint = this.blockPosition().above(5);
      this.setAIType(this.random.nextInt(10));
      return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
   }

   protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
      return 2.6F;
   }

   static class FireballAttackGoal extends Goal {
      private final ERO_Ghast ghast;
      public int chargeTime;

      public FireballAttackGoal(ERO_Ghast p_i45837_1_) {
         this.ghast = p_i45837_1_;
      }

      public boolean canUse() {
         return this.ghast.getTarget() != null;
      }

      public void start() {
         this.chargeTime = 0;
      }

      public void stop() {
         this.ghast.setCharging(false);
      }

	public void AIWeapon2(double w, double h, double z){
		if(this.ghast.getTarget()!=null){
			double d5 = this.ghast.getTarget().getX() - this.ghast.getX();
			double d7 = this.ghast.getTarget().getZ() - this.ghast.getZ();
			double d6 = this.ghast.getTarget().getY() - this.ghast.getY();
			double d1 = this.ghast.getEyeY() - (this.ghast.getTarget().getEyeY());
			double d3 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7);
			float f11 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			this.ghast.xRot = -f11 + 0;//
		}
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= MathHelper.sin(this.ghast.yRot * 0.01745329252F) * z;
		zz11 += MathHelper.cos(this.ghast.yRot * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(this.ghast.yRot * 0.01745329252F + 1) * w;
		zz11 += MathHelper.cos(this.ghast.yRot * 0.01745329252F + 1) * w;
		EntityShell bullet = new EntityShell(this.ghast.level, this.ghast);
		bullet.power = 20;
		bullet.setGravity(0.02F);
		bullet.setExLevel(3);
		bullet.setBulletType(9);
		bullet.moveTo(this.ghast.getX() + xx11, this.ghast.getY()+h, this.ghast.getZ() + zz11, this.ghast.yRot, this.ghast.xRot);
		bullet.shootFromRotation(this.ghast, this.ghast.xRot, this.ghast.yRot, 0.0F, 1F, 2);
		bullet.setFX("FlamethrowerTrail");
		bullet.setModel("wmlib:textures/entity/flare.obj");
		bullet.setTex("wmlib:textures/entity/flare.png");
		if (!this.ghast.level.isClientSide) this.ghast.level.addFreshEntity(bullet);
	}

      public void tick() {
         LivingEntity livingentity = this.ghast.getTarget();
         double d0 = 64.0D;
         if (livingentity.distanceToSqr(this.ghast) < 4096.0D && this.ghast.canSee(livingentity)) {
            World world = this.ghast.level;
            ++this.chargeTime;
            if (this.chargeTime == 10 && !this.ghast.isSilent()) {
               world.levelEvent((PlayerEntity)null, 1015, this.ghast.blockPosition(), 0);
            }

            if (this.chargeTime == 20) {
               double d1 = 4.0D;
               Vector3d vector3d = this.ghast.getViewVector(1);
               double d2 = livingentity.getX() - (this.ghast.getX() + vector3d.x * 4.0D);
               double d3 = livingentity.getY(0.5D) - (0.5D + this.ghast.getY(0.5D));
               double d4 = livingentity.getZ() - (this.ghast.getZ() + vector3d.z * 4.0D);
               if (!this.ghast.isSilent()) {
					world.levelEvent((PlayerEntity)null, 1016, this.ghast.blockPosition(), 0);
               }
			   
			   if(this.ghast.getAIType()>5){
				   if (!( this.ghast.level instanceof ServerWorld)) {
						//return false;
					} else {
						ServerWorld serverworld = (ServerWorld)this.ghast.level;
						ERO_Phantom pha = new ERO_Phantom(AdvanceArmy.ENTITY_PHA, serverworld);
						pha.setPos((double)this.ghast.getX(), (double)this.ghast.getY()-5, (double)this.ghast.getZ());
						pha.setEvilPhantomSize(2);
						//pha.finalizeSpawn(serverworld, this.ghast.level.getCurrentDifficultyAt(pha.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
						serverworld.addFreshEntityWithPassengers(pha);
						if (livingentity != null)pha.setTarget(livingentity); 
					}
			   }else{
					this.AIWeapon2(0,0,1);
			   }
			   this.ghast.setAggressive(false);
               this.chargeTime = -40;
            }
         } else if (this.chargeTime > 0) {
            --this.chargeTime;
         }
		if(this.chargeTime > 10)this.ghast.setAggressive(true);
        //this.ghast.setCharging(this.ghast.isAggressive());
      }
   }

   static class LookAroundGoal extends Goal {
      private final ERO_Ghast ghast;

      public LookAroundGoal(ERO_Ghast p_i45839_1_) {
         this.ghast = p_i45839_1_;
         this.setFlags(EnumSet.of(Goal.Flag.LOOK));
      }

      public boolean canUse() {
         return true;
      }

      public void tick() {
         if (this.ghast.getTarget() == null) {
            Vector3d vector3d = this.ghast.getDeltaMovement();
            this.ghast.yRot = -((float)MathHelper.atan2(vector3d.x, vector3d.z)) * (180F / (float)Math.PI);
            this.ghast.yBodyRot = this.ghast.yRot;
         } else {
            LivingEntity livingentity = this.ghast.getTarget();
            double d0 = 64.0D;
            if (livingentity.distanceToSqr(this.ghast) < 4096.0D) {
               double d1 = livingentity.getX() - this.ghast.getX();
               double d2 = livingentity.getZ() - this.ghast.getZ();
               this.ghast.yRot = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
               this.ghast.yBodyRot = this.ghast.yRot;
            }
         }

      }
   }

   static class MoveHelperController extends MovementController {
      private final ERO_Ghast ghast;
      private int floatDuration;

      public MoveHelperController(ERO_Ghast p_i45838_1_) {
         super(p_i45838_1_);
         this.ghast = p_i45838_1_;
      }

      public void tick() {
         if (this.operation == MovementController.Action.MOVE_TO) {
            if (this.floatDuration-- <= 0) {
               this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
               Vector3d vector3d = new Vector3d(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
               double d0 = vector3d.length();
               vector3d = vector3d.normalize();
               if (this.canReach(vector3d, MathHelper.ceil(d0))) {
                  this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vector3d.scale(0.1D)));
               } else {
                  this.operation = MovementController.Action.WAIT;
               }
            }
         }
      }

      private boolean canReach(Vector3d p_220673_1_, int p_220673_2_) {
         AxisAlignedBB axisalignedbb = this.ghast.getBoundingBox();

         for(int i = 1; i < p_220673_2_; ++i) {
            axisalignedbb = axisalignedbb.move(p_220673_1_);
            if (!this.ghast.level.noCollision(this.ghast, axisalignedbb)) {
               return false;
            }
         }

         return true;
      }
   }

   static class RandomFlyGoal extends Goal {
      private final ERO_Ghast ghast;

      public RandomFlyGoal(ERO_Ghast p_i45836_1_) {
         this.ghast = p_i45836_1_;
         this.setFlags(EnumSet.of(Goal.Flag.MOVE));
      }

      public boolean canUse() {
         MovementController movementcontroller = this.ghast.getMoveControl();
         if (!movementcontroller.hasWanted()) {
            return true;
         } else {
            double d0 = movementcontroller.getWantedX() - this.ghast.getX();
            double d1 = movementcontroller.getWantedY() - this.ghast.getY();
            double d2 = movementcontroller.getWantedZ() - this.ghast.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            return d3 < 1.0D || d3 > 3600.0D;
         }
      }

      public boolean canContinueToUse() {
         return false;
      }
		int block_height = 0;
      public void start() {
         Random random = this.ghast.getRandom();
         double d0 = this.ghast.getX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0);
         double d1 = this.ghast.getY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0);
		 this.block_height = this.ghast.level.getHeight(Heightmap.Type.WORLD_SURFACE, (int)this.ghast.getX(), (int)this.ghast.getZ());
		 if(d1>block_height)d1=block_height;
         double d2 = this.ghast.getZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0);
         this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
      }
   }
}