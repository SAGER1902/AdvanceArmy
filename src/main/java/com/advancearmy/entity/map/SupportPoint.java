package advancearmy.entity.map;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.GroundPathHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.MobEntity;
import advancearmy.AdvanceArmy;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.item.Item;

import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraft.entity.CreatureEntity;

import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.BossInfo;

import wmlib.api.IEnemy;
import net.minecraft.entity.player.ServerPlayerEntity;
import wmlib.common.living.WeaponVehicleBase;

import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;

import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.entity.soldier.EntitySA_Conscript;
import advancearmy.entity.soldier.EntitySA_GI;

import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.sea.EntitySA_BattleShip;
import advancearmy.entity.land.EntitySA_FTK_H;
import advancearmy.entity.air.EntitySA_Fw020;
import advancearmy.entity.land.EntitySA_Ember;
import advancearmy.entity.air.EntitySA_YouHun;
import advancearmy.entity.air.EntitySA_Yw010;
import advancearmy.entity.air.EntitySA_F35;
import advancearmy.entity.air.EntitySA_A10a;
import net.minecraft.entity.passive.TameableEntity;
import wmlib.common.living.EntityWMVehicleBase;
import net.minecraft.entity.AgeableEntity;
import wmlib.api.ITool;
import wmlib.common.bullet.EntityMissile;
import wmlib.common.bullet.EntityShell;
import advancearmy.entity.soldier.EntitySA_Swun;

import advancearmy.event.SASoundEvent;
public class SupportPoint extends TameableEntity implements ITool{
	public SupportPoint(EntityType<? extends SupportPoint> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
	}
	public SupportPoint getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
	  return null;
	}
	/*
      PINK("pink", TextFormatting.RED),
      BLUE("blue", TextFormatting.BLUE),
      RED("red", TextFormatting.DARK_RED),
      GREEN("green", TextFormatting.GREEN),
      YELLOW("yellow", TextFormatting.YELLOW),
      PURPLE("purple", TextFormatting.DARK_BLUE),
      WHITE("white", TextFormatting.WHITE);
	*/
	//private final ServerBossInfo FriendCount = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS))/*.setDarkenScreen(true)*/;
	//private final ServerBossInfo EnemyCount = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS))/*.setDarkenScreen(true)*/;
	public SupportPoint(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_SPT, worldIn);
	}
	public void checkDespawn() {
	}
	private static final DataParameter<Integer> SummonID = EntityDataManager.<Integer>defineId(SupportPoint.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("SummonID", this.getSummonID());
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setSummonID(compound.getInt("SummonID"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(SummonID, Integer.valueOf(0));
	}
	public int getSummonID() {
	return ((this.entityData.get(SummonID)).intValue());
	}
	public void setSummonID(int stack) {
	this.entityData.set(SummonID, Integer.valueOf(stack));
	}
	
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		return ActionResultType.PASS;
    }

	public boolean isAttack = false;
	
	/*public void startSeenByPlayer(ServerPlayerEntity p_184178_1_) {
	  super.startSeenByPlayer(p_184178_1_);
	  if(isEnemyRespawn){
		  this.EnemyCount.addPlayer(p_184178_1_);
	  }else{
		  this.FriendCount.addPlayer(p_184178_1_);
	  }
	}
	public void stopSeenByPlayer(ServerPlayerEntity p_184203_1_) {
	  super.stopSeenByPlayer(p_184203_1_);
	  if(isEnemyRespawn){
			this.EnemyCount.removePlayer(p_184203_1_);
	  }else{
			this.FriendCount.removePlayer(p_184203_1_);
	  }
	}
	public void setCustomName(@Nullable ITextComponent p_200203_1_) {
		super.setCustomName(p_200203_1_);
		//this.EnemyCount.setName(this.getDisplayName());
		if(isEnemyRespawn){
			this.EnemyCount.setName(this.getDisplayName());
		}else{
			this.FriendCount.setName(this.getDisplayName());
		}
	}*/
	
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	
	/*protected void tickDeath() {

	}*/
	public boolean needaim = false;
	int count = 1;
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int staytime = 0;
	public int startTime = 50;
	public int summontime = 0;
	public int summon_ammol = 0;
	public int summon_count = 0;
	public int summon_cyc = 10;
	public void aiStep() {
    	if(this.setx == 0) {
    		this.setx=((int)this.getX());
    		this.sety=((int)this.getY());
    		this.setz=((int)this.getZ());
    	}
    	{
			BlockPos blockpos = new BlockPos(this.setx + 0.5,this.sety - 1,this.setz + 0.5);
			BlockState iblockstate = this.level.getBlockState(blockpos);
			if (this.setx != 0 && !iblockstate.isAir(this.level, blockpos)){
				this.moveTo(this.setx,this.sety,this.setz);
			}else{
				this.moveTo(this.setx,this.getY(), this.setz);
			}
    	}
		if (this.isAlive()){
			++summontime;
			++staytime;
			
			if(this.getSummonID() == 8){
				summon_cyc = 30;
				count = 4;
				isAttack = true;
			}else if(this.getSummonID() == 9){
				count = 4;
				isAttack = true;
			}else if(this.getSummonID() == 10){
				summon_cyc = 15;
				count = 20;
				isAttack = true;
			}
			if(this.getSummonID()>10 && this.getSummonID()<15){
				needaim = true;
				isAttack = true;
			}
			
			{
				if(summontime>startTime/* && this.getSummonID()>0*/){
					int ve = 0;
					if (!(this.level instanceof ServerWorld)) {
					}else{
						ServerWorld serverworld = (ServerWorld)this.level;
						LivingEntity caller = this;
						if(this.getOwner()!=null)caller = this.getOwner();
						++summon_ammol;
						if(summon_ammol>summon_cyc){
							if(this.getSummonID()==10){
								int rax = this.level.random.nextInt(50);
								int raz = this.level.random.nextInt(50);
								this.callShell(serverworld,caller, this.getX() - 25 + rax, this.getY() + 80+summon_count*5, this.getZ() - 25 + raz, this.getSummonID());
							}
							if(this.getSummonID()==15){
								int rax = this.level.random.nextInt(10);
								int raz = this.level.random.nextInt(10);
								this.callShell(serverworld,caller, this.getX() - 5 + rax, this.getY(), this.getZ() - 5 + raz, this.getSummonID());
							}
							this.callSupport(serverworld,caller,this.getX(),this.getY()+count*2,this.getZ(),80,this.getSummonID(), summon_count);
							++summon_count;
							summon_ammol = 0;
						}
						if(summon_count>=count)this.summontime = 0;
					}
				}
			}
			if(staytime>count*summon_cyc+startTime||needaim && summontime>startTime)this.remove();
		}
		super.aiStep();
    }
	public void callShell(World world1, LivingEntity caller, double ix, double iy, double iz, int id) {
		if(id==15){
			EntityMissile bullet = new EntityMissile(world1, caller, ix, iy-1, iz, caller);
			bullet.modid="advancearmy";
			bullet.fly_sound="advancearmy.missile_fly1";
			bullet.isRad = true;
			bullet.timemax=1000;
			bullet.power = 2000;
			bullet.setGravity(0.01F);
			bullet.setExLevel(35);
			bullet.hitEntitySound=SASoundEvent.nuclear_exp.get();
			bullet.hitBlockSound=SASoundEvent.nuclear_exp.get();
			bullet.setBulletType(8);
			bullet.setModel("advancearmy:textures/entity/bullet/nuclear_missile.obj");
			bullet.setTex("advancearmy:textures/entity/bullet/nuclear.png");
			bullet.setFX("BigMissileTrail");
			//bullet.flame = true;
			bullet.moveTo(caller.getX(), caller.getY()+100, caller.getZ(), caller.yRot, caller.xRot);
			bullet.shootFromRotation(caller, caller.xRot, caller.yRot, 0.0F, 7F, 1F);
			if (!world1.isClientSide) world1.addFreshEntity(bullet);
		}else{
			EntityShell shell = new EntityShell(world1, caller);
			shell.moveTo(ix, iy, iz, caller.yRot, caller.xRot);
			shell.modid="advancearmy";
			shell.fly_sound="advancearmy.shell_fly";
			shell.timemax=700;
			shell.power = 80;
			shell.setGravity(0.1F);
			shell.setExLevel(6);
			shell.hitEntitySound=SASoundEvent.artillery_impact.get();
			shell.hitBlockSound=SASoundEvent.artillery_impact.get();
			shell.setModel("advancearmy:textures/entity/bullet/bulletcannon.obj");
			shell.setTex("advancearmy:textures/entity/bullet/bullet.png");
			//shell.shootFromRotation(caller, caller.xRot, caller.yRot, 0.0F, 9F, 1F);
			if (!world1.isClientSide) world1.addFreshEntity(shell);
		}
	}
	
	public void callSupport(World world1, LivingEntity caller, double ix, double iy, double iz, double range, int id, int count) {
		if(caller != null){
			int height = 80;
			boolean isone = false;
			boolean have_driver = false;
			boolean center = false;
			
			WeaponVehicleBase attacker=null;
			WeaponVehicleBase trans=null;
			if(id == 1) {
				trans = new EntitySA_Yw010(AdvanceArmy.ENTITY_YW010, world1);
			}else if(id == 2){
				attacker = new EntitySA_Fw020(AdvanceArmy.ENTITY_FW020, world1);
				center = true;
				height = 1;
			}else if(id == 3){
				attacker = new EntitySA_YouHun(AdvanceArmy.ENTITY_YOUHUN, world1);
				center = true;
				height = 1;
			}else if(id == 4){
				attacker = new EntitySA_Ember(AdvanceArmy.ENTITY_EMBER, world1);
				center = true;
			}else if(id == 5){
				attacker = new EntitySA_FTK_H(AdvanceArmy.ENTITY_FTK_H, world1);
				center = true;
			}else if(id == 6){
				attacker = new EntitySA_A10a(AdvanceArmy.ENTITY_A10A, world1);
				have_driver = true;
				attacker.setArmyType2(250);
				isone = true;
			}else if(id == 7){
				attacker = new EntitySA_F35(AdvanceArmy.ENTITY_F35, world1);
				have_driver = true;
				attacker.setArmyType2(100);
				isone = true;
			}else if(id == 8){
				attacker = new EntitySA_A10a(AdvanceArmy.ENTITY_A10A, world1);
				have_driver = true;
				attacker.setArmyType2(250);
				isone = true;
			}else if(id == 9){
				attacker = new EntitySA_F35(AdvanceArmy.ENTITY_F35, world1);
				have_driver = true;
				attacker.setArmyType2(100);
			}else if(id == 16){
				attacker = new EntitySA_BattleShip(AdvanceArmy.ENTITY_BSHIP, world1);
				height = 1;
				center = true;
			}
			
			double xx11 = 0;
			double zz11 = 0;
			xx11 -= MathHelper.sin((caller.yRot+count*10) * 0.01745329252F - 1.57F) * range;
			zz11 += MathHelper.cos((caller.yRot+count*10) * 0.01745329252F - 1.57F) * range;
			
			double xx = ix - xx11;
			double yy = iy + height;
			double zz = iz - zz11;
			
			if(attacker!= null){
				attacker.setRemain1(attacker.magazine);
				attacker.setRemain2(attacker.magazine2);
				attacker.setRemain3(attacker.magazine3);
				attacker.setRemain4(attacker.magazine4);
				attacker.yRot = attacker.yHeadRot = attacker.yRotO = -((float) Math.atan2(ix - xx, iz - zz)) * 180.0F/ (float) Math.PI;
				
				if(center){
					if(height==1)this.playSound(SASoundEvent.csk.get(), 6.0F, 1.0F);
					attacker.moveTo(ix, iy+height, iz, attacker.yHeadRot, attacker.xRot);
				}else{
					attacker.moveTo(xx, yy, zz, attacker.yHeadRot, attacker.xRot);
					attacker.setTargetType(3);
					/*if(id == 2)*/{
						attacker.setMoveMode(5);
					}/*else{
						attacker.setAIType(1);
					}*/
				}
				if(caller instanceof PlayerEntity){
					PlayerEntity owner = (PlayerEntity)caller;
					attacker.tame(owner);
				}
				//attacker.setcanDespawn(1);
				//if(caller.getTeam()!=null)attacker.world.getScoreboard().addPlayerToTeam(var9.getCachedUniqueIdString(), caller.getTeam().getName());
				if(!world1.isClientSide){
					world1.addFreshEntity(attacker);
					if (have_driver){//
						EntitySA_Soldier pilot = new EntitySA_Soldier(AdvanceArmy.ENTITY_SOLDIER,world1);
						/*if(caller instanceof PlayerEntity){
							PlayerEntity owner = (PlayerEntity)caller;
							pilot.tame(owner);
						}*/
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						pilot.fastRid=true;
						world1.addFreshEntity(pilot);
						attacker.catchPassenger(pilot);
					}
				}	
			}
			if(trans!= null){
				//trans.fri = caller;
				trans.yRot = trans.yHeadRot = trans.yRotO = -((float) Math.atan2(ix - xx, iz - zz)) * 180.0F/ (float) Math.PI;
				trans.moveTo(xx, yy, zz, trans.yHeadRot, trans.xRot);
				//trans.setMoveMode(id);
				trans.setMoveType(5);
				trans.setMovePosX((int)ix);
				trans.setMovePosY((int)iy);
				trans.setMovePosZ((int)iz);
				//trans.setcanDespawn(1);
				if(caller instanceof PlayerEntity){
					PlayerEntity owner = (PlayerEntity)caller;
					trans.tame(owner);
				}
				if (!world1.isClientSide) {
					world1.addFreshEntity(trans);
					
					trans.movePower=trans.throttleMax;
					trans.throttle=trans.throttleMax;
					
					for(int k2 = 0; k2 < trans.seatMaxCount; ++k2){
						EntitySA_Swun pilot = new EntitySA_Swun(AdvanceArmy.ENTITY_SWUN,world1);
						if(caller instanceof PlayerEntity){
							PlayerEntity owner = (PlayerEntity)caller;
							pilot.tame(owner);
						}
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						pilot.fastRid=true;
						world1.addFreshEntity(pilot);
						trans.catchPassenger(pilot);
					}
					
					//pilot.startRiding(attacker);
					/*if (id < 4){//
						EntitySA_Soldier pilot = new EntitySA_Soldier(AdvanceArmy.ENTITY_SOLDIER,world1);
						if(caller instanceof PlayerEntity){
							PlayerEntity owner = (PlayerEntity)caller;
							pilot.tame(owner);
						}
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						//pilot.setcanDespawn(1);
						world1.addFreshEntity(pilot);
						pilot.startRiding(trans);
					}else
					if (id == 5){//
						EntitySA_GI pilot = new EntitySA_GI(AdvanceArmy.ENTITY_GI,world1);
						if(caller instanceof PlayerEntity){
							PlayerEntity owner = (PlayerEntity)caller;
							pilot.tame(owner);
						}
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						//pilot.setcanDespawn(1);
						world1.addFreshEntity(pilot);
						pilot.startRiding(trans);
					}else
					if (id == 6){//
						EntitySA_Conscript pilot = new EntitySA_Conscript(AdvanceArmy.ENTITY_CONS,world1);
						if(caller instanceof PlayerEntity){
							PlayerEntity owner = (PlayerEntity)caller;
							pilot.tame(owner);
						}
						pilot.moveTo(xx, yy, zz, 0, 0.0F);
						pilot.setMoveType(1);
						//pilot.setcanDespawn(1);
						world1.addFreshEntity(pilot);
						pilot.startRiding(trans);
					}*/
				}
			}
		}
	}
}
