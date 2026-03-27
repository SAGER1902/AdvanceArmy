package advancearmy.entity.mob;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;

import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import advancearmy.entity.ai.WaterAvoidingRandomWalkingGoalSA;
import com.mrcrayfish.guns.item.GunItem;
import wmlib.common.living.ai.LivingSearchTargetGoalSA;
import advancearmy.AdvanceArmy;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import wmlib.api.ITool;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.entity.projectile.ArrowEntity;

public abstract class AbstractERO_Skeleton extends MonsterEntity implements IRangedAttackMob{//, IMob
   private final RangedBowAttackGoal<AbstractERO_Skeleton> bowGoal = new RangedBowAttackGoal<>(this, 1.0D, 20, 25.0F);
   private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false) {
      public void stop() {
         super.stop();
         AbstractERO_Skeleton.this.setAggressive(false);
      }

      public void start() {
         super.start();
         AbstractERO_Skeleton.this.setAggressive(true);
      }
   };

	public AbstractERO_Skeleton(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_SKELETON, worldIn);
	}
   
   protected AbstractERO_Skeleton(EntityType<? extends AbstractERO_Skeleton> p_i48555_1_, World p_i48555_2_) {
      super(p_i48555_1_, p_i48555_2_);
      this.reassessWeaponGoal();
   }

   protected void registerGoals() {
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoalSA(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new LivingSearchTargetGoalSA<>(this, MobEntity.class, 10, 15F, false, false, (attackentity) -> {
			return this.CanAttack(attackentity);
		}));
		this.targetSelector.addGoal(2, new LivingSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 15F, false, false, (attackentity) -> {
			return true;
		}));
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

   protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
      this.playSound(this.getStepSound(), 0.15F, 1.0F);
   }

   protected abstract SoundEvent getStepSound();

   public CreatureAttribute getMobType() {
      return CreatureAttribute.UNDEAD;
   }
	public int cooltime;
   public void aiStep() {
	   /*if(this.sneak_aim && !this.isCrouching())this.setPose(Pose.CROUCHING);
	   if(!this.sneak_aim && this.isCrouching())this.setPose(Pose.STANDING);*/
      /*boolean flag = this.isSunBurnTick();
      if (flag) {
         ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
         if (!itemstack.isEmpty()) {
            if (itemstack.isDamageableItem()) {
               itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
               if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                  this.broadcastBreakEvent(EquipmentSlotType.HEAD);
                  this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
               }
            }

            flag = false;
         }

         if (flag) {
            this.setSecondsOnFire(8);
         }
      }*/
      super.aiStep();
   }

   public void rideTick() {
      super.rideTick();
      if (this.getVehicle() instanceof CreatureEntity) {
         CreatureEntity creatureentity = (CreatureEntity)this.getVehicle();
         this.yBodyRot = creatureentity.yBodyRot;
      }
   }

   protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
      super.populateDefaultEquipmentSlots(p_180481_1_);
      this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
	  this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.BOW));
   }

   @Nullable
   public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
      p_213386_4_ = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
      this.populateDefaultEquipmentSlots(p_213386_2_);
      this.populateDefaultEquipmentEnchantments(p_213386_2_);
      this.reassessWeaponGoal();
      this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * p_213386_2_.getSpecialMultiplier());
      if (this.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
         LocalDate localdate = LocalDate.now();
         int i = localdate.get(ChronoField.DAY_OF_MONTH);
         int j = localdate.get(ChronoField.MONTH_OF_YEAR);
         if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
            this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
            this.armorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
         }
      }

      return p_213386_4_;
   }

   public void reassessWeaponGoal() {
      if (this.level != null && !this.level.isClientSide) {
         this.goalSelector.removeGoal(this.meleeGoal);
         this.goalSelector.removeGoal(this.bowGoal);
         ItemStack itemstack = this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.item.BowItem));
		 ItemStack this_heldItem = this.getMainHandItem();
         if (itemstack.getItem() == Items.BOW) {
            int i = 20;
            if (this.level.getDifficulty() != Difficulty.HARD) {
               i = 40;
            }
            this.bowGoal.setMinAttackInterval(i);
            this.goalSelector.addGoal(4, this.bowGoal);
         } else {
			/*if(this_heldItem.getItem() instanceof GunItem){
				int i = 20;
				this.bowGoal.setMinAttackInterval(i);
				this.goalSelector.addGoal(4, this.bowGoal);
			}else*/{
				this.goalSelector.addGoal(4, this.meleeGoal);
			}
         }
      }
   }

   public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {
      ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.item.BowItem)));
      AbstractArrowEntity abstractarrowentity = this.getArrow(itemstack, p_82196_2_);
      if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem)
         abstractarrowentity = ((net.minecraft.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
      double d0 = p_82196_1_.getX() - this.getX();
      double d1 = p_82196_1_.getY(0.3333333333333333D) - abstractarrowentity.getY();
      double d2 = p_82196_1_.getZ() - this.getZ();
      double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
      abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 2.0F, 10F);
	  abstractarrowentity.setCritArrow(true);
      this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
      this.level.addFreshEntity(abstractarrowentity);
   }

   protected AbstractArrowEntity getArrow(ItemStack p_213624_1_, float p_213624_2_) {
      AbstractArrowEntity abstractarrowentity = ProjectileHelper.getMobArrow(this, p_213624_1_, p_213624_2_);
      if (abstractarrowentity instanceof ArrowEntity) {
         ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(Effects.WITHER, 300));
      }
      return abstractarrowentity;
   }

   public boolean canFireProjectileWeapon(ShootableItem p_230280_1_) {
      return p_230280_1_ == Items.BOW;
   }

   public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
      super.readAdditionalSaveData(p_70037_1_);
      this.reassessWeaponGoal();
   }

   public void setItemSlot(EquipmentSlotType p_184201_1_, ItemStack p_184201_2_) {
      super.setItemSlot(p_184201_1_, p_184201_2_);
      if (!this.level.isClientSide) {
         this.reassessWeaponGoal();
      }

   }

   protected float getStandingEyeHeight(Pose p_213348_1_, EntitySize p_213348_2_) {
      return 1.74F;
   }

   public double getMyRidingOffset() {
      return -0.6D;
   }
}
