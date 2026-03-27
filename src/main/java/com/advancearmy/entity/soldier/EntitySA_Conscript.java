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
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;

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
import wmlib.common.living.EntityWMVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;

import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.ai.SoldierAttackableTargetGoalSA;
import wmlib.common.living.WeaponVehicleBase;
import net.minecraftforge.fml.ModList;
import wmlib.api.IArmy;
import wmlib.util.ThrowBullet;
import wmlib.common.bullet.EntityGrenade;

import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import advancearmy.util.SummonEntity;
import javax.annotation.Nullable;
import wmlib.common.living.EntityWMSeat;
public class EntitySA_Conscript extends EntitySA_SquadBase{
	public EntitySA_Conscript(EntityType<? extends EntitySA_Conscript> sodier, World worldIn) {
		super(sodier, worldIn);
		this.unittex = new ResourceLocation("advancearmy:textures/item/item_spawn_conscript.png");
	}
	public EntitySA_Conscript(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_CONS, worldIn);
	}
	int summonTime = 0;
	@Nullable
	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance diff, SpawnReason reason, @Nullable ILivingEntityData data, @Nullable CompoundNBT nbt) {
		data = super.finalizeSpawn(world, diff, reason, data, nbt);
		if(reason == SpawnReason.NATURAL)this.setWeaponId(-1);
		return data;
	}
	public void startSummon(){
		if (level.random.nextInt(3) == 1)SummonEntity.wildSummon(level, this.getX(), this.getY(), this.getZ() + 5, 31, true, this.getTeam(), 4);
		if (level.random.nextInt(4) == 1)SummonEntity.wildSummon(level, this.getX(), this.getY(), this.getZ() + 5, 31, true, this.getTeam(), 6);
		int summonType = -1;
		float rand = level.random.nextFloat(); // 生成一个[0, 1)的随机浮点数
		if (rand < 0.02f) {          // 原 1/50
			summonType = 5;
		} else if (rand < 0.05f) {   // 0.02 + 0.03 (原1/35)
			summonType = 4;
		} else if (rand < 0.10f) {   // 0.05 + 0.05 (原1/20)
			summonType = 3;
		} else if (rand < 0.20f) {   // 0.10 + 0.10 (原1/10)
			summonType = 2;
		} else if (rand < 0.4667f) { // 0.20 + 0.2667 (原1/3.75≈0.2667)
			summonType = 1;
		} else if (rand < 0.80f) {   // 0.4667 + 0.3333 (原1/3≈0.3333)
			summonType = 0;
		}
		if (summonType > -1) {
			int nearbyAllies = 0;
			boolean stop = false;
			List<Entity> nearbyEntities = level.getEntities(this, this.getBoundingBox().inflate(60.0, 80.0, 60.0));
			for (Entity entity : nearbyEntities) {
				if (entity instanceof WeaponVehicleBase && ((WeaponVehicleBase) entity).getTargetType() == 3) {
					nearbyAllies++;
					if (nearbyAllies > 5) {
						stop=true;
						break;
					}
				}
			}
			if(!stop){
				double offsetX = level.random.nextDouble() * 6.0 - 3.0; // -3到+3
				double offsetZ = level.random.nextDouble() * 6.0 - 3.0;
				switch (summonType) {
					case 0:
						SummonEntity.wildSummon(level, this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 23, true, this.getTeam(), 1);
						break;
					case 1:
						SummonEntity.wildSummon(level, this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 11, true, this.getTeam(), 1);
						break;
					case 2:
						SummonEntity.wildSummon(level, this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 3, true, this.getTeam(), 1);
						break;
					case 3:
						SummonEntity.wildSummon(level, this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 12, true, this.getTeam(), 1);
						break;
					case 4:
						SummonEntity.wildSummon(level, this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 4, true, this.getTeam(), 1);
						break;
					case 5:
						SummonEntity.wildSummon(level, this.getX() + offsetX, this.getY(), this.getZ() + 5 + offsetZ, 5, true, this.getTeam(), 1);
						break;
				}
			}
		}
	}
	
	public void specialAttack(double w, double h, double z, float bure, float speed, LivingEntity target){
		this.playSound(SoundEvents.EGG_THROW, 4.0F, 1.0F);
		double xx11 = 0;
		double zz11 = 0;
		xx11 -= MathHelper.sin(this.yRot * 0.01745329252F) * z;
		zz11 += MathHelper.cos(this.yRot * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(this.yRot * 0.01745329252F + 1) * w;
		zz11 += MathHelper.cos(this.yRot * 0.01745329252F + 1) * w;
		EntityGrenade bullet = new EntityGrenade(this.level, this);
		bullet.selfExpSound=SASoundEvent.exp_m67.get();
		float targetpitch = this.xRot;
		if(this.getTarget()!=null){
			double[] angles = new double[2];
			boolean flag = ThrowBullet.canReachTarget(speed, 0.06F, 0.99,
					(int) this.getX(), (int) this.getEyeY(), (int) this.getZ(),
					(int) target.getX(), (int) target.getEyeY(), (int) target.getZ(),
					angles, true);
			if (flag) {
				targetpitch = (float)-angles[1];
			}
		}
		bullet.power = 10;
		bullet.timemax = 80;
		bullet.setExLevel(5);
		bullet.setModel("advancearmy:textures/gun/tool.obj");
		bullet.setTex("advancearmy:textures/gun/gun.png");
		bullet.setGravity(0.06F);
		bullet.moveTo(this.getX() + xx11, this.getY()+h, this.getZ() + zz11, this.yRot, targetpitch);
		bullet.shootFromRotation(this, targetpitch, this.yRot, 0.0F, speed, bure);
		if (!this.level.isClientSide) this.level.addFreshEntity(bullet);
	}
	
    protected SoundEvent getAmbientSound()
    {
        return SASoundEvent.cons_say.get();
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SASoundEvent.cons_hurt.get();
    }

    protected SoundEvent getDeathSound()
    {
        return SASoundEvent.cons_die.get();
    }
	
	boolean wp2true = false;
	int wp2time = 0;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(summonTime<20)++summonTime;
			if(this.getWeaponId()==-1){
				if(summonTime>15){
					startSummon();
					this.setWeaponId(0);
				}
			}else{
				this.weaponidmax = 8;
				if(cheack){
					if(this.getWeaponId()==0)this.setWeaponId(1+this.level.random.nextInt(this.weaponidmax));
					cheack = false;
				}
			}
			float moveSpeed = 0.20F;
			if(this.canfire){
				this.weaponidmax = 8;
				if(cheack){
					if(this.getWeaponId()==0)this.setWeaponId(1+this.level.random.nextInt(this.weaponidmax));
					cheack = false;
				}
				if(this.getWeaponId()==1||this.getWeaponId()==8){//ak47
					this.wp2true = true;
					this.needaim = false;
					this.magazine = 30;
					this.fire_tick = 35;
					if(this.getWeaponId()==8){
						this.changeWeaponId=2;
						this.mainWeaponId=2;
					}else{
						this.changeWeaponId=0;
						this.mainWeaponId=0;
					}
					this.w1cycle = 4;
					this.reload_time1 = 60;
					this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
					"SmokeGun", null, SASoundEvent.ak47_fire.get(), 0.5F,1.5F,1F,0,0,
					6, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
				}
				if(this.getWeaponId()==2){//rpg7
					this.needaim = true;
					this.changeWeaponId=8;
					this.mainWeaponId=2;
					this.soldierType=1;
					this.magazine = 1;
					this.fire_tick = 35;
					this.w1cycle = 15;
					this.reload_time1 = 150;
					this.setWeapon(0, 3, "advancearmy:textures/entity/bullet/smawrocket.obj", "advancearmy:textures/gun/gun.png",
					"SmokeGun", "RocketTrail", SASoundEvent.fire_rpg7.get(), 0.5F,1.5F,1F,0,0,
					70, 3F, 2F, 3, false, 1, 0.01F, 40, 0);
				}
				if(this.getWeaponId()==3){//svd
					this.soldierType=2;
					this.attack_range_max = 50;
					this.needaim = true;
					this.changeWeaponId=6;
					this.mainWeaponId=3;
					this.magazine = 5;
					this.fire_tick = 35;
					this.w1cycle = 20;
					this.reload_time1 = 100;
					this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
					"SmokeGun", null, SASoundEvent.l96a1_fire.get(), 0.5F,1.5F,1F,0,0,
					30, 5F, 1.2F, 0, false, 1, 0.01F, 40, 0);
				}
				if(this.getWeaponId()==4){//pkm
					this.attack_range_max = 40;
					this.needaim = false;
					this.magazine = 100;
					this.fire_tick = 20;
					this.changeWeaponId=0;
					this.mainWeaponId=0;
					this.w1cycle = 2;
					this.reload_time1 = 60;
					this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
					"SmokeGun", null, SASoundEvent.pkm_fire.get(), 0.5F,1.5F,1F,0,0,
					6, 6F, 3F, 0, false, 1, 0.01F, 20, 0);
				}
				if(this.getWeaponId()==5){//fire
					this.needaim = true;
					this.changeWeaponId=0;
					this.mainWeaponId=0;
					this.magazine = 10;
					this.fire_tick = 35;
					this.w1cycle = 3;
					this.reload_time1 = 25;
					this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/null.obj", "advancearmy:textures/entity/bullet/bullet1.png",
					"SmokeGun", "FlamethrowerTrail", SASoundEvent.fire_flame.get(), 0.5F,1.5F,1F,0,0,
					4, 2F, 4F, 0, false, 5, 0.02F, 40, 5);
				}
				if(this.getWeaponId()==6){//ppsh41
					this.needaim = false;
					this.changeWeaponId=3;
					this.mainWeaponId=3;
					this.magazine = 71;
					this.fire_tick = 20;
					this.w1cycle = 3;
					this.reload_time1 = 40;
					this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
					"SmokeGun", null, SASoundEvent.ppsh41f.get(), 0.5F,1.5F,0.5F,0,0,
					4, 6F, 2F, 0, false, 1, 0.01F, 20, 0);
				}
				if(this.getWeaponId()==7){//aagun
					this.attack_range_max=40;
					this.needaim = true;
					this.changeWeaponId=0;
					this.mainWeaponId=0;
					this.magazine = 6;
					this.fire_tick = 35;
					this.w1cycle = 15;
					this.reload_time1 = 90;
					this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet30mm.obj", "advancearmy:textures/entity/bullet/bullet1.png",
					"SmokeGun", null, SASoundEvent.fire_2a42.get(), 0.5F,1.5F,1F,0,0,
					40, 4F, 2F, 2, false, 1, 0.01F, 40, 0);
				}
				if(this.getRemain2()==1){
					moveSpeed = 0.1F;
					if(this.getBbHeight()!=0.5F)this.setSize(0.5F, 0.5F);
					this.height = 0.5F;
				}else if(this.sit_aim){
					moveSpeed = 0.05F;
					if(this.getBbHeight()!=0.8F)this.setSize(0.5F, 0.8F);
					this.height = 1.2F;
				}else{
					if(this.getRemain2()==2){
						if(this.getBbHeight()!=1.6F)this.setSize(0.5F, 1.7F);
					}else{
						if(this.getBbHeight()!=1.8F)this.setSize(0.5F, 1.8F);
					}
					this.height = 1.8F;
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
					this.setRemain2(1);
					moveSpeed = 0.02F;
				}else{
					if(this.getRemain2()==1)this.setRemain2(0);
					moveSpeed = 0.2F;
				}
			}
			this.attack_height_max = this.attack_range_max;
			
			float movesp = moveSpeed;
			if(this.move_type==5 && this.getMoveType()!=2)movesp = moveSpeed*1.5F;
			if(this.isInWater()|| this.isInLava())movesp = moveSpeed*3F;
			this.moveway(this, movesp, this.attack_range_max);
			boolean isAttackVehicle = false;
			if(this.getTarget()!=null && this.isAttacking() && (this.getVehicle()==null||this.canfire)){
				if(movecool>99){
					this.move_type=this.level.random.nextInt(6);
					movecool = 0;
				}
				if(this.getMoveType()==3)this.move_type=0;
				LivingEntity living = this.getTarget();
				if(living.getMaxHealth()>this.getMaxHealth()||living.getVehicle()!=null||living instanceof EntityWMVehicleBase)isAttackVehicle = true;
				if(this.isAttacking() && this.find_time<40 && this.getRemain1()>0){
					++this.find_time;
				}
				if(this.level.random.nextInt(6) >= 3 && this.find_time > 20){
					if((this.mainWeaponId!=this.getWeaponId()||isAttackVehicle) && this.mainWeaponId!=0 && this.aim_time>60)this.setWeaponId(this.mainWeaponId);//main
					this.setRemain2(2);
					if(this.getRemain1()>this.magazine)this.setRemain1(this.magazine);
					this.find_time = 0;
				}else if(this.level.random.nextInt(6) < 3 && this.find_time > 20){
					if(this.changeWeaponId!=0 && !isAttackVehicle&&this.getRemain1()>0 && this.aim_time>60){
						if(this.soldierType!=2||this.soldierType==2&&this.distanceTo(living)<this.attack_range_max*0.2F)this.setWeaponId(this.changeWeaponId);
					}
					this.setRemain2(0);
					if(this.getRemain1()>this.magazine)this.setRemain1(this.magazine);
					this.find_time = 0;
				}
				if(this.soldierType==1&& this.getRemain1()<=0 && this.getWeaponId()==this.mainWeaponId)this.setWeaponId(this.changeWeaponId);
				if(living.isAlive() && living!=null && (this.aim_time>50||!this.needaim && this.aim_time>30)){
					if(this.cooltime >= this.ammo1 && this.cooltime > this.fire_tick){
						this.counter1 = true;
						this.cooltime = 0;
					}
					if(this.counter1 && this.guncyle >= this.w1cycle && this.getRemain1() > 0){
						this.setAnimFire(1);
						float side = 1.57F;
						if(this.weaponcross){
							if(this.getRemain1()%2==0){
								side = -1.57F;
							}else{
								side = 1.57F;
							}
						}
						double px = this.getX();
						double py = this.getY();
						double pz = this.getZ();
						AI_EntityWeapon.Attacktask(this, this, this.getTarget(), this.bulletid, this.bulletmodel1, this.bullettex1, this.firefx1, this.bulletfx1, this.firesound1,side, this.fireposX,this.fireposY,this.fireposZ,this.firebaseX,this.firebaseZ,px, py, pz,this.yRot, this.xRot,this.bulletdamage, this.bulletspeed, this.bulletspread, this.bulletexp, this.bulletdestroy, this.bulletcount, this.bulletgravity, this.bullettime, this.bullettype);
						this.setRemain1(this.getRemain1() - 1);
						this.guncyle = 0;
						this.gun_count1 = 0;
						++this.countlimit1;
						if(this.countlimit1>(1+this.level.random.nextInt(8))||this.needaim){
							this.counter1 = false;
							this.countlimit1 = 0;
						}
					}
				}
			}
		}
	}
}