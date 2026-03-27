package advancearmy.entity;

import java.util.List;
import wmlib.WarMachineLib;
import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
import wmlib.common.bullet.EntityMine;
import advancearmy.event.SASoundEvent;

import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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

import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.block.Blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.block.material.Material;

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
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IAmmo;
import com.mrcrayfish.guns.init.ModSounds;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ActionResultType;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.EntityPredicate;
import java.util.function.Predicate;

import advancearmy.entity.ai.SoldierAttackableTargetGoalSA;
import wmlib.common.living.WeaponVehicleBase;

import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.ai.LivingLockGoal;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraftforge.fml.ModList;
import net.minecraft.entity.passive.TameableEntity;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import wmlib.api.IArmy;
import wmlib.api.IBuilding;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageSoldierAnim;
import net.minecraftforge.fml.network.PacketDistributor;
import wmlib.api.IPara;
import wmlib.api.IHealthBar;
import net.minecraft.particles.ParticleTypes;
import advancearmy.entity.soldier.EntitySA_Soldier;
import net.minecraft.potion.Effects;
import advancearmy.util.InteractSoldier;
public abstract class EntitySA_SquadBase extends EntitySA_SoldierBase implements IArmy,IHealthBar,IPara{
	public EntitySA_SquadBase(EntityType<? extends EntitySA_SquadBase> sodier, World worldIn) {
		super(sodier, worldIn);
		this.maxUpStep = 1.5F;
	}
	public EntitySA_SquadBase(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_SOLDIER, worldIn);
	}
	public ResourceLocation unittex = null;
	public ResourceLocation getIcon1(){
		return this.unittex;
	}
	public ResourceLocation getIcon2(){
		return null;
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
		return this.getChoose();
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

    private static final DataParameter<Integer> weaponid = EntityDataManager.<Integer>defineId(EntitySA_SquadBase.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound){
		super.addAdditionalSaveData(compound);
		compound.putInt("weaponid", getWeaponId());
	}
	public void readAdditionalSaveData(CompoundNBT compound){
	   super.readAdditionalSaveData(compound);
		this.setWeaponId(compound.getInt("weaponid"));
	}
	protected void defineSynchedData(){
		super.defineSynchedData();
		this.entityData.define(weaponid, Integer.valueOf(0));
	}
	public int getWeaponId() {
		return ((this.entityData.get(weaponid)).intValue());
	}
	public void setWeaponId(int stack) {
		this.entityData.set(weaponid, Integer.valueOf(stack));
	}
	public void setWeapon(int stack) {
		this.entityData.set(weaponid, Integer.valueOf(stack));
	}
	
	protected void registerGoals() {
		//this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D, 1.0000001E-5F));
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(6, new LivingLockGoal(this, 1.0D, true));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new SoldierAttackableTargetGoalSA<>(this, MobEntity.class, 10, 15F, true, false, (attackentity) -> {
			if(ModList.get().isLoaded("pvz")){
				return this.CanAttack(attackentity)||attackentity instanceof PVZZombieEntity;
			}else{
				return this.CanAttack(attackentity);
			}
		}));
	}
	
	public static boolean getRange(float f1, float f2, float min, float max) {
		float x = f1-f2;
		if (x > min && x < max) {
			return true;
		} else {
			return false;
		}
	}
    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F){
			double height = entity.getY() - this.getY();
			if(this.distanceTo(entity)>this.attack_range_min && height >this.attack_height_min && height <this.attack_height_max){
				if(entity instanceof IMob||entity==this.getTarget()||entity==this.targetentity){
					if(this.getVehicle()!=null && this.getVehicle() instanceof EntityWMSeat){
						EntityWMSeat seat = (EntityWMSeat)this.getVehicle();
						double d5 = entity.getX() - this.getX();
						double d7 = entity.getZ() - this.getZ();
						float yaw= -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
						if(this.getRange(yaw, seat.yRot, seat.minyaw, seat.maxyaw)){
							return true;
						}else{
							return false;
						}
					}else{
						return true;
					}
					/*if(entity instanceof TameableEntity){
						TameableEntity soldier = (TameableEntity)entity;
						if(this.getOwner()==soldier.getOwner()){
							return false;
						}else{
							return true;
						}
					}else*/
				}else{
					return false;
				}
			}else{
				return false;
			}
    	}else{
			return false;
		}
    }
	int alertTime = 0;
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof LivingEntity){
				LivingEntity entity1 = (LivingEntity) entity;
				boolean flag = this.getSensing().canSee(entity1);
				if(this.getOwner()==entity||this.getVehicle()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null||this.getTeam()==null && entity.getTeam()==null && entity instanceof EntitySA_SquadBase){
					return false;
				}else{
					if(entity instanceof TameableEntity){
						TameableEntity soldier = (TameableEntity)entity;
						if(this.getOwner()!=null && this.getOwner()==soldier.getOwner()){
							return false;
						}else{
							if(this.distanceTo(entity1)>8D && flag){
								if(this.groundtime>50){
									this.setRemain2(3);
									this.setTarget(entity1);
								}
							}
							if(this.getRemain2()==1)par2 = par2*0.4F;//
							return super.hurt(source, par2);
						}
					}else{
						if(this.distanceToSqr(entity)>16D && flag){
							if(this.groundtime>50){
								this.setRemain2(3);
								this.groundtime = 0;
							}
							if(this.distanceToSqr(entity1)<this.attack_range_max*this.attack_range_max){
								this.setTarget(entity1);
							}else{
								if(this.getTarget()==null && alertTime>80){
									int i1 = (int)entity.getX() + this.random.nextInt(10)-5;
									int k1 = (int)entity.getZ() + this.random.nextInt(10)-5;
									List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(this.attack_range_max, this.attack_range_max, this.attack_range_max));
									for(int k2 = 0; k2 < list.size(); ++k2) {
										Entity ent = list.get(k2);
										if(ent instanceof EntitySA_SoldierBase){
											EntitySA_SoldierBase unit = (EntitySA_SoldierBase)ent;
											if(unit.getHealth()>0&&unit.getTarget()==null && (unit.getMoveType()==1 && unit.getOwner()==this.getOwner())||(unit.getMoveType()!=2 && unit.getOwner()==null)){
												unit.setMovePosX(i1);
												unit.setMovePosZ(k1);
												unit.setMoveType(4);
											}
										}
										if(ent instanceof WeaponVehicleBase){
											WeaponVehicleBase unit = (WeaponVehicleBase)ent;
											if(unit.getTargetType()==3 && unit.getTarget()==null){
												if(unit.getHealth()>0&&unit.getTarget()==null && (unit.getMoveType()==1 && unit.getOwner()==this.getOwner())||(unit.getMoveType()!=2 && unit.getOwner()==null)){
													unit.setMoveType(4);
													unit.setMovePosX(i1);
													unit.setMovePosZ(k1);
													break;
												}
											}
										}
									}
									if(this.getMoveType()==1){
										this.setMovePosX(i1);
										this.setMovePosZ(k1);
										this.setMoveType(4);
									}
									alertTime=0;
								}
							}
						}
						if(this.getRemain2()==1)par2 = par2*0.4F;//
						return super.hurt(source, par2);
					}
				}
			}else{
				if(this.getRemain2()==1)par2 = par2*0.4F;//
				return super.hurt(source, par2);
			}
		}else {
			if(this.getRemain2()==1)par2 = par2*0.4F;//
			return super.hurt(source, par2);
		}
    }
	
	public EntitySize dimensions_s;
	public void setSize(float w,float h){
		dimensions_s = EntitySize.scalable(w,h);
		double d0 = (double)dimensions_s.width / 2.0D;
        this.setBoundingBox(new AxisAlignedBB(this.getX() - d0, this.getY(), this.getZ() - d0, this.getX() + d0, this.getY() + (double)dimensions_s.height, this.getZ() + d0));
	}
    public void setAnimFire(int id)
    {
        if(this != null && !this.level.isClientSide)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MessageSoldierAnim(this.getId(), id));
        }
    }
	public int w1cycle;
	public int ammo1;
	protected LivingEntity vehicle;
	public boolean sit_aim = false;//坐下
	public int ground_time = 50;
	public int groundtime =50;
	public float height = 1.8F;
	//public float cooltime6 = 0;
	public Vector3d motions = this.getDeltaMovement();
	public int fire_tick = 46;
	public int find_time = 0;	 
	public static int gun_count1 = 0;
	public int guncyle = 0;
	
	public float fireposX = 0.5F;
	public float fireposY = 1.5F;
	public float fireposZ = 1F;
	public float firebaseX = 1.5F;
	public float firebaseZ = 0;
	public int bulletid = 0;
	public int bullettype = 0;
	public int bulletdamage = 5;
	public int bulletcount = 1;
	public int bullettime = 50;
	public int reloadSoundStart1 = 20;
	public float bulletspeed = 4;
	public float bulletspread = 2;
	public float bulletexp = 0;
	public float bulletgravity = 0.01F;
	public boolean bulletdestroy = false;
	public boolean weaponcross = false;
	public String bulletmodel1 = "advancearmy:textures/entity/bullet/bullet.obj";
	public String bullettex1 = "advancearmy:textures/entity/bullet/bullet.png";
	public String firefx1 = "SmokeGun";
	public String bulletfx1 = null;
	public SoundEvent reloadSound1 = SASoundEvent.reload1.get();
	public SoundEvent firesound1;
	public void setWeapon(int wpid,int id,
		String model, String tex, String fx1, String fx2, SoundEvent sound, 
		float w, float h, float z, float bx, float bz,
		int damage, float speed, float recoil, float ex, boolean extrue, int count,  float gra, int maxtime, int typeid){
		this.bulletid = id;
		this.bullettype = typeid;
		this.bulletdamage = damage;
		this.bulletspeed = speed;
		this.bulletspread = recoil;
		this.bulletexp = ex;
		this.bulletdestroy = extrue;
		this.bulletcount = count;
		this.bulletgravity = gra;
		this.bullettime = maxtime;
		this.bulletmodel1 = model;
		this.bullettex1 = tex;
		this.firefx1 = fx1;
		this.bulletfx1 = fx2;
		this.firesound1 = sound;
		this.fireposX = w;
		this.fireposY = h;
		this.fireposZ = z;
		this.firebaseX = bx;
		this.firebaseZ = bz;
	}
	
	public int weaponidmax = 14;
	public int soldierType = 0;//1 assult 2 scout 3 engineer 4 support 5 medic
	public int changeWeaponId=0;
	public int mainWeaponId=0;
	public boolean needaim = false;
	public float move_type = 0;
	public float movecool = 0;
	public boolean hide = false;
	public boolean canfire = false;
	
	public boolean cheack = true;
	
	int showbartime = 0;
	public boolean isShow(){
		return this.showbartime>0||this.getOwner()!=null;
	}
	public int getBarType(){
		return 0;
	}
	public LivingEntity getBarOwner(){
		return this.getOwner();
	}
	
	public void setDrop(){
		this.canPara = true;
	}
	public boolean isDrop(){
		return this.canPara && this.getVehicle()==null && !canDrop;
	}
	
	boolean change = true;
	public void updateSwingTime() {
		int i = 7;
		if (this.swinging) {
			++this.swingTime;
			if(!change && this.attackAnim<6){
				this.attackAnim+=0.6F;
			}
			if(this.attackAnim>=6)change = true;
			if(change && this.attackAnim>-3){
				this.attackAnim-=0.3F;
			}
			if(this.attackAnim<=0)change = false;
			if (this.swingTime >= i) {
				this.swingTime = 0;
				this.swinging = false;
			}
		} else {
			this.swingTime = 0;
			this.attackAnim = 0;
		}
	}
	
	public int samecount = 0;
	public boolean fastRid = false;
	public void specialAttack(double w, double h, double z, float bure, float speed, LivingEntity target){}
	public boolean canPara = false;
	public boolean canDrop = false;
	public void tick() {
		super.tick();
		
		if(special_cool<400)++special_cool;
		if(canPara && !canDrop){
			if(!this.onGround){
				Vector3d vector3d = this.getDeltaMovement();
				this.setDeltaMovement(vector3d.x, -0.5D, vector3d.z);
				this.fallDistance = 0.0F;
			}else{
				canPara =false;
			}
		}
		if(this.hurtTime>0){
			if(showbartime<1)showbartime = 70;
		}
		if(showbartime>0)--showbartime;
		if(movecool<100)++movecool;
		if (this.getVehicle() != null && this.getVehicle() instanceof EntityWMSeat) {
			this.canPara = true;
			EntityWMSeat seat = (EntityWMSeat) this.getVehicle();
			this.attack_range_max = seat.attack_range_max;
			this.attack_range_min = seat.attack_range_min;
			this.attack_height_max = seat.attack_height_max;
			this.attack_height_min = seat.attack_height_min;
			if(seat.seatHide/*||this.hasEffect(Effects.INVISIBILITY)*/) {
				hide = true;
			}else{
				hide = false;
			}
			if(seat.seatCanFire) {
				canfire = true;
			}else{
				canfire = false;
				this.setMoveType(1);
			}
		}else{
			hide = false;
			canfire = true;
		}
		if(alertTime<100)++alertTime;
		if(guncyle<50)++guncyle;
		if(cooltime < 500)++cooltime;
		if(gun_count1 < 200)++gun_count1;
		if(cooltime6<50)++cooltime6;
		if(this.getHealth()>0 && this.getHealth()<this.getMaxHealth() && this.cooltime6>45){
			this.setHealth(this.getHealth()+1);
			this.cooltime6 = 0;
		}
		
		if(this.getRemain1() <= 0){
			++reload1;
			if(reload1 == reload_time1 - reloadSoundStart1)this.playSound(reloadSound1, 2.0F, 1.0F);
			if(reload1 >= reload_time1){
				this.setRemain1(this.magazine);
				this.aim_time = 0;
				reload1 = 0;
			}
		}
		
		this.updateSwingTime();
		
		if(this.level.random.nextInt(6) == 0 && special_cool>25||fastRid){
			if(canfire && this.isAttacking()&& this.getTarget()!=null){
				if(special_cool>10 && this.distanceToSqr(this.getTarget())<4){
					this.getTarget().hurt(DamageSource.mobAttack(this), 8);
					this.playSound(SoundEvents.PLAYER_ATTACK_STRONG, 2.0F, 1.0F);
					special_cool = 0;
					this.swing(Hand.MAIN_HAND);
					//this.setAnimFire(1);
				}
				if(soldierType==0||soldierType==3||soldierType==4){
					if(special_cool>350 && this.distanceToSqr(this.getTarget())>16){
						specialAttack(0,this.height,0.8F, 2.5F, 1.5F, this.getTarget());
						special_cool = 0;
						this.swing(Hand.MAIN_HAND);
						//this.setAnimFire(1);
					}
				}
			}
			//int count = 0;
			//samecount=0;
			List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().inflate(18D, 18.0D, 18D));
			for (Entity target : entities) {
				if(this.getVehicle()==null && !this.isPassenger() && this.getMoveType()!=3&&(this.getMoveType()==1||!this.getChoose())){
					if (target instanceof EntityWMSeat) {
						EntityWMSeat seat = (EntityWMSeat) target;
						if (seat.getAnyPassenger() == null) {
							if (seat.getVehicle() != null && seat.getVehicle() instanceof WeaponVehicleBase) {
								WeaponVehicleBase ve = (WeaponVehicleBase) seat.getVehicle();
								if (ve.getTargetType() != 2 && ve.ridcool <= 0 && ve.getHealth()>1) {
									this.vehicle = seat;
									break;
								}
							}
						}
					}
				}
				//if(target instanceof EntityMine)++count;
				if(soldierType==1 && special_cool>25 && !this.isAttacking() && this instanceof EntitySA_Soldier){
					if(target instanceof WeaponVehicleBase){
						WeaponVehicleBase wv = (WeaponVehicleBase)target;
						if(wv.getHealth()>0 && wv.getHealth()<wv.getMaxHealth() && (this.getOwner()==wv.getOwner()||
						this.getTeam()==wv.getTeam()&&this.getTeam()!=null||
						this.getTeam()==null && wv.getTeam()==null && wv.getTargetType()==3)){
							if(this.distanceToSqr(wv)>16){
								if(this.getMoveType()!=3)this.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.6);
							}else{
								this.playSound(SoundEvents.ANVIL_USE, 3.0F, 1.0F);
								wv.setHealth(wv.getHealth()+5);
								special_cool = 0;
								this.swing(Hand.MAIN_HAND);
							}
							break;
						}
					}
					if(target instanceof TameableEntity && target instanceof IBuilding){
						TameableEntity living = (TameableEntity)target;
						if(living.getHealth()<living.getMaxHealth() && (this.getOwner()==living.getOwner()||
						this.getTeam()==living.getTeam()&&this.getTeam()!=null)){
							if(this.distanceToSqr(living)>9){
								if(this.getMoveType()!=3)this.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.6);
							}else{
								this.playSound(SoundEvents.ANVIL_USE, 2.0F, 1.0F);
								living.setHealth(living.getHealth()+3);
								special_cool = 0;
								this.swing(Hand.MAIN_HAND);
							}
							break;
						}
					}
				}
				if(soldierType==4 && special_cool>25  && !this.isAttacking() && this instanceof EntitySA_Soldier){
					if(target instanceof TameableEntity && !(target instanceof IBuilding) && !(target instanceof WeaponVehicleBase)){
						TameableEntity living = (TameableEntity)target;
						if(living.getHealth()<living.getMaxHealth() && (this.getOwner()==living.getOwner()||
						this.getTeam()==living.getTeam()&&this.getTeam()!=null)){
							if(this.distanceToSqr(living)>9){
								if(this.getMoveType()!=3)this.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.6);
							}else{
								this.playSound(SASoundEvent.box_heal.get(), 2.0F, 1.0F);
								living.setHealth(living.getHealth()+3);
								special_cool = 0;
								this.swing(Hand.MAIN_HAND);
							}
							break;
						}
					}
					if(target instanceof PlayerEntity){
						PlayerEntity living = (PlayerEntity)target;
						if(living.getHealth()<living.getMaxHealth() && (this.getOwner()==living||
						this.getTeam()==living.getTeam()&&this.getTeam()!=null)){
							if(this.distanceToSqr(living)>9){
								if(this.getMoveType()!=3)this.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.6);
							}else{
								this.playSound(SASoundEvent.box_heal.get(), 2.0F, 1.0F);
								living.setHealth(living.getHealth()+3);
								special_cool = 0;
								this.swing(Hand.MAIN_HAND);
							}
							break;
						}
					}
				}
				/*if(target instanceof WeaponVehicleBase){
					WeaponVehicleBase living = (WeaponVehicleBase)target;
					if(living.isthrow && living.getTargetType()==3 && living.getOwner()==this.getOwner()){
						if(!living.isAttacking()/*living.getTarget()==null*){
							Vector3d pos1 = this.position();
							Vector3d pos2 = living.position();
							Vector3d direction = pos2.subtract(pos1);
							int particleCount = (int)this.distanceTo(living);
							double step = 1.0 / particleCount;
							for (int i = 0; i <= particleCount; i++) {
								double t = step * i;
								Vector3d particlePos = pos1.add(
									direction.x * t,
									direction.y * t,
									direction.z * t
								);
								this.level.addParticle(
									ParticleTypes.HAPPY_VILLAGER,
									particlePos.x,
									particlePos.y + 2, // 调整到实体中心高度
									particlePos.z,
									0, 0, 0 // 无速度
								);
							}
							if(this.isAttacking()||this.getTarget()!=null){
								living.setTarget(this.getTarget());
								living.setAttacking(true);
								this.playSound(SASoundEvent.command_say.get(), 2.0F, 1.0F);
								break;
							}
						}
					}
				}*/
				/*if(count<2 && this.getVehicle()==null){
					if(soldierType==1){
						if(special_cool>350 && this.getMoveType()==3){
							EntityMine bullet = new EntityMine(WarMachineLib.ENTITY_MINE, this.level);
							bullet.setMineID(3);
							bullet.setOwner(this);
							bullet.moveTo(this.getX()+1, this.getY(), this.getZ(), this.yRot, this.xRot);
							if (!this.level.isClientSide) this.level.addFreshEntity(bullet);
							this.playSound(SoundEvents.SAND_BREAK, 3.0F, 1.0F);
							this.swing(Hand.MAIN_HAND);
							special_cool = 0;
							break;
						}
					}
					if(soldierType==2){
						if(special_cool>300 && this.getMoveType()==3){
							EntityMine bullet = new EntityMine(WarMachineLib.ENTITY_MINE, this.level);
							bullet.setMineID(1);
							bullet.setOwner(this);
							bullet.moveTo(this.getX()+1, this.getY(), this.getZ(), this.yRot, this.xRot);
							if (!this.level.isClientSide) this.level.addFreshEntity(bullet);
							this.playSound(SoundEvents.SAND_BREAK, 3.0F, 1.0F);
							this.swing(Hand.MAIN_HAND);
							special_cool = 0;
							break;
						}
					}
				}*/
			}
			if(this.getVehicle()==null&&this.vehicle!=null){
				if (this.distanceToSqr(this.vehicle) > 64){
					this.getNavigation().moveTo(this.vehicle.getX(), this.vehicle.getY(), this.vehicle.getZ(), 1.6);
				}else{
					this.getNavigation().stop();
					this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
					if (!this.level.isClientSide){
						this.startRiding(this.vehicle);
					}
					fastRid=false;
					this.vehicle=null;
					this.playSound(SoundEvents.IRON_DOOR_CLOSE, 3.0F, 1.0F);
				}
			}
		}
	}
	
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		InteractSoldier.interactSoldier(this,player,hand);
		return super.mobInteract(player, hand);
    }
	
	public void moveway(EntitySA_SquadBase entity, float moveSpeed, double max) {
		if (entity.getTarget() != null) {
			LivingEntity living = entity.getTarget();
			if(living.isAlive() && living!=null){
				boolean flag = entity.getSensing().canSee(living);
				if(!flag)entity.setAttacking(false);
				if (!living.isInvisible()) {//target
					if(living.getHealth() > 0.0F  && entity.getMoveType()!=2){
						float height = living.getBbHeight()*0.75F;
						if(height>3)height=3;
						double dx = living.getX()+living.getDeltaMovement().x - entity.getX();
						double dz = living.getZ()+living.getDeltaMovement().y - entity.getZ();
						double d1 = entity.getEyeY() - living.getY() - height -living.getDeltaMovement().y;
						if(living.getVehicle()!=null && living.getVehicle() instanceof EntityWMSeat){
							EntityWMSeat seat = (EntityWMSeat)living.getVehicle();
							if(seat.getVehicle()!=null){
								dx = seat.getVehicle().getX()+seat.getDeltaMovement().x - entity.getX();
								dz = seat.getVehicle().getZ()+seat.getDeltaMovement().z - entity.getZ();
								d1 = seat.getVehicle().getY()+seat.getVehicle().getBbHeight()*0.5F - living.getEyeY();
							}
						}
						double dis = Math.sqrt(dx * dx + dz * dz);
						if (flag){
							float f11 = (float) (-(Math.atan2(d1, dis) * 180.0D / Math.PI));
							float f12 = -((float) Math.atan2(dx, dz)) * 180.0F / (float) Math.PI;
							entity.yRotO = entity.yRot = f12;//
							entity.setYHeadRot(f12);//
							entity.xRot = -f11 + 0;//
							entity.setAttacking(true);
							if(entity.getMoveType()==4&&entity.getOwner()==null)entity.setMoveType(1);
						}
						if(entity.getVehicle()==null && !entity.isPassenger() && entity.getMoveType()!=3){
							if (dis>max*0.5F && entity.soldierType!=2||dis>max) {//
								if(entity.getMoveType()==1||entity.getOwner()==null)MoveS(entity, moveSpeed, living.getX(), living.getY(), living.getZ(), flag);
							}else if(dis < 4){//
								MoveS(entity, -moveSpeed, living.getX(), living.getY(), living.getZ(), flag);
							}else{
								if(entity.move_type>1 && entity.move_type!=5)MoveS(entity, moveSpeed, living.getX(), living.getY(), living.getZ(), flag);
							}
							if(entity.move_type==1 && entity.cooltime6>40){//跳跃模式
								Vector3d vector3d = entity.getDeltaMovement();
								entity.setDeltaMovement(3F*vector3d.x, 0.3D+entity.level.random.nextInt(2)*moveSpeed, 3F*vector3d.z);
								entity.cooltime6 = 0;
							}
						}
					}
				}
			}
		}
		if(entity.getVehicle()==null && !entity.isPassenger()){
			if(entity.getMovePosX()==0&&entity.getMovePosZ()==0){
				entity.setMovePosX((int)entity.getX());
				entity.setMovePosZ((int)entity.getZ());
			}
			if(entity.getMovePosX()!=0&&entity.getMovePosZ()!=0/*&&entity.getMovePosY()!=0*/ && (entity.getMoveType()==2||!entity.isAttacking())){
				double dx = entity.getMovePosX() - entity.getX();
				double dz = entity.getMovePosZ() - entity.getZ();
				double d6 = entity.getMovePosY() - entity.getY();
				double dis = Math.sqrt(dx * dx + dz * dz);
				float f12 = -((float) Math.atan2(dx, dz)) * 180.0F / (float) Math.PI;
				int min = 5;
				if(entity.getMoveType()==3)min=1;
				if(entity.getMoveType()==1)min=18;
				if(dis>min){
					if(entity.vehicle!=null)entity.vehicle=null;
					if(entity.move_type!=0)entity.move_type=0;
					MoveS(entity, moveSpeed, entity.getMovePosX(), entity.getMovePosY(), entity.getMovePosZ(), false);
				}else{
					if(entity.getMoveType()==2){
						entity.setMoveType(3);
					}/*else if(entity.getMoveType()!=3){
						entity.setMoveType(1);
					}*/
					//entity.setMovePosX(0);
					//entity.setMovePosY(0);
					//entity.setMovePosZ(0);
				}
			}
			if(entity.getMoveType()==0&&entity.getOwner() != null){
				if (!entity.getOwner().isInvisible()) {//target
					if (entity.getOwner().getHealth() > 0.0F) {
						double dx = entity.getOwner().getX() - entity.getX();
						double dz = entity.getOwner().getZ() - entity.getZ();
						double d1 = entity.getEyeY() - (entity.getOwner().getEyeY());
						double dis = Math.sqrt(dx * dx + dz * dz);
						float f11 = (float) (-(Math.atan2(d1, dis) * 180.0D / Math.PI));
						float f12 = -((float) Math.atan2(dx, dz)) * 180.0F / (float) Math.PI;
						if (dis>=6F) {//
							entity.getNavigation().moveTo(entity.getOwner().getX(), entity.getOwner().getY(), entity.getOwner().getZ(), moveSpeed*8);
							entity.setMovePosX((int)entity.getOwner().getX());
							entity.setMovePosZ((int)entity.getOwner().getZ());
						}
						if (dis>=25F) {//
							//entity.moveTo(entity.getOwner().getX()+1, entity.getOwner().getY()+1, entity.getOwner().getZ()+1, 1F, 0);
							entity.setPos(entity.getOwner().getX()+1, entity.getOwner().getY()+1, entity.getOwner().getZ()+1);
							if(entity.getNavigation()!=null)entity.getNavigation().stop();
							entity.setMovePosX(0);
							entity.setMovePosZ(0);
						}
					}
				}
			}
		}else{
			if(entity.getMovePosX()!=0||entity.getMovePosZ()!=0){
				entity.setMovePosX(0);
				entity.setMovePosZ(0);
			}
		}
	}
	
	public void MoveS(EntitySA_SquadBase entity, double speed, double targetx, double targety, double targetz, boolean canSee){
		double d5 = targetx - entity.getX();
		double d7 = targetz - entity.getZ();
		float yawoffset = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
		float yaw = yawoffset * (2 * (float) Math.PI / 360);
		double mox = 0;
		double moy = -1D;
		double moz = 0;
		if(entity.getMoveType()!=2){
			if(entity.move_type == 2) {
				mox -= MathHelper.sin(yaw + 1.57F) * speed;
				moz += MathHelper.cos(yaw + 1.57F) * speed;
			}else if(entity.move_type == 3) {
				mox -= MathHelper.sin(yaw - 1.57F) * speed;
				moz += MathHelper.cos(yaw - 1.57F) * speed;
			}else if(entity.move_type == 4 && speed>0) {
				mox -= MathHelper.sin(yaw) * speed*-0.7F;
				moz += MathHelper.cos(yaw) * speed*-0.7F;
			}else{
				mox -= MathHelper.sin(yaw) * speed;
				moz += MathHelper.cos(yaw) * speed;
			}
		}else{
			mox -= MathHelper.sin(yaw) * speed;
			moz += MathHelper.cos(yaw) * speed;
		}
		if((canSee || speed<0) || entity.move_type>0 && entity.move_type<5 && entity.getMoveType()!=2){
			entity.setDeltaMovement(mox, moy, moz);
		}else{
			entity.getNavigation().moveTo(targetx, targety, targetz, speed*8);
		}
	}
}