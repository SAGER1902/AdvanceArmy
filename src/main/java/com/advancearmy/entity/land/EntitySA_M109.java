package advancearmy.entity.land;
import net.minecraftforge.fml.ModList;
import net.minecraft.world.World;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.text.TranslationTextComponent;
import advancearmy.entity.EntitySA_Seat;

public class EntitySA_M109 extends EntitySA_LandBase{
	public EntitySA_M109(EntityType<? extends EntitySA_M109> sodier, World worldIn) {
		super(sodier, worldIn);
		this.isthrow = true;
		this.throwspeed = 8F;
		this.throwgrav = 0.1F;
		seatPosX[0] = 1.07F;
		seatPosY[0] = 1.1F;
		seatPosZ[0] = 2.7F;
		seatTurret[0] = true;
		seatHide[0] = true;
		this.render_hud_box = true;
		this.hud_box_obj = "wmlib:textures/hud/box.obj";
		this.hud_box_tex = "wmlib:textures/hud/box.png";
		
		this.renderHudIcon = false;
		this.renderHudOverlay = true;
		this.hudOverlay = "wmlib:textures/misc/cannon_scope.png";
		this.renderHudOverlayZoom = true;
		this.hudOverlayZoom = "wmlib:textures/misc/tank_scope.png";
		this.w1name = new TranslationTextComponent("advancearmy.weapon.155cannon.desc").getString();
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		this.armor_front = 15;
		this.armor_side = 10;
		this.armor_back = 10;
		this.armor_top = 10;
		this.armor_bottom = 10;
		this.canNightV=true;
		this.w1recoilp = 8;
		this.w1recoilr = 8;
		this.icon1tex = new ResourceLocation("advancearmy:textures/hud/m109head.png");
		this.icon2tex = new ResourceLocation("advancearmy:textures/hud/m109body.png");
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 0.1F;
		this.turretPitchMax = -90;
		this.turretPitchMin = 10;
        this.MoveSpeed = 0.03F;
        this.turnSpeed = 1.25F;
		this.turretSpeed = 0.2F;
        this.throttleMax = 5F;
		this.throttleMin = -2F;
		this.thFrontSpeed = 0.3F;
		this.thBackSpeed = -0.3F;
		this.maxUpStep = 1.5F;
		this.soundspeed=0.7F;
		this.ammo1=5;
		this.fireposX1 = 0;
		this.fireposY1 = 2.35F;
		this.fireposZ1 = 7.4F;
		this.firebaseX = 0;
		this.firebaseZ = 1.32F;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/m109.obj");
		this.tex = new ResourceLocation("advancearmy:textures/mob/m109.png");
		this.tracktex = new ResourceLocation("advancearmy:textures/mob/track.png");
		this.magazine = 1;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_m1a2.get();
		this.firesound1 = SASoundEvent.fire_lw155.get();
		
		this.startsound = SASoundEvent.start_m6.get();
		this.movesound = SASoundEvent.move_track2.get();
		
		this.weaponCount = 1;
		this.w1icon="wmlib:textures/hud/heat120mm.png";
		
		this.wheelcount = 9;
		this.setWheel(0,0, 0.75F, 4.33F);
		this.setWheel(1,0, 0.47F, 3.63F);
		this.setWheel(2,0, 0.47F, 2.75F);
		this.setWheel(3,0, 0.47F, 1.87F);
		this.setWheel(4,0, 0.47F, 1F);
		this.setWheel(5,0, 0.47F, 0.12F);
		this.setWheel(6,0, 0.47F, -0.76F);
		this.setWheel(7,0, 0.47F, -1.64F);
		this.setWheel(8,0, 0.75F, -2.33F);
	}

	public EntitySA_M109(FMLPlayMessages.SpawnEntity packet, World worldIn) {//
		super(AdvanceArmy.ENTITY_TANK, worldIn);
	}
	public int getUnitType(){
		return 2;
	}
	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/bulletcannon.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "AdvTankFire";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger()!=null)shooter = ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, null, firesound1,
		1F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		80, this.throwspeed, 1F, 6, false, 1, this.throwgrav, 5000, 3);
	}
}