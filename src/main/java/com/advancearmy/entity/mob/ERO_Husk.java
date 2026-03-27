package advancearmy.entity.mob;
import javax.annotation.Nullable;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySize;

import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.nbt.CompoundNBT;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Item;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;

import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.util.DamageSource;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.LivingEntity;
import advancearmy.AdvanceArmy;
import net.minecraft.entity.Pose;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import wmlib.api.IEnemy;
import advancearmy.entity.ai.ZombieAttackGoalSA;

import wmlib.api.ITool;
public class ERO_Husk extends CreatureEntity implements IMob,IEnemy{
	public ERO_Husk(EntityType<? extends ERO_Husk> p_i48549_1_, World p_i48549_2_) {
	  super(p_i48549_1_, p_i48549_2_);
	  this.xpReward = 1;
	}
	public ERO_Husk(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_EHUSK, worldIn);
	}
	protected float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(2, new ZombieAttackGoalSA(this, 1.0D, false));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, MobEntity.class, 10, 10F, false, false, (attackentity) -> {
		return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 10F, false, false, (attackentity) -> {
		return true;}));
	}

    public boolean CanAttack(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F){
			if(!(entity instanceof IMob)&&!(entity instanceof ITool)||entity==this.getTarget()){
				return true;
			}else{
				return false;
			}
    	}else{
			return false;
		}
    }

	public boolean hurt(DamageSource source, float par2)
    {
    	Entity entity;
    	entity = source.getEntity();
		if(entity != null){
			if(entity instanceof IEnemy){
				return false;
			}else{
				return super.hurt(source, par2);
			}
		}else{
			return super.hurt(source, par2);
		}
	}

	protected boolean isSunSensitive() {
	  return true;
	}
	protected SoundEvent getAmbientSound() {
	  return SoundEvents.HUSK_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
	  return SoundEvents.HUSK_HURT;
	}

	protected SoundEvent getDeathSound() {
	  return SoundEvents.HUSK_DEATH;
	}

	protected SoundEvent getStepSound() {
	  return SoundEvents.HUSK_STEP;
	}
	protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
	  this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	public CreatureAttribute getMobType() {
	  return CreatureAttribute.UNDEAD;
	}

	protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
	  return this.isBaby() ? 0.93F : 1.74F;
	}
	public double getMyRidingOffset() {
	  return this.isBaby() ? 0.0D : -0.45D;
	}
   @Nullable
   public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance diff, SpawnReason reason, @Nullable ILivingEntityData data ,@Nullable CompoundNBT nbt) {
      data = super.finalizeSpawn(world, diff, reason, data, nbt);
      populateDefaultEquipmentSlots(null);
      return data;
   }

   protected void populateDefaultEquipmentSlots(DifficultyInstance diff) {
      if (this.random.nextFloat() < 0.45F) {
         int i = this.random.nextInt(2);
         float f = 0.5F;
         if (this.random.nextFloat() < 0.095F)++i;
         if (this.random.nextFloat() < 0.095F)++i;
         if (this.random.nextFloat() < 0.095F)++i;
         boolean flag = true;
         for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
            if (equipmentslottype.getType() == EquipmentSlotType.Group.ARMOR) {
               ItemStack itemstack = this.getItemBySlot(equipmentslottype);
               if (!flag && this.random.nextFloat() < f) {
                  break;
               }
               flag = false;
               if (itemstack.isEmpty()) {
                  Item item = getEquipmentForSlot(equipmentslottype, i);
                  if (item != null) {
                     this.setItemSlot(equipmentslottype, new ItemStack(item));
                  }
               }
            }
         }
      }
   }
}
