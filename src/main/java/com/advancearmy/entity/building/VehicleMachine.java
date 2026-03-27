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
import net.minecraft.util.math.AxisAlignedBB;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.api.IHealthBar;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.passive.TameableEntity;
import advancearmy.event.SASoundEvent;
import net.minecraft.entity.item.ItemEntity;
import wmlib.api.IBuilding;

import net.minecraft.item.PickaxeItem;
public class VehicleMachine extends TameableEntity implements IHealthBar, IBuilding{
	public VehicleMachine(EntityType<? extends VehicleMachine> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
	}
	public VehicleMachine(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_VMAC, worldIn);
	}
	public void checkDespawn() {
	}
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
	public VehicleMachine getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
	  return null;
	}
   
	private static final DataParameter<Integer> VehicleC = EntityDataManager.<Integer>defineId(VehicleMachine.class, DataSerializers.INT);
	private static final DataParameter<Integer> Money = EntityDataManager.<Integer>defineId(VehicleMachine.class, DataSerializers.INT);
	private static final DataParameter<Integer> Build = EntityDataManager.<Integer>defineId(VehicleMachine.class, DataSerializers.INT);
	private static final DataParameter<Integer> YawRote = EntityDataManager.<Integer>defineId(VehicleMachine.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("VehicleC", this.getVehicleC());
		compound.putInt("Money", this.getMoney());
		compound.putInt("Build", this.getBuild());
		compound.putInt("YawRote", this.getYawRoteNBT());
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setVehicleC(compound.getInt("VehicleC"));
	   this.setMoney(compound.getInt("Money"));
	   this.setBuild(compound.getInt("Build"));
	   this.setYawRoteNBT(compound.getInt("YawRote"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(VehicleC, Integer.valueOf(0));
		this.entityData.define(Money, Integer.valueOf(0));
		this.entityData.define(Build, Integer.valueOf(0));
		this.entityData.define(YawRote, Integer.valueOf(0));
	}
	public int getYawRoteNBT() {
		return ((this.entityData.get(YawRote)).intValue());
	}
	public void setYawRoteNBT(int stack) {
		this.entityData.set(YawRote, Integer.valueOf(stack));
	}
	
	public int getVehicleC() {
	return ((this.entityData.get(VehicleC)).intValue());
	}
	public void setVehicleC(int stack) {
	this.entityData.set(VehicleC, Integer.valueOf(stack));
	}
	public int getMoney() {
	return ((this.entityData.get(Money)).intValue());
	}
	public void setMoney(int stack) {
	this.entityData.set(Money, Integer.valueOf(stack));
	}
	public int getBuild() {
	return ((this.entityData.get(Build)).intValue());
	}
	public void setBuild(int stack) {
	this.entityData.set(Build, Integer.valueOf(stack));
	}
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		if(player.isCreative()||this.getOwner()==player){
			ItemStack heldItem = player.getItemInHand(hand);
			ItemStack this_heldItem = this.getMainHandItem();
			Item item = heldItem.getItem();
			{
				if(heldItem.getItem() instanceof PickaxeItem){
					if(player.isCrouching()){
						if(!this.level.isClientSide && this.getHealth()>=this.getMaxHealth()){
							this.dropItemStack(new ItemStack(AdvanceArmy.vehicle_machine));
							if(!this_heldItem.isEmpty()&&this_heldItem!=null)this.dropItemStack(this_heldItem);
							int amount = this.getMoney() / 100;
							if(amount>0){
								for(int i=0; i<amount; ++i){
									this.dropItemStack(new ItemStack(Items.GOLD_INGOT));
								}
							}
							player.sendMessage(new TranslationTextComponent("Recycle", new Object[0]), player.getUUID());
							this.remove();
							return ActionResultType.SUCCESS;
						}
					}else{
						if(this.getYawRoteNBT()<360){
							this.setYawRoteNBT(this.getYawRoteNBT()+90);
						}else{
							this.setYawRoteNBT(0);
						}
						return ActionResultType.SUCCESS;
					}
				}
				if(!this_heldItem.isEmpty()&&this_heldItem!=null){
					if(item == Items.GOLD_INGOT){
						this.setMoney(this.getMoney()+100);
						if(!heldItem.isEmpty())heldItem.shrink(1);
						return ActionResultType.SUCCESS;
					}else if(item == Items.DIAMOND){
						this.setMoney(this.getMoney()+300);
						if(!heldItem.isEmpty())heldItem.shrink(1);
						return ActionResultType.SUCCESS;
					}else if(item == Items.EMERALD){
						this.setMoney(this.getMoney()+200);
						if(!heldItem.isEmpty())heldItem.shrink(1);
						return ActionResultType.SUCCESS;
					}else{
						player.sendMessage(new TranslationTextComponent("------", new Object[0]), player.getUUID());
						player.sendMessage(new TranslationTextComponent("Count="+this.getVehicleC(), new Object[0]), player.getUUID());
						player.sendMessage(new TranslationTextComponent("======", new Object[0]), player.getUUID());
						if(player.isCrouching()){
							if(this.getVehicleC()>0)this.setVehicleC(this.getVehicleC()-1);
							return ActionResultType.SUCCESS;
						}else{
							this.setVehicleC(this.getVehicleC()+1);
							return ActionResultType.SUCCESS;
						}
					}
				}else{
					 if(item instanceof ItemSpawn){
						ItemSpawn vehicleitem = (ItemSpawn)item;
						if(this.getVehicleC()==0){
							if(vehicleitem.type == 0){
								{
									if(!this_heldItem.isEmpty()&&this_heldItem!=null)this.dropItemStack(this_heldItem);
								}
								if(!this.level.isClientSide)this.setItemSlot(EquipmentSlotType.MAINHAND, heldItem.copy());
								heldItem.shrink(1);
								return ActionResultType.SUCCESS;
							}else{
								player.sendMessage(new TranslationTextComponent("advancearmy.interact.machinev.desc", new Object[0]), player.getUUID());
								return ActionResultType.PASS;
							}
						}else{
							player.sendMessage(new TranslationTextComponent("advancearmy.interact.machineadd.desc", new Object[0]), player.getUUID());
							return ActionResultType.PASS;
						}
					}else{
						player.sendMessage(new TranslationTextComponent("advancearmy.interact.machinev.desc", new Object[0]), player.getUUID());
						return ActionResultType.PASS;
					}
				}
			}
		}
		return super.mobInteract(player, hand);
    }
	private void dropItemStack(ItemStack item) {
	  ItemEntity itementity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), item);
	  this.level.addFreshEntity(itementity);
	}
	public boolean isCover = false;
	
	/*public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}*/
	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if (entity instanceof LivingEntity) {
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
	PlayerEntity player = null;
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int total_count = 200;
	public int finish_time = 20;
	public float cooltime6 = 0;
	
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
	
	public void aiStep() {
		if(this.hurtTime>0){
			if(showbartime<1)showbartime = 70;
		}
		if(showbartime>0)--showbartime;
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
			if(this.getHealth() < this.getMaxHealth() && this.getHealth() > 0.0F) {
				++cooltime6;
				if(cooltime6 > 60){
					this.setHealth(this.getHealth() + 1);
					cooltime6=0;
				}
			}
			if(this.getOwner()!=null && this.getOwner() instanceof PlayerEntity)player=(PlayerEntity)this.getOwner();
			if(this.getMainHandItem()!=null){
				ItemStack this_heldItem = this.getMainHandItem();
				Item item = this_heldItem.getItem();
				if(item instanceof ItemSpawn){
					ItemSpawn vehicleitem = (ItemSpawn)item;
					this.finish_time = vehicleitem.xp;
					double xx11 = 0;
					double zz11 = 0;
					xx11 -= Math.sin(this.getYawRoteNBT() * 0.01745329252F) * -6;
					zz11 += Math.cos(this.getYawRoteNBT() * 0.01745329252F) * 6;
					/*xx11 -= Math.sin(this.getYawRoteNBT() * 0.01745329252F + 1.57F) * -3;
					zz11 += Math.cos(this.getYawRoteNBT() * 0.01745329252F + 1.57F) * -3;*/
					double i1 = this.getX()+xx11;
					double j1 = this.getY();
					double k1 = this.getZ()+zz11;
					int range = 2;
					boolean covered = false;
					AxisAlignedBB axisalignedbb = (new AxisAlignedBB(i1-range, j1-range, k1-range, i1+range, j1+range, k1+range)).inflate(1D);
					List<Entity> list = this.level.getEntities(this, axisalignedbb);
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity ent = list.get(k2);
						{
							if(ent != null && ent instanceof LivingEntity){
								covered= true;
							}
						}
					}
					this.isCover = covered;
					if(!this.isCover){
						if(this.getVehicleC()>0){
							/*if(player != null && this.isOwner(player))*/{
								if(this.getMoney()>0){
									if(this.getBuild() < this.finish_time){
										this.setBuild(this.getBuild()+2);
										this.setMoney(this.getMoney()-2);
										if(this.getBuild()%10==0)this.playSound(SASoundEvent.fix.get(), 2.0F, 1.0F);
									}
								}
							}
						}
						if(this.getMoney()>0){
							if(this.getBuild() >= this.finish_time){
								if (!(this.level instanceof ServerWorld)) {
								} else {
									ServerWorld serverworld = (ServerWorld)this.level;
									vehicleitem.spawnCreature(serverworld, player, 0, true, (double)i1, (double)j1, (double)k1, 0);
								}
								this.setBuild(0);
								this.setVehicleC(this.getVehicleC() - 1);
							}
						}
					}
				}
			}
		}
		super.aiStep();
    }
}
