package advancearmy.entity.soldier;

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

import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ActionResultType;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.EntityPredicate;
import java.util.function.Predicate;

import net.minecraftforge.fml.ModList;
import net.minecraft.entity.passive.TameableEntity;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.ai.LivingLockGoal;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import advancearmy.entity.EntitySA_SoldierBase;
import net.minecraft.particles.ParticleTypes;
import safx.SagerFX;
import advancearmy.entity.ai.SoldierAttackableTargetGoalSA;
import advancearmy.util.InteractSoldier;
public class EntitySA_ConscriptX extends EntitySA_SoldierBase{
	public EntitySA_ConscriptX(EntityType<? extends EntitySA_ConscriptX> sodier, World worldIn) {
		super(sodier, worldIn);
		this.attack_range_max = 35;
	}
	public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
		List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(4D, 2.0D, 4D));
		for(int k2 = 0; k2 < list.size(); ++k2) {
			Entity entity = list.get(k2);
			if(entity!=null && entity instanceof LivingEntity && entity!=this){
				if(entity instanceof LivingEntity && this.CanAttack(entity)){
					entity.hurt(DamageSource.thrown(this, this), 6);
					this.playSound(SASoundEvent.sickle_land.get(), 5.0F,1);
				}
				((LivingEntity)entity).knockback(0.8F, 3F, 3);
			}
		}
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("DropRing", null, this.getX(), this.getY(), this.getZ(), 0F, 0F, 0F, 1F);
	  return false;
	}
	public EntitySA_ConscriptX(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_CONSX, worldIn);
	}
	
	public float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return this.getBbHeight()-0.3F;
	}

	public float getScale() {
		return this.isBaby() ? 0.5F : 1.0F;
	}
	
	public double getMountedYOffset() {
		return 0.6D;//0.12D
	}
    private static final DataParameter<Integer> action = 
    		EntityDataManager.<Integer>defineId(EntitySA_ConscriptX.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		{
			compound.putInt("action", getAction());
		}
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
		{
			this.setAction(compound.getInt("action"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(action, Integer.valueOf(0));
	}
	public int getAction() {
		return ((this.entityData.get(action)).intValue());
	}
	public void setAction(int stack) {
		this.entityData.set(action, Integer.valueOf(stack));
	}
		
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D, 1.0000001E-5F));
		//this.goalSelector.addGoal(6, new FollowOwnerGoalSA(this, 1.0D, 10.0F, 2.0F, false));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(6, new LivingLockGoal(this, 1.0D, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new SoldierAttackableTargetGoalSA<>(this, MobEntity.class, 10, 15F, true, false, (attackentity) -> {
			if(ModList.get().isLoaded("pvz")){
				return attackentity instanceof IMob||attackentity instanceof PVZZombieEntity;
			}else{
				return this.CanAttack(attackentity);
			}
		}));
	}
	
	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return EntitySA_SoldierBase.createMonsterAttributes()
		.add(Attributes.MAX_HEALTH, 40.0D)
		.add(Attributes.KNOCKBACK_RESISTANCE, (double) 5.0F)
		.add(Attributes.MOVEMENT_SPEED, (double)0.23F)
		.add(Attributes.FOLLOW_RANGE, 35.0D)
		.add(Attributes.ARMOR, (double) 8D);
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
	
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof LivingEntity){
				LivingEntity entity1 = (LivingEntity) entity;
				boolean flag = this.getSensing().canSee(entity1);
				if(this.getOwner()==entity||this.getVehicle()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
					return false;
				}else{
					if(entity instanceof TameableEntity){
						TameableEntity soldier = (TameableEntity)entity;
						if(this.getOwner()!=null && this.getOwner()==soldier.getOwner()){
							return false;
						}else{
							if(this.distanceTo(entity1)>8D && flag){
								if(this.groundtime>50){
									this.setAction(2+this.level.random.nextInt(1));
									this.setRemain2(3);
									this.setTarget(entity1);
								}
							}
							if(this.getRemain2()==1)par2 = par2*0.4F;//
							return super.hurt(source, par2);
						}
					}else{
						if(this.distanceToSqr(entity)>8D && flag){
							if(this.groundtime>50){
								this.setAction(2+this.level.random.nextInt(1));
								this.setRemain2(3);
								this.setTarget(entity1);
								this.groundtime = 0;
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
	
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		InteractSoldier.interactSoldier(this,player,hand);
		return super.mobInteract(player, hand);
    }
	
    protected SoundEvent getAmbientSound()
    {
        return SASoundEvent.cons_say.get();
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return null;
    }

    protected SoundEvent getDeathSound()
    {
        return SASoundEvent.cons_die.get();
    }
	
	public boolean doHurtTarget(Entity entity) {
		boolean flag = entity.hurt(DamageSource.mobAttack(this), 20);
		if (flag) {
			this.doEnchantDamageEffects(this, entity);
		}
		return flag;
	}
	protected LivingEntity vehicle;
	public boolean ground_aim = false;
	public boolean sit_aim = false;
	public int ground_time = 0;
	public int groundtime =50;
	public float height = 1.8F;
	//public float cooltime6 = 0;
	public int fire_tick = 46;
	public int fire_tick2 = 0;
	public int fire_tick3 = 0;
	public int find_time = 0;	 
	public static int gun_count1 = 0;
	public int guncyle = 0;
	
	public EntitySize dimensions_s;
	public void setSize(float w,float h){
		dimensions_s = EntitySize.scalable(w,h);
		double d0 = (double)dimensions_s.width / 2.0D;
        this.setBoundingBox(new AxisAlignedBB(this.getX() - d0, this.getY(), this.getZ() - d0, this.getX() + d0, this.getY() + (double)dimensions_s.height, this.getZ() + d0));
	}
	
	public void AIWeapon(double w, double h, double z, boolean shell, float bure, float speed){
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= MathHelper.sin(this.yRot * 0.01745329252F) * z;
		zz11 += MathHelper.cos(this.yRot * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(this.yRot * 0.01745329252F + 1) * w;
		zz11 += MathHelper.cos(this.yRot * 0.01745329252F + 1) * w;
		EntityShell bullet = new EntityShell(this.level, this);
		bullet.power = 20;
		bullet.setBulletType(2);
		bullet.setExLevel(1);
		bullet.setGravity(0.01F);
		bullet.moveTo(this.getX() + xx11, this.getY()+h, this.getZ() + zz11, this.yRot, this.xRot);
		bullet.shootFromRotation(this, this.xRot, this.yRot, 0.0F, speed, bure);
		if (!this.level.isClientSide) this.level.addFreshEntity(bullet);
	}
	public boolean hide = false;
	
	
	protected void checkAndPerformAttack(LivingEntity living, double range) {
		if(range <= 3){
			if (this.attack_time > 10) {
				this.playSound(SASoundEvent.chaincrash.get(), 3.0F, 1.0F);
				this.setMovePosY(1+this.random.nextInt(4));
				this.setHealth(this.getHealth()+(this.getMaxHealth()-this.getHealth())*0.02F);
				living.invulnerableTime = 0;
				this.doHurtTarget(living);
				List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(3D, 2.0D, 3D));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity attackentity = list.get(k2);
					if(this.CanAttack(attackentity)){
						attackentity.hurt(DamageSource.thrown(this, this), 5);
						if(this.getAction()==5)((LivingEntity)attackentity).knockback(2F, 1F, 2);
					}
				}
			}
		}else{
			if(this.getMovePosY()!=0 && this.attack_time > 9)this.setMovePosY(0);
		}
	}
	
	protected float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.7F;
	}
	
	boolean change = true;
	public void updateSwingTime() {
		int i = 11;
		if (this.swinging) {
			++this.swingTime;
			if(!change && this.attackAnim<12){
				this.attackAnim+=1.2F;
			}
			if(this.attackAnim>=12)change = true;
			if(change && this.attackAnim>-6){
				this.attackAnim-=0.6F;
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
	
	public int attack_time = 0;
	public float movecool = 0;
	public void tick() {
		super.tick();
		if(!this.onGround && this.getAction()==5){
			double x1 = 0;
			double z1 = 0;
			float ff = this.yRot * 0.01745329252F;
			x1 -= MathHelper.sin(ff -1.57F) * 0.56F;
			z1 += MathHelper.cos(ff -1.57F) * 0.56F;
			x1 -= MathHelper.sin(ff) * -0.47F;
			z1 += MathHelper.cos(ff) * -0.47F;
			this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() + x1, this.getY() + 2.23F, this.getZ() + z1, 0.0D, 0.0D, 0.0D);
			this.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() - x1, this.getY() + 2.23F, this.getZ() - z1, 0.0D, 0.0D, 0.0D);
		}

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
			if(reload1 >= reload_time1){
				this.setRemain1(this.magazine);
				this.playSound(SASoundEvent.reload_mag.get(), 2.0F, 1.0F);
				reload1 = 0;
			}
		}
		
		float moveSpeed = 0.40F;//移速
		if(this.getRemain2()==1){
			moveSpeed = 0.1F;
			if(this.getBbHeight()!=0.75F)this.setSize(1.5F, 0.75F);
			this.height = 0.75F;//开枪高度
		}else{
			if(this.getRemain2()==2){
				if(this.getBbHeight()!=2.7F)this.setSize(1.5F, 2.7F);
			}else{
				if(this.getBbHeight()!=3F)this.setSize(1.5F, 3);
			}
			this.height = 2F;//
			moveSpeed = 0.2F;
		}
		if(this.getRemain2()==3){
			this.groundtime = 0;
		}
		if(groundtime<200)++groundtime;
		if(this.groundtime>5 && this.groundtime<8){
			this.ground_time = 0;
		}
		if(this.ground_time<60)++ground_time;
		
		if(this.ground_time<50){
			//this.setRemain2(0);
		}else{
			if(this.getRemain2()==3)this.setRemain2(0);
		}
		
		if(this.movecool<100)++this.movecool;
		
		if(this.getMovePosY()>0 && this.attack_time>11){
			this.attack_time=0;
			this.swing(Hand.MAIN_HAND);
		}

		if(this.getAction()==0){
			this.moveway(this, moveSpeed);
		}else{
			this.moveway(this, moveSpeed*4);
		}
		this.updateSwingTime();
		ItemStack heldItem = this.getMainHandItem();
		World world = this.level;
		if(this.getTarget()!=null && this.isAttacking() && this.getVehicle()==null){
				LivingEntity livingentity = this.getTarget();
				if(livingentity.isAlive() && livingentity!=null){
					if(this.movecool>80){
						this.setAction(this.level.random.nextInt(6));
						this.movecool = 0;
					}
					if(this.attack_time<20)++this.attack_time;
					this.checkAndPerformAttack(livingentity, this.distanceTo(livingentity));
					boolean fire_flag =true;
					this.magazine = 71;
					this.fire_tick = 35;
					this.reload_time1 = 80;
					float bure = 3.0F;
					if(this.getRemain2()==2){
						bure = 2F;
					}
					if(this.cooltime > this.fire_tick && this.getRemain1() > 0 && fire_flag && this.getMovePosY()==0)
					{
						this.counter1 = true;
						this.cooltime = 0;
						this.fire_tick2 = 0;
					}
					if(this.counter1 && this.guncyle > 2){
						AIWeapon(0,this.height,1.8F, false, bure, 4);
						this.playSound(SASoundEvent.fire_2a42.get(), 4.0F, 0.8F);//ppsh41f
						this.setRemain1(this.getRemain1() - 1);
						this.guncyle = 0;//
						this.gun_count1 = 0;
						++this.countlimit1;
						if(this.countlimit1>(1+this.level.random.nextInt(8))){
							this.counter1 = false;
							this.countlimit1 = 0;
						}
					}
				}
			if(this.isAttacking() && this.find_time<40 && this.getRemain1()>0){
				++this.find_time;
				++this.fire_tick2;
				++this.fire_tick3;
			}
			if(this.level.random.nextInt(6) > 3 && this.find_time > 20){
				this.setRemain2(2);
				this.find_time = 0;
			}else if(this.level.random.nextInt(6) < 3 && this.find_time > 20){
				this.setRemain2(0);
				this.find_time = 0;
			}
		}
	}
	
	public float face = 0;
	public float face2 = 0;
	public void moveway(EntitySA_ConscriptX entity, float moveSpeed) {
		boolean ta = false;
		{
			if (entity.getTarget() != null) {
				{
				boolean flag = entity.getSensing().canSee(entity.getTarget());
				if (!entity.getTarget().isInvisible()) {//target
					if (flag) {
						entity.setAttacking(true);
					}else{
						entity.setAttacking(false);
					}
					if (entity.getTarget().getHealth() > 0.0F && flag) {
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
							if ((ddx>15F||ddz>15F)||entity.getMovePosY()>0 && entity.getHealth()>50){//
								{
									if(entity.getMoveType()!=3)MoveS(entity, moveSpeed, entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), (LivingEntity)entity.getTarget());
								}
							}else if((ddx < 4 || ddz < 4) && entity.getHealth()<100){//
								if(entity.getMoveType()!=3)MoveS(entity, -moveSpeed, entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), (LivingEntity)entity.getTarget());
							}
						}
						entity.yRotO = entity.yRot = f12;//
						entity.setYHeadRot(f12);//
						entity.xRot = -f11 + 0;//
					}
				}
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
						}
						if (dis>=25F) {//
							//entity.moveTo(entity.getOwner().getX()+1, entity.getOwner().getY()+1, entity.getOwner().getZ()+1, 1F, 0);
							entity.setPos(entity.getOwner().getX()+1, entity.getOwner().getY()+1, entity.getOwner().getZ()+1);
							if(entity.getNavigation()!=null)entity.getNavigation().stop();
						}
					}
				}
			}
		}
	}
	
	public void MoveS(EntitySA_ConscriptX entity, double speed, double ex, double ey, double ez, LivingEntity target){
		if(!entity.level.isClientSide)
		{
			double d5 = ex - entity.getX();
			double d7 = ez - entity.getZ();
			float yawoffset = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
			float yaw = yawoffset * (2 * (float) Math.PI / 360);
			double mox = 0;
			double moy = -1D;
			double moz = 0;
			
			if(speed>0){
				if(entity.distanceTo(target)>2){
					if(entity.getAction() == 1 && entity.movecool<15) {
						mox -= MathHelper.sin(yaw) * speed * 1;
						moz += MathHelper.cos(yaw) * speed * 1;
					}else if(entity.getAction() == 4) {
						mox -= MathHelper.sin(yaw) * speed * -2;
						moz += MathHelper.cos(yaw) * speed * -2;
						moy = 1.8D;
						entity.movecool = 0;
						entity.setAction(0);
					}else if(entity.getAction() == 5) {
						if(entity.movecool > 5) {
							mox -= MathHelper.sin(yaw) * speed * 2;
							moz += MathHelper.cos(yaw) * speed * 2;
							if(entity.movecool == 6)moy = 4D;
						}
					}
				}
			}else{
				if(entity.getAction() == 4) {
					mox -= MathHelper.sin(yaw) * speed * 2;
					moz += MathHelper.cos(yaw) * speed * 2;
					moy = 1.8D;
					entity.movecool = 0;
					entity.setAction(0);
				}
			}
			if(entity.getAction() == 2) {
				//System.out.println("---"+entity.getAction());
				mox -= MathHelper.sin(yaw + 1.57F) * speed * 5;
				moz += MathHelper.cos(yaw + 1.57F) * speed * 5;
				moy = 0.6D;
				int ran = entity.level.random.nextInt(10);
				if(ran == 0 || ran == 1) {
					entity.setAction(3);
				}else
				{
					entity.movecool = 0;
					entity.setAction(0);
				}
			}
			else if(entity.getAction() == 3) {
				mox -= MathHelper.sin(yaw - 1.57F) * speed * 5;
				moz += MathHelper.cos(yaw - 1.57F) * speed * 5;
				moy = 0.6D;
				int ran = entity.level.random.nextInt(10);
				if(ran == 0 || ran == 1) {
					entity.setAction(2);
				}else{
					entity.movecool = 0;
					entity.setAction(0);
				}
			}
			if(entity.getAction() == 0){
				mox -= MathHelper.sin(yaw) * speed * 1;
				moz += MathHelper.cos(yaw) * speed * 1;
			}
			boolean flag = entity.getSensing().canSee(target);
			if(flag){
				entity.setDeltaMovement(mox, moy, moz);
			}else{
				entity.getNavigation().moveTo(ex, ey, ez, 2);
			}
		}
	}
}