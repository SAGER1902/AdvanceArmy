package advancearmy.entity.land;

import java.util.List;

import javax.annotation.Nullable;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
import advancearmy.event.SASoundEvent;

import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;


import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

import net.minecraft.util.text.TranslationTextComponent;

import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.SoundEvents;

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
import net.minecraft.entity.AgeableEntity;

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
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;

import net.minecraft.entity.EntityPredicate;
import java.util.function.Predicate;
import java.util.Random;

import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import wmlib.common.living.PL_LandMove;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraftforge.fml.ModList;
import net.minecraft.util.SoundCategory;
import advancearmy.entity.EntitySA_Seat;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageVehicleAnim;
import net.minecraftforge.fml.network.PacketDistributor;
import safx.SagerFX;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.VehicleSearchTargetGoalSA;
import wmlib.api.IArmy;
public class EntitySA_Sickle extends WeaponVehicleBase implements IArmy{
	public EntitySA_Sickle(EntityType<? extends EntitySA_Sickle> sodier, World worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0F;
		seatPosY[0] = 2F;
		seatPosZ[0] = 1.2F;
		seatHide[0] = true;
		this.selfheal = true;
		seatPosX[1] = 0F;
		seatPosY[1] = 4F;
		seatPosZ[1] = -0.54F;
		this.canNightV=true;
		this.armor_front = 30;
		this.armor_side = 20;
		this.armor_back = 10;
		this.armor_top = 20;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 30;
		this.armor_turret_side = 20;
		this.armor_turret_back = 20;
		this.turretSpeed = 0.8F;
		this.seatTurret[0] = true;
		this.seatTurret[1] = true;
		
		seatMaxCount = 2;
		this.renderHudIcon = true;
		this.hudIcon = "wmlib:textures/hud/aim.png";
		this.renderHudOverlay = true;
		this.hudOverlay = "wmlib:textures/misc/robot.png";
		this.renderHudOverlayZoom = true;
		this.hudOverlayZoom = "wmlib:textures/misc/robot_scope.png";
		
		this.icon1tex = new ResourceLocation("advancearmy:textures/hud/sicklehead.png");
		this.icon2tex = new ResourceLocation("advancearmy:textures/hud/sicklebody.png");
		this.weaponCount = 3;
		this.w1name = "MiniGun x 2";
		this.w2name = "Fire Shell";
		this.w3name = "Jump";
		
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		seatView3X=0F;
		seatView3Y=-1.5F;
		seatView3Z=-4F;
        this.MoveSpeed = 0.05F;
        this.turnSpeed = 4.5F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.maxUpStep = 1.5F;
		
		this.magazine = 400;
		this.reload_time1 = 100;
		this.magazine2 = 8;
		this.reload_time2 = 100;
		this.magazine3 = 1;
		this.reload_time3 = 30;
		
		this.w1icon="advancearmy:textures/hud/sicklegun.png";
		this.w2icon="advancearmy:textures/hud/hjw2.png";
		this.w3icon="advancearmy:textures/hud/jump.png";
		
		this.is_aa=true;
		this.attack_range_max = 45;
		this.attack_height_max = 90;
	}
	
	public ResourceLocation getIcon1(){
		return this.icon1tex;
	}
	public ResourceLocation getIcon2(){
		return this.icon2tex;
	}
	public void stopUnitPassenger(){
		this.stopPassenger();
	}
	public void setAttack(LivingEntity target){
		this.setTarget(target);
	}
	public void setSelect(boolean stack){
		this.setChoose(stack);
	}
	public void setMove(int id, int x, int y, int z){
		this.setMoveType(id);
		this.setMovePosX(x);
		this.setMovePosY(y);
		this.setMovePosZ(z);
	}
	public boolean getSelect(){
		return this.getChoose() && this.getTargetType()==3;
	}
	public boolean isDrive(){
		return this.getVehicle()!=null;
	}
	public boolean isCommander(LivingEntity owner){
		return this.getOwner() == owner;
	}
	public LivingEntity getArmyOwner(){
		return this.getOwner();
	}
	public int getArmyMoveT(){
		return this.getMoveType();
	}

	public int getTeamCount(){
		return getTeamC();
	}
	public void setTeamCount(int id){
		setTeamC(id);
	}
	public int getArmyMoveX(){
		return this.getMovePosX();
	}
	public int getArmyMoveY(){
		return this.getMovePosY();
	}
	public int getArmyMoveZ(){
		return this.getMovePosZ();
	}
	
