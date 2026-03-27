package advancearmy.entity.mob;

import java.util.List;

import advancearmy.AdvanceArmy;
import wmlib.common.bullet.EntityBullet;
import wmlib.common.bullet.EntityShell;
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
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;

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

import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.ai.SoldierAttackableTargetGoalSA;
import wmlib.api.ITool;
import advancearmy.entity.ai.AI_EntityWeapon;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraft.entity.ai.goal.SwimGoal;
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import wmlib.api.IEnemy;
import wmlib.api.ITool;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageSoldierAnim;
import net.minecraftforge.fml.network.PacketDistributor;
import wmlib.api.IPara;
import wmlib.api.IHealthBar;
public abstract class EntityMobSquadBase extends EntityMobSoldierBase implements IMob,IEnemy,IHealthBar,IPara{
	public EntityMobSquadBase(EntityType<? extends EntityMobSquadBase> sodier, World worldIn) {
		super(sodier, worldIn);
		this.maxUpStep = 1.5F;
	}
	public EntityMobSquadBase(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_AOHUAN, worldIn);
	}

    private static final DataParameter<Integer> weaponid = EntityDataManager.<Integer>defineId(EntityMobSquadBase.class, DataSerializers.INT);
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
	public void checkDespawn() {
	}
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D, 1.0000001E-5F));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(6, new LivingLockGoal(this, 1.0D, true));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, MobEntity.class, 10, 75F, true, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 75F, true, false, (attackentity) -> {return true;}));
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
				if(!(entity instanceof IMob||entity instanceof ITool)||entity==this.getTarget()){
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
				if(this.getVehicle()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
					return false;
				}else{
					if(entity instanceof IEnemy){
						return false;
					}else{
						if(this.distanceToSqr(entity1)>16D && flag){
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
										if(ent instanceof EntityMobSoldierBase){
											EntityMobSoldierBase unit = (EntityMobSoldierBase)ent;
											if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMoveType()!=2){
												unit.setMovePosX(i1);
												unit.setMovePosZ(k1);
												unit.setMoveType(4);
											}
										}
										if(ent instanceof WeaponVehicleBase){
											WeaponVehicleBase unit = (WeaponVehicleBase)ent;
											if(unit.getTargetType()==2 && unit.getTarget()==null){
												if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMoveType()!=2){
													unit.setMoveType(4);
													unit.setMovePosX(i1);
													unit.setMovePosZ(k1);
													break;
												}
											}
										}
									}
									if(this.getMoveType()!=2){
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
	
	public int weaponidmax = 6;
	public int soldierType = 0;//1 assult 2 recon 3 engineer 4 support 5 medic
	public int changeWeaponId=0;
	public int mainWeaponId=0;
	public boolean needaim = false;
	public float move_type = 0;
	public float movecool = 0;
	public boolean hide = false;
	public boolean canfire = false;
	boolean cheack = true;
	
	public void weaponActive1() {}
	public void weaponActive2() {}
	
	int showbartime = 0;
	public boolean isShow(){
		return this.showbartime>0;
	}
	public int getBarType(){
		return 1;
	}
	public LivingEntity getBarOwner(){
		return null;
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
	
	public void setDrop(){
		this.canPara = true;
	}
	public boolean isDrop(){
		return this.canPara && this.getVehicle()==null && !canDrop;
	}
	public boolean fastRid = false;
	int special_cool;
	public void specialAttack(double w, double h, double z, float bure, float speed, LivingEntity target){}
	public boolean canPara = false;
	public boolean canDrop = false;
	public void tick() {
		super.tick();
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
		this.updateSwingTime();
		if(special_cool<400)++special_cool;
		if(showbartime>0)--showbartime;
		if(movecool<100)++movecool;
		if (this.getVehicle() != null && this.getVehicle() instanceof EntitySA_Seat) {
			this.canPara = true;
			EntitySA_Seat seat = (EntitySA_Seat) this.getVehicle();
			this.attack_range_max = seat.attack_range_max;
			this.attack_range_min = seat.attack_range_min;
			this.attack_height_max = seat.attack_height_max;
			this.attack_height_min = seat.attack_height_min;
			if(seat.seatHide) {
				hide = true;
			}else{
				hide = false;
			}
			if(seat.seatCanFire) {
				canfire = true;
			}else{
				canfire = false;
			}
			this.setMoveType(1);
		}else{
			hide = false;
			canfire = true;
		}
		if(alertTime<100)++alertTime;
		if(guncyle<50)++guncyle;
		if(cooltime < 200)++cooltime;
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
		
		if(this.level.random.nextInt(6) == 0 && this.getVehicle()==null && !this.isPassenger() && this.getMoveType()!=3 && special_cool>25||fastRid){//上车
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
			this.vehicle = this.level.getNearestLoadedEntity(MobEntity.class, (new EntityPredicate()).range(18D).selector((attackentity) -> {
				if(attackentity instanceof EntityWMSeat){
					EntityWMSeat seat = (EntityWMSeat)attackentity;
					{
						if(seat.getAnyPassenger()==null){
							if(seat.getVehicle()!=null && seat.getVehicle() instanceof WeaponVehicleBase){
								WeaponVehicleBase ve = (WeaponVehicleBase)seat.getVehicle();
								//ve.checkseatspawn=true;
								if(ve.getOwner()!=null || ve.getTargetType()==3||ve.getHealth()<1){
									return false;
								}else{
									return true;
								}
							}else{
								return true;
							}
						}else{
							return false;
						}
					}
				}else{
					return false;
				}
			}), 
			this, this.getX(), this.getEyeY(), this.getZ(), this.getBoundingBox().inflate(18D, 18.0D, 18D));
			if(this.getVehicle()==null&&this.vehicle!=null){
				if (this.distanceToSqr(this.vehicle) > 36){
					this.getNavigation().moveTo(this.vehicle.getX(), this.vehicle.getY(), this.vehicle.getZ(), 1.6);
				}else{
					this.getNavigation().stop();
					this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
					if (!this.level.isClientSide){
						this.startRiding(this.vehicle);
					}
					fastRid=false;
					this.playSound(SoundEvents.IRON_DOOR_CLOSE, 3.0F, 1.0F);
				}
			}
		}
	}
	public void moveway(EntityMobSquadBase entity, float moveSpeed, double max) {
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
							if(entity.getMoveType()==4)entity.setMoveType(1);
						}
						if(entity.getVehicle()==null && !entity.isPassenger() && entity.getMoveType()!=3){
							if (dis>max*0.5F && entity.soldierType!=2||dis>max) {//
								if(entity.getMoveType()==1)MoveS(entity, moveSpeed, living.getX(), living.getY(), living.getZ(), flag);
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
					MoveS(entity, moveSpeed, entity.getMovePosX(), entity.getMovePosY(), entity.getMovePosZ(), false);
				}else{
					if(entity.getMoveType()==2){
						entity.setMoveType(3);
					}else if(entity.getMoveType()!=3){
						entity.setMoveType(1);
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
	public void MoveS(EntityMobSquadBase entity, double speed, double targetx, double targety, double targetz, boolean canSee){
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