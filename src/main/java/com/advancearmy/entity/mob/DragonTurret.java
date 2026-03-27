package advancearmy.entity.mob;

import java.util.List;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;
import advancearmy.event.SASoundEvent;

import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import net.minecraft.util.text.TranslationTextComponent;

import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.ModList;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.block.Blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.block.material.Material;

import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;

import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.Entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;

import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ActionResultType;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import safx.SagerFX;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.EntityPredicate;
import java.util.function.Predicate;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import net.minecraft.world.Explosion;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

import net.minecraft.entity.CreatureEntity;
public class DragonTurret extends CreatureEntity implements IMob{
	public DragonTurret(EntityType<? extends DragonTurret> sodier, World worldIn) {
		super(sodier, worldIn);
		this.fire_tick = 1;
		this.xpReward = 10;
	}

	public DragonTurret(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_DT, worldIn);
	}
	
	public float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 1.8F;
	}

	public float getScale() {
		return this.isBaby() ? 0.5F : 1.0F;
	}
	
	public double getMountedYOffset() {
		return 0.6D;//0.12D
	}
	public void checkDespawn() {
		
	}
	protected void registerGoals() {
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, MobEntity.class, 10, 80F, true, false, (attackentity) -> {
		return !(attackentity instanceof EnderDragonEntity)&&!(attackentity instanceof IAngerable)&&!(attackentity instanceof DragonTurret);
		// attackentity instanceof IMob&&!(attackentity instanceof EnderDragonEntity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 80F, false, false, (attackentity) -> {
			return true;
		}));
	}

    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F){
			if(!(entity instanceof EnderDragonEntity)&&!(entity instanceof IAngerable)&&!(entity instanceof DragonTurret)){
				return true;
			}else{
				return false;
			}
			/*}else if(entity instanceof PlayerEntity && ((LivingEntity) entity).getHealth() > 0.0F){//
				PlayerEntity entityplayer = (PlayerEntity) entity;
				if(entityplayer.capabilities.isClientSidereativeMode){
					return false;
				}else{
					return true;
				}*/
    	}else{
			return false;
		}
    }
	
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof LivingEntity){
				LivingEntity entity1 = (LivingEntity) entity;
				if(this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
					return false;
				}else{
					if(this.distanceToSqr(entity)>4D){
						if(this.cooltime6>40 && this.random.nextInt(3) == 0){
							this.setTarget(entity1);
							this.cooltime6 = 0;
						}
					}
					if(cooltime6<30)par2 = par2*0.4F;
					return super.hurt(source, par2);
				}
			}else{
				if(cooltime6<30)par2 = par2*0.4F;
				return super.hurt(source, par2);
			}
		}else {
			if(cooltime6<30)par2 = par2*0.4F;
			return super.hurt(source, par2);
		}
    }
	
    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.IRON_GOLEM_HURT;
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.IRON_GOLEM_DEATH;
    }	

	protected LivingEntity dragon;
	public boolean cannon_fire = false;//
	public float turretYaw;
	public float turretPitch;
	public void AIWeapon1(double w, double h, double z){
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= MathHelper.sin(this.yRot * 0.01745329252F) * z;
		zz11 += MathHelper.cos(this.yRot * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(this.yRot * 0.01745329252F + 1) * w;
		zz11 += MathHelper.cos(this.yRot * 0.01745329252F + 1) * w;
		EntityShell bullet = new EntityShell(this.level, this);
		bullet.power = 45;
		bullet.setGravity(0.026F);
		//bullet.setBulletType(3);
		bullet.setExLevel(3);
		//bullet.flame = true;
		bullet.moveTo(this.getX() + xx11, this.getY()+h, this.getZ() + zz11, this.yRot, this.xRot);
		bullet.shootFromRotation(this, this.xRot, this.yRot, 0.0F, 3F, 1);
		if (!this.level.isClientSide) this.level.addFreshEntity(bullet);
		bullet.setFX("DragonTrail");
	}
	public void AIWeapon2(double w, double h, double z){
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F + 1) * w;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F + 1) * w;
		EntityBullet bullet2 = new EntityBullet(this.level, this);
		bullet2.power = 4;
		bullet2.setGravity(0.025F);
		bullet2.setBulletType(3);
		bullet2.setExLevel(0);
		bullet2.moveTo(this.getX() + xx11, this.getY()+h, this.getZ() + zz11, this.yRot, this.xRot);
		bullet2.shootFromRotation(this, this.xRot, this.turretYaw, 0.0F, 4F, 2);
		if (!this.level.isClientSide) this.level.addFreshEntity(bullet2);
	}
	public void AIWeapon3(double w, double h, double z){
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F + 1) * w;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F + 1) * w;
		EntityMissile bullet = new EntityMissile(this.level, this, this.getTarget(), this.getVehicle());
		bullet.setModel("advancearmy:textures/entity/bullet/bulletrocket.obj");
		bullet.setTex("advancearmy:textures/entity/bullet/bulletrocket.png");
		bullet.power = 40;
		bullet.setGravity(0.03F);
		bullet.setExLevel(1);
		//bullet.flame = true;
		bullet.setBulletType(2);
		bullet.moveTo(this.getX() + xx11, this.getY()+h, this.getZ() + zz11, this.yRot, this.xRot);
		bullet.shootFromRotation(this, this.xRot, this.turretYaw, 0.0F, 5F, 1);
		if (!this.level.isClientSide) this.level.addFreshEntity(bullet);
		bullet.setFX("SAMissileTrail");
	}
	protected void tickDeath() {
	  ++this.deathTime;
	  if (this.deathTime == 1){
		  this.playSound(SASoundEvent.tank_explode.get(), 3.0F+this.getBbWidth()*0.1F, 1.0F);
		  this.level.explode(this, this.getX(), this.getY(), this.getZ(), 3+this.getBbWidth()*0.1F, false, Explosion.Mode.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("VehicleExp1", null, this.getX(), this.getY(), this.getZ(), 0,0,0,1+this.getBbWidth()*0.1F);
	  }
	  if (this.deathTime >= 120) {
		 this.remove(); //Forge keep data until we revive player
		 this.playSound(SASoundEvent.wreck_explosion.get(), 2.0F+this.getBbWidth()*0.1F, 1.0F);
		 this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2+this.getBbWidth()*0.1F, false, Explosion.Mode.NONE);
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("VehicleExp2", null, this.getX(), this.getY(), this.getZ(), 0,0,0,1+this.getBbWidth()*0.1F);
	  }
	}
	public boolean counter1 = false;
	public int cooltime;
	public int cooltime2;
	public int fire_count = 0;
	public float cooltime3 = 0;
	public float cooltime5 = 0;
	public float cooltime6 = 0;
	public Vector3d motions = this.getDeltaMovement();
	public static int fire_tick = 4;
	public static int fire_tick2 = 0;
	public int fire_tick3 = 0;
	public int find_time = 0;	 
	public static int gun_count1 = 0;
	public float turretYawMove = 0;
	public void tick() {
		super.tick();
		if(cooltime < 200)++cooltime;
		if(gun_count1 < 200)++gun_count1;
		if(this.getHealth()>0 && this.getHealth()<this.getMaxHealth() && this.cooltime6>45){
			this.setHealth(this.getHealth()+1);
			this.cooltime6 = 0;
		}
		
		if(cooltime2<50)++cooltime2;
		if(cooltime3<50)++cooltime3;
		if(cooltime5<50)++cooltime5;
		if(cooltime6<50)++cooltime6;
		if(this.fire_count>20)this.fire_count = 0;
		float MoveSpeed = 0.20F;//

		if(this.cooltime % 4 == 0 && this.getVehicle()==null&&this.dragon==null){//
			this.dragon = this.level.getNearestLoadedEntity(MobEntity.class, (new EntityPredicate()).range(18D).selector((attackentity) -> {return attackentity instanceof EnderDragonEntity;}), 
			this, this.getX(), this.getEyeY(), this.getZ(), this.getBoundingBox().inflate(18D, 18.0D, 18D));
			if(this.getVehicle()==null&&this.dragon!=null && this.dragon.getPassengers().size()==0){
				if (!this.level.isClientSide)this.startRiding(this.dragon);
				this.playSound(SoundEvents.ENDERMAN_TELEPORT, 5.0F, 1.0F);
				if (this.level.isClientSide) {
					for(int i = 0; i < 5; ++i) {
						this.level.addParticle(ParticleTypes.DRAGON_BREATH, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
					}
				}
			}
		}
		
		float h= 0;
		
		if(this.getVehicle()!=null){
			this.yRot=this.getVehicle().yRot;
			if(this.getTarget()==null)this.setYHeadRot(this.yRot);
			h= -3.2F;
		}
		
		this.moveway(this);//
		boolean fire_flag =true;
		float f3 = (float) (this.getYHeadRot() - this.turretYaw);// -180 ~ 0 ~ 180
		if(f3>0){// +1
			if(f3>180F){
				--this.turretYawMove;
				--this.turretYawMove;
				--this.turretYawMove;
			}else{
				++this.turretYawMove;
				++this.turretYawMove;
				++this.turretYawMove;
			}
		}else if(f3<0){// -1
			if(f3<-180F){
				++this.turretYawMove;
				++this.turretYawMove;
				++this.turretYawMove;
			}else{
				--this.turretYawMove;
				--this.turretYawMove;
				--this.turretYawMove;
			}
		}
		if(f3>-6F&&f3<6F||this.turretYaw>179F||this.turretYaw<-179F){
			this.turretYawMove = this.getYHeadRot();
			fire_flag = true;
		}else{
			fire_flag = false;
		}
		this.turretYaw = this.turretYawMove;//yaw
		this.turretPitch = this.xRot;

		if(this.fire_tick2<50)++this.fire_tick2;
		if(this.getTarget()!=null){
			if(this.cooltime > this.fire_tick && fire_flag && this.xRot<20)
			{
				this.counter1 = true;
				this.cooltime = 0;
				this.fire_tick2 = 0;
				if(this.cooltime2>4)this.cooltime2=0;
			}
			if(this.cooltime2==0)this.playSound(SASoundEvent.fire_minigun.get(), 4.0F, 1.0F);
			if(this.counter1){
				this.AIWeapon2(0F,2.64F+h,2);
				this.counter1 = false;
			}
			float w= -2F;
			if(this.fire_count%2==0){
				w = 2F;
			}else{
				w= -2F;
			}
			if(this.cooltime3>8 && this.cannon_fire){
				this.AIWeapon1(w,1F+h,4);
				this.cooltime3=0;
				++this.fire_count;
				this.playSound(SASoundEvent.powercannon.get(), 4.0F, 1.0F);
			}else
			if(this.cooltime5>30){
				this.AIWeapon3(w,2.7F+h,2);
				this.cooltime5=0;
				++this.fire_count;
				this.playSound(SASoundEvent.fire_missile.get(), 4.0F, 1.0F);
			}
		}
	}

	public void moveway(DragonTurret entity) {
		boolean ta = false;
		{//
			if (entity.getTarget() != null) {
				boolean flag = entity.getSensing().canSee(entity.getTarget());
				if (!entity.getTarget().isInvisible()) {//target
					if (entity.getTarget().getHealth() > 0.0F && flag) {
						double d5 = entity.getTarget().getX() - entity.getX();
						double d7 = entity.getTarget().getZ() - entity.getZ();
						double d6 = entity.getTarget().getY() - entity.getY();
						double d1 = entity.getY()+3.7F - (entity.getTarget().getEyeY());
						double d3 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7);
						float f11 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
						double ddx = Math.abs(d5);
						double ddz = Math.abs(d7);
						float f12 = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
						//entity.yRotO = entity.yRot = f12;//
						float f3 = (float) (f12-entity.yRot);
						if(f3>-6F&&f3<6F){
							entity.cannon_fire = true;
						}else{
							entity.cannon_fire = false;
						}
						entity.setYHeadRot(f12);//
						entity.xRot = -f11 + 0;//
					}
				}
			}
		}
	}
}