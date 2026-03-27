package advancearmy.entity;

import java.util.List;

import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
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
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.util.ActionResultType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.ModList;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;

import advancearmy.AdvanceArmy;

import advancearmy.event.SASoundEvent;
import wmlib.common.living.PL_LandMove;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.VehicleSearchTargetGoalSA;

import net.minecraft.util.SoundCategory;
import wmlib.common.living.EntityWMSeat;

import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageVehicleAnim;
import net.minecraftforge.fml.network.PacketDistributor;
import wmlib.client.obj.SAObjModel;
import net.minecraft.util.ResourceLocation;

import wmlib.common.living.PL_AirCraftMove;
import wmlib.common.living.AI_AirCraftSet;

import wmlib.common.bullet.EntityFlare;
import wmlib.WarMachineLib;
import net.minecraft.world.gen.Heightmap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import wmlib.api.IArmy;
import wmlib.api.ITool;
import advancearmy.AAConfig;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.block.BlockState;
public abstract class EntitySA_HeliBase extends WeaponVehicleBase implements IArmy{
	public EntitySA_HeliBase(EntityType<? extends EntitySA_HeliBase> sodier, World worldIn) {
		super(sodier, worldIn);
		this.lockAlertSound=SASoundEvent.laser_lock.get();
		this.destroySoundStart=SASoundEvent.helicopter_explosion.get();
		this.destroySoundEnd=SASoundEvent.wreck_explosion.get();
		this.armor_front = 7;
		this.armor_side = 6;
		this.armor_back = 6;
		this.armor_top = 6;
		this.armor_bottom = 5;
		this.seatProtect = 0.1F;
		this.attack_height_max=100;
	}
	public int getUnitType(){
		return 1;
	}
	public void setAttack(LivingEntity target){
		this.setTarget(target);
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
		return this.getVehicle()!=null || this.getTargetType()<2;
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
	
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new VehicleLockGoal(this, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new VehicleSearchTargetGoalSA<>(this, MobEntity.class, 10, 70F, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new VehicleSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 70F, false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}
	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return EntitySA_SoldierBase.createMonsterAttributes();
	}

    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && this.getTargetType()!=1){
			if(this.getTargetType()==2){
				return !(entity instanceof IMob||entity instanceof ITool)||entity==this.getTarget()||entity==this.targetentity;
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

	public float w1recoilp = 1;
	public float w1recoilr = 1;

	public float w1barrelsize = 1F;

	public float fireposX1 = 0;
	public float fireposY1 = 0;
	public float fireposZ1 = 0;
	public float fireposX2 = 0;
	public float fireposY2 = 0;
	public float fireposZ2 = 0;
	public float firebaseX = 0;
	public float firebaseZ = 0;
	public int rotorcount = 0;
	
	public float[] rotorx = new float[5];
	public float[] rotory = new float[5];
	public float[] rotorz = new float[5];
	public float[] rotor_rotex = new float[5];
	public float[] rotor_rotey = new float[5];
	public float[] rotor_rotez = new float[5];
	
	public boolean change_roter = true;
	
	public float mgx = 0;
	public float mgy = 0;
	public float mgz = 0;
	public float mgbz = 0;
	
	public float turretYawO1;
	public float turretPitchO1;
	public SoundEvent firesound1;
	public SoundEvent firesound2;
	public SoundEvent startsound;
	public SoundEvent movesound = SASoundEvent.heli_move.get();
	public boolean ammo = false;
	public static int count = 0;
	public int healtime = 0;
	public ResourceLocation drivetex = null;
	public SAObjModel mgobj = null;
	public ResourceLocation enemytex = null;
	public ResourceLocation mgtex = null;
	public ResourceLocation rotortex1 = null;
	public ResourceLocation rotortex2 = null;
	public void setRotor(int id, float x,float y,float z){
		this.rotorx[id] = x;
		this.rotory[id] = y;
		this.rotorz[id] = z;
	}
	public void setMg(float x,float y,float z, float bz){
		this.mgx = x;
		this.mgy = y;
		this.mgz = z;
		this.mgbz= bz;
	}
	
    public void setAnimFire(int id)
    {
        if(this != null && !this.level.isClientSide)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageVehicleAnim(this.getId(), id));
        }
    }
	
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
	  return false;
	}
	public boolean canDespawn(double distanceToClosestPlayer) {
		return true;
	}
	
	protected void tickDeath() {
	  ++this.deathTime;
	  this.setDeltaMovement(this.getDeltaMovement().x*0.1F, -1F, this.getDeltaMovement().z*0.1F);
	  if (this.deathTime == 1){
		  this.playSound(SASoundEvent.helicopter_explosion.get(), 3.0F, 1.0F);
		  this.level.explode(this, this.getX(), this.getY(), this.getZ(), 3, false, Explosion.Mode.NONE);
	  }
	  if (this.deathTime == 120||this.isOnGround()) {
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
	public int max_moveangle = 60;
	public boolean w1aa = false;
	public int w1max = 100;
	public int w1min = 0;
	public int w1aim = 10;
	public boolean w2aa = true;
	public int w2max = 100;
	public int w2min = 10;
	public int w2aim = 20;
	public boolean w4aa = false;
	public int w4max = 50;
	public int w4min = 0;
	public int w4aim = 5;
	public int stayrange = 40;
	public int fly_height = 55;
	public int min_height = 15;

	public int control_tick = 0;
	public int cracktime = 0;
	public int random_height = 1;
	boolean aim_true = false;
	int flarecd = 0;
	int flyuptime = 0;
	
	boolean flypower = true;
	int drop = 0;
	public boolean autoPitchLock = true;
	float moveyaw = 0;
	float moveptich = 0;
	public void tick() {
		super.tick();
		
		if(this.isOnGround() && this.getTargetType()!=1 && this.getHealth()>0){
			if(flyPitch<0)flyPitch+=0.5F;
			if(flyPitch>0)flyPitch-=0.5F;
			if(flyRoll<0)flyRoll+=0.5F;
			if(flyRoll>0)flyRoll-=0.5F;
		}
		
		if(this.getHealth()>0){
			if(this.canAddPassenger(null)){
				if (!this.level.isClientSide){
					EntitySA_Seat seat = new EntitySA_Seat(AdvanceArmy.ENTITY_SEAT, this.level);
					seat.moveTo(this.getX(), this.getY()+1, this.getZ(), 0, 0);
					this.level.addFreshEntity(seat);
					seat.startRiding(this);
				}
			}
			while(this.turretYaw_1 - this.turretYawO1 < -180.0F) {
				this.turretYawO1 -= 360.0F;
			}
			while(this.turretPitch_1 - this.turretPitchO1 >= 180.0F) {
				this.turretPitchO1 += 360.0F;
			}
			this.turretYawO1 = this.turretYaw_1;
			this.turretPitchO1 = this.turretPitch_1;
			if(this.startTime==1 && this.startsound!=null)this.playSound(this.startsound, 5.0F, 1.0F);
			if(this.getMovePosX()==0&&this.getMovePosZ()==0&&this.getTargetType()!=1){
				this.setMovePosX((int)this.getX());
				this.setMovePosY((int)this.getY());
				this.setMovePosZ((int)this.getZ());
			}
			float f1 = this.yHeadRot * (2 * (float) Math.PI / 360);//
			this.block_height = this.level.getHeight(Heightmap.Type.WORLD_SURFACE, (int)this.getX(), (int)this.getZ());
			
			if(flypower&&(this.getY()-this.block_height)>10&&this.movePower<10){
				this.movePower=10;
				this.throttle=10;
				this.drop = 200;
				flypower=false;
			}
			
			AI_AirCraftSet.setHeliCopterMode(this, this.movesound, f1, this.MoveSpeed, 0.02F);
			boolean fire1 = false;
			boolean fire2 = false;
			boolean fire4 = false;
			float speedy = this.turnSpeed+this.MoveSpeed*5;
			if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null){
				if(cracktime<10)++cracktime;
				if(this.isOnGround() && this.flyPitch>5 && this.throttle>5){
					if(cracktime>8){
						cracktime = 0;
						this.hurt(DamageSource.explosion((LivingEntity)null), this.flyPitch*this.throttle);
					}
				}
				if(this.getTargetType()>0)this.setTargetType(0);//
				PlayerEntity player = (PlayerEntity)this.getFirstSeat().getAnyPassenger();
				PL_AirCraftMove.moveHeliCopterMode(player, this, this.MoveSpeed, this.turnSpeed);
				if (this.getFirstSeat() != null) {
					EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
					if(seat.keylook){
						if(cooltime5 >10){
							if(this.getArmyType1()==0){
								this.playSound(SoundEvents.ARMOR_EQUIP_CHAIN, 2.0F,1);
								this.setArmyType1(1);
							}else{
								this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 2.0F,1);
								this.setArmyType1(0);
							}
							cooltime5=0;
						}
						seat.keylook = false;
					}
					if(seat.keyv){
						if(cooltime3>150)cooltime3=0;
						if(this.getRemain4() > 0){
							if(this.getHealth() < this.getMaxHealth() && this.getHealth() > 0.0F) {
								++healtime;
								if(healtime > 2){
									this.setHealth(this.getHealth() + 1);
									this.playSound(SASoundEvent.fix.get(), 1.0F, 1.0F);
									healtime=0;
								}
							}
						}
						if(cooltime3>80){
							this.setRemain4(0);
							seat.keyv = false;
						}
					}
					if(seat.keyg){
						if(cooltime4 >10){
							if(this.getMoveMode()==0){
								this.playSound(SoundEvents.STONE_BUTTON_CLICK_ON, 2.0F,1);
								this.setMoveMode(1);
							}else{
								this.playSound(SoundEvents.STONE_BUTTON_CLICK_OFF, 2.0F,1);
								this.setMoveMode(0);
							}
							cooltime4=0;
						}
						seat.keyg = false;
					}
					if(seat.keyx){
						if(flaretime>90)flaretime=0;
						seat.keyx = false;
					}
					if(seat.fire1){
						fire1 = true;
						seat.fire1 = false;
					}
					if(seat.fire2){
						fire2 = true;
						seat.fire2 = false;
					}
				}
				if(!this.isOnGround()){
					if(this.getArmyType1()==0){
						this.flyPitch += this.getMovePitch() * (this.turnSpeed) * ((180F-Math.abs(this.flyRoll)*2)/180);
						this.flyRoll -= this.getMoveYaw()*(this.turnSpeed);
						this.turretPitch = this.xRot = this.flyPitch;
						this.turretYaw = this.yHeadRot;
					}else{
						if(this.enc_control>0||this.can_follow){
							this.turretYaw=player.yHeadRot;
							this.turretPitch=player.xRot;
							this.moveyaw = this.yRot = this.yHeadRot;
							float f4 = this.flyPitch - player.xRot;
							if (f4 > speedy) {
								this.flyPitch-=speedy;
							} else if (f4 < -speedy) {
								this.flyPitch+=speedy;
							}else{
								this.flyPitch = player.xRot;
							}
							this.moveptich = this.xRot = this.flyPitch;
							
							float follow = player.yHeadRot;
							follow = this.clampYaw(follow);
							float f3 = this.moveyaw - follow;
							f3 = this.clampYaw(f3);
							if (f3 > this.turnSpeed) {
								this.setStrafingMove(1);
							} else if (f3 < -this.turnSpeed) {
								this.setStrafingMove(-1);
							}
							if(f3 < this.turnSpeed*1.5F && f3 > -this.turnSpeed*1.5F){
								//this.moveyaw = this.targetYaw;
								this.setStrafingMove(0);
							}
						}
					}
				}
			}else{
				if(this.getTargetType()==0)this.setTargetType(1);//
			}
			
			if(flarecd<20)++flarecd;
			if(flaretime<100)++flaretime;
			if(flarecd >2){
				flarecd = 0;
				if(this.getRemain3() > 0){
					if(!this.isSpaceShip){
						if(flaretime<40){
							float angle = 90;
							float firex = 2F;
							if(this.getRemain3()%2==0){
								angle = 75;
								firex = 2F;
							}else{
								angle = -75;
								firex = -2F;
							}
							double xx11 = 0;
							double zz11 = 0;
							xx11 -= MathHelper.sin(this.yRot * 0.01745329252F) * -2;
							zz11 += MathHelper.cos(this.yRot * 0.01745329252F) * -2;
							xx11 -= MathHelper.sin(this.yRot * 0.01745329252F + 1) * firex;
							zz11 += MathHelper.cos(this.yRot * 0.01745329252F + 1) * firex;
							EntityFlare bullet = new EntityFlare(WarMachineLib.ENTITY_FLARE, this.level);
							bullet.moveTo(this.getX() + xx11, this.getY(), this.getZ() + zz11, this.yRot, this.xRot);
							bullet.shootFromRotation(this, -25, this.yRot+angle, 0, 1F, 1);
							if (!this.level.isClientSide) this.level.addFreshEntity(bullet);
							this.playSound(SoundEvents.LAVA_EXTINGUISH, 2.0F, 1.0F);
							this.setRemain3(this.getRemain3() - 1);
						}
					}else{
						if(flaretime<80){
							this.startShield=true;
							this.setRemain3(this.getRemain3() - 1);
						}else{
							this.startShield=false;
						}
					}
				}else{
					this.startShield=false;
				}
			}
			
			if(this.control_tick<20)++this.control_tick;
			if(this.getTargetType()>0){
				if(cracktime<10)++cracktime;
				if(this.isOnGround() && this.flyPitch>5 && this.throttle>5){
					if(cracktime>8){
						cracktime = 0;
						this.hurt(DamageSource.explosion((LivingEntity)null), 5);
					}
				}
				
				if(flyuptime<100)++flyuptime;
				if (this.getFirstSeat() != null) {
					EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
					if(seat.getTargetType()==0&&this.enc_soul==0){
						if(this.getTargetType()!=1)this.setTargetType(1);
					}
					if(seat.getTargetType()==2){
						if(this.getTargetType()!=2)this.setTargetType(2);
					}
					if(seat.getTargetType()==3||this.enc_soul>0)if(this.getTargetType()!=3){
						this.setTargetType(3);
					}
				}
				if(this.getTargetType()>1){
					if(!this.can_follow){
						this.xRot = this.flyPitch;
						if(!autoPitchLock)this.turretPitch = this.xRot;
						this.turretYaw = this.yRot = this.yHeadRot;
					}else{
						this.xRot = this.flyPitch;
						this.yRot = this.yHeadRot;
					}
					int heightY = block_height + this.random_height+this.fly_height;
					int heightMin = block_height +this.min_height;
					if(this.control_tick>2){
						if(this.getStrafingMove()!=0)this.control_tick = 0;
						float angle = 0;
						if(this.hurtTime>0){
							if(this.flyPitch<-30)this.setMoveYaw(2);
							if(flaretime>60 && this.getHealth()<this.getMaxHealth()*0.7)flaretime=0;
						}
						if(this.getY()<heightY - this.fly_height*0.3F){
							this.setForwardMove(2);
							angle = -3;
							float xxx = (angle-this.flyPitch)*0.02F;
							this.setMovePitch(xxx);
						}else{
							this.setForwardMove(0);
						}
						
						if(this.throttle<this.throttleMax*0.3F){
							this.setForwardMove(1);
						}
						
						if(this.getAIType()==0||!this.isAttacking() && this.getAIType()!=3){
							if(this.flyRoll<-2)this.setMoveYaw(-0.1F);
							if(this.flyRoll>2)this.setMoveYaw(0.1F);
							if(this.flyPitch<-85)this.setMovePitch(0.1F);
							if(this.flyPitch>85)this.setMovePitch(-0.1F);
							this.setStrafingMove(0);
							if(this.getY()>heightY + this.fly_height*0.3F){
								angle = 15;
								float xxx = (angle-this.flyPitch)*0.02F;
								this.setMovePitch(xxx);
							}
						}
						if(this.isAttacking()&&(this.getAIType()==3||this.getAIType()==0) && this.getY()>heightMin)this.setAIType(4);
						if(this.getAIType()==1 && this.getY()>heightMin){
							this.setStrafingMove(0);
							angle = this.max_moveangle;
							if(this.getMoveType()==3&&!this.isAttacking())angle=10;
							float xxx = (angle-this.flyPitch)*0.03F;
							this.setMovePitch(xxx);
						}
						if(this.getAIType()==2){
							this.setStrafingMove(0);
							angle = -25;
							float xxx = (angle-this.flyPitch)*0.02F;
							this.setMovePitch(xxx);
						}
						/*if(this.getAIType()==5){
							this.setMovePitch(-1);
						}
						if(this.getAIType()==6){
							this.setStrafingMove(1);
						}
						if(this.getAIType()==7){
							this.setStrafingMove(-1);
						}*/
						
						if(this.getMoveType()==0 && this.getOwner()!=null && followTime>30){
							if(this.distanceTo(this.getOwner())>12){
								this.setMovePosX((int)this.getOwner().getX());
								this.setMovePosY((int)this.getOwner().getY());
								this.setMovePosZ((int)this.getOwner().getZ());
								followTime=0;
							}
						}
						
						if(this.getMovePosX()!=0 || this.getMovePosZ()!=0 || this.getMovePosY()!=0) {
							double dx = this.getMovePosX() - this.getX();
							double dz = this.getMovePosZ() - this.getZ();
							double dis = MathHelper.sqrt(dx * dx + dz * dz);
							double min = this.stayrange;
							if(this.getChoose()||this.getMoveType()==2||this.getMoveType()==4)min = 5;
							if(dis > min){
								if(!this.isAttacking()||this.getAIType()!=4)this.setAIType(3);
							}
							if(this.getAIType()==3||this.getMoveType()==2||this.getMoveType()==4||this.getMoveType()==5){
								if(dis > min) {
									this.targetYaw = (float) MathHelper.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
									//this.turretYaw = this.clampYaw(this.turretYaw);
									this.targetYaw = this.clampYaw(this.targetYaw);
									float f3 = this.yHeadRot - this.targetYaw;
									f3 = this.clampYaw(f3);
									{
										//if(this.throttle>this.throttleMax/4)this.setForwardMove(-2);
										if (f3 > this.turnSpeed) {
											this.setStrafingMove(1);
										} else if (f3 < -this.turnSpeed) {
											this.setStrafingMove(-1);
										}else{
											this.setStrafingMove(0);
										}
										if(f3 < this.turnSpeed*1.5F && f3 > -this.turnSpeed*1.5F){
											this.setStrafingMove(0);
											angle = (int)dis;
											if(angle>45)angle=45;
											if(this.getChoose())angle = 45;
											float xxx = (angle-this.flyPitch)*0.02F;
											this.setMovePitch(xxx);
										}else{
											angle = -2;
											float xxx = (angle-this.flyPitch)*0.02F;
											this.setMovePitch(xxx);
										}
									}
								}else if(dis < 5){
									if(!this.getChoose())this.dropPassenger();
									if(this.getMoveType()==2||this.getMoveType()==4||this.getMoveType()==5)this.setMoveType(1);
									if(!this.isAttacking())this.setAIType(0);
								}
							}
						}
						if (this.getTarget() != null && this.movePower >= 5F && !this.isOnGround() && flyuptime>99) {
							LivingEntity target = this.getTarget();
							if(target.isAlive() && target!=null){
								this.setAttacking(true);
								double dx = target.getX() - this.getX();
								double dz = target.getZ() - this.getZ();
								double ddy = Math.abs(target.getY() - this.getY());
								double dyy = this.getEyeY() - target.getY();
								double dis = Math.sqrt(dx * dx + dz * dz);
								if(this.getAIType()!=3&&dis>60 && this.getY()>heightMin)this.setAIType(4);
								float f11 = (float) (Math.atan2(dyy, dis) * 180.0D / Math.PI);
								angle = /*this.xRot =*/ f11;
								if(angle>89)angle = 89;
								if(angle<-89)angle = -89;
								float xxx = (angle-this.flyPitch)*0.04F;
								float aim = angle-this.flyPitch;
								this.targetYaw = (float) MathHelper.atan2(dz, dx) * (180F / (float) Math.PI) - 90.0f;
								//this.turretYaw = this.clampYaw(this.turretYaw);
								this.targetYaw = this.clampYaw(this.targetYaw);
								float f3 = this.yHeadRot - this.targetYaw;
								f3 = this.clampYaw(f3);
								if(this.getAIType()==4 && this.getMoveType()!=5 && this.getMoveType()!=2){
									if (f3 > this.turnSpeed) {
										this.setStrafingMove(1);
									} else if (f3 < -this.turnSpeed) {
										this.setStrafingMove(-1);
									} else {
										this.setStrafingMove(0);
									}
								}
								if(f3>-this.turnSpeed*(15+w1aim)&&f3<this.turnSpeed*(15+w1aim)){
									if(this.getAIType()==4 && this.getMoveType()!=5 && this.getMoveType()!=2)this.setMovePitch(xxx);
									if(aim<10+w1aim&&aim>-10-w1aim){
										if(!w1aa && dis<w1max && target.getY()<w1min+this.getY()||w1aa && ddy<w1max && target.getY()>w1min+block_height)fire1 = true;
									}
								}else{
									if(this.getAIType()==4 && this.getMoveType()!=5 && this.getMoveType()!=2){
										float back = (-2-this.flyPitch)*0.04F;
										this.setMovePitch(back);
										/*if(this.flyRoll<-2)this.setMoveYaw((-2-this.flyRoll)*0.04F);
										if(this.flyRoll>2)this.setMoveYaw((2-this.flyRoll)*0.04F);*/
									}
									//if(this.hurtTime>0 && dis>12)this.setAIType(5+this.random.nextInt(3));
								}
								if(autoPitchLock)this.turretPitch=f11;
								if(this.can_follow){
									this.turretYaw=this.targetYaw;
									this.turretPitch=f11;
								}
								if(f3>-this.turnSpeed*(15+w2aim)&&f3<this.turnSpeed*(15+w2aim)){
									if(aim<10+w2aim&&aim>-10-w2aim){
										if(!w2aa && dis<w2max && target.getY()<w2min+this.getY()||w2aa && ddy<w2max && target.getY()>w2min+block_height)fire2 = true;
									}
								}
								if(f3>-this.turnSpeed*(15+w4aim)&&f3<this.turnSpeed*(15+w4aim)){
									if(aim<10+w4aim&&aim>-10-w4aim){
										if(!w4aa && dis<w4max && target.getY()<w4min+this.getY()||w4aa && ddy<w4max && target.getY()>w4min+block_height)fire4 = true;
									}
								}
								if(dis<10){
									this.setAIType(1);
								}
								/*if(dis>10 && dis<20){
									this.setAIType(2);
								}*/
							}
						}
					}else{
						this.setMovePitch(this.getMovePitch()*0.8F);
						this.setMoveYaw(this.getMoveYaw()*0.8F);
					}
					PL_AirCraftMove.moveHeliCopterMode(null, this, this.MoveSpeed, this.turnSpeed);
					if(!this.isOnGround()){
						this.flyPitch += this.getMovePitch() * (this.turnSpeed) * ((180F-Math.abs(this.flyRoll)*2)/180);
						this.flyRoll -= this.getMoveYaw()*(this.turnSpeed);
					}
				}
			}
			if (this.getForwardMove() > 0.0F) {
				if(this.throttle < this.throttleMax)++this.throttle;
			}
			if (this.getForwardMove() < 0.0F) {
				if(this.throttle >= 1)--this.throttle;
			}
			if (this.getStrafingMove() < 0.0F) {
				if(this.flyRoll < 30)this.flyRoll =  this.flyRoll + (float)(this.turnSpeed * 0.15F);
				this.deltaRotation = this.deltaRotation + this.turnSpeed* (180F+this.flyRoll)/180F;
			}
			if (this.getStrafingMove() > 0.0F) {
				if(this.flyRoll > -30)this.flyRoll = this.flyRoll - (float)(this.turnSpeed * 0.15F);
				this.deltaRotation = this.deltaRotation - this.turnSpeed* (180F-this.flyRoll)/180F;
			}
			this.deltaRotation += this.getMovePitch() * -this.flyRoll/60F;
			this.deltaRotation *= 0.9F;
			if(this.deltaRotation > 20)this.deltaRotation = 20;
			if(this.deltaRotation < -20)this.deltaRotation = -20;
			float sensitivityAdjust = 2f - (float)Math.exp(-2.0f * this.throttle) / (4.5f * (this.throttle + 0.1f));
			sensitivityAdjust = MathHelper.clamp(sensitivityAdjust, 0.0f, 1.0f);
			sensitivityAdjust *= 0.125F;
			float yaw = this.deltaRotation * sensitivityAdjust;
			this.yHeadRot += yaw;
			this.yBodyRot += yaw;
			this.yRot += yaw;
			this.turretYaw += yaw;
			/*if(flarecd>2){
				System.out.println("==================");
				if(this.getTarget()!=null)System.out.println("target="+this.getTarget().getName().getString());
				//System.out.println("this.getForwardMove()"+this.getForwardMove());
				System.out.println("this.getStrafingMove()"+this.getStrafingMove());
				System.out.println("this.flyPitch"+this.flyPitch);
				System.out.println("this.getAIType()"+this.getAIType());
				System.out.println("this.targetYaw"+this.targetYaw);
				System.out.println("this.turretYaw"+this.turretYaw);
				//System.out.println("this.yHeadRot"+this.yHeadRot);
				System.out.println("------------------");
				flarecd = 0;
			}*/
			if(fire1){
				if(this.cooltime >= this.ammo1){
					this.counter1 = true;
					this.cooltime = 0;
				}
				if(this.counter1 && this.gun_count1 >= this.w1cycle && this.getRemain1() > 0){
					this.setAnimFire(1);
					this.weaponActive1();
					this.setRemain1(this.getRemain1() - 1);
					this.gun_count1 = 0;//
					this.counter1 = false;
					if(this.getTargetType()==0)this.onFireAnimation(this.w1recoilp,this.w1recoilr);
				}
			}
			if(fire2){
				if(cooltime2 >= this.ammo2){
					this.counter2 = true;
					this.cooltime2 = 0;
				}
				if(this.counter2 && this.gun_count2 >= this.w2cycle && this.getRemain2()>0){
					this.setAnimFire(2);
					this.weaponActive2();
					this.setRemain2(this.getRemain2() - 1);
					this.gun_count2 = 0;//
					this.counter2 = false;
				}
			}
			if(this.getTargetType()!=1)crashEnemy();
		}
	}
	
	int crash = 0;
	public void crashEnemy(){
		if(crash<20)++crash;
		if(crash>10 && this.throttle>4 && AAConfig.COMMON.spawn.vehicle_break_block.get()){
			breakNearbyFragileBlocks();
			crash=0;
		}
		if(this.tracktick % 5 == 0 && this.throttle>0){//
			List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(2D));
			for(int k2 = 0; k2 < list.size(); ++k2) {
				Entity attackentity = list.get(k2);
				if(attackentity instanceof LivingEntity && ((LivingEntity)attackentity).getHealth()>0){
					if(this.CanAttack(attackentity))attackentity.hurt(DamageSource.thrown(this, this), this.getBbHeight()*this.getBbWidth()+this.throttle);
				}
			}
		}
	}
	private void breakNearbyFragileBlocks() {
		if (!(this.level instanceof ServerWorld)) {
			return;
		}
		ServerWorld serverWorld = (ServerWorld) this.level;
		AxisAlignedBB expandedBoundingBox = (new AxisAlignedBB(
		this.getX()-this.getBbWidth(), this.getY()+2, this.getZ()-this.getBbWidth(), 
		this.getX()+this.getBbWidth(), this.getY()+this.getBbHeight(), this.getZ()+this.getBbWidth())).inflate(1D);
		
		BlockPos.betweenClosedStream(expandedBoundingBox).forEach(pos -> {
			BlockState state = serverWorld.getBlockState(pos);
			if (state.isAir() || state.getDestroySpeed(serverWorld, pos) < 0) {
				return;
			}
			boolean isFragile = state.is(BlockTags.LEAVES)
					|| state.is(BlockTags.FLOWERS)
					|| state.is(BlockTags.CROPS)
					|| state.is(BlockTags.MUSHROOM_GROW_BLOCK)
					|| state.is(BlockTags.LOGS)||state.is(BlockTags.PLANKS); // 添加了破坏原木
			if (isFragile) {
				boolean dropItems = true; // 对于树叶，你可能想设为 false 来防止掉落
				serverWorld.destroyBlock(pos, dropItems, this); // null 表示无实体破坏者
			}
		});
	}
}