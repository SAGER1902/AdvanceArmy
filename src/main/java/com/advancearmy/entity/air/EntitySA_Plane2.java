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

import net.minecraft.util.ResourceLocation;
import wmlib.client.obj.SAObjModel;
import advancearmy.entity.EntitySA_AirBase;
import net.minecraft.util.math.MathHelper;
import advancearmy.entity.EntitySA_Seat;
import net.minecraft.entity.player.PlayerEntity;
import safx.util.EntityCondition;
import safx.SagerFX;
public class EntitySA_Plane2 extends EntitySA_AirBase{
	public EntitySA_Plane2(EntityType<? extends EntitySA_Plane2> sodier, World worldIn) {
		super(sodier, worldIn);
		seatPosX[0] = 0;
		seatPosY[0] = 1.4F;
		seatPosZ[0] = 0F;
		this.renderHudIcon = false;
		this.renderHudOverlay = false;
		this.renderHudOverlayZoom = false;
		seatMaxCount = 1;
		this.render_hud_box = false;
		VehicleType = 4;
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		seatView3X=0F;
		seatView3Y=-4F;
		seatView3Z=-12F;
		this.noCulling = true;
        this.MoveSpeed = 0.036F;
		this.fly_height = 60;
		this.w1aa = false;
		this.w1max = 18;
		this.w1min = -10;
		this.w1aim = 5;
		this.w2aa = false;
		this.w2max = 100;
		this.w2min = 5;
		this.w2aim = 5;
		this.stayrange = 40;
		this.min_height = 10;
		this.icon1tex = null;
		this.icon2tex = new ResourceLocation("advancearmy:textures/hud/plane2icon.png");
        this.turnSpeed = 2F;
		this.maxUpStep = 1.5F;
		this.flyPitchMax = 90F;
		this.flyPitchMin = -90F;
        this.throttleMax = 20F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.2F;
		this.thBackSpeed = -0.15F;
		this.obj = new SAObjModel("advancearmy:textures/mob/plane2.obj");
		this.tex = new ResourceLocation("advancearmy:textures/mob/plane2.png");
		this.ammo2=5;
		this.firesound1 = SASoundEvent.bomb_release.get();
		this.firesound2 = SASoundEvent.fire_m230.get();
		
		this.reloadSound1 = SASoundEvent.bomb_reload.get();
		this.reloadSound2 = SASoundEvent.reload_mag.get();
		this.magazine = 4;
		this.reload_time1 = 200;
		this.magazine2 = 400;
		this.weaponCount = 2;
		this.w1name = "轻型航空炸弹";
		this.w2name = "20毫米机炮";
		this.w1icon="wmlib:textures/hud/bomb.png";
		this.w2icon="wmlib:textures/hud/ap30mm.png";
		this.fireposX2 = 2.97F;
		this.fireposY2 = 1.05F;
		this.fireposZ2 = 1.59F;
		this.w2cross = true;
		this.w1showammo = true;
		this.rotorcount = 1;
		this.rotor_rotez[0]=10;
		this.setRotor(0,0, 1.44F, 4.5F);
		this.rotortex = new ResourceLocation("advancearmy:textures/mob/plane2rotor.png");
	}
	public EntitySA_Plane2(FMLPlayMessages.SpawnEntity packet, World worldIn) {
		super(AdvanceArmy.ENTITY_PLANE2, worldIn);
	}
	
	/*boolean trail = false;
	public void tick() {
		super.tick();
		if(this.getHealth()>0){
			if(!this.isOnGround() && (this.getMovePitch()>0.1F||this.getMovePitch()<-0.1F||this.getMoveYaw()>0.1F||this.getMoveYaw()<-0.1F)){
				if(!trail){
					if(ModList.get().isLoaded("safx")){
						if(this.level.isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, 4f, -0.5f, -3.0f, true, EntityCondition.ENTITY_ALIVE);
						if(this.level.isClientSide)SagerFX.proxy.createFXOnEntityWithOffset("PlaneTrail", this, -4f, -0.5f, -3.0f, true, EntityCondition.ENTITY_ALIVE);
					}
					trail = true;
				}
			}else{
				if(trail)trail = false;
			}
		}
	}*/
	
	public void weaponActive1(){
		float fireX = 0;
		if(this.getRemain1()%2==0){
			fireX = 3;
		}else{
			fireX = -3;
		}
		String model = "advancearmy:textures/entity/bullet/smallbomb.obj";
		String tex = "advancearmy:textures/entity/bullet/bomb.png";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger()!=null)shooter = ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, null, null, firesound1,
		1F, fireX,0,0,0.53F,-2.89F,
		this.getX(), this.getY(), this.getZ(),this.yRot, this.turretPitch,
		50, 1F, 1.1F, 4, false, 1, 0.04F, 500, 3);
	}
	public void weaponActive2(){
		String model = "advancearmy:textures/entity/bullet/bullet12.7.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet12.7.png";
		LivingEntity shooter = this;
		float fireX = 0;
		if(this.getRemain2()%2==0){
			fireX = this.fireposX2;
		}else{
			fireX = -this.fireposX2;
		}
		if(this.getFirstSeat() != null && ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger()!=null)shooter = ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, null, 3, model, tex, "SmokeGun", null, firesound2,
		1F, fireX,this.fireposY2,this.fireposZ2,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.yRot, this.turretPitch,
		9, 8F, 1.5F, 1, false, 1, 0.01F, 50, 0);
	}
}