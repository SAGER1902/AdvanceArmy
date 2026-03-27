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

import advancearmy.AdvanceArmy;
import wmlib.common.block.BlockRegister;
import net.minecraft.network.play.server.SDestroyEntitiesPacket;
import advancearmy.event.SASoundEvent;
import net.minecraft.entity.item.ItemEntity;
public class RewardBox extends MobEntity implements ITool{
	public RewardBox(EntityType<? extends RewardBox> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.noCulling = true;
	}
	public RewardBox(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_RBOX, worldIn);
	}
	public void checkDespawn() {
	}
	private static final DataParameter<Integer> BoxID = EntityDataManager.<Integer>defineId(RewardBox.class, DataSerializers.INT);
	public void addAdditionalSaveData(CompoundNBT compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("BoxID", this.getBoxID());
	}
	public void readAdditionalSaveData(CompoundNBT compound)
	{
	   super.readAdditionalSaveData(compound);
	   this.setBoxID(compound.getInt("BoxID"));
	}
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(BoxID, Integer.valueOf(0));
	}
	public int getBoxID() {
	return ((this.entityData.get(BoxID)).intValue());
	}
	public void setBoxID(int stack) {
	this.entityData.set(BoxID, Integer.valueOf(stack));
	}
	
	/*public boolean canBeCollidedWith() {//
		return false;
	}*/
	
	int iron = 0;
	int gold = 0;
	int emerald = 0;
	int diamond = 0;
	int goldmelon = 0;
	int ironmelon = 0;
	
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		player.playSound(SASoundEvent.open_box.get(),1F,1F);
		if(!level.isClientSide){
			this.remove();
			if (player instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) player).connection.send(new SDestroyEntitiesPacket(this.getId()));
            }
			//this.die(DamageSource.GENERIC);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
    }
	
   public void remove() {
      super.remove();
		if(this.getBoxID()==1){//mob
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_m2hb));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_tow));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_stin));
			iron = 10;
			gold = 10;
			emerald = 3;
			goldmelon = 1;
			ironmelon = 2;
			this.dropItemStack(new ItemStack(AdvanceArmy.challenge_reb));
		}else if(this.getBoxID()==2){//reb
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_tank));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_m2a2));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_egal));
			this.dropItemStack(new ItemStack(AdvanceArmy.support_155));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_mortar));
			iron = 15;
			gold = 20;
			emerald = 8;
			diamond = 3;
			goldmelon = 3;
			ironmelon = 4;
			this.dropItemStack(new ItemStack(AdvanceArmy.challenge_pillager));
		}else if(this.getBoxID()==3){//pill
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_t90));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_bmpt));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_heli));
			this.dropItemStack(new ItemStack(AdvanceArmy.support_kh29l));
			this.dropItemStack(new ItemStack(AdvanceArmy.support_a10a));
			iron = 20;
			gold = 30;
			emerald = 12;
			diamond = 8;
			goldmelon = 4;
			ironmelon = 5;
			this.dropItemStack(new ItemStack(AdvanceArmy.challenge_tank));
		}else if(this.getBoxID()==4){//tank
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_a10a));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_m6aa));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_gltk));
			iron = 30;
			gold = 40;
			emerald = 16;
			diamond = 12;
			goldmelon = 5;
			ironmelon = 6;
			this.dropItemStack(new ItemStack(AdvanceArmy.challenge_mobair));
		}else if(this.getBoxID()==5){//mobair
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_su33));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_mi24));
			iron = 35;
			gold = 45;
			emerald = 18;
			diamond = 15;
			goldmelon = 6;
			ironmelon = 7;
			this.dropItemStack(new ItemStack(AdvanceArmy.challenge_air));
		}else if(this.getBoxID()==6){//air
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_f35));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_skyfire));
			this.dropItemStack(new ItemStack(AdvanceArmy.support_f35bomb));
			iron = 40;
			gold = 50;
			emerald = 20;
			diamond = 18;
			goldmelon = 7;
			ironmelon = 8;
			this.dropItemStack(new ItemStack(AdvanceArmy.challenge_aohuan));//challenge_sea
		}else if(this.getBoxID()==7){//
			this.dropItemStack(new ItemStack(AdvanceArmy.support_trident));
			iron = 45;
			gold = 55;
			emerald = 25;
			diamond = 20;
			goldmelon = 8;
			ironmelon = 9;
			this.dropItemStack(new ItemStack(AdvanceArmy.challenge_aohuan));
		}else if(this.getBoxID()==8){
			this.dropItemStack(new ItemStack(AdvanceArmy.support_3m22));
			this.dropItemStack(new ItemStack(AdvanceArmy.support_ember));
			this.dropItemStack(new ItemStack(AdvanceArmy.item_spawn_mast));
			if(this.level.random.nextInt(2)==1)this.dropItemStack(new ItemStack(AdvanceArmy.support_swun));
			if(this.level.random.nextInt(3)==1)this.dropItemStack(new ItemStack(AdvanceArmy.support_youhun));
			iron = 50;
			gold = 60;
			emerald = 30;
			diamond = 25;
			goldmelon = 9;
			ironmelon = 10;
			this.dropItemStack(new ItemStack(AdvanceArmy.challenge_portal));
		}else if(this.getBoxID()==9){
			this.dropItemStack(new ItemStack(AdvanceArmy.support_ftkh));
			this.dropItemStack(new ItemStack(AdvanceArmy.support_nuke));
			this.dropItemStack(new ItemStack(AdvanceArmy.support_fw020));
			iron = 64;
			gold = 64;
			emerald = 32;
			diamond = 32;
			goldmelon = 10;
			ironmelon = 12;
		}else{
			this.dropItemStack(new ItemStack(BlockRegister.GOLD_MELON.get().asItem()));
		}
		
		for(int k2 = 0; k2 < iron+this.level.random.nextInt(iron); ++k2){
			this.dropItemStack(new ItemStack(Items.IRON_INGOT));
		}
		for(int k2 = 0; k2 < gold+this.level.random.nextInt(gold); ++k2){
			this.dropItemStack(new ItemStack(Items.GOLD_INGOT));
		}
		for(int k2 = 0; k2 < emerald+this.level.random.nextInt(emerald); ++k2){
			this.dropItemStack(new ItemStack(Items.EMERALD));
		}
		for(int k2 = 0; k2 < diamond+this.level.random.nextInt(diamond); ++k2){
			this.dropItemStack(new ItemStack(Items.DIAMOND));
		}
		for(int k2 = 0; k2 < goldmelon+this.level.random.nextInt(goldmelon); ++k2){
			this.dropItemStack(new ItemStack(BlockRegister.GOLD_MELON.get().asItem()));
		}
		for(int k2 = 0; k2 < ironmelon+this.level.random.nextInt(ironmelon); ++k2){
			this.dropItemStack(new ItemStack(BlockRegister.IRON_MELON.get().asItem()));
		}
   }
	

	private void dropItemStack(ItemStack item) {
	  ItemEntity itementity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), item);
	  this.level.addFreshEntity(itementity);
	}
	
	/*public boolean hurt(DamageSource source, float par2)
    {
		return false;
	}*/
	/*protected void tickDeath() {
	}*/
}
