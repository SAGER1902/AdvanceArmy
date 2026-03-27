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
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.EntityPredicate;
import java.util.function.Predicate;

import net.minecraftforge.fml.ModList;
import net.minecraft.entity.passive.TameableEntity;
import com.hungteen.pvz.common.entity.zombie.PVZZombieEntity;
import wmlib.common.living.EntityWMSeat;

import advancearmy.entity.EntitySA_SquadBase;
import advancearmy.entity.EntitySA_Seat;
import advancearmy.entity.ai.SoldierAttackableTargetGoalSA;
import wmlib.common.living.ai.LivingLockGoal;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import wmlib.common.living.EntityWMVehicleBase;
public class EntitySA_GAT extends EntitySA_SquadBase{
	public EntitySA_GAT(EntityType<? extends EntitySA_GAT> sodier, World worldIn) {
		super(sodier, worldIn);
		this.unittex = new ResourceLocation("advancearmy:textures/item/item_spawn_minigunner.png");
		this.attack_height_max = 110;
	}
	public EntitySA_GAT(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_GAT, worldIn);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		//this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D, 1.0000001E-5F));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(6, new LivingLockGoal(this, 1.0D, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new SoldierAttackableTargetGoalSA<>(this, MobEntity.class, 10, 115F, true, false, (attackentity) -> {
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
			boolean can = false;
			double ddy = Math.abs(entity.getY()-this.getY());
			if(ddy>15){
				can = true;
			}else{
				if(this.distanceTo(entity)<=45){
					can = true;
				}
			}
			if(can){
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
					}else{
						return false;
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
	
    protected SoundEvent getAmbientSound()
    {
        return SASoundEvent.gt_say.get();
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SASoundEvent.apa_hurt.get();
    }

    protected SoundEvent getDeathSound()
    {
        return SASoundEvent.apa_die.get();
    }
	
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(this.getSame())par2 = par2*0.5F;
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
							if(this.sit_aim)par2 = par2*0.8F;//
							return super.hurt(source, par2);
						}
					}else{
						if(this.distanceToSqr(entity)>4D && flag){
							if(this.groundtime>50){
								this.setRemain2(3);
								this.setTarget(entity1);
								this.groundtime = 0;
							}
						}
						if(this.getRemain2()==1)par2 = par2*0.4F;//
						if(this.sit_aim)par2 = par2*0.8F;//
						return super.hurt(source, par2);
					}
				}
			}else{
				if(this.getRemain2()==1)par2 = par2*0.4F;//
				if(this.sit_aim)par2 = par2*0.8F;//
				return super.hurt(source, par2);
			}
		}else {
			if(this.getRemain2()==1)par2 = par2*0.4F;//
			if(this.sit_aim)par2 = par2*0.8F;//
			return super.hurt(source, par2);
		}
    }
	
    private static final DataParameter<Boolean> same = 
    		EntityDataManager.<Boolean>defineId(EntitySA_GAT.class, DataSerializers.BOOLEAN);
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		{
			compound.putBoolean("same", getSame());
		}
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
		{
			this.setSame(compound.getBoolean("same"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(same, Boolean.valueOf(false));
	}
	public boolean getSame() {
		return ((this.entityData.get(same)).booleanValue());
	}
	public void setSame(boolean stack) {
		this.entityData.set(same, Boolean.valueOf(stack));
	}
	
	public float defend = 0;
	public boolean isSame(Entity ent){
		if(ent instanceof EntitySA_GAT && (this.getTeam()!=null && this.getTeam()==ent.getTeam())){
			return true;
		}else{
			return false;
		}
	}
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
		int count=0;
		List<Entity> entities = this.level.getEntities(this, this.getBoundingBox().inflate(18D, 18.0D, 18D));
		for (Entity target : entities) {
			if(this.isSame(target))++count;
		}
		if(count>2){
			this.setSame(true);
		}else{
			this.setSame(false);
		}
		float moveSpeed = 0.4F;
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
		
		{
			this.attack_range_max = 45;
			this.needaim = false;
			this.magazine = 300;
			this.w1cycle = 2;
			this.reload_time1 = 60;
			this.setWeapon(0, 0, "advancearmy:textures/entity/bullet/bullet.obj", "advancearmy:textures/entity/bullet/bullet.png",
			"SmokeGun", null, SASoundEvent.fire_gat.get(), 0.3F,1.1F,2.5F,0,0,
			7, 7F, 2F, 0, false, 1, 0.01F, 20, 0);
		}
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
			if(this.getMoveType()==3 && this.move_type!=0)this.move_type=0;
			LivingEntity livingentity = this.getTarget();
			if(livingentity.getMaxHealth()>this.getMaxHealth()||livingentity.getVehicle()!=null||livingentity instanceof EntityWMVehicleBase)isAttackVehicle = true;
			if(this.isAttacking() && this.find_time<40 && this.getRemain1()>0){
				++this.find_time;
			}
			if(this.getMoveType()!=3){
				if(this.level.random.nextInt(6) >= 3 && this.find_time > 20){
					this.setRemain2(2);
					this.find_time = 0;
				}else if(this.level.random.nextInt(6) < 3 && this.find_time > 20){
					this.setRemain2(0);
				}
			}

			if(livingentity.isAlive() && livingentity!=null && (this.aim_time>15)){
				if(this.cooltime >= this.ammo1 && this.cooltime > 2){
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
					if(this.countlimit1>(1+this.level.random.nextInt(8))){
						this.counter1 = false;
						this.countlimit1 = 0;
					}
				}
			}
		}
		}
	}
}