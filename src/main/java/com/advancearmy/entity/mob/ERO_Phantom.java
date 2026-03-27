package advancearmy.entity.mob;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import advancearmy.AdvanceArmy;
import net.minecraft.world.server.ServerWorld;
import wmlib.common.bullet.EntityBullet;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.Explosion;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import wmlib.api.ITool;
import net.minecraft.entity.MoverType;
public class ERO_Phantom extends CreatureEntity implements IMob,IEnemy{
   private static final DataParameter<Integer> ID_SIZE = EntityDataManager.defineId(ERO_Phantom.class, DataSerializers.INT);
   private Vector3d moveTargetPoint = Vector3d.ZERO;
   private BlockPos anchorPoint = BlockPos.ZERO;
   private ERO_Phantom.AttackPhase attackPhase = ERO_Phantom.AttackPhase.CIRCLE;

   public ERO_Phantom(EntityType<? extends ERO_Phantom> p_i50200_1_, World p_i50200_2_) {
      super(p_i50200_1_, p_i50200_2_);
      this.xpReward = 5;
      this.moveControl = new ERO_Phantom.MoveHelperController(this);
      this.lookControl = new ERO_Phantom.LookHelperController(this);
   }
	public ERO_Phantom(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_PHA, worldIn);
	}
   public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
      return false;
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

   protected BodyController createBodyControl() {
      return new ERO_Phantom.BodyHelperController(this);
   }
	protected float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
   protected void registerGoals() {
	this.goalSelector.addGoal(1, new ERO_Phantom.PickAttackGoal());
	this.goalSelector.addGoal(2, new ERO_Phantom.SweepAttackGoal());
	this.goalSelector.addGoal(3, new ERO_Phantom.OrbitPointGoal());
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
   }

   public void setEvilPhantomSize(int p_203034_1_) {
      this.entityData.set(ID_SIZE, MathHelper.clamp(p_203034_1_, 0, 64));
   }

   private void updateEvilPhantomSizeInfo() {
      this.refreshDimensions();
	  if(this.getEvilPhantomSize()<3){
		  this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)(40 + this.getEvilPhantomSize()));
	  }else{
		  this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)(6 + this.getEvilPhantomSize()));
	  }
   }

   public int getEvilPhantomSize() {
      return this.entityData.get(ID_SIZE);
   }

   protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
      return p_213348_2_.height * 0.35F;
   }

   public void onSyncedDataUpdated(DataParameter<?> p_184206_1_) {
      if (ID_SIZE.equals(p_184206_1_)) {
         this.updateEvilPhantomSizeInfo();
      }
      super.onSyncedDataUpdated(p_184206_1_);
   }
	/*public void checkDespawn() {
		
	}*/
   /*protected boolean shouldDespawnInPeaceful() {
      return true;
   }*/
	public void AIWeapon2(double w, double h, double z){
		if(this.getTarget()!=null){
			double d5 = this.getTarget().getX() - this.getX();
			double d7 = this.getTarget().getZ() - this.getZ();
			double d6 = this.getTarget().getY() - this.getY();
			double d1 = this.getEyeY() - (this.getTarget().getEyeY());
			double d3 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7);
			float f11 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			this.xRot = -f11 + 0;//
		}
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= MathHelper.sin(this.yRot * 0.01745329252F) * z;
		zz11 += MathHelper.cos(this.yRot * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(this.yRot * 0.01745329252F + 1) * w;
		zz11 += MathHelper.cos(this.yRot * 0.01745329252F + 1) * w;
		EntityBullet bullet2 = new EntityBullet(this.level, this);
		bullet2.power = 4;
		bullet2.setGravity(0.025F);
		bullet2.setBulletType(5);
		bullet2.moveTo(this.getX() + xx11, this.getY()+h, this.getZ() + zz11, this.yRot, this.xRot);
		bullet2.shootFromRotation(this, this.xRot, this.yRot, 0.0F, 3F, 2);
		if (!this.level.isClientSide) this.level.addFreshEntity(bullet2);
	}
	int cooltime = 0;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(this.getEvilPhantomSize()>5){
				if(this.cooltime<20)++this.cooltime;
				if(this.attackPhase == AttackPhase.SWOOP && this.cooltime>10){
					this.AIWeapon2(0,0,1);
					this.cooltime = 0;
				}
			}
		}
   }

	/*public boolean canAttack(Entity ent){
		//return !(ent instanceof IMob);
		return true;
	}*/
	/*public void aiStep() {
	  if (this.isAlive() && this.isSunBurnTick()) {
		 this.setSecondsOnFire(8);
	  }
	  super.aiStep();
	}*/

	/*protected void customServerAiStep() {
	  super.customServerAiStep();
	}*/

	public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
	  this.anchorPoint = this.blockPosition().above(5);
	  this.setEvilPhantomSize(this.random.nextInt(8));
	  return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
	}

	public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
	  super.readAdditionalSaveData(p_70037_1_);
	  if (p_70037_1_.contains("AX")) {
		 this.anchorPoint = new BlockPos(p_70037_1_.getInt("AX"), p_70037_1_.getInt("AY"), p_70037_1_.getInt("AZ"));
	  }
	  this.setEvilPhantomSize(p_70037_1_.getInt("Size"));
	}

	public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
	  super.addAdditionalSaveData(p_213281_1_);
	  p_213281_1_.putInt("AX", this.anchorPoint.getX());
	  p_213281_1_.putInt("AY", this.anchorPoint.getY());
	  p_213281_1_.putInt("AZ", this.anchorPoint.getZ());
	  p_213281_1_.putInt("Size", this.getEvilPhantomSize());
	}

	@OnlyIn(Dist.CLIENT)
	public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
	  return true;
	}

	public SoundCategory getSoundSource() {
	  return SoundCategory.HOSTILE;
	}

	protected SoundEvent getAmbientSound() {
	  return SoundEvents.PHANTOM_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
	  return SoundEvents.PHANTOM_HURT;
	}

	protected SoundEvent getDeathSound() {
	  return SoundEvents.PHANTOM_DEATH;
	}

	public CreatureAttribute getMobType() {
	  return CreatureAttribute.UNDEAD;
	}

	protected float getSoundVolume() {
	  return 1.0F;
	}

	public boolean canAttackType(EntityType<?> p_213358_1_) {
	  return true;
	}

	public EntitySize getDimensions(Pose p_213305_1_) {
	  int i = this.getEvilPhantomSize();
	  EntitySize entitysize = super.getDimensions(p_213305_1_);
	  float f = (entitysize.width + 0.2F * (float)i) / entitysize.width;
	  return entitysize.scale(f);
	}

	static enum AttackPhase {
	  CIRCLE,
	  SWOOP;
	}

	class BodyHelperController extends BodyController {
	  public BodyHelperController(MobEntity p_i49925_2_) {
		 super(p_i49925_2_);
	  }

	  public void clientTick() {
		 ERO_Phantom.this.yHeadRot = ERO_Phantom.this.yBodyRot;
		 ERO_Phantom.this.yBodyRot = ERO_Phantom.this.yRot;
	  }
	}

	class LookHelperController extends LookController {
	  public LookHelperController(MobEntity p_i48802_2_) {
		 super(p_i48802_2_);
	  }

	  public void tick() {
	  }
	}

	abstract class MoveGoal extends Goal {
	  public MoveGoal() {
		 this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	  }

	  protected boolean touchingTarget() {
		 return ERO_Phantom.this.moveTargetPoint.distanceToSqr(ERO_Phantom.this.getX(), ERO_Phantom.this.getY(), ERO_Phantom.this.getZ()) < 4.0D;
	  }
	}

	class MoveHelperController extends MovementController {
	  private float speed = 0.1F;

	  public MoveHelperController(MobEntity p_i48801_2_) {
		 super(p_i48801_2_);
	  }

	  public void tick() {
		 if (ERO_Phantom.this.horizontalCollision) {
			ERO_Phantom.this.yRot += 180.0F;
			this.speed = 0.1F;
		 }

		 float f = (float)(ERO_Phantom.this.moveTargetPoint.x - ERO_Phantom.this.getX());
		 float f1 = (float)(ERO_Phantom.this.moveTargetPoint.y - ERO_Phantom.this.getY());
		 float f2 = (float)(ERO_Phantom.this.moveTargetPoint.z - ERO_Phantom.this.getZ());
		 double d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
		 double d1 = 1.0D - (double)MathHelper.abs(f1 * 0.7F) / d0;
		 f = (float)((double)f * d1);
		 f2 = (float)((double)f2 * d1);
		 d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
		 double d2 = (double)MathHelper.sqrt(f * f + f2 * f2 + f1 * f1);
		 float f3 = ERO_Phantom.this.yRot;
		 float f4 = (float)MathHelper.atan2((double)f2, (double)f);
		 float f5 = MathHelper.wrapDegrees(ERO_Phantom.this.yRot + 90);
		 float f6 = MathHelper.wrapDegrees(f4 * (180F / (float)Math.PI));
		 ERO_Phantom.this.yRot = MathHelper.approachDegrees(f5, f6, 4.0F) - 90.0F;
		 ERO_Phantom.this.yBodyRot = ERO_Phantom.this.yRot;
		 if (MathHelper.degreesDifferenceAbs(f3, ERO_Phantom.this.yRot) < 3.0F) {
			this.speed = MathHelper.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
		 } else {
			this.speed = MathHelper.approach(this.speed, 0.2F, 0.025F);
		 }

		 float f7 = (float)(-(MathHelper.atan2((double)(-f1), d0) * (double)(180F / (float)Math.PI)));
		 ERO_Phantom.this.xRot = f7;
		 float f8 = ERO_Phantom.this.yRot + 90.0F;
		 double d3 = (double)(this.speed * MathHelper.cos(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f / d2);
		 double d4 = (double)(this.speed * MathHelper.sin(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f2 / d2);
		 double d5 = (double)(this.speed * MathHelper.sin(f7 * ((float)Math.PI / 180F))) * Math.abs((double)f1 / d2);
		 Vector3d vector3d = ERO_Phantom.this.getDeltaMovement();
		 ERO_Phantom.this.setDeltaMovement(vector3d.add((new Vector3d(d3, d5, d4)).subtract(vector3d).scale(0.2D)));
	  }
	}

	class OrbitPointGoal extends ERO_Phantom.MoveGoal {
	  private float angle;
	  private float distance;
	  private float height;
	  private float clockwise;

	  private OrbitPointGoal() {
	  }

	  public boolean canUse() {
		 return ERO_Phantom.this.getTarget() == null || ERO_Phantom.this.attackPhase == ERO_Phantom.AttackPhase.CIRCLE;
	  }

	  public void start() {
		 this.distance = 5.0F + ERO_Phantom.this.random.nextFloat() * 10.0F;
		 this.height = -4.0F + ERO_Phantom.this.random.nextFloat() * 9.0F;
		 this.clockwise = ERO_Phantom.this.random.nextBoolean() ? 1.0F : -1.0F;
		 this.selectNext();
	  }

	  public void tick() {
		 if (ERO_Phantom.this.random.nextInt(350) == 0) {
			this.height = -4.0F + ERO_Phantom.this.random.nextFloat() * 9.0F;
		 }

		 if (ERO_Phantom.this.random.nextInt(250) == 0) {
			++this.distance;
			if (this.distance > 15.0F) {
			   this.distance = 5.0F;
			   this.clockwise = -this.clockwise;
			}
		 }

		 if (ERO_Phantom.this.random.nextInt(450) == 0) {
			this.angle = ERO_Phantom.this.random.nextFloat() * 2.0F * (float)Math.PI;
			this.selectNext();
		 }

		 if (this.touchingTarget()) {
			this.selectNext();
		 }

		 if (ERO_Phantom.this.moveTargetPoint.y < ERO_Phantom.this.getY() && !ERO_Phantom.this.level.isEmptyBlock(ERO_Phantom.this.blockPosition().below(1))) {
			this.height = Math.max(1.0F, this.height);
			this.selectNext();
		 }

		 if (ERO_Phantom.this.moveTargetPoint.y > ERO_Phantom.this.getY() && !ERO_Phantom.this.level.isEmptyBlock(ERO_Phantom.this.blockPosition().above(1))) {
			this.height = Math.min(-1.0F, this.height);
			this.selectNext();
		 }

	  }

	  private void selectNext() {
		 if (BlockPos.ZERO.equals(ERO_Phantom.this.anchorPoint)) {
			ERO_Phantom.this.anchorPoint = ERO_Phantom.this.blockPosition();
		 }

		 this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180);
		 ERO_Phantom.this.moveTargetPoint = Vector3d.atLowerCornerOf(ERO_Phantom.this.anchorPoint).add((double)(this.distance * MathHelper.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * MathHelper.sin(this.angle)));
	  }
	}

	class PickAttackGoal extends Goal {
	  private int nextSweepTick;

	  private PickAttackGoal() {
	  }

	  public boolean canUse() {
		 LivingEntity livingentity = ERO_Phantom.this.getTarget();
		 return livingentity != null ? /*ERO_Phantom.this.canAttack(ERO_Phantom.this.getTarget(), EntityPredicate.DEFAULT)*/true : false;
	  }

	  public void start() {
		 this.nextSweepTick = 10;
		 ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.CIRCLE;
		 this.setAnchorAboveTarget();
	  }

	  public void stop() {
		 ERO_Phantom.this.anchorPoint = ERO_Phantom.this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, ERO_Phantom.this.anchorPoint).above(10 + ERO_Phantom.this.random.nextInt(20));
	  }

	  public void tick() {
		 if (ERO_Phantom.this.attackPhase == ERO_Phantom.AttackPhase.CIRCLE) {
			--this.nextSweepTick;
			if (this.nextSweepTick <= 0) {
			   ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.SWOOP;
			   this.setAnchorAboveTarget();
			   this.nextSweepTick = (8 + ERO_Phantom.this.random.nextInt(4)) * 20;
			   ERO_Phantom.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + ERO_Phantom.this.random.nextFloat() * 0.1F);
			}
		 }

	  }

	  private void setAnchorAboveTarget() {
		 ERO_Phantom.this.anchorPoint = ERO_Phantom.this.getTarget().blockPosition().above(20 + ERO_Phantom.this.random.nextInt(20));
		 if (ERO_Phantom.this.anchorPoint.getY() < ERO_Phantom.this.level.getSeaLevel()) {
			ERO_Phantom.this.anchorPoint = new BlockPos(ERO_Phantom.this.anchorPoint.getX(), ERO_Phantom.this.level.getSeaLevel() + 1, ERO_Phantom.this.anchorPoint.getZ());
		 }
	  }
	}

	class SweepAttackGoal extends ERO_Phantom.MoveGoal {
	  private SweepAttackGoal() {
	  }

	  public boolean canUse() {
		 return ERO_Phantom.this.getTarget() != null && ERO_Phantom.this.attackPhase == ERO_Phantom.AttackPhase.SWOOP;
	  }

	  public boolean canContinueToUse() {
		 LivingEntity livingentity = ERO_Phantom.this.getTarget();
		 if (livingentity == null) {
			return false;
		 } else if (!livingentity.isAlive()) {
			return false;
		 } else if (!(livingentity instanceof PlayerEntity) || !((PlayerEntity)livingentity).isSpectator() && !((PlayerEntity)livingentity).isCreative()) {
			if (!this.canUse()) {
			   return false;
			} else {
			   /*if (ERO_Phantom.this.tickCount % 20 == 0) {
				  List<CatEntity> list = ERO_Phantom.this.level.getEntitiesOfClass(CatEntity.class, ERO_Phantom.this.getBoundingBox().inflate(16.0D), EntityPredicates.ENTITY_STILL_ALIVE);
				  if (!list.isEmpty()) {
					 for(CatEntity catentity : list) {
						catentity.hiss();
					 }

					 return false;
				  }
			   }*/
			   return true;
			}
		 } else {
			return false;
		 }
	  }

	  public void start() {
	  }

	  public void stop() {
		 ERO_Phantom.this.setTarget((LivingEntity)null);
		 ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.CIRCLE;
	  }

	  public void tick() {
		 LivingEntity livingentity = ERO_Phantom.this.getTarget();
		 ERO_Phantom.this.moveTargetPoint = new Vector3d(livingentity.getX(), livingentity.getY(0.5D), livingentity.getZ());
		 if (ERO_Phantom.this.getBoundingBox().inflate((double)0.2F).intersects(livingentity.getBoundingBox())) {
			if(ERO_Phantom.this.getEvilPhantomSize()<3){
				ERO_Phantom.this.dead = true;
				ERO_Phantom.this.remove();
				ERO_Phantom.this.level.explode(ERO_Phantom.this, ERO_Phantom.this.getX(), ERO_Phantom.this.getY(), ERO_Phantom.this.getZ(), 2+ERO_Phantom.this.getEvilPhantomSize(), false, Explosion.Mode.NONE);
			}
			ERO_Phantom.this.doHurtTarget(livingentity);

			if (!( ERO_Phantom.this.level instanceof ServerWorld)) {
				//return false;
			} else {
				 ServerWorld serverworld = (ServerWorld)ERO_Phantom.this.level;
				if(ERO_Phantom.this.level.random.nextInt(4)==1){
					ERO_Phantom pha = new ERO_Phantom(AdvanceArmy.ENTITY_PHA, serverworld);
					pha.setPos((double)ERO_Phantom.this.getX(), (double)ERO_Phantom.this.getY()+50, (double)ERO_Phantom.this.getZ());
					pha.finalizeSpawn(serverworld, ERO_Phantom.this.level.getCurrentDifficultyAt(pha.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
					serverworld.addFreshEntityWithPassengers(pha);
					if (livingentity != null)pha.setTarget(livingentity);
				}
				if(ERO_Phantom.this.level.random.nextInt(6)==1){
					ERO_Ghast gst = new ERO_Ghast(AdvanceArmy.ENTITY_GST, serverworld);
					gst.setPos((double)ERO_Phantom.this.getX(), (double)ERO_Phantom.this.getY()+50, (double)ERO_Phantom.this.getZ());
					gst.finalizeSpawn(serverworld, ERO_Phantom.this.level.getCurrentDifficultyAt(gst.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
					serverworld.addFreshEntityWithPassengers(gst);
					if (livingentity != null)gst.setTarget(livingentity);
				}
			}
			
			ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.CIRCLE;
			if (!ERO_Phantom.this.isSilent()) {
			   ERO_Phantom.this.level.levelEvent(1039, ERO_Phantom.this.blockPosition(), 0);
			}
		 } else if (ERO_Phantom.this.horizontalCollision || ERO_Phantom.this.hurtTime > 20 || ERO_Phantom.this.hurtTime > 5 && ERO_Phantom.this.getEvilPhantomSize()<3) {
			if(ERO_Phantom.this.getEvilPhantomSize()<3){
				ERO_Phantom.this.dead = true;
				ERO_Phantom.this.remove();
				ERO_Phantom.this.level.explode(ERO_Phantom.this, ERO_Phantom.this.getX(), ERO_Phantom.this.getY(), ERO_Phantom.this.getZ(), 2+ERO_Phantom.this.getEvilPhantomSize(), false, Explosion.Mode.NONE);
			}
			ERO_Phantom.this.attackPhase = ERO_Phantom.AttackPhase.CIRCLE;
		 }
	  }
	}
}