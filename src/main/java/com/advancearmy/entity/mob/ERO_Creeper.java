package advancearmy.entity.mob;

import java.util.Collection;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IChargeableMob;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
//import net.minecraft.entity.ai.goal.CreeperSwellGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
//import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.MobEntity;
import advancearmy.AdvanceArmy;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;

import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraftforge.fml.ModList;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import advancearmy.entity.ai.CreeperSwellGoalSA;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;

import wmlib.api.ITool;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.CreatureAttribute;
@OnlyIn(
   value = Dist.CLIENT,
   _interface = IChargeableMob.class
)
public class ERO_Creeper extends CreatureEntity implements IMob, IChargeableMob {
   private static final DataParameter<Integer> DATA_SWELL_DIR = EntityDataManager.defineId(ERO_Creeper.class, DataSerializers.INT);
   private static final DataParameter<Boolean> DATA_IS_POWERED = EntityDataManager.defineId(ERO_Creeper.class, DataSerializers.BOOLEAN);
   private static final DataParameter<Boolean> DATA_IS_IGNITED = EntityDataManager.defineId(ERO_Creeper.class, DataSerializers.BOOLEAN);
   private int oldSwell;
   private int swell;
   private int maxSwell = 30;
   private int explosionRadius = 3;
   private int droppedSkulls;

	public ERO_Creeper(EntityType<? extends ERO_Creeper> p_i50213_1_, World p_i50213_2_) {
	  super(p_i50213_1_, p_i50213_2_);
	  this.xpReward = 2;
	}
	public CreatureAttribute getMobType() {
	  return CreatureAttribute.UNDEAD;
	}
	public ERO_Creeper(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_CREEPER, worldIn);
	}
	
	public boolean hurt(DamageSource source, float par2)
    {
		if(source.isExplosion()){
			this.setHealth(this.getHealth()+par2*0.5F);
			return false;
		}
		return super.hurt(source, par2);
	}
   	protected float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
   protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new CreeperSwellGoalSA(this));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoalSA(this, 0.8D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, MobEntity.class, 10, 10F, false, false, (attackentity) -> {
			return this.CanAttack(attackentity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 10F, false, false, (attackentity) -> {
			return true;
		}));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
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

   public int getMaxFallDistance() {
      return this.getTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1);
   }

   public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
      boolean flag = super.causeFallDamage(p_225503_1_, p_225503_2_);
      this.swell = (int)((float)this.swell + p_225503_1_ * 1.5F);
      if (this.swell > this.maxSwell - 5) {
         this.swell = this.maxSwell - 5;
      }

      return flag;
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(DATA_SWELL_DIR, -1);
      this.entityData.define(DATA_IS_POWERED, false);
      this.entityData.define(DATA_IS_IGNITED, false);
   }

   public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
      super.addAdditionalSaveData(p_213281_1_);
      if (this.entityData.get(DATA_IS_POWERED)) {
         p_213281_1_.putBoolean("powered", true);
      }

      p_213281_1_.putShort("Fuse", (short)this.maxSwell);
      p_213281_1_.putByte("ExplosionRadius", (byte)this.explosionRadius);
      p_213281_1_.putBoolean("ignited", this.isIgnited());
   }

   public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
      super.readAdditionalSaveData(p_70037_1_);
      this.entityData.set(DATA_IS_POWERED, p_70037_1_.getBoolean("powered"));
      if (p_70037_1_.contains("Fuse", 99)) {
         this.maxSwell = p_70037_1_.getShort("Fuse");
      }

      if (p_70037_1_.contains("ExplosionRadius", 99)) {
         this.explosionRadius = p_70037_1_.getByte("ExplosionRadius");
      }

      if (p_70037_1_.getBoolean("ignited")) {
         this.ignite();
      }

   }

	public float cooltime6 = 0;
	public Vector3d motions = this.getDeltaMovement();
   public void tick() {
      if (this.isAlive()) {
         this.oldSwell = this.swell;
         /*if (this.isIgnited()) {
            this.setSwellDir(1);
         }*/

		if(this.getHealth()<this.getMaxHealth() && this.cooltime6>45){
			this.setHealth(this.getHealth()+1);
			this.cooltime6 = 0;
		}
		if(cooltime6<50)++cooltime6;
		float sp = 0.20F;//
		this.moveway(this, sp, 25, 25);//
		
         int i = this.getSwellDir();
         if (i > 0 && this.swell == 0) {
            this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
         }

         this.swell += i;
         if (this.swell < 0) {
            this.swell = 0;
         }

         if (this.swell >= this.maxSwell) {
			//if(cooltime < 200)++cooltime;
            this.explodeCreeper();
			this.swell =0/* = this.maxSwell*/;
			setSwellDir(-1);
         }
      }

      super.tick();
   }

	public float face = 0;
	public void moveway(ERO_Creeper entity, float sp, double max, double range1) {
		boolean ta = false;
		{//
			if (entity.getTarget() != null) {
				if (!entity.getTarget().isInvisible()) {//target
					if (entity.getTarget().getHealth() > 0.0F) {
						double d5 = entity.getTarget().getX() - entity.getX();
						double d7 = entity.getTarget().getZ() - entity.getZ();
						double d6 = entity.getTarget().getY() - entity.getY();
						double d1 = entity.getEyeY() - (entity.getTarget().getEyeY());
						double d3 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7);
						float f11 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
						double ddx = Math.abs(d5);
						double ddz = Math.abs(d7);
						float f12 = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
						{
							if ((ddx>2F||ddz>2F)) {//
								{
									MoveS(entity, sp, 1, entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), (LivingEntity)entity.getTarget());
								}
							}else if((ddx < 2 || ddz < 2)){//
								MoveS(entity, sp, 1, entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), (LivingEntity)entity.getTarget());
							}
						}
						entity.yRotO = entity.yRot = f12;//
						entity.setYHeadRot(f12);//
						entity.xRot = -f11 + 0;//
					}
				}
			}
		}
	}
	
	public void MoveS(ERO_Creeper entity, double speed, double han, double ex, double ey, double ez, LivingEntity en){
		if(!entity.level.isClientSide)
		{
			double d5 = ex - entity.getX();
			double d7 = ez - entity.getZ();
			float yawoffset = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
			float yaw = yawoffset * (2 * (float) Math.PI / 360);
			double mox = 0;
			double moy = -1D;
			double moz = 0;
			//entity.stepHeight = entity.height * 0.8F;
			
			if (entity.distanceToSqr(en) < 4) {//8 * 8
					mox -= MathHelper.sin(yaw) * speed * -1;
					moz += MathHelper.cos(yaw) * speed * -1;
					entity.setDeltaMovement(mox, moy, moz);
			}else{
				{
					mox -= MathHelper.sin(yaw) * speed * 0.5F;
					moz += MathHelper.cos(yaw) * speed * 0.5F;
				}
			}
			
			boolean flag = true;
			//Vector3d vector3d1 = this.getDeltaMovement().scale(0.75D);
			if(flag){
				{
					entity.getNavigation().moveTo(ex, ey, ez, 1.6);
					//entity.move(MoverType.PLAYER, entity.getDeltaMovement());
				}
			}
		}
	}
   
   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.CREEPER_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.CREEPER_DEATH;
   }

   public boolean doHurtTarget(Entity p_70652_1_) {
      return true;
   }

   public boolean isPowered() {
      return this.entityData.get(DATA_IS_POWERED);
   }

   @OnlyIn(Dist.CLIENT)
   public float getSwelling(float p_70831_1_) {
      return MathHelper.lerp(p_70831_1_, (float)this.oldSwell, (float)this.swell) / (float)(this.maxSwell - 2);
   }

   public int getSwellDir() {
      return this.entityData.get(DATA_SWELL_DIR);
   }

   public void setSwellDir(int p_70829_1_) {
      this.entityData.set(DATA_SWELL_DIR, p_70829_1_);
   }

   public void thunderHit(ServerWorld p_241841_1_, LightningBoltEntity p_241841_2_) {
      super.thunderHit(p_241841_1_, p_241841_2_);
      this.entityData.set(DATA_IS_POWERED, true);
   }

   private void explodeCreeper() {
      if (!this.level.isClientSide) {
         Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
         float f = this.isPowered() ? 2.0F : 1.0F;
		 if(this.getHealth()<=2F){
			this.dead = true;
			this.remove();
		 }
		 /*if(cooltime>20)*/{
			//cooltime = 0;
			this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)this.explosionRadius * f, /*explosion$mode*/Explosion.Mode.NONE);
			this.spawnLingeringCloud();
		 }
      }
   }

   private void spawnLingeringCloud() {
      Collection<EffectInstance> collection = this.getActiveEffects();
      if (!collection.isEmpty()) {
         AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
         areaeffectcloudentity.setRadius(2.5F);
         areaeffectcloudentity.setRadiusOnUse(-0.5F);
         areaeffectcloudentity.setWaitTime(10);
         areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
         areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());
         for(EffectInstance effectinstance : collection) {
            areaeffectcloudentity.addEffect(new EffectInstance(effectinstance));
         }
         this.level.addFreshEntity(areaeffectcloudentity);
      }

   }

   public boolean isIgnited() {
      return this.entityData.get(DATA_IS_IGNITED);
   }

   public void ignite() {
      this.entityData.set(DATA_IS_IGNITED, true);
   }

   public boolean canDropMobsSkull() {
      return this.isPowered() && this.droppedSkulls < 1;
   }

   public void increaseDroppedSkulls() {
      ++this.droppedSkulls;
   }
}
