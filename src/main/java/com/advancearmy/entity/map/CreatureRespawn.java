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

import advancearmy.entity.mob.ERO_Skeleton;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Pillager;
import advancearmy.entity.mob.ERO_REB;
import advancearmy.entity.mob.ERO_Creeper;

import wmlib.common.living.EntityWMVehicleBase;
import wmlib.api.ITool;

import net.minecraft.item.PickaxeItem;
public class CreatureRespawn extends MobEntity implements ITool{
	public CreatureRespawn(EntityType<? extends CreatureRespawn> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
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
	public CreatureRespawn(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_CRES, worldIn);
	}
	public void checkDespawn() {
	}
	private static final DataParameter<Integer> RespawnCount = EntityDataManager.<Integer>defineId(CreatureRespawn.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("RespawnCount", this.getRespawnCount());
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setRespawnCount(compound.getInt("RespawnCount"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(RespawnCount, Integer.valueOf(0));
	}
	public int getRespawnCount() {
	return ((this.entityData.get(RespawnCount)).intValue());
	}
	public void setRespawnCount(int stack) {
	this.entityData.set(RespawnCount, Integer.valueOf(stack));
	}
	
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
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
				}
				if(item == Items.GOLD_INGOT){
					if(isEnemyRespawn){
						if(!this.level.isClientSide)this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND));
						return ActionResultType.SUCCESS;
					}else{
						if(!this.level.isClientSide)this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_INGOT));
						return ActionResultType.SUCCESS;
					}
				}else{
					player.sendMessage(new TranslationTextComponent("use GOLD_INGOT click to change[Friend/Enemy]", new Object[0]), player.getUUID());
				}
			}else{
				if(player.isCrouching()){
					this.setRespawnCount(0);
					return ActionResultType.SUCCESS;
				}else{
					this.setRespawnCount(this.total_count);
					return ActionResultType.SUCCESS;
				}
			}
			player.sendMessage(new TranslationTextComponent("------", new Object[0]), player.getUUID());
			if(isEnemyRespawn){
				player.sendMessage(new TranslationTextComponent("Enemy Spawner", new Object[0]), player.getUUID());
			}else{
				player.sendMessage(new TranslationTextComponent("Friend Spawner", new Object[0]), player.getUUID());
			}
			player.sendMessage(new TranslationTextComponent("Max Count ="+this.total_count, new Object[0]), player.getUUID());
			player.sendMessage(new TranslationTextComponent("Now Count ="+this.getRespawnCount(), new Object[0]), player.getUUID());
			player.sendMessage(new TranslationTextComponent("======", new Object[0]), player.getUUID());
		}
		//return super.mobInteract(player, hand);
		return ActionResultType.PASS;
    }

	public boolean isEnemyRespawn = false;
	
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
	public int setx = 0;
	public int sety = 0;
	public int setz = 0;
	public int total_count = 800;
	public int max_summon = 20;
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
			if(item == Items.DIAMOND){
				this.isEnemyRespawn = false;
				//this.setDisplayName("敌方兵力");
			}
			if(item == Items.IRON_INGOT){
				this.isEnemyRespawn = true;
				//this.setDisplayName("我方兵力");
			}
		}
		/*if(this.isEnemyRespawn){
			this.EnemyCount.setPercent(this.getRespawnCount() / 200);//this.getMoveY()
		}else{
			this.FriendCount.setPercent(this.getRespawnCount() / 200);
		}*/
		if (this.isAlive()){
			if(cooltime6<50)++cooltime6;
			/*if (!(this.level instanceof ServerWorld)) {
				//return false;
			}*/ else {
				//ServerWorld serverworld = (ServerWorld)this.level;
				int i = MathHelper.floor(this.getX());
				int j = MathHelper.floor(this.getY());
				int k = MathHelper.floor(this.getZ());
				if(summontime<100)++summontime;
				if(summontime>20 && this.getRespawnCount()>0){
					int count = 0;
					int ve = 0;
					int i1 = i + MathHelper.nextInt(this.random, 2, 10) * MathHelper.nextInt(this.random, -1, 1);
					int j1 = j + MathHelper.nextInt(this.random,1, 2);
					int k1 = k + MathHelper.nextInt(this.random, 2, 10) * MathHelper.nextInt(this.random, -1, 1);
					List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(200D, 100D, 200D));
					for(int k2 = 0; k2 < list.size(); ++k2) {
						Entity ent = list.get(k2);
						if(ent instanceof LivingEntity){
							LivingEntity living = (LivingEntity)ent;
							if(isEnemyRespawn){
								if(ent instanceof ERO_REB){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
								if(ent instanceof ERO_Pillager){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
							}else{
								if(ent instanceof EntitySA_Soldier){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
								if(ent instanceof EntitySA_GI){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
								if(ent instanceof EntitySA_Conscript){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
								if(ent instanceof PlayerEntity){
									if(living.getHealth()>0){
										++count;
									}else{
										if(this.getRespawnCount()>0)this.setRespawnCount(this.getRespawnCount()-1);
									}
								}
							}
						}
					}
					if (!(this.level instanceof ServerWorld)) {
						//return false;
					}else{
						ServerWorld serverworld = (ServerWorld)this.level;
						if(count<max_summon){
							if(summontime>50+count){
								   BlockPos blockpos = new BlockPos(i1, j1, k1);
								   if(isEnemyRespawn){
										if(this.level.random.nextInt(6)==2){
											ERO_Zombie army = new ERO_Zombie(AdvanceArmy.ENTITY_EZOMBIE, serverworld);
											army.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(army);
										}else if(this.level.random.nextInt(6)==4){
											ERO_Skeleton ent = new ERO_Skeleton(AdvanceArmy.ENTITY_SKELETON, serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}else if(this.level.random.nextInt(5)==2){
											ERO_Creeper ent = new ERO_Creeper(AdvanceArmy.ENTITY_CREEPER, serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}
										if(this.level.random.nextInt(3)==1){
											ERO_Pillager ent = new ERO_Pillager(AdvanceArmy.ENTITY_PI, serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}else{
											ERO_REB ent = new ERO_REB(AdvanceArmy.ENTITY_REB, serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}
								   }else{
										if(this.level.random.nextInt(8)==1){
											EntitySA_Conscript ent = new EntitySA_Conscript(AdvanceArmy.ENTITY_CONS, serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}else
										if(this.level.random.nextInt(8)==2){
											EntitySA_GI ent = new EntitySA_GI(AdvanceArmy.ENTITY_GI, serverworld);
											ent.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(ent);
										}else{
											EntitySA_Soldier army = new EntitySA_Soldier(AdvanceArmy.ENTITY_SOLDIER, serverworld);
											army.setPos((double)i1, (double)j1, (double)k1);
											serverworld.addFreshEntity(army);
										}
								   }
								this.summontime = 0;
							}
						}
					}
				}
			}
		}
		super.aiStep();
    }
}
