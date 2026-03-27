package advancearmy.entity;

import java.util.List;

import advancearmy.AdvanceArmy;

import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;

import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import wmlib.common.living.EntityWMVehicleBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.entity.AgeableEntity;
import java.util.UUID;

public abstract class EntitySA_SoldierBase extends TameableEntity{
	public EntitySA_SoldierBase(EntityType<? extends EntitySA_SoldierBase> sodier, World worldIn) {
		super(sodier, worldIn);
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 1.8F;
	}
	
	public double getMountedYOffset() {
		return 0.6D;//0.12D
	}
	
	protected void registerGoals() {
	}
	
	public Team getTeam() {
	  if (this.getOwner()!=null) {
		 LivingEntity livingentity = this.getOwner();
		 if (livingentity != null) {
			return livingentity.getTeam();
		 }
	  }
	  return super.getTeam();
	}
	
	public void setWeapon(int stack) {
	}
	
	public EntitySA_SoldierBase getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
	  return null;
	}
	
	public static AttributeModifierMap.MutableAttribute createMonsterAttributes() {
		return MobEntity.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D);
	}
	private static final DataParameter<Integer> tc = EntityDataManager.<Integer>defineId(EntitySA_SoldierBase.class, DataSerializers.INT);
	
    private static final DataParameter<Integer> remain_r = 
    		EntityDataManager.<Integer>defineId(EntitySA_SoldierBase.class, DataSerializers.INT);
    private static final DataParameter<Integer> remain_l = 
    		EntityDataManager.<Integer>defineId(EntitySA_SoldierBase.class, DataSerializers.INT);
	/*private static final DataParameter<Boolean> isattack = 
    		EntityDataManager.<Boolean>defineId(EntitySA_SoldierBase.class, DataSerializers.BOOLEAN);*/
	private static final DataParameter<Boolean> choose = 
    		EntityDataManager.<Boolean>defineId(EntitySA_SoldierBase.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> MoveType = EntityDataManager.<Integer>defineId(EntitySA_SoldierBase.class, DataSerializers.INT);
	private static final DataParameter<Integer> MovePosX = EntityDataManager.<Integer>defineId(EntitySA_SoldierBase.class, DataSerializers.INT);
	private static final DataParameter<Integer> MovePosY = EntityDataManager.<Integer>defineId(EntitySA_SoldierBase.class, DataSerializers.INT);
	private static final DataParameter<Integer> MovePosZ = EntityDataManager.<Integer>defineId(EntitySA_SoldierBase.class, DataSerializers.INT);

	public int cooltime;
	public int cooltime2;
	public int reload1 = 0;
	public int reload_time1;
	public int special_cool = 0;
	public float turretYaw;
	public float turretPitch;
	public float rotation_max = 60;
	public float turretPitchMax = -90;
	public float turretPitchMin = 90;
	
	public float attack_range_max = 35;
	public float attack_range_min = 0;
	public float attack_height_max = 20;
	public float attack_height_min = -20;

	public int magazine = 5;
	public boolean counter1 = false;
	public boolean counter2 = false;

	public int countlimit1 = 0;
	
	public LivingEntity targetentity = null;

	public int startTime = 0;
	public float rote =0;
	public float yaw =0;
	public float throttle;//
	public float throttleRight;//
	public float throttleLeft;//
	public float thpera;
	public boolean sneak_aim = false;//
	
	public int anim1 = 0;
	public int anim2 = 0;

	public float cooltime5 = 0;
	public float cooltime6 = 0;
	
	
	
	
	public int find_time = 0;	 
	int clear_time = 0;
	
	public int aim_time = 0;
	public void tick() {
		super.tick();
		if(this.isAttacking()){
			if(aim_time<100)++aim_time;
		}else{
			if(aim_time>0)--aim_time;
		}
		if(this.anim1<25)++this.anim1;
		if(this.anim2<25)++this.anim2;
	}
    public boolean CanAttack(Entity entity){
    	return false;
    }
	
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		{
			compound.putInt("MoveType", this.getMoveType());
			compound.putInt("MovePosX", this.getMovePosX());
			compound.putInt("MovePosY", this.getMovePosY());
			compound.putInt("MovePosZ", this.getMovePosZ());
			compound.putInt("remain_r", this.getRemain2());
			compound.putInt("remain_l", this.getRemain1());
			//compound.putBoolean("isattack", this.isAttacking());
			compound.putBoolean("choose", this.getChoose());
			compound.putInt("tc", getTeamC());
		}
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
		{
			this.setMoveType(compound.getInt("MoveType"));
			this.setMovePosX(compound.getInt("MovePosX"));
			this.setMovePosY(compound.getInt("MovePosY"));
			this.setMovePosZ(compound.getInt("MovePosZ"));
			this.setRemain2(compound.getInt("remain_r"));
			this.setRemain1(compound.getInt("remain_l"));
			//this.setAttacking(compound.getBoolean("isattack"));
			this.setChoose(compound.getBoolean("choose"));
			this.setTeamC(compound.getInt("tc"));
		}
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(MoveType, Integer.valueOf(0));
		this.entityData.define(MovePosX, Integer.valueOf(0));
		this.entityData.define(MovePosY, Integer.valueOf(0));
		this.entityData.define(MovePosZ, Integer.valueOf(0));
		this.entityData.define(remain_r, Integer.valueOf(0));
		this.entityData.define(remain_l, Integer.valueOf(0));
		//this.entityData.define(isattack, Boolean.valueOf(false));
		this.entityData.define(choose, Boolean.valueOf(false));
		this.entityData.define(tc, Integer.valueOf(0));
	}
	
	public int getTeamC() {
		return ((this.entityData.get(tc)).intValue());
	}
	public void setTeamC(int stack) {
		this.entityData.set(tc, Integer.valueOf(stack));
	}
	
	public int getMoveType() {
			return ((this.entityData.get(MoveType)).intValue());
	}
	public void setMoveType(int stack) {
		this.entityData.set(MoveType, Integer.valueOf(stack));
	}
	public int getMovePosX() {
		return ((this.entityData.get(MovePosX)).intValue());
	}
	public void setMovePosX(int stack) {
	this.entityData.set(MovePosX, Integer.valueOf(stack));
	}
	public int getMovePosY() {
	return ((this.entityData.get(MovePosY)).intValue());
	}
	public void setMovePosY(int stack) {
	this.entityData.set(MovePosY, Integer.valueOf(stack));
	}
	public int getMovePosZ() {
	return ((this.entityData.get(MovePosZ)).intValue());
	}
	public void setMovePosZ(int stack) {
	this.entityData.set(MovePosZ, Integer.valueOf(stack));
	}
	public int getRemain2() {
		return ((this.entityData.get(remain_r)).intValue());
	}
	public void setRemain2(int stack) {
		this.entityData.set(remain_r, Integer.valueOf(stack));
	}
	public int getRemain1() {
		return ((this.entityData.get(remain_l)).intValue());
	}
	public void setRemain1(int stack) {
		this.entityData.set(remain_l, Integer.valueOf(stack));
	}
	public boolean isAttacking() {
		//return ((this.entityData.get(isattack)).booleanValue());
		return this.isAggressive();
	}
	public void setAttacking(boolean stack) {
		//this.entityData.set(isattack, Boolean.valueOf(stack));
        this.setAggressive(stack);
	}
	public boolean getChoose() {
		return ((this.entityData.get(choose)).booleanValue());
	}
	public void setChoose(boolean stack) {
		this.entityData.set(choose, Boolean.valueOf(stack));
	}
}