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
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import net.minecraft.entity.player.ServerPlayerEntity;
import wmlib.common.living.WeaponVehicleBase;

import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;

import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.mob.EntityMobSoldierBase;
import wmlib.api.ITool;

import net.minecraft.item.PickaxeItem;
public class ArmyMovePoint extends MobEntity implements ITool{
	public ArmyMovePoint(EntityType<? extends ArmyMovePoint> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	}
	public ArmyMovePoint(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_MOVEP, worldIn);
	}
	public void checkDespawn() {
	}
	private static final DataParameter<Integer> MoveId = EntityDataManager.<Integer>defineId(ArmyMovePoint.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("MoveId", this.getMoveId());
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setMoveId(compound.getInt("MoveId"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(MoveId, Integer.valueOf(0));
	}
	public int getMoveId() {
	return ((this.entityData.get(MoveId)).intValue());
	}
	public void setMoveId(int stack) {
	this.entityData.set(MoveId, Integer.valueOf(stack));
	}
	
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		if(player.isCreative()){
			ItemStack heldItem = player.getItemInHand(hand);
			Item item = heldItem.getItem();

			if(!heldItem.isEmpty()){
				if(heldItem.getItem() instanceof PickaxeItem && player.isCrouching()){
					if(!this.level.isClientSide){
						this.remove();
						player.sendMessage(new TranslationTextComponent("Remove", new Object[0]), player.getUUID());
						return ActionResultType.SUCCESS;
					}
				}else
				if(!this.level.isClientSide){
					if(item == Items.GOLD_INGOT){
						if(isEnemyPoint){
							this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND));
							player.sendMessage(new TranslationTextComponent("set Friend Type", new Object[0]), player.getUUID());
							return ActionResultType.SUCCESS;
						}else{
							this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_INGOT));
							player.sendMessage(new TranslationTextComponent("set Enemy Type", new Object[0]), player.getUUID());
							return ActionResultType.SUCCESS;
						}
					}else if(item == Items.GOLDEN_SWORD){
						if(this.pointType==3){
							this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.IRON_SWORD));
							player.sendMessage(new TranslationTextComponent("set Land Vehicle Type", new Object[0]), player.getUUID());
							return ActionResultType.SUCCESS;
						}else if(this.pointType==2){
							this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.GOLDEN_SWORD));
							player.sendMessage(new TranslationTextComponent("set Heli Type", new Object[0]), player.getUUID());
							return ActionResultType.SUCCESS;
						}else{
							this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.DIAMOND_SWORD));
							player.sendMessage(new TranslationTextComponent("set Plane Type", new Object[0]), player.getUUID());
							return ActionResultType.SUCCESS;
						}
					}
				}else{
					return ActionResultType.PASS;
				}
			}else{
				if(player.isCrouching()){
					if(this.getMoveId()>0)this.setMoveId(this.getMoveId()-1);
					return ActionResultType.SUCCESS;
				}else{
					this.setMoveId(this.getMoveId()+1);
					return ActionResultType.SUCCESS;
				}
			}
			player.sendMessage(new TranslationTextComponent("------", new Object[0]), player.getUUID());
			if(isEnemyPoint){
				player.sendMessage(new TranslationTextComponent("Enemy Type", new Object[0]), player.getUUID());
			}else{
				player.sendMessage(new TranslationTextComponent("Friend Type", new Object[0]), player.getUUID());
			}
			player.sendMessage(new TranslationTextComponent("Move ID ="+this.getMoveId(), new Object[0]), player.getUUID());
			player.sendMessage(new TranslationTextComponent("======", new Object[0]), player.getUUID());
		}
		return super.mobInteract(player, hand);
    }
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
	
	public int pointType = 0;
	public int connectRange = 50;
	public boolean isEnemyPoint = false;
	public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}
	
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public float summontime = 0;
	public float cooltime6 = 0;
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
		
		if(this.getMainHandItem()!=null){
			ItemStack this_heldItem = this.getMainHandItem();
			Item item = this_heldItem.getItem();
			if(item == Items.DIAMOND)this.isEnemyPoint = false;
			if(item == Items.IRON_INGOT)this.isEnemyPoint = true;
		}
		if(this.getOffhandItem()!=null){
			ItemStack this_heldItem2 = this.getOffhandItem();
			Item item2 = this_heldItem2.getItem();
			if(item2 == Items.DIAMOND_SWORD){
				this.pointType = 3;
			}
			if(item2 == Items.IRON_SWORD){
				this.pointType = 2;
			}
			if(item2 == Items.GOLDEN_SWORD){
				this.pointType = 1;
				connectRange = 30;
			}
		}
		
		if (this.isAlive()){
			//this.bossEvent.setPercent(this.getHealth() / this.getMaxHealth());
			if(cooltime6<50)++cooltime6;
			/*if (!(this.level instanceof ServerWorld)) {
				//return false;
			} else */{
			//ServerWorld serverworld = (ServerWorld)this.level;
			int i = MathHelper.floor(this.getX());
			int j = MathHelper.floor(this.getY());
			int k = MathHelper.floor(this.getZ());
			if(summontime<100)++summontime;
			if(summontime>10){//
				int i1 = i + MathHelper.nextInt(this.random, 2, 4) * MathHelper.nextInt(this.random, -1, 1);
				int j1 = j + MathHelper.nextInt(this.random, 2, 3);
				int k1 = k + MathHelper.nextInt(this.random, 2, 4) * MathHelper.nextInt(this.random, -1, 1);
				List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(connectRange, connectRange*2F, connectRange));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					if(ent!=null && ent.getVehicle()==null && !(ent instanceof EntityWMSeat)){
						if(isEnemyPoint){
							if(this.pointType ==0 && ent instanceof EntityMobSoldierBase){
								EntityMobSoldierBase unit = (EntityMobSoldierBase)ent;
								if(this.distanceTo(unit)<10 && unit.getMovePosY()==this.getMoveId() && unit.getMoveType()==4 && this.random.nextInt(3)==1){
									unit.setMoveType(1);
									unit.setMovePosY(unit.getMovePosY()+1);
								}
								if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMovePosY()==this.getMoveId() && unit.getMoveType()!=2 && this.random.nextInt(3)==1){
									//unit.getNavigation().moveTo(i1, j1, k1, 1.5F);
									unit.setMovePosX(i1);
									unit.setMovePosZ(k1);
									unit.setMoveType(4);
								}
							}
							if(ent instanceof WeaponVehicleBase){
								WeaponVehicleBase unit = (WeaponVehicleBase)ent;
								if(unit.getTargetType()==2 && unit.getTarget()==null && 
								(this.pointType ==1 && unit.VehicleType <3||this.pointType ==2 && unit.VehicleType == 3||this.pointType ==3 && unit.VehicleType == 4)){
									if(this.distanceTo(unit)<8 && unit.getMoveMode()==this.getMoveId() && unit.getMoveType()==4 && this.random.nextInt(3)==1){
										unit.setMoveType(1);
										unit.setMoveMode(unit.getMoveMode()+1);
										break;
									}
									if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMoveMode()==this.getMoveId() && unit.getMoveType()!=2 && this.random.nextInt(3)==1){
										unit.setMoveType(4);
										unit.setMovePosX(i1);
										//unit.setMoveId(j1);
										unit.setMovePosZ(k1);
										break;
									}
								}
							}
						}else{
							if(this.pointType ==0 && ent instanceof EntitySA_SoldierBase){
								EntitySA_SoldierBase unit = (EntitySA_SoldierBase)ent;
								if(this.distanceTo(unit)<10 && unit.getMovePosY()==this.getMoveId() && unit.getMoveType()==4 && this.random.nextInt(3)==1){
									unit.setMoveType(1);
									unit.setMovePosY(unit.getMovePosY()+1);
								}
								if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMovePosY()==this.getMoveId() && unit.getMoveType()!=2 && this.random.nextInt(3)==1){
									//unit.getNavigation().moveTo(i1, j1, k1, 1.5F);
									unit.setMovePosX(i1);
									unit.setMovePosZ(k1);
									unit.setMoveType(4);
								}
							}
							if(ent instanceof WeaponVehicleBase){
								WeaponVehicleBase unit = (WeaponVehicleBase)ent;
								if(unit.getTargetType()==3 && unit.getTarget()==null && 
								(this.pointType ==1 && unit.VehicleType <3||this.pointType ==2 && unit.VehicleType == 3||this.pointType ==3 && unit.VehicleType == 4)){
									if(this.distanceTo(unit)<8 && unit.getMoveMode()==this.getMoveId() && unit.getMoveType()==4 && this.random.nextInt(3)==1){
										unit.setMoveType(1);
										unit.setMoveMode(unit.getMoveMode()+1);
										break;
									}
									if(unit.getHealth()>0&&unit.getTarget()==null && unit.getMoveMode()==this.getMoveId() && unit.getMoveType()!=2 && this.random.nextInt(3)==1){
										unit.setMoveType(4);
										unit.setMovePosX(i1);
										//unit.setMoveId(j1);
										unit.setMovePosZ(k1);
										break;
									}
								}
							}
						}
					}
				}
				this.summontime = 0;
			}
		}
      }
      super.aiStep();
   }
}
