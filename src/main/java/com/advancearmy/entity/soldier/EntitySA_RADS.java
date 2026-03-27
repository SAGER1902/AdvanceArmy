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

import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.EntityPredicate;
import java.util.function.Predicate;

import net.minecraftforge.fml.ModList;
import net.minecraft.entity.passive.TameableEntity;
import wmlib.common.living.EntityWMSeat;
import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.ai.SoldierAttackableTargetGoalSA;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.bullet.EntityRad;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.block.BlockState;
import wmlib.api.IRadSoldier;
import wmlib.WarMachineLib;
import safx.SagerFX;
public class EntitySA_RADS extends EntitySA_SquadBase implements IRadSoldier{
	public EntitySA_RADS(EntityType<? extends EntitySA_RADS> sodier, World worldIn) {
		super(sodier, worldIn);
		this.fireposX=0.5F;
		this.fireposZ=2F;
		this.unittex = new ResourceLocation("advancearmy:textures/item/item_spawn_rads.png");
	}
	public EntitySA_RADS(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_RADS, worldIn);
	}
	
    protected SoundEvent getAmbientSound()
    {
        return SASoundEvent.fs_say.get();
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SASoundEvent.fs_hurt.get();
    }

    protected SoundEvent getDeathSound()
    {
        return SASoundEvent.fs_die.get();
    }
	
	public void setMove(int id, int x, int y, int z){
		this.setMoveType(id);
		this.setMovePosX(x);
		this.setMovePosY(y);
		this.setMovePosZ(z);
		if(id==3){
			if(this.getNavigation()!=null)this.getNavigation().stop();
			if(this.getMoveType()==3){
				if(this.getRemain2()!=0){
					//this.setRemain2(0);
				}else{
					this.setRemain2(5);
				}
			}
		}else{
			this.setRemain2(0);
		}
	}
	
	public float defend = 0;
	int radtime = 0;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
		this.weaponidmax = 2;
		if(cheack){
			this.setWeaponId(1);
			cheack = false;
		}
		float bure = 1.0F;
		if(radtime<500)++radtime;
		if(this.getRemain2()==5&&!this.isPassenger()){
			if(this.getNavigation()!=null)this.getNavigation().stop();
			if(this.defend<1){
				this.defend+=0.1F;
			}else{
				//if(this.getRemain2()!=0)this.setRemain2(0);
				this.sit_aim = true;
			}
		}else{
			if(this.defend>0){
				this.defend-=0.1F;
			}else{
				this.sit_aim = false;
			}
		}
		
		if(this.getWeaponId()==1){//fsp
			this.needaim = false;
			this.changeWeaponId=2;
			this.mainWeaponId=1;
			this.magazine = 10;
			this.fire_tick = 30;
			this.w1cycle = 30;
			this.reload_time1 = 50;
		}
		if(this.getWeaponId()==2){//deploy
			if(this.sit_aim){
				bure = 0.6F;
				this.attack_range_max = 45;
			}else{
				this.attack_range_max = 40;
			}
			this.needaim = true;
			this.magazine = 1;
			this.fire_tick = 400;
			this.changeWeaponId=1;
			this.mainWeaponId=1;
			this.w1cycle = 400;
			this.reload_time1 = 50;
		}
		if(this.sit_aim){
			this.setWeaponId(2);
		}else{
			this.setWeaponId(1);
		}
		
		this.attack_height_max = this.attack_range_max;
		
		float moveSpeed = 0.20F;
		if(this.getRemain2()==1){
			moveSpeed = 0.1F;
			if(this.getBbHeight()!=0.5F)this.setSize(0.5F, 0.5F);
			this.height = 0.5F;
		}else if(this.sit_aim){
			moveSpeed = 0F;
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
		
		if(!this.sit_aim){
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
		
		float movesp = moveSpeed;
		this.moveway(this, movesp, this.attack_range_max);
		if(this.isInWater()|| this.isInLava())movesp = moveSpeed*3F;
		boolean isAttackVehicle = false;
		if(this.getTarget()!=null && this.isAttacking() && (this.getVehicle()==null||this.canfire)){
			if(/*!this.getChoose()*/this.getOwner()==null){
				if(radtime>450&&this.getMoveType()==1)this.setRemain2(5);
				if(this.getRemain2()==5 && radtime>20&&radtime<50){
					this.setRemain2(0);
				}
			}
			LivingEntity livingentity = this.getTarget();
			if(livingentity.getMaxHealth()>this.getMaxHealth()||livingentity.getVehicle()!=null||livingentity instanceof EntityWMVehicleBase)isAttackVehicle = true;
			if(livingentity.isAlive() && livingentity!=null && (this.aim_time>50||!this.needaim && this.aim_time>30)){
				if(this.getWeaponId()==1){
					if(this.cooltime > this.fire_tick){
						this.counter1 = true;
						this.cooltime = 0;
					}
					if(this.counter1 && this.getRemain1() > 0){
						this.setAnimFire(1);
						{
							this.playSound(SASoundEvent.fire_fsp.get(),3,1);
							weaponActive1();
						}
						this.setRemain1(this.getRemain1() - 1);
						this.guncyle = 0;
						this.gun_count1 = 0;
						this.counter1 = false;
					}		
				}else{
					if(this.radtime>460 &&!this.isPassenger()){
						this.playSound(SASoundEvent.deploy_fsp.get(),5,1);
						EntityRad rad = new EntityRad(WarMachineLib.ENTITY_RAD,this.level);
						rad.moveTo(this.getX(), this.getY(), this.getZ(), 0, 0.0F);
						rad.setAggressive(true);
						this.level.addFreshEntity(rad);
						this.radtime=0;
					}
				}

			}
		}
		}
	}

	public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F){//Living
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getTeam();
			Team team1 = entity1.getTeam();
			if(team != null && team1 != team && team1 != null){
				return true;
			}else if(entity instanceof IMob && ((LivingEntity) entity).getHealth() > 0.0F && (team == null||team != team1)){
				return true;
			}else{
				return false;
			}
    	}else{
			return false;
		}
	}

	LivingEntity lockTarget = null;
	public void weaponActive1(){
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = MathHelper.sqrt((this.fireposZ - this.firebaseZ)* (this.fireposZ - this.firebaseZ) + (this.fireposX - 0)*(this.fireposX - 0)) * MathHelper.sin(-this.xRot  * (1 * (float) Math.PI / 180));
		xx11 -= MathHelper.sin(this.yHeadRot * 0.01745329252F) * this.fireposZ;
		zz11 += MathHelper.cos(this.yHeadRot * 0.01745329252F) * this.fireposZ;
		xx11 -= MathHelper.sin(this.yHeadRot * 0.01745329252F + 1.57F) * fireposX;
		zz11 += MathHelper.cos(this.yHeadRot * 0.01745329252F + 1.57F) * fireposX;
		LivingEntity shooter = this;
		/*{
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("RadGun", null, this.getX()+xx11, this.getY()+this.fireposY+base, this.getZ()+zz11, 0, 0, 0, 2);
		}*/
		Vector3d locken = Vector3d.directionFromRotation(this.getRotationVector());
		float d = 120;
		int range = 3;
		int ix = 0;
		int iy = 0;
		int iz = 0;
		boolean stop = false;
		int pierce = 0;
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (int) (this.getX()+xx11 + locken.x * xxx);
			iy = (int) (this.getY()+this.fireposY+base + locken.y * xxx);
			iz = (int) (this.getZ()+zz11 + locken.z * xxx);
			BlockPos blockpos = new BlockPos(ix, iy, iz);
			BlockState iblockstate = this.level.getBlockState(blockpos);
			if (!iblockstate.isAir(this.level, blockpos)&& !iblockstate.getCollisionShape(this.level, blockpos).isEmpty()){
				break;
			}else{
				AxisAlignedBB axisalignedbb = (new AxisAlignedBB(ix-range, iy-range, iz-range, 
						ix+range, iy+range, iz+range)).inflate(1D);
				List<Entity> llist = this.level.getEntities(this,axisalignedbb);
				if (llist != null) {
					for (int lj = 0; lj < llist.size(); lj++) {
						Entity entity1 = (Entity) llist.get(lj);
						if (entity1 != null && entity1 instanceof LivingEntity) {
							if (NotFriend(entity1) && entity1 != shooter && entity1 != this) {
								lockTarget = (LivingEntity)entity1;
								if(lockTarget.getVehicle()!=null && lockTarget.getVehicle() instanceof LivingEntity){
									LivingEntity ve = (LivingEntity)lockTarget.getVehicle();
									ve.invulnerableTime = 0;
									ve.hurt(DamageSource.WITHER, 12+(0.01F*(ve.getMaxHealth()-ve.getHealth())));
									ve.hurt(DamageSource.IN_FIRE, 5);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(DamageSource.WITHER, 12+(0.01F*(lockTarget.getMaxHealth()-lockTarget.getHealth())));
									lockTarget.hurt(DamageSource.IN_FIRE, 5);
								}
								stop = true;
								break;
							}
						}
					}
				}
				if(stop){
					++pierce;
					if(pierce>2)break;
				}
			}
		}
		///if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("RadHit", null, ix, iy+0.5D, iz, 0, 0, 0, 2);
		MessageTrail messageBulletTrail = new MessageTrail(true, 2, "advancearmy:textures/entity/flash/rad_beam" ,this.getX()+xx11, this.getY()+this.fireposY-1.5F+base, this.getZ()+zz11, this.getDeltaMovement().x, this.getDeltaMovement().z, ix, iy-1, iz, 15F, 1);
		PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messageBulletTrail);
	}
}