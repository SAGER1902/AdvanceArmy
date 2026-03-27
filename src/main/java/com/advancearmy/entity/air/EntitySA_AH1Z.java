package advancearmy.entity.air;

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
import advancearmy.entity.EntitySA_HeliBase;
import net.minecraft.util.math.MathHelper;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.entity.player.PlayerEntity;

public class EntitySA_AH1Z extends EntitySA_HeliBase{
	
	public EntitySA_AH1Z(EntityType<? extends EntitySA_AH1Z> sodier, World worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0;
		seatPosY[0] = 0.8F;
		seatPosZ[0] = 0;
		seatPosX[1] = 0;
		seatPosY[1] = 0.4F;
		seatPosZ[1] = 1.7F;
		seatTurret[1] = false;
		this.canlock = true;
		this.is_aa = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/line.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		this.icon1tex = null;
		this.icon2tex = new ResourceLocation("advancearmy:textures/hud/ah1zicon.png");
		VehicleType = 3;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-10F;
		seatMaxCount = 2;
        this.MoveSpeed = 0.035F;
        this.turnSpeed = 2.2F;
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
	
		this.magazine = 12;
		this.reload_time1 = 380;
		this.reloadSound1 = SASoundEvent.reload_missile.get();
		
		this.magazine2 = 2;
		this.reload_time2 = 300;
		this.reloadSound2 = SASoundEvent.reload_missile.get();
		
		this.magazine3 = 12;
		this.reload_time3 = 200;
		
		this.startsound = SASoundEvent.start_ah.get();
		this.movesound = SASoundEvent.heli_move.get();
		
		this.firesound1 = SASoundEvent.fire_missile.get();
		this.firesound2 = SASoundEvent.fire_missile.get();
		
		this.ammo1=5;
		this.ammo2=5;
		this.fireposX1 = 1.47F;
		this.fireposY1 = 0.62F;
		this.fireposZ1 = -1.2F;
		this.fireposX2 = 2.13F;
		this.fireposY2 = 0.69F;
		this.fireposZ2 = -0.8F;
		this.firebaseX = 0;
		this.firebaseZ = 0;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/ah1z.obj");
		this.tex = new ResourceLocation("advancearmy:textures/mob/ah1z.png");
		this.canNightV=true;
		this.mgobj = new SAObjModel("advancearmy:textures/mob/20mm.obj");
		this.rotortex1 = new ResourceLocation("advancearmy:textures/mob/ah1zrotor.png");
		this.rotortex2 = new ResourceLocation("advancearmy:textures/mob/ah1zrotor2.png");

		this.setMg(0F, -0.14F, 2.56F, 0.21F);
		this.rotorcount = 2;
		this.rotor_rotey[0]=10;
		this.rotor_rotex[1]=10;
		this.setRotor(0,0, 3.2F, -1.3F);
		this.setRotor(1,0.54F, 2.96F, -9.39F);

		this.weaponCount = 4;
		this.w1icon="advancearmy:textures/hud/hy70.png";
		this.w2icon="advancearmy:textures/hud/aim9x.png";
		this.w3icon="wmlib:textures/hud/flare.png";
		this.w4icon="wmlib:textures/hud/repair.png";
		this.w1name = "70毫米九头蛇火箭弹";
		this.w2name = "AIM9X空空导弹";
		this.w4name = "紧急维修";
	}
	
	public EntitySA_AH1Z(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_AH1Z, worldIn);
	}
	
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if (this.getAnySeat(1) != null){//
				EntitySA_Seat seat = (EntitySA_Seat)this.getAnySeat(1);
				if(this.setSeat){
					String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
					String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
					String model2 = "advancearmy:textures/entity/bullet/agm114.obj";
					String tex2 = "advancearmy:textures/entity/bullet/agm114.png";
					String fx1 = "SmokeGun";
					String fx2 = null;
					seat.seatProtect = 0.9F;
					seat.attack_range_max = 50;
					seat.attack_height_max = 10;
					seat.attack_height_min = -90;
					seat.render_hud_box = true;
					seat.w1name="20毫米加特林机炮/AGM114地狱火导弹";
					seat.w1icon="advancearmy:textures/hud/minigun20mm.png";
					seat.w2icon="advancearmy:textures/hud/agm114.png";
					seat.hud_box_obj = "wmlib:textures/hud/gunner.obj";
					seat.hud_box_tex = "wmlib:textures/hud/box.png";
					seat.seatHide = false;
					seat.seatHide = false;
					seat.weaponCount = 2;
					
					seat.canNightV=true;
					seat.gunner_aim=true;
					seat.turretPitchMax = 0;
					seat.minyaw=-90;
					seat.maxyaw=90;
					//seat.canlock = true;
					seat.ammo1 = 2;
					seat.magazine = 200;
					seat.reload_time1 = 100;
					seat.ammo2 = 60;
					seat.magazine2 = 4;
					seat.reload_time2 = 100;
					seat.reloadSound1 = SASoundEvent.reload_chaingun.get();
					seat.reloadSound2 = SASoundEvent.reload_missile.get();
					seat.setWeapon(0, 3, model, tex, fx1, fx2, SASoundEvent.fire_gatling.get(), 0,-1,2,0,0.38F,
					12, 6F, 1.25F, 1, false, 1, 0.01F, 20, 0);
					
					seat.weaponcross[1] = true;
					seat.followvehicle[1] = true;
					seat.setWeapon(1, 4, model2, tex2, null, "SAMissileSmoke", SASoundEvent.fire_agm114.get(), this.fireposX2,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
					125, 3F, 1.05F, 4, false, 1, 0.01F, 200, 6);
				}
				this.turretYaw_1=seat.getYHeadRot();
				this.turretPitch_1=seat.turretPitch;
			}
		}
	}
	
	public void weaponActive1(){
		float fireX = 0;
		if(this.getRemain1()%2==0){
			fireX = this.fireposX1;
		}else{
			fireX = -this.fireposX1;
		}
		String model = "advancearmy:textures/entity/bullet/hy70.obj";
		String tex = "advancearmy:textures/entity/bullet/hy70.png";
		String fx1 = "SmokeGun";
		String fx2 = "SAMissileTrail";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, fx2, firesound1,
		1F, fireX,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.yRot, this.turretPitch,
		45, 3F, 1.1F, 2, false, 1, 0.001F, 50, 3);
	}
	public void weaponActive2(){
		float fireX = 0;
		if(this.getRemain2()%2==0){
			fireX = this.fireposX2;
		}else{
			fireX = -this.fireposX2;
		}
		String model = "advancearmy:textures/entity/bullet/aim9x.obj";
		String tex = "advancearmy:textures/entity/bullet/aim9x.png";

		String fx2 = "SAMissileSmoke";
		LivingEntity shooter = this;
		
		
		if(this.getFirstSeat() != null && this.getFirstSeat().getAnyPassenger()!=null)shooter = this.getFirstSeat().getAnyPassenger();
		Entity locktarget = null;
		if(this.getFirstSeat() != null && ((EntitySA_Seat)this.getFirstSeat()).mitarget!=null){
			locktarget = ((EntitySA_Seat)this.getFirstSeat()).mitarget;
		}else{
			locktarget = this.getTarget();
		}
		AI_EntityWeapon.Attacktask(this, shooter, locktarget, 4, model, tex, null, fx2, firesound2,
		1F, fireX,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.yRot, this.turretPitch,
		35, 3F, 1.5F, 2, false, 1, 0.01F, 250, 0);
	}
}