package advancearmy.entity.building;

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
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import net.minecraft.item.ArmorItem;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.pathfinding.GroundPathNavigator;

import net.minecraftforge.fml.ModList;
import net.minecraft.entity.CreatureEntity;

import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.BossInfo;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import net.minecraft.entity.player.ServerPlayerEntity;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.EntitySA_HeliBase;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.item.ItemSpawn;
import net.minecraft.entity.passive.TameableEntity;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.api.ITool;
import net.minecraft.entity.AgeableEntity;
import wmlib.api.IBuilding;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.PickaxeItem;
public class SandBag extends TameableEntity implements ITool,IBuilding{
	public SandBag(EntityType<? extends SandBag> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  //this.noCulling = true;
	}
	public SandBag(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_SANDBAG, worldIn);
	}
	public void checkDespawn() {
	}
	public boolean canBeCollidedWith() {//
		return !this.removed;
	}
	
	public float boxheight = 0.9F;
	public float boxwidth = 1F;
	public EntitySize getDimensions(Pose pos) {
		EntitySize entitysize = super.getDimensions(pos);
		if(this.getHealth()>60){
			this.boxheight=1F;
		}else if(this.getHealth()>30&&this.getHealth()<=60){
			this.boxheight=0.7F;
		}else if(this.getHealth()<=30){
			this.boxheight=0.4F;
		}
		if(this.boxwidth!=0&&this.boxheight!=0){
			return entitysize.scale(boxwidth,boxheight);
		}else{
			return entitysize;
		}
	}
	private void updateSizeInfo() {
	  this.refreshDimensions();
	}
	public void onSyncedDataUpdated(DataParameter<?> nbt) {
		if(this.boxwidth!=0&&this.boxheight!=0){
			this.updateSizeInfo();
		}
		super.onSyncedDataUpdated(nbt);
	}
	
	private static final DataParameter<Integer> YawRote = EntityDataManager.<Integer>defineId(SandBag.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("YawRote", this.getYawRote());
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setYawRote(compound.getInt("YawRote"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(YawRote, Integer.valueOf(0));
	}
	public int getYawRote() {
	return ((this.entityData.get(YawRote)).intValue());
	}
	public void setYawRote(int stack) {
	this.entityData.set(YawRote, Integer.valueOf(stack));
	}
	public VehicleMachine getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
	  return null;
	}
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(par2>25){
		}else{
			par2 = par2*0.1F;
		}
		if(entity != null){
			if(entity instanceof LivingEntity){
				LivingEntity attacker = (LivingEntity)entity;
				ItemStack heldItem = attacker.getMainHandItem();
				if(heldItem.getItem() instanceof PickaxeItem &&this.getHealth()<this.getMaxHealth()){
					this.setHealth(this.getHealth()+1+par2);
					this.playSound(SoundEvents.ANVIL_USE, 2.0F, 1.0F);
					heldItem.hurtAndBreak(1, attacker, (ent) -> {
					ent.broadcastBreakEvent(attacker.getUsedItemHand());});
					//par2=0;
					return false;
				}
				if(this.getOwner()==entity||this.getVehicle()==entity||this.getTeam()==entity.getTeam()&&this.getTeam()!=null){
					return false;
				}else{
					if(entity instanceof TameableEntity){
						TameableEntity soldier = (TameableEntity)entity;
						if(this.getOwner()!=null && this.getOwner()==soldier.getOwner()){
							return false;
						}else{
							return super.hurt(source, par2);
						}
					}else{
						return super.hurt(source, par2);
					}
				}
			}else{
				return super.hurt(source, par2);
			}
		}else {
			return super.hurt(source, par2);
		}
    }
	
	private void dropItemStack(ItemStack item) {
	  ItemEntity itementity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), item);
	  this.level.addFreshEntity(itementity);
	}
	
	int ridcool = 0;
	LivingEntity deployer = null;
	public boolean can_hand_deploy = true;
	public boolean isDeploying = false;
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ItemStack heldItem = player.getMainHandItem();
		if(player.isCrouching() && this.getOwner()==player && heldItem.getItem() instanceof PickaxeItem &&this.getHealth()>=this.getMaxHealth()){
			if(!this.level.isClientSide){
				if(this.getHealth()>60){
					this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_sandbag3));
				}else if(this.getHealth()>30&&this.getHealth()<=60){
					this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_sandbag2));
				}else if(this.getHealth()<=30){
					this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_sandbag));
				}
				this.remove();
				player.sendMessage(new TranslationTextComponent("Recycle", new Object[0]), player.getUUID());
				return ActionResultType.SUCCESS;
			}
		}
		if(can_hand_deploy){
			if(player.isCrouching() && this.getOwner()==player && player.getVehicle()==null){
				if(!isDeploying){
					isDeploying=true;
					deployer=player;
					ridcool = 20;
				}
				return ActionResultType.PASS;
			}else{
				if(isDeploying||deployer!=null){
					return ActionResultType.PASS;
				}else{
					return super.mobInteract(player, hand);
				}
			}
		}else{
			return super.mobInteract(player, hand);
		}
    }
	
	/*public int setx = 0;
	public int sety = 0;
	public int setz = 0;*/
	public void aiStep() {
		if(ridcool>0)--ridcool;
		
		if(can_hand_deploy){
			if(deployer!=null && isDeploying){
				deployer.setDeltaMovement(deployer.getDeltaMovement().x * 0.5F, deployer.getDeltaMovement().y, deployer.getDeltaMovement().z * 0.5F);
				float f1 = deployer.yRot * (2 * (float) Math.PI / 360);
				double ix = 0;
				double iz = 0;
				ix -= MathHelper.sin(f1) * 1.5F;
				iz += MathHelper.cos(f1) * 1.5F;
				this.setPos(deployer.getX() + ix, deployer.getY()+0.5F, deployer.getZ() + iz);
				this.setYawRote((90-(int)deployer.yHeadRot));
				if(ridcool==0 && deployer.isCrouching()||deployer.getHealth()==0){
					isDeploying=false;
					deployer=null;
				}
			}
		}
		if(deployer!=null && isDeploying){
			
		}else{
			this.setDeltaMovement(this.getDeltaMovement().add(-this.getDeltaMovement().x*0.5F, -0.05D, -this.getDeltaMovement().z*0.5F));
		}
		this.move(MoverType.SELF, this.getDeltaMovement());

    	/*if(this.setx == 0) {
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
    	}*/
   }
}
