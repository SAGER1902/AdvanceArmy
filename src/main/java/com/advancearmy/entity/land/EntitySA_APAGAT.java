package advancearmy.entity.land;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.entity.player.PlayerEntity;
public class EntitySA_APAGAT extends EntitySA_LandBase{
	public EntitySA_APAGAT(EntityType<? extends EntitySA_APAGAT> sodier, World worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0.6F;
		seatPosY[0] = 2.8F;
		seatPosZ[0] = 1.2F;
		this.attack_height_max = 100;
		this.attack_range_max = 50;
		this.is_aa = true;
		seatTurret[0] = true;
		seatHide[0] = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/aa.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		this.w1name = "23mmAA";
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.maxUpStep = 1.5F;
		this.soundspeed=0.7F;
		this.canNightV=true;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -75;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.04F;
        this.turnSpeed = 1.5F;
		this.turretSpeed = 0.6F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.icon1tex = new ResourceLocation("advancearmy:textures/hud/apagathead.png");
		this.icon2tex = new ResourceLocation("advancearmy:textures/hud/apagatbody.png");
		this.w1barrelsize = 0.1F;
		this.ammo1=3;
		this.fireposX1 = 0;
		this.fireposY1 = 3.28F;
		this.fireposZ1 = 2.98F;
		this.firebaseX = 0;
		this.firebaseZ = 0F;
		this.armor_front = 30;
		this.armor_side = 10;
		this.armor_back = 10;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.haveTurretArmor = true;
		this.armor_turret_height = 2;
		this.armor_turret_front = 30;
		this.armor_turret_side = 10;
		this.armor_turret_back = 10;
		this.obj = new SAObjModel("advancearmy:textures/mob/apagat.obj");
		this.tex = new ResourceLocation("advancearmy:textures/mob/apagat.png");
		//this.tracktex = new ResourceLocation("advancearmy:textures/mob/track.png");
		this.magazine = 400;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_chaingun.get();
		this.firesound1 = SASoundEvent.fire_gat.get();
		
		this.startsound = SASoundEvent.start_m6.get();
		this.movesound = SASoundEvent.move_track2.get();
		
		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/23mm.png";
		this.w3icon="wmlib:textures/hud/cloud.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		
		this.wheelcount = 7;
		this.setWheel(0,0, 0.82F, 4.13F);
		this.setWheel(1,0, 0.53F, 3.33F);
		this.setWheel(2,0, 0.53F, 2.27F);
		this.setWheel(3,0, 0.53F, 1.11F);
		this.setWheel(4,0, 0.53F, -0.05F);
		this.setWheel(5,0, 0.53F, -1.16F);
		this.setWheel(6,0, 0.82F, -1.95F);
	}

	public EntitySA_APAGAT(FMLPlayMessages.SpawnEntity packet, World worldIn) {//
		super(AdvanceArmy.ENTITY_APAGAT, worldIn);
	}
	


	public void tick() {
		super.tick();
		if (this.getFirstSeat() != null && this.getFirstSeat().getControllingPassenger()!=null){
			if (this.getFirstSeat() != null) {
				EntitySA_Seat seat = (EntitySA_Seat)this.getFirstSeat();
				if(seat.keyv){
					if(cooltime3>150)cooltime3=0;
					if(this.getRemain4() > 0){
						if(this.getHealth() < this.getMaxHealth() && this.getHealth() > 0.0F) {
							++healtime;
							if(healtime > 2){
								this.setHealth(this.getHealth() + 1);
								this.playSound(SASoundEvent.fix.get(), 1.0F, 1.0F);
								healtime=0;
							}
						}
					}
					if(cooltime3>80){
						this.setRemain4(0);
						seat.keyv = false;
					}
				}
				if(seat.keyx){
					if(this.getRemain3() > 0){
						if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX("TankSmoke", null, this.getX(), this.getY(), this.getZ(), 0, 0, 0, 1);
						this.playSound(SASoundEvent.shell_impact.get(), 3.0F, 1.0F);
						this.setRemain3(0);
					}
					seat.keyx = false;
				}
			}
		}
	}

	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/bullet30mm.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet1.png";
		String fx1 = "SmokeGun";
		String fx2 = null;//SAAPTrail
		double b = 0;
		double a = 0;
		double ax = 0;
		double az = 0;
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger()!=null)shooter = ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, fx1, fx2, firesound1,
		1F, this.fireposX1,this.fireposY1+0.2F,this.fireposZ1,this.firebaseX,this.firebaseZ,
		ax + this.getX(), this.getY()+b, az + this.getZ(),this.turretYaw, this.turretPitch,
		17, 6F, 1.1F, 1, false, 1, 0.025F, 40, 0);
	}
}