package advancearmy.entity.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.Hand;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.vector.Vector3d;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IAmmo;
import com.mrcrayfish.guns.init.ModSounds;

import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import advancearmy.entity.EntitySA_LandBase;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.item.ArmorItem;
import net.minecraft.entity.MobEntity;
import net.minecraftforge.fml.ModList;

public class ERO_Skeleton extends AbstractERO_Skeleton {
   public ERO_Skeleton(EntityType<? extends ERO_Skeleton> p_i50194_1_, World p_i50194_2_) {
      super(p_i50194_1_, p_i50194_2_);
	  //this.setCanPickUpLoot(true);
	  this.xpReward = 2;
   }
   public ERO_Skeleton(World p_i1745_1_) {
      this(AdvanceArmy.ENTITY_SKELETON, p_i1745_1_);
   }
	/*public ERO_Skeleton(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_SOLDIER, worldIn);
	}*/
	protected float getVoicePitch() {
	  return (this.random.nextFloat() - this.random.nextFloat()) * 0.4F *(0.5F-this.random.nextFloat()) + 0.8F;
	}
   protected SoundEvent getAmbientSound() {
      return SoundEvents.SKELETON_AMBIENT;
   }

   protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
      return SoundEvents.SKELETON_HURT;
   }

   protected SoundEvent getDeathSound() {
      return SoundEvents.SKELETON_DEATH;
   }

   protected SoundEvent getStepSound() {
      return SoundEvents.SKELETON_STEP;
   }
	
	public float cooltime6 = 0;
	public Vector3d motions = this.getDeltaMovement();
	public int fire_tick = 4;
	public int fire_tick2 = 0;
	public int fire_tick3 = 0;
	public static int gun_count1 = 0;
	public void aiStep() {
		if (this.isAlive()) {
			if(cooltime < 200)++cooltime;
			if(gun_count1 < 200)++gun_count1;
			ItemStack heldItem = this.getMainHandItem();
			/*if(ModList.get().isLoaded("cgm")){
			if(heldItem.getItem() instanceof GunItem){
				if(this.getRemain1() <= 0 && this.getRemain2()>0){
					++reload1;
					if(this.isAggressive())this.setAggressive(false);
					if(reload1 >= reload_time1){
						this.setRemain1(this.magazine);
						//this.setRemain2(this.getRemain2()-1);
						this.playSound(SASoundEvent.reload_mag.get(), 2.0F, 1.0F);
						reload1 = 0;
					}
				}
			}
			}*/
			if(this.getHealth()<this.getMaxHealth() && this.cooltime6>45){
				this.setHealth(this.getHealth()+1);
				this.cooltime6 = 0;
			}

			if(cooltime6<50)++cooltime6;
			float sp = 0.20F;
			this.moveway(this, sp, 25, 25);
			/*World world = this.level;
			if(this.getTarget()!=null && this.isAttacking()){
				if(ModList.get().isLoaded("cgm")){
				if(heldItem.getItem() instanceof GunItem){
					GunItem item = (GunItem) heldItem.getItem();
					Gun modifiedGun = item.getModifiedGun(heldItem);
					if(modifiedGun != null)
					{
						if(modifiedGun.getGeneral().getMaxAmmo()>0){
							this.magazine = modifiedGun.getGeneral().getMaxAmmo();
						}else{
							this.magazine = 30;
						}
						this.fire_tick = modifiedGun.getGeneral().getRate()+8;
						if(modifiedGun.getGeneral().getMaxAmmo()>0){
							this.reload_time1 = 3*modifiedGun.getGeneral().getMaxAmmo();
						}else{
							this.reload_time1 = 80;
						}
						if(reload1 % 10 == 0 && this.getRemain1()<=0)this.playSound(ModSounds.ITEM_PISTOL_RELOAD.get(), 2.0F, 1.0F);
					}
					if(this.cooltime > this.fire_tick && this.getRemain1() > 0)
					{
						this.counter1 = true;
						this.cooltime = 0;
						this.fire_tick2 = 0;
					}
					if(this.counter1 && this.gun_count1 > this.fire_tick && this.level.random.nextInt(6) > 3){
						AI_EntityWeapon_CGM.fireFromGun(this, world, heldItem, modifiedGun);
						this.gun_count1 = 0;
						this.setRemain1(this.getRemain1() - 1);
						this.counter1 = false;
						//this.setAggressive(false);
					}
				}
				}
			}
			if(ModList.get().isLoaded("cgm")){
				if(heldItem.getItem() instanceof GunItem && this.cooltime < this.fire_tick){
					if(!this.isAggressive())this.setAggressive(true);
				}else if(this.cooltime>50||this.gun_count1==0){
					//if(this.isAggressive())this.setAggressive(false);
				}
			}*/
		}
		super.aiStep();
	}
   
	public float face = 0;
	public void moveway(ERO_Skeleton entity, float sp, double max, double range1) {
		boolean ta = false;
		{
			if (entity.getTarget() != null) {
				if (!entity.getTarget().isInvisible()) {//target
					if (entity.getTarget().getHealth() > 0.0F) {
						double d5 = entity.getTarget().getX() - entity.getX();
						double d7 = entity.getTarget().getZ() - entity.getZ();
						double d6 = entity.getTarget().getY() - entity.getY();
						double d1 = entity.getEyeY() - (entity.getTarget().getEyeY());
						double d3 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7);
						float f11 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
						double ddx = Math.abs(d5);
						double ddz = Math.abs(d7);
						float f12 = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
						{
							if ((ddx>15F||ddz>15F)) {//
								{
									MoveS(entity, sp, 1, entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), (LivingEntity)entity.getTarget());
								}
							}else if((ddx < 4 || ddz < 4)){//
								MoveS(entity, sp, 1, entity.getTarget().getX(), entity.getTarget().getY(), entity.getTarget().getZ(), (LivingEntity)entity.getTarget());
							}
						}
						entity.yRotO = entity.yRot = f12;//
						entity.setYHeadRot(f12);//
						entity.xRot = -f11 + 0;//
					}
				}
			}
		}
	}
	
	public void MoveS(ERO_Skeleton entity, double speed, double han, double ex, double ey, double ez, LivingEntity en){
		if(!entity.level.isClientSide)
		{
			double d5 = ex - entity.getX();
			double d7 = ez - entity.getZ();
			float yawoffset = -((float) Math.atan2(d5, d7)) * 180.0F / (float) Math.PI;
			float yaw = yawoffset * (2 * (float) Math.PI / 360);
			double mox = 0;
			double moy = -1D;
			double moz = 0;
			//entity.stepHeight = entity.height * 0.8F;
			
			if (entity.distanceToSqr(en) < 64) {//8 * 8
					mox -= MathHelper.sin(yaw) * speed * -1;
					moz += MathHelper.cos(yaw) * speed * -1;
					entity.setDeltaMovement(mox, moy, moz);
			}else{
				{
					mox -= MathHelper.sin(yaw) * speed * 0.5F;
					moz += MathHelper.cos(yaw) * speed * 0.5F;
				}
			}
			
			boolean flag = true;
			//Vector3d vector3d1 = this.getDeltaMovement().scale(0.75D);
			if(flag){
				{
					entity.getNavigation().moveTo(ex, ey, ez, 1.6);
					//entity.move(MoverType.PLAYER, entity.getDeltaMovement());
				}
			}
		}
	}
}