package advancearmy.entity.turret;
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
import advancearmy.entity.EntitySA_TurretBase;
import net.minecraft.util.math.MathHelper;
import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.EntitySA_Seat;

public class EntitySA_Mortar extends EntitySA_TurretBase{
	public EntitySA_Mortar(EntityType<? extends EntitySA_Mortar> sodier, World worldIn) {
		super(sodier, worldIn);
		this.isturret=true;
		this.onlythrow=true;
		this.isthrow = true;
		this.throwspeed = 8F;
		this.throwgrav = 0.1F;
		seatPosX[0] = 1.07F;
		seatPosY[0] = 0;
		seatPosZ[0] = 0;
		this.renderHudOverlay = false;
		this.renderHudIcon = false;
		this.renderHudOverlayZoom = false;
		this.w1name = "81毫米迫击炮";
		this.seatView1X = 0F;
		this.seatView1Y = 0F;
		this.seatView1Z = 0.01F;
		seatTurret[0] = true;
		this.w1recoilp = 2;
		this.w1recoilr = 8;
		seatView3X=0F;
		seatView3Y=-2.5F;
		seatView3Z=-6F;
		this.seatProtect = 1F;
		this.turretPitchMax = -90;
		this.turretPitchMin = -45;
		this.fire1tex = new ResourceLocation("advancearmy:textures/entity/flash/muzzleflash3.png");
		
		this.turretSpeed = 0.2F;
		this.ammo1=5;
		this.fireposX1 = 0;
		this.fireposY1 = 0.36F;
		this.fireposZ1 = 1.95F;
		this.firebaseX = 0;
		this.firebaseZ = 0F;
		
		this.obj = new SAObjModel("advancearmy:textures/mob/mortar.obj");
		this.tex = new ResourceLocation("advancearmy:textures/mob/mortar.png");
		this.icon1tex = new ResourceLocation("advancearmy:textures/hud/mortarhead.png");
		this.icon2tex = new ResourceLocation("advancearmy:textures/hud/mortarbody.png");
		this.magazine = 1;
		this.reload_time1 = 95;
		this.reloadSound1 = SASoundEvent.reload_81mm_m1.get();
		this.firesound1 = SASoundEvent.fire_81mm_m1.get();

		this.weaponCount = 1;
		this.w1icon="wmlib:textures/hud/heat120mm.png";
	}

	public EntitySA_Mortar(FMLPlayMessages.SpawnEntity packet, World worldIn) {//
		super(AdvanceArmy.ENTITY_MORTAR, worldIn);
	}
	
	public void weaponActive1(){
		String model = "advancearmy:textures/entity/bullet/bulletcannon_small.obj";
		String tex = "advancearmy:textures/entity/bullet/bullet.png";
		String fx1 = "AdvTankFire";
		LivingEntity shooter = this;
		if(this.getFirstSeat() != null && ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger()!=null)shooter = ((EntitySA_Seat)this.getFirstSeat()).getAnyPassenger();
		AI_EntityWeapon.Attacktask(this, shooter, this.getTarget(), 3, model, tex, fx1, null, firesound1,
		1F, this.fireposX1,this.fireposY1,this.fireposZ1,this.firebaseX,this.firebaseZ,
		this.getX(), this.getY(), this.getZ(),this.turretYaw, this.turretPitch,
		40, this.throwspeed, 1F, 4, false, 1, this.throwgrav, 5000, 3);
	}
}