    public float playerJumpPendingScale;
	
	public EntitySA_Sickle(FMLPlayMessages.SpawnEntity packet, World worldIn) {//
		super(AdvanceArmy.ENTITY_SICKLE, worldIn);
	}
	
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new VehicleLockGoal(this, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new VehicleSearchTargetGoalSA<>(this, MobEntity.class, 10, 0, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new VehicleSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 0, false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}

    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && this.getTargetType()!=1){
			boolean can = false;
				double ddy = Math.abs(entity.getY()-this.getY());
				if(ddy>15){
					can = true;
				}else{
					if(this.distanceTo(entity)<=this.attack_range_max||this.distanceTo(entity)<=this.getAttributeValue(Attributes.FOLLOW_RANGE)){
						can = true;
					}
				}
			if(can){
			if(this.getTargetType()==2){
				return !(entity instanceof IMob||entity instanceof EntityWMSeat||entity instanceof WeaponVehicleBase)||entity==this.getTarget()||entity==this.targetentity;
			}else{
				if(ModList.get().isLoaded("pvz")){
					return entity instanceof IMob||entity instanceof PVZZombieEntity;
				}else{
					return entity instanceof IMob;
				}
			}
			}else{
				return false;
			}
    	}else{
			return false;
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
	
	protected void tickDeath() {
	  ++this.deathTime;
	  if (this.deathTime == 1){
		  this.playSound(SASoundEvent.tank_explode.get(), 3.0F, 1.0F);
		  this.level.explode(this, this.getX(), this.getY(), this.getZ(), 3, false, Explosion.Mode.NONE);
	  }
	  if (this.deathTime == 120) {
		 this.remove(); //Forge keep data until we revive player
		 this.playSound(SASoundEvent.wreck_explosion.get(), 3.0F, 1.0F);
		 this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2, false, Explosion.Mode.NONE);
		 for(int i = 0; i < 20; ++i) {
			double d0 = this.random.nextGaussian() * 0.02D;
			double d1 = this.random.nextGaussian() * 0.02D;
			double d2 = this.random.nextGaussian() * 0.02D;
			this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
		 }
	  }
	}
    public void setAnimFire(int id)
    {
        if(this != null && !this.level.isClientSide)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageVehicleAnim(this.getId(), id));
        }
    }
	
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("DropRing", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);	
		List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(5D, 3.0D, 5D));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity = list.get(k2);
			if(entity!=null && entity instanceof LivingEntity && entity!=this){
				if(entity instanceof LivingEntity && this.CanAttack(entity)){
					entity.hurt(DamageSource.thrown(this, this), 6);
					this.setMovePitch(0);
					this.playSound(SASoundEvent.sickle_land.get(), 5.0F,1);
				}
				((LivingEntity)entity).knockback(0.8F, 3F, 3);
			}
		}
		return false;
	}
	public float turretX = -0.28F;
	int flytime = 0;
	float moveyaw = 0;
	public void tick() {
		super.tick();
		if(this.canAddPassenger(null)){
			EntitySA_Seat seat = new EntitySA_Seat(AdvanceArmy.ENTITY_SEAT, this.level);
			seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
			this.level.addFreshEntity(seat);
			seat.startRiding(this);
		}
		
		{
			if (!this.level.isClientSide && this.fallDistance > 3.0F) {
				this.playerJumpPendingScale = 0.0F;
				//this.setMovePitch(0);
			}
		}
		
		if (this.playerJumpPendingScale > 0.0F && this.getMovePitch()==0/* && this.onGround*/) {
		   double d0 = 2D * (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
		   Vector3d vector3d = this.getDeltaMovement();
           this.setDeltaMovement(/*(playerJumpPendingScale+1.5F)*/vector3d.x, d0+this.enc_power, /*(playerJumpPendingScale+1.5F)*/vector3d.z);
		   /*if (f1 > 0.0F) */{
			  float f2 = MathHelper.sin(this.yHeadRot * ((float)Math.PI / 180F));
			  float f3 = MathHelper.cos(this.yHeadRot * ((float)Math.PI / 180F));
			  this.setDeltaMovement(this.getDeltaMovement().add((double)(-0.8F * f2 * this.playerJumpPendingScale), 0.0D, (double)(0.8F * f3 * this.playerJumpPendingScale)));
		   }
		   this.playerJumpPendingScale = 0.0F;
		   this.setMovePitch(1);
		   this.playSound(SASoundEvent.sickle_jump.get(), 4.0F,1);
		}
		if (this.onGround){
			if(this.getMovePitch()>0){
				this.playerJumpPendingScale = 0.0F;
				this.playSound(SASoundEvent.sickle_land.get(), 5.0F,1);
				this.setMovePitch(0);
			}
		}
		
		if (this.getAnySeat(1) != null){//
			EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
			if(this.setSeat){
			seat.attack_height_max = 80;
			this.seatWeapon1(seat);
			}
			this.turretYaw_1=seat.getYHeadRot();
			if(seat.xRot<15)this.turretPitch_1=seat.xRot;
		}
		if(this.flytime<100)++this.flytime;
		boolean fire1 = false;
		boolean fire2 = false;
		boolean is_aim = false;
		float speedy = 4;
		if(this.tracktick % 25 == 0 && (this.getX() != this.xo || this.getZ() != this.zo) && this.onGround){
			if(!this.level.isClientSide)this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SASoundEvent.sickle_move.get(), SoundCategory.WEATHER, 3.0F, 1.0F);
		}
		PlayerEntity player = null;
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null){
			if(this.getTargetType()>0)this.setTargetType(0);//
			 player = (PlayerEntity)this.getFirstSeat().getAnyPassenger();
			//PL_LandMove.moveRobotMode(player, this, this.MoveSpeed, turnSpeed);
			
			float follow = player.yHeadRot;
			follow = this.clampYaw(follow);
			this.setMoveYaw(follow);
			if(player.xRot>0){
				this.xRot = this.turretPitch = player.xRot*0.8F;
			}else{
				this.xRot = this.turretPitch = player.xRot*1.2F;
			}
			if(player.yHeadRot>=0){
				this.turretYaw = player.yHeadRot*0.995F;
			}else{
				this.turretYaw = player.yHeadRot*1.005F;
			}
			if (this.getFirstSeat() != null) {
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				if(seat.fire2){
					/*if(this.getRemain3() > 0)*/{
						this.setRemain3(0);
						this.playerJumpPendingScale = 1;
					}
					seat.fire2 = false;
				}
				if(seat.fire1)
				{
					fire1=true;
					seat.fire1 = false;
				}
				if(seat.keyf){
					if(cooltime3 >10){
						if(this.getArmyType2()==0){
							this.playSound(SoundEvents.PISTON_EXTEND, 2.0F,1);
							this.setArmyType2(2);
						}else{
							this.playSound(SoundEvents.PISTON_CONTRACT, 2.0F,1);
							this.setArmyType2(0);
						}
						cooltime3=0;
					}
					seat.keyf = false;
				}
				
				float f = Math.abs(this.yHeadRot - this.getMoveYaw());
				float f2 = this.yHeadRot - this.getMoveYaw();
				f2 = this.clampYaw(f2);
				boolean is_move = false;
				if (this.getStrafingMove() < 0.0F) {
					is_move = true;
				}
				if (this.getStrafingMove() > 0.0F) {
					is_move = true;
				}
				if (this.getForwardMove() > 0.5F) {
					is_move = true;
				}
				if (this.getForwardMove() < -0.5F) {
					is_move = true;
				}
				if(is_move) {
					if(f <= turnSpeed*2 && f >= -turnSpeed*2){
						this.yHeadRot = this.getMoveYaw();
						this.yRot = this.yBodyRot = this.yHeadRot;
					}else if(f2 > 0.1F){
						this.yHeadRot -= turnSpeed;
						this.yBodyRot -= turnSpeed;
						this.yRot -= turnSpeed;
					}else if(f2 < -0.1F){
						this.yHeadRot += turnSpeed;
						this.yBodyRot += turnSpeed;
						this.yRot += turnSpeed;
					}
				}
			}
		}else{
			if(this.getTargetType()==0)this.setTargetType(1);//
			if(this.getTargetType()==1){
				if(this.getForwardMove()!=0||this.getStrafingMove()!=0){
					this.setForwardMove(0);
					this.setStrafingMove(0);
				}
			}
		}
		
		if(this.getTargetType()>0){
			if (this.getFirstSeat() != null) {
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				//if(seat.getTargetType()==0 && this.getTargetType()!=1)this.setTargetType(1);//空
				if(seat.getTargetType()==2 && this.getTargetType()!=2)this.setTargetType(2);//敌
				if(seat.getTargetType()==3 && this.getTargetType()!=3)this.setTargetType(3);//友
			}
			if(this.getTargetType()>1){
				if(this.getMoveType()==0 && this.getOwner()!=null && followTime>30){
					if(this.distanceTo(this.getOwner())>12){
						this.setMovePosX((int)this.getOwner().getX());
						this.setMovePosY((int)this.getOwner().getY());
						this.setMovePosZ((int)this.getOwner().getZ());
						followTime=0;
					}
				}
				if((this.getMovePosX()!=0||this.getMovePosZ()!=0)&&(this.getMoveType()==0||this.getMoveType()==2||!this.isAttacking()&&this.getMoveType()==4)){
					double dx = this.getMovePosX() - this.getX();
					double dz = this.getMovePosZ() - this.getZ();
					double dis = MathHelper.sqrt(dx * dx + dz * dz);
					if(dis>5){
						if(dis>20 && this.flytime > 50){
							if(this.flytime > 60)this.flytime = 0;
							this.playerJumpPendingScale = 1;
						}
						this.moveyaw = (float) MathHelper.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
						this.moveyaw = this.clampYaw(this.moveyaw);
						if(!this.isAttacking()){
							this.setMoveYaw(this.moveyaw);
						}
						float f3 = this.yHeadRot - this.moveyaw;
						f3 = this.clampYaw(f3);
						if (f3 > this.turnSpeed*1.5F) {
							this.yHeadRot -= turnSpeed;
							this.yBodyRot -= turnSpeed;
							this.yRot -= turnSpeed;
						} else if (f3 < -this.turnSpeed*1.5F) {
							this.yHeadRot += turnSpeed;
							this.yBodyRot += turnSpeed;
							this.yRot += turnSpeed;
						}else{
							//this.setStrafingMove(0);
						}
						if(f3>-this.turnSpeed*2F&&f3<this.turnSpeed*2F){
							this.yRot=this.yBodyRot=this.yHeadRot=this.moveyaw;
							this.setForwardMove(2);
						}else{
							this.setForwardMove(-2);
							is_aim=true;
						}
					}else{
						if(!this.isAttacking())this.setStrafingMove(0);
						this.setForwardMove(0);
						if(this.getMoveType()==2){
							this.setMoveType(3);
							this.setMovePosX(0);
							//this.setMovePosY(0);
							this.setMovePosZ(0);
						}
					}
				}
				if (this.getTarget() != null) {
					LivingEntity target = this.getTarget();
					if(target.isAlive() && target!=null){
						boolean crash = false;
						double dx = target.getX() - this.getX();
						double dz = target.getZ() - this.getZ();
						double ddy = Math.abs(target.getY() - this.getY());
						double dyy = this.getY()+3.29F - target.getEyeY();
						double dis = Math.sqrt(dx * dx + dz * dz);
						this.targetYaw = (float) MathHelper.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
						this.targetYaw = this.clampYaw(this.targetYaw);
						this.setMoveYaw(this.targetYaw);
						float f11 = (float) (Math.atan2(dyy, dis) * 180.0D / Math.PI);
						this.setAttacking(true);
						if(this.getMoveType() == 1||this.getTargetType()==2 && this.getMoveType()!=2&&this.getMoveType()!=4){
							if(this.find_time<40)++this.find_time;
							if(this.level.random.nextInt(6) > 3 && this.find_time > 20){
								this.find_time = 0;
								this.setAIType(this.level.random.nextInt(7));
							}else if(this.level.random.nextInt(6) < 3 && this.find_time > 20){
								this.find_time = 0;
								this.setAIType(0);
							}
							if (dis>40F) {//
								float f3 = this.yHeadRot - this.getMoveYaw();
								f3 = this.clampYaw(f3);
								if(f3>-4F&&f3<4F && this.getAIType()>3){
									if(target.getMaxHealth()<100 && !this.isthrow)this.setForwardMove(2);
								}
							}else if(dis < 6){//
								crash = true;
							}else{
								this.setForwardMove(0);
							}
							if(crash){//
								is_aim=false;
								if (this.getAIType()<3||target.getMaxHealth()>100){
									this.setForwardMove(-2);
								}else{
									this.setForwardMove(2);
								}
								if(this.flytime > 50){
									if(this.flytime > 60)this.flytime = 0;
									this.playerJumpPendingScale = 1;
								}
							}
						}
						this.turretPitch = this.xRot = f11;
						float f4 = this.turretYaw - this.getMoveYaw();
						f4 = this.clampYaw(f4);
						if(f4 < speedy*1.1F && f4 > -speedy*1.1F){
							fire1 = true;
							fire2 = true;
						}
						if(this.getMoveType()==1){
							float f3 = this.yHeadRot - this.getMoveYaw();
							f3 = this.clampYaw(f3);
							if (f3 > this.turnSpeed*1.5F) {
								this.yHeadRot -= turnSpeed;
								this.yBodyRot -= turnSpeed;
								this.yRot -= turnSpeed;
							} else if (f3 < -this.turnSpeed*1.5F) {
								this.yHeadRot += turnSpeed;
								this.yBodyRot += turnSpeed;
								this.yRot += turnSpeed;
							}else{
								//this.setStrafingMove(0);
							}
							if(f3>-this.turnSpeed*2F&&f3<this.turnSpeed*2F){
								this.yRot=this.yBodyRot=this.yHeadRot=this.getMoveYaw();
								//this.setForwardMove(0);
							}else{
								this.setForwardMove(-2);
								is_aim=true;
							}
						}
					}
				}else{
					if(!this.isAttacking()){
						if(this.getMoveType()!=2&&this.getMoveType()!=4){
							this.setForwardMove(0);
							this.setStrafingMove(0);
							this.xRot = 0;
						}
					}
				}
				if (this.getForwardMove()>0.1F)this.setForwardMove(this.getForwardMove()-0.05F);
				if (this.getForwardMove()<-0.1F)this.setForwardMove(this.getForwardMove()+0.05F);
			}
		}
		
		if(fire1){

			if(this.getArmyType2()==0){
				if(cooltime>2){
					this.counter1 = true;
					this.cooltime = 0;
				}
				if(this.counter1 && this.getRemain1() > 0){
					this.setAnimFire(1);
					this.AIWeapon1(1.25F, 2.83F, 2.29F,0,0);
					this.AIWeapon1(-1.25F, 2.83F, 2.29F,0,0);
					this.playSound(SASoundEvent.sickle_fire.get(), 5.0F, 1.0F);
					this.setRemain1(this.getRemain1() - 2);
					this.counter1 = false;
				}
			}else if(this.getArmyType2()==2){
				if(cooltime2>25){
					this.counter2 = true;
					this.cooltime2 = 0;
				}
				if(this.counter2 && this.getRemain2() > 0){
					this.setAnimFire(2);
					this.AIWeapon2(1.25F, 3.3F, 2.29F,0,0);
					this.AIWeapon2(-1.25F, 3.3F, 2.29F,0,0);
					this.playSound(SASoundEvent.powercannon.get(), 5.0F, 1.0F);
					this.setRemain2(this.getRemain2() - 2);
					this.counter2 = false;
				}
			}
		}
		if(fire2){
			if(cooltime2>25){
				this.counter2 = true;
				this.cooltime2 = 0;
			}
			if(this.counter2 && this.getRemain2() > 0){
				this.setAnimFire(2);
				this.AIWeapon2(1.25F, 3.3F, 2.29F,0,0);
				this.AIWeapon2(-1.25F, 3.3F, 2.29F,0,0);
				this.playSound(SASoundEvent.powercannon.get(), 5.0F, 1.0F);
				this.setRemain2(this.getRemain2() - 2);
				this.counter2 = false;
			}
		}
		
		if(this.getChange()>0||this.getTargetType()>1){
			float f4 = this.turretYaw - this.getMoveYaw();
			f4 = this.clampYaw(f4);
			if (f4 > speedy) {
				this.turretYaw-=speedy;
			} else if (f4 < -speedy) {
				this.turretYaw+=speedy;
			}else{
				this.turretYaw = this.getMoveYaw();
			}
		}
		float f1 = this.yHeadRot * (2 * (float) Math.PI / 360);
		double x = 0;
		double y = this.getDeltaMovement().y();
		double z = 0;
		if (this.getStrafingMove() < -0.5F) {
			x += MathHelper.sin(this.yHeadRot * 0.01745329252F - 1.57F) * MoveSpeed * 5;
			z -= MathHelper.cos(this.yHeadRot * 0.01745329252F - 1.57F) * MoveSpeed * 5;
		}
		if (this.getStrafingMove() > 0.5F) {
			x += MathHelper.sin(this.yHeadRot * 0.01745329252F + 1.57F) * MoveSpeed * 5;
			z -= MathHelper.cos(this.yHeadRot * 0.01745329252F + 1.57F) * MoveSpeed * 5;
		}
		if (this.getForwardMove() > 0.5F) {
			x -= MathHelper.sin(f1) * MoveSpeed * 5;
			z += MathHelper.cos(f1) * MoveSpeed * 5;
		}
		if (this.getForwardMove() < -0.5F) {
			x -= MathHelper.sin(f1) * MoveSpeed * -5;
			z += MathHelper.cos(f1) * MoveSpeed * -5;
		}
		{
			float aim_sp = 1;
			if(is_aim)aim_sp=0.5F;
			float enc_sp = 1+this.enc_power*0.1F;
			this.setDeltaMovement(x*enc_sp*aim_sp, y, z*enc_sp*aim_sp);
		}
	}

	public void seatWeapon1(EntitySA_Seat seat){
		seat.attack_range_max = 35;
		seat.hudfollow = true;
		seat.seatHide = true;
		seat.weaponCount = 1;
		seat.ammo1 = 3;
		seat.magazine = 200;
		seat.reload_time1 = 80;
		seat.reloadSound1 = SASoundEvent.reload_mag.get();
		String model = "advancearmy:textures/entity/bullet/bullet20mm.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet5.png";
		String fx1 = "SmokeGun";
		String fx2 = null;
		if(seat.getRemain1()%2==0)turretX=-turretX;
		seat.setWeapon(0, 3, model, tex, fx1, fx2, SASoundEvent.gun4.get(), turretX,0F,2F,0,0.2F,
		14, 6F, 1.25F, 0, false, 1, 0.01F, 20, 0);
	}
	
	public void AIWeapon2(double w, double h, double z, double bx, double bz){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = MathHelper.sqrt((z - bz)* (z - bz) + (w - bx)*(w - bx)) * MathHelper.sin(-this.xRot  * (1 * (float) Math.PI / 180));
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F + 1) * w;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F + 1) * w;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger()!=null)shooter = ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger();
		EntityShell bullet = new EntityShell(this.level, shooter);
		bullet.power = 60;
		bullet.setExLevel(2);
		bullet.setModel("wmlib:textures/entity/flare.obj");
		bullet.setTex("wmlib:textures/entity/flare.png");
		bullet.setGravity(0.01F);
		bullet.setBulletType(9);
		bullet.moveTo(this.getX() + xx11, this.getY()+h+base, this.getZ() + zz11, this.yHeadRot, this.xRot);
		bullet.shootFromRotation(this, this.turretPitch, this.turretYaw, 0.0F, 4F, 2);
		if (!this.level.isClientSide) this.level.addFreshEntity(bullet);
	}
	public void AIWeapon1(double w, double h, double z, double bx, double bz){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = MathHelper.sqrt((z - bz)* (z - bz) + (w - bx)*(w - bx)) * MathHelper.sin(-this.xRot  * (1 * (float) Math.PI / 180));
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F) * z;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F + 1) * w;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F + 1) * w;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger()!=null)shooter = ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger();
		EntityBullet bullet = new EntityBullet(this.level, shooter);
		bullet.power = 8;
		bullet.setModel("advancearmy:textures/entity/bullet/bullet3.obj");
		bullet.setTex("advancearmy:textures/entity/bullet/bullet3.png");
		bullet.setGravity(0.01F);
		bullet.moveTo(this.getX() + xx11, this.getY()+h+base, this.getZ() + zz11, this.yHeadRot, this.xRot);
		bullet.shootFromRotation(this, this.turretPitch, this.turretYaw, 0.0F, 6F, 2);
		if (!this.level.isClientSide) this.level.addFreshEntity(bullet);
	}
}