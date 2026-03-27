package advancearmy.entity.land;
import java.util.List;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.World;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.network.FMLPlayMessages;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.ai.AI_EntityWeapon;
import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import safx.SagerFX;
import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import advancearmy.entity.EntitySA_LandBase;
import net.minecraft.util.math.MathHelper;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import wmlib.common.world.WMExplosionBase;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.OwnerHurtByTargetGoal;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;
import wmlib.common.living.ai.VehicleLockGoal;
import wmlib.common.living.ai.VehicleSearchTargetGoalSA;
import net.minecraft.util.text.TranslationTextComponent;
public class EntitySA_LaserAA extends EntitySA_LandBase{
	public EntitySA_LaserAA(EntityType<? extends EntitySA_LaserAA> sodier, World worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0F;
		seatPosY[0] = 2.1F;
		seatPosZ[0] = 0.1F;
		seatTurret[0] = true;
		seatHide[0] = true;
		this.attack_range_max = 60;
		seatMaxCount = 1;
		this.attack_height_max = 130;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/laseraa.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.w1name = new TranslationTextComponent("advancearmy.weapon.aalaser.desc").getString();
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.w1recoilp = 0;
		this.w1recoilr = 6;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -90;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.03F;
        this.turnSpeed = 1.5F;
		this.turretSpeed = 0.8F;
        this.throttleMax = 5F;
		this.throttleMin = -4F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.maxUpStep = 1.5F;
		this.w1barrelsize = 0.2F;
		this.armor_front = 25;
		this.armor_side = 10;
		this.armor_back = 10;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 30;
		this.armor_turret_side = 10;
		this.armor_turret_back = 10;
		this.armor_front = 35;
		this.armor_side = 20;
		this.armor_back = 20;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 1.8F;
		this.armor_turret_front = 30;
		this.armor_turret_side = 20;
		this.armor_turret_back = 20;
		this.soundspeed=0.7F;
		this.ammo1=5;
		this.fireposX1 = 1.12F;
		this.fireposY1 = 2.36F;
		this.fireposZ1 = 3.8F;
		this.firebaseZ = 0F;
		this.icon1tex = new ResourceLocation("advancearmy:textures/hud/skyfhead.png");
		this.icon2tex = new ResourceLocation("advancearmy:textures/hud/skyfbody.png");
		this.tracktex = new ResourceLocation("advancearmy:textures/mob/track.png");
		this.obj = new SAObjModel("advancearmy:textures/mob/skyfire.obj");
		this.tex = new ResourceLocation("advancearmy:textures/mob/skyfire.png");
		this.canNightV=true;
		this.magazine = 2;
		this.reload_time1 = 25;
		this.reloadSound1 = null;
		this.firesound1 = SASoundEvent.fire_skyfire.get();
		
		this.startsound = SASoundEvent.start_m6.get();
		this.movesound = SASoundEvent.move_track2.get();
		this.weaponCount = 1;
		this.w1icon="advancearmy:textures/hud/skyfaa.png";
		
		this.radercount = 1;
		this.setRader(0,0, 0, -1.3F);
		
		this.wheelcount = 9;
		this.setWheel(0,0, 0.99F, 4.03F);
		this.setWheel(1,0, 0.49F, 3.34F);
		this.setWheel(2,0, 0.49F, 2.45F);
		this.setWheel(3,0, 0.49F, 1.55F);
		this.setWheel(4,0, 0.49F, 0.65F);
		this.setWheel(5,0, 0.49F, -0.24F);
		this.setWheel(6,0, 0.49F, -1.14F);
		this.setWheel(7,0, 0.49F, -2.04F);
		this.setWheel(8,0, 1.02F, -2.74F);
		this.is_aa = true;
		this.canlock = true;
	}

	public EntitySA_LaserAA(FMLPlayMessages.SpawnEntity packet, World worldIn) {//
		super(AdvanceArmy.ENTITY_LAA, worldIn);
	}
	public Vector2f getLockVector() {
	  return new Vector2f(this.turretPitch, this.turretYaw);
	}
	protected void registerGoals() {
		this.goalSelector.addGoal(2, new VehicleLockGoal(this, true));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(1, new VehicleSearchTargetGoalSA<>(this, MobEntity.class, 10, 125F, false, (attackentity) -> {return this.CanAttack(attackentity);}));
		this.targetSelector.addGoal(2, new VehicleSearchTargetGoalSA<>(this, PlayerEntity.class, 10, 125F, false, (attackentity) -> {return this.CanAttack(attackentity);}));
	}
	LivingEntity lockTarget = null;
	public void weaponActive1(){
		float fireX = this.fireposX1;
		if(this.getRemain1()%2==0){
			fireX = this.fireposX1;
		}else{
			fireX = -this.fireposX1;
		}
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = MathHelper.sqrt((this.fireposZ1 - this.firebaseZ)* (this.fireposZ1 - this.firebaseZ) + (this.fireposX1 - 0)*(this.fireposX1 - 0)) * MathHelper.sin(-this.turretPitch  * (1 * (float) Math.PI / 180));
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F) * this.fireposZ1;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F) * this.fireposZ1;
		xx11 -= MathHelper.sin(this.turretYaw * 0.01745329252F + 1.57F) * fireX;
		zz11 += MathHelper.cos(this.turretYaw * 0.01745329252F + 1.57F) * fireX;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger()!=null)shooter = ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger();
		{
			if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("LaserFlashGun", null, this.getX()+xx11, this.getY()+this.fireposY1+base, this.getZ()+zz11, 0, 0, 0, 2);
		}
		Vector3d locken = Vector3d.directionFromRotation(this.getLockVector());//getLookAngle
		float d = 120;
		int range = 3;
		double ix = 0;
		double iy = 0;
		double iz = 0;
		boolean stop = false;
		int pierce = 0;
		this.playSound(firesound1, 5.0F, 1.0F);
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (this.getX()+xx11 + locken.x * xxx);
			iy = (this.getY()+this.fireposY1+base + locken.y * xxx);
			iz = (this.getZ()+zz11 + locken.z * xxx);
			BlockPos blockpos = new BlockPos(ix, iy, iz);
			BlockState iblockstate = this.level.getBlockState(blockpos);
			if (!iblockstate.isAir(this.level, blockpos)&& !iblockstate.getCollisionShape(this.level, blockpos).isEmpty()){
				break;
			}else{
				AxisAlignedBB axisalignedbb = (new AxisAlignedBB(ix-range, iy-range, iz-range, 
						ix+range, iy+range, iz+range)).inflate(1D);
				List<Entity> llist = this.level.getEntities(this,axisalignedbb);
				if (llist != null) {
					for (int lj = 0; lj < llist.size(); lj++) {
						Entity entity1 = (Entity) llist.get(lj);
						if (entity1 != null && entity1 instanceof LivingEntity) {
							if (NotFriend(entity1) && entity1 != shooter && entity1 != this) {
								lockTarget = (LivingEntity)entity1;
								if(lockTarget.getVehicle()!=null && lockTarget.getVehicle() instanceof LivingEntity){
									LivingEntity ve = (LivingEntity)lockTarget.getVehicle();
									ve.invulnerableTime = 0;
									ve.hurt(DamageSource.thrown(this, shooter), 30);
									ve.setSecondsOnFire(8);
								}else{
									lockTarget.invulnerableTime = 0;
									lockTarget.hurt(DamageSource.thrown(this, shooter), 30);
									lockTarget.setSecondsOnFire(8);
								}
								stop = true;
								ix=lockTarget.getX();
								iy=lockTarget.getY();
								iz=lockTarget.getZ();
								break;
							}
						}
					}
				}
				if(stop){
					++pierce;
					if(pierce>2)break;
				}
			}
		}
		WMExplosionBase.createExplosionDamage(this, ix, iy+1.5D, iz,10, 2, false,  false);
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("LaserHit", null, ix, iy+1.5D, iz, 0, 0, 0, 2);
		MessageTrail messageBulletTrail = new MessageTrail(true, 2, "advancearmy:textures/entity/flash/aa_beam" ,this.getX()+xx11, this.getY()+this.fireposY1-1.5F+base, this.getZ()+zz11, this.getDeltaMovement().x, this.getDeltaMovement().z, ix, iy+0.5D, iz, 15F, 1);
		PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), 80, this.level.dimension())), messageBulletTrail);
	}
}