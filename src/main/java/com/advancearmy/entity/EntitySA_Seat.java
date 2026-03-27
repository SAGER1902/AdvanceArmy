package advancearmy.entity;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.SoundEvents;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.ActionResultType;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraftforge.fml.ModList;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.client.Minecraft;

import java.util.Random;
import org.lwjgl.glfw.GLFW;
import wmlib.util.ThrowBullet;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageFire;
import wmlib.common.network.message.MessageThrottle;
import wmlib.client.RenderParameters;
import static wmlib.client.RenderParameters.*;
import wmlib.common.living.EntityWMSeat;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.living.AI_MissileLock;
import advancearmy.entity.ai.AI_EntityWeapon;
import advancearmy.AdvanceArmy;
import advancearmy.event.SASoundEvent;
import net.minecraft.util.DamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.AxisAlignedBB;
import wmlib.common.world.WMExplosionBase;
import wmlib.common.network.PacketHandler;
import wmlib.common.network.message.MessageTrail;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.util.math.vector.Vector2f;
import safx.SagerFX;
import net.minecraft.entity.EntityPredicate;
import wmlib.WarMachineLib;

import wmlib.api.ITool;
public class EntitySA_Seat extends EntityWMSeat{
	//protected PathNavigator navigationsa;
	public EntitySA_Seat(EntityType<? extends EntitySA_Seat> sodier, World worldIn) {
		super(sodier, worldIn);
		//this.navigationsa = this.createNavigation(worldIn);
		this.seatTurret=true;
		this.lockTargetSound=SASoundEvent.growler_lock.get();
	}

	public boolean ridding_rotemgPitch = false;
	public boolean seatRotePitch = false;
	public int seatMaxCount = 1;
	public double[] seatPosX = new double[1];
	public double[] seatPosY = new double[1];
	public double[] seatPosZ = new double[1];
	public double[] seatRoteX = new double[1];
	public double[] seatRoteY = new double[1];
	public double[] seatRoteZ = new double[1];
	
	public EntitySA_Seat(FMLPlayMessages.SpawnEntity packet, World worldIn) {//
		super(AdvanceArmy.ENTITY_SEAT, worldIn);
	}
	
	protected boolean canAddPassenger(Entity passenger) {//
		return this.getPassengers().size() < 1;
	}
	public boolean canBeCollidedWith() {//
		return !this.removed;
	}
	public boolean canRid() {//
		return this.getPassengers().size() < 1;
	}
	
	public int interact_time = 0;
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		if(player.getVehicle()==null){
			{
				if(this.canAddPassenger(player)&&!player.isSecondaryUseActive()){
					if (!this.level.isClientSide) {
						player.startRiding(this);
					}
					return ActionResultType.sidedSuccess(this.level.isClientSide);
				}else{
					if(this.getAnyPassenger()!=null&&this.getAnyPassenger() instanceof TameableEntity){
						if(((TameableEntity)this.getAnyPassenger()).getOwner()==player||player.isCreative()){
							this.getAnyPassenger().unRide();
						}
					}
					return ActionResultType.sidedSuccess(this.level.isClientSide);
				}
			}
		}
		return super.mobInteract(player, hand);
    }
	
	public static float clampCount(float f1, float f2, float min, float max) {
		float x = f1-f2;
		//clampYaw(x);
		if (x < min) {
			return (f2+min);
		} else {
			return x > max ? (f2+max) : f1;
		}
	}
	@OnlyIn(Dist.CLIENT)
	public void applyOrientationToEntity(Entity passenger) {//乘客方向
		this.applyYawToTurret(passenger);
	}
	@OnlyIn(Dist.CLIENT)
	public void onPassengerTurned(Entity passenger) {
		this.applyYawToTurret(passenger);
	}
	public int startRidTime = 0;
	protected void applyYawToTurret(Entity passenger){
		if(this.getVehicle()!=null){
			int i = this.getVehicle().getPassengers().indexOf(this);
			if(i==0){
				if(this.getVehicle() instanceof WeaponVehicleBase){
					WeaponVehicleBase ve = (WeaponVehicleBase)this.getVehicle();
					if(startRidTime<10&&this.getControllingPassenger()!=null){
						float currentYaw = passenger.yRot;
						float targetYaw = ve.turretYaw;
						float angleDiff = targetYaw - currentYaw;
						if (angleDiff > 180.0F) {
							angleDiff -= 360.0F;
						} else if (angleDiff < -180.0F) {
							angleDiff += 360.0F;
						}
						if (MathHelper.abs(angleDiff) > 170.0F && Math.abs(angleDiff) < 190.0F) {
							passenger.yRot=(targetYaw);
						} else {
							passenger.yRot=(MathHelper.lerp(0.5F, currentYaw, targetYaw));
						}
						passenger.xRot=(MathHelper.lerp(0.5F, passenger.xRot, ve.turretPitch));
					}else{
						if(ve.VehicleType>2){
							if(ve.getArmyType1()==0){
								float currentYaw = passenger.yRot;
								float targetYaw = ve.yRot;
								float angleDiff = targetYaw - currentYaw;
								if (angleDiff > 180.0F) {
									angleDiff -= 360.0F;
								} else if (angleDiff < -180.0F) {
									angleDiff += 360.0F;
								}
								if (MathHelper.abs(angleDiff) > 170.0F && Math.abs(angleDiff) < 190.0F) {
									passenger.yRot=(targetYaw);
								} else {
									passenger.yRot=(MathHelper.lerp(0.5F, currentYaw, targetYaw));
								}
								passenger.xRot=(MathHelper.lerp(0.5F, passenger.xRot, ve.flyPitch));
							}
						}else{
							if(!ve.isthrow || ve.getMoveMode()==0){
								if(this.minyaw>-360F && this.maxyaw<360F){
									float f1 = clampCount(passenger.yRot, this.yRot, this.minyaw, this.maxyaw);
									passenger.yRotO = f1;
									passenger.yRot = f1;
								}
								float f2 = MathHelper.wrapDegrees(passenger.xRot - 0);//this.xRot
								float f22 = MathHelper.clamp(f2, this.turretPitchMax, this.turretPitchMin);
								passenger.xRotO += f22 - f2;
								passenger.xRot += f22 - f2;
								passenger.xRot = passenger.xRot;
							}
						}
					}
				}
			}else{
				if(this.getVehicle() instanceof WeaponVehicleBase){
					WeaponVehicleBase ve = (WeaponVehicleBase)this.getVehicle();
					if(this.minyaw>-360F && this.maxyaw<360F){
						float f1 = clampCount(passenger.yRot, this.yRot, this.minyaw, this.maxyaw);
						passenger.yRotO = f1;
						passenger.yRot = f1;
					}
					float f2 = MathHelper.wrapDegrees(passenger.xRot - ve.flyPitch);//
					float f22 = MathHelper.clamp(f2, this.turretPitchMax, this.turretPitchMin);
					passenger.xRotO += f22 - f2;
					passenger.xRot += f22 - f2;
					passenger.xRot = passenger.xRot;
				}
			}
		}
    }
	public float w1recoilp = 0.5F;
	public float w1recoilr = 1;
	public static boolean powerfire = false;
	int changetime = 0;
	int aimtime = 0;
	int keytime = 0;
	@OnlyIn(Dist.CLIENT)
    public void onClientUpdate()
    {
		Minecraft mc = Minecraft.getInstance();
        if (this.getControllingPassenger()!=null && mc.screen==null){
			if(aimtime<10)++aimtime;
			if(keytime<15)++keytime;
			if(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS){
				PacketHandler.getPlayChannel().sendToServer(new MessageFire(1));
				if(!this.powerfire)PacketHandler.getPlayChannel().sendToServer(new MessageFire(8));
			}else{
				if(this.powerfire)PacketHandler.getPlayChannel().sendToServer(new MessageFire(9));
			}
			if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS){
				PacketHandler.getPlayChannel().sendToServer(new MessageFire(2));
			}
			if(GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS){
				if(aimtime>8){
					if(this.isZoom){
						this.isZoom = false;
						this.getControllingPassenger().playSound(SoundEvents.BARREL_CLOSE, 1.0F, 1.0F);
					}else{
						this.isZoom = true;	
						this.getControllingPassenger().playSound(SoundEvents.BARREL_OPEN, 1.0F, 1.0F);
					}
					aimtime=0;
				}
			}
			
			if(keytime>8){
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_F) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(3));
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(12));
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(4));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_G) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(5));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(6));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_V) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(7));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_R) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(10));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_C) == GLFW.GLFW_PRESS){
					//PacketHandler.getPlayChannel().sendToServer(new MessageFire(11));
					if(this.showhelp){
						this.showhelp = false;
					}else{
						this.showhelp = true;
					}
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_N) == GLFW.GLFW_PRESS && this.canNightV){
					if(this.openNightV){
						this.openNightV = false;
						this.getControllingPassenger().playSound(SoundEvents.BARREL_CLOSE, 1.0F, 1.0F);
					}else{
						this.openNightV = true;	
						this.getControllingPassenger().playSound(SoundEvents.BARREL_OPEN, 1.0F, 1.0F);
					}
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_J) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(13));
					keytime=0;
				}
				if(GLFW.glfwGetKey(mc.getWindow().getWindow(), GLFW.GLFW_KEY_K) == GLFW.GLFW_PRESS){
					PacketHandler.getPlayChannel().sendToServer(new MessageFire(14));
					keytime=0;
				}
			}
		}else{
			if(this.isZoom){
				this.isZoom = false;
			}
			if(this.showhelp){
				this.showhelp = false;
			}
		}
	}
	public boolean NotFriend(Entity entity){
		if(entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() > 0.0F && !(entity instanceof EntityWMSeat)){
			LivingEntity entity1 = (LivingEntity) entity;
			Team team = this.getTeam();
			Team team1 = entity1.getTeam();
			boolean canattack = true;
			if(entity instanceof TameableEntity && this.getAnyPassenger()!=null){
				TameableEntity soldier = (TameableEntity)entity;
				if(soldier.getOwner()!=null && soldier.getOwner()==this.getAnyPassenger())canattack=false;
				if(this.getAnyPassenger() instanceof TameableEntity){
					TameableEntity rider = (TameableEntity)entity;
					if(rider.getOwner()!=null && rider.getOwner()==soldier.getOwner())canattack=false;
				}
			}
			if(team != null && team1 == team)canattack= false;
			if(this.getTargetType()==2){
				if(entity instanceof IMob && ((LivingEntity) entity).getHealth() > 0.0F && (team == null||team != team1))canattack= false;
			}
			return canattack;
    	}else{
			return false;
		}
	}
	
	public void setSize(float w,float h){
		this.boxwidth = w;
		this.boxheight = h;
	}

	public int[] bulletid = new int[2];
	public int[] bullettype = new int[2];
	public int[] bulletdamage = new int[2];
	public int[] bulletcount = new int[2];
	public int[] bullettime = new int[2];
	public int reloadSoundStart1 = 20;
	public int reloadSoundStart2 = 20;
	public float[] bulletspeed = new float[2];
	public float[] bulletspread = new float[2];
	public float[] bulletexp = new float[2];
	public float[] bulletgravity = new float[2];
	public float[] fireposX = new float[2];
	public float[] fireposY = new float[2];
	public float[] fireposZ = new float[2];
	public float[] firebaseX = new float[2];
	public float[] firebaseZ = new float[2];
	public boolean[] bulletdestroy = new boolean[2];
	public boolean[] followvehicle = new boolean[2];
	public boolean[] weaponcross = new boolean[2];
	
	public boolean[] weaponThreeFire = new boolean[2];
	
	
	public String bulletmodel1 = "advancearmy:textures/entity/bullet/bullet.obj";
	public String bullettex1 = "advancearmy:textures/entity/bullet/bullet.png";
	public String firefx1 = "SmokeGun";
	public String bulletfx1 = null;
	public String bulletmodel2 = "advancearmy:textures/entity/bullet/bullet.obj";
	public String bullettex2 = "advancearmy:textures/entity/bullet/bullet.png";
	public String firefx2 = "SmokeGun";
	public String bulletfx2 = null;
	public SoundEvent reloadSound1;
	public SoundEvent reloadSound2;
	public SoundEvent firesound1;
	public SoundEvent firesound1_3p=null;;
	public SoundEvent firesound2;
	
	public boolean[] laserweapon = new boolean[2];
	public int[] connect_cout = new int[2];
	public boolean[] can_connect = new boolean[2];
	public float[] laserwidth = new float[2];
	public String laser_model_tex1 = "advancearmy:textures/entity/flash/aa_beam";
	public String laser_model_tex2 = "advancearmy:textures/entity/flash/aa_beam";
	public String laserfxfire1 = "LaserFlashGun";
	public String laserfxhit1 = "LaserHit";
	public String laserfxfire2 = "LaserFlashGun";
	public String laserfxhit2 = "LaserHit";
	
	public boolean isthrow = false;
	public boolean changethrow = true;
	public void setWeapon(int wpid,int id,
		String model, String tex, String fx1, String fx2, SoundEvent sound, 
		float w, float h, float z, float bx, float bz,
		int damage, float speed, float recoil, float ex, boolean extrue, int count,  float gra, int maxtime, int typeid){
		if(wpid<2){
			this.bulletid[wpid] = id;
			this.bullettype[wpid] = typeid;
			this.bulletdamage[wpid] = damage;
			this.bulletspeed[wpid] = speed;
			this.bulletspread[wpid] = recoil;
			this.bulletexp[wpid] = ex;
			this.bulletdestroy[wpid] = extrue;
			this.bulletcount[wpid] = count;
			this.bulletgravity[wpid] = gra;
			this.bullettime[wpid] = maxtime;
			if(wpid==0){
				this.bulletmodel1 = model;
				this.bullettex1 = tex;
				this.firefx1 = fx1;
				this.bulletfx1 = fx2;
				this.firesound1 = sound;
			}else{
				this.bulletmodel2 = model;
				this.bullettex2 = tex;
				this.firefx2 = fx1;
				this.bulletfx2 = fx2;
				this.firesound2 = sound;
			}
			this.fireposX[wpid] = w;
			this.fireposY[wpid] = h;
			this.fireposZ[wpid] = z;
			this.firebaseX[wpid] = bx;
			this.firebaseZ[wpid] = bz;
		}
	}
	
	public void weaponActive1(){
		float side = 1.57F;
		float fire_x = this.fireposX[0];
		/*if(weaponThreeFire[0]){
			if(this.getRemain1()==2){
				fire_x = 0;
			}else if(this.getRemain1()==1){
				fire_x = -this.fireposX[0];
			}
		}else*/{
			if(this.weaponcross[0]){
				if(this.getRemain1()%2==0){
					side = -1.57F;
				}else{
					side = 1.57F;
				}
			}
		}
		

		Entity locktarget = null;
		if(this.mitarget!=null){
			locktarget = this.mitarget;
		}else{
			locktarget = this.getTarget();
		}
		LivingEntity shooter = (WeaponVehicleBase)this.getVehicle();
		if(this.getAnyPassenger()!=null){
			shooter = this.getAnyPassenger();
		}
		double px = this.getX();
		double py = this.getY();
		double pz = this.getZ();
		float wx = this.turretPitch;
		float wy = this.turretYaw;
		if(followvehicle[0]){
			px = shooter.getX();
			py = shooter.getY();
			pz = shooter.getZ();
			wx = 0;//shooter.xRot
			wy = shooter.yRot;
		}
		if(this.firesound1_3p!=null && this.getNpcPassenger()!=null){
			this.firesound1=this.firesound1_3p;
		}
		if(shooter!=null){
			if(this.laserweapon[0]){
				Attacklaser(this, shooter, locktarget, this.bulletid[0], this.laser_model_tex1, this.laserfxfire1, this.laserfxhit1, 
				this.firesound1,side, fire_x,this.fireposY[0],this.fireposZ[0],this.firebaseX[0],this.firebaseZ[0],px, py, pz,wy, wx,
				this.bulletdamage[0], this.bulletspeed[0], this.laserwidth[0], this.bulletexp[0], this.can_connect[0], this.connect_cout[0], this.bullettime[0]);
			}else{
				AI_EntityWeapon.Attacktask(this, shooter, locktarget, this.bulletid[0], this.bulletmodel1, this.bullettex1, this.firefx1, this.bulletfx1, this.firesound1,side, this.fireposX[0],this.fireposY[0],this.fireposZ[0],this.firebaseX[0],this.firebaseZ[0],px, py, pz,wy, wx,this.bulletdamage[0], this.bulletspeed[0], this.bulletspread[0], this.bulletexp[0], this.bulletdestroy[0], this.bulletcount[0], this.bulletgravity[0], this.bullettime[0], this.bullettype[0]);
			}
		}
	}
	public void weaponActive2(){
		float side = 1.57F;
		
		float fire_x = this.fireposX[1];
		/*if(weaponThreeFire[1]){
			if(this.getRemain2()==2){
				fire_x = 0;
			}else if(this.getRemain2()==1){
				fire_x = -this.fireposX[1];
			}
		}else*/{
			if(this.weaponcross[1]){
				if(this.getRemain2()%2==0){
					side = -1.57F;
				}else{
					side = 1.57F;
				}
			}
		}
		
		Entity locktarget = null;
		if(this.mitarget!=null){
			locktarget = this.mitarget;
		}else{
			locktarget = this.getTarget();
		}
		LivingEntity shooter = (WeaponVehicleBase)this.getVehicle();
		if(this.getAnyPassenger()!=null){
			shooter = this.getAnyPassenger();
		}
		double px = this.getX();
		double py = this.getY();
		double pz = this.getZ();
		float wx = this.turretPitch;
		float wy = this.turretYaw;
		if(followvehicle[1]){
			px = shooter.getX();
			py = shooter.getY();
			pz = shooter.getZ();
			wx = 0;//shooter.xRot
			wy = shooter.yRot;
		}
		if(shooter!=null){
			if(this.laserweapon[1]){
				Attacklaser(this, shooter, locktarget, this.bulletid[1], this.laser_model_tex2, this.laserfxfire2, this.laserfxhit2, 
				this.firesound2,side, fire_x,this.fireposY[1],this.fireposZ[1],this.firebaseX[1],this.firebaseZ[1],px, py, pz,wy, wx,
				this.bulletdamage[1], this.bulletspeed[1], this.laserwidth[1], this.bulletexp[1], this.can_connect[1], this.connect_cout[1], this.bullettime[1]);
			}else{
				AI_EntityWeapon.Attacktask(this, shooter, locktarget, this.bulletid[1], this.bulletmodel2, this.bullettex2, this.firefx2, this.bulletfx2, this.firesound2,side, this.fireposX[1],this.fireposY[1],this.fireposZ[1],this.firebaseX[1],this.firebaseZ[1],px, py, pz,wy, wx,this.bulletdamage[1], this.bulletspeed[1], this.bulletspread[1], this.bulletexp[1], this.bulletdestroy[1], this.bulletcount[1], this.bulletgravity[1], this.bullettime[1], this.bullettype[1]);
			}
		}
	}
	public Vector2f getLockVector() {
	  return new Vector2f(this.turretPitch, this.turretYaw);
	}
	LivingEntity laserTarget = null;
	LivingEntity rangeTarget = null;
	public void Attacklaser(LivingEntity living, LivingEntity shooter, Entity target, int id,
		String modeltex, String fxfire, String fxhit, SoundEvent sound, 
		float f, double w, double h, double z, double bx, double bz, double px, double py, double pz, float rotey, float rotex,
		int damage, float speed, float width, float exlevel, boolean isconnect, int maxcount, int maxtime){
    	int ra = living.level.random.nextInt(10) + 1;
    	float val = ra * 0.02F;
    	if(sound != null)living.playSound(sound, 5.0F, 0.9F + val);
		double xx11 = 0;
		double zz11 = 0;
		float base = 0;
		base = MathHelper.sqrt((z - bz)* (z - bz) + (w - bx)*(w - bx)) * MathHelper.sin(-rotex  * (1 * (float) Math.PI / 180));
		xx11 -= MathHelper.sin(rotey * 0.01745329252F) * z;
		zz11 += MathHelper.cos(rotey * 0.01745329252F) * z;
		xx11 -= MathHelper.sin(rotey * 0.01745329252F + 1.57F) * w;
		zz11 += MathHelper.cos(rotey * 0.01745329252F + 1.57F) * w;
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX(fxfire, null, px+xx11, py+h+base, pz+zz11, 0, 0, 0, exlevel);
		Vector3d locken = Vector3d.directionFromRotation(this.getLockVector());
		float d = speed*maxtime;
		if(width<1)width=1;
		int ix = 0;
		int iy = 0;
		int iz = 0;
		boolean stop = false;
		int count = 0;
		int pierce = 0;
		for(int xxx = 0; xxx < 120; ++xxx) {
			ix = (int) (px+xx11 + locken.x * xxx);
			iy = (int) (py+h+base + locken.y * xxx);
			iz = (int) (pz+zz11 + locken.z * xxx);
			BlockPos blockpos = new BlockPos(ix, iy, iz);
			BlockState iblockstate = living.level.getBlockState(blockpos);
			if (!iblockstate.isAir(living.level, blockpos)&& !iblockstate.getCollisionShape(this.level, blockpos).isEmpty()){
				break;
			}else{
				AxisAlignedBB axisalignedbb = (new AxisAlignedBB(ix-width, iy-width, iz-width, 
						ix+width, iy+width, iz+width)).inflate(1D);
				List<Entity> llist = living.level.getEntities(living,axisalignedbb);
				if (llist != null) {
					for (int lj = 0; lj < llist.size(); lj++) {
						Entity entity1 = (Entity) llist.get(lj);
						if (entity1 != null && entity1 instanceof LivingEntity) {
							if (NotFriend(entity1) && entity1 != shooter && entity1 != living) {
								laserTarget = (LivingEntity)entity1;
								if(laserTarget.getVehicle()!=null && laserTarget.getVehicle() instanceof LivingEntity){
									LivingEntity ve = (LivingEntity)laserTarget.getVehicle();
									ve.invulnerableTime = 0;
									ve.hurt(DamageSource.thrown(living, shooter), damage);
									ve.setSecondsOnFire(8);
								}else{
									laserTarget.invulnerableTime = 0;
									laserTarget.hurt(DamageSource.thrown(living, shooter), damage);
									laserTarget.setSecondsOnFire(8);
								}
								if(laserTarget!=null && isconnect){
									rangeTarget = laserTarget.level.getNearestLoadedEntity(MobEntity.class, (new EntityPredicate()).range(exlevel*8).selector((attackentity) -> {return NotFriend(attackentity);}), 
									laserTarget, laserTarget.getX(), laserTarget.getEyeY(), laserTarget.getZ(), laserTarget.getBoundingBox().inflate(exlevel*8, exlevel*4, exlevel*8));
									if(rangeTarget!=null){
										rangeTarget.hurt(DamageSource.thrown(living, shooter), damage*0.5F);
										{
											if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX(fxhit, null, rangeTarget.getX(), rangeTarget.getBoundingBox().minY + rangeTarget.getEyeHeight()/2F+1F, rangeTarget.getZ(), 0, 0, 0, 1);
											MessageTrail messageBulletTrail = new MessageTrail(true, id, modeltex, laserTarget.getX(), laserTarget.getBoundingBox().minY + laserTarget.getEyeHeight()/2F - 0.1, laserTarget.getZ(), rangeTarget.getDeltaMovement().x, rangeTarget.getDeltaMovement().z, rangeTarget.getX(), rangeTarget.getBoundingBox().minY + rangeTarget.getEyeHeight()/2F - 0.1, rangeTarget.getZ(), maxtime, 0.8F);
											PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(rangeTarget.getX(), rangeTarget.getY(), rangeTarget.getZ(), 50, rangeTarget.level.dimension())), messageBulletTrail);
										}
										WMExplosionBase.createExplosionDamage(living, laserTarget.getX()+1.0D, laserTarget.getY()+1.5D, laserTarget.getZ()+1.0D,20, 2, false,  false);
									}
									++count;
									if(count>maxcount){
										stop = true;
										break;
									}
								}
								stop = true;
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
		if(exlevel>0)WMExplosionBase.createExplosionDamage(living, ix, iy+1.5D, iz,damage, exlevel, false,  false);
		if(ModList.get().isLoaded("safx"))SagerFX.proxy.createFX(fxhit, null, ix, iy+1.5D, iz, 0, 0, 0, exlevel);
		MessageTrail messageBulletTrail = new MessageTrail(true, id, modeltex, px+xx11, py+h-1.5F+base, pz+zz11, living.getDeltaMovement().x, living.getDeltaMovement().z, ix, iy+0.5D, iz, maxtime, 1);
		PacketHandler.getPlayChannel2().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(px, py, pz, 80, living.level.dimension())), messageBulletTrail);
	}
	
	
	public void turretFollow(float y1, float speedy, float x1, float speedx){
		float f3 = (float) (y1 - this.turretYaw);// -180 ~ 0 ~ 180
		if(f3>speedy){// +1
			if(f3>180F){
				this.turretYawMove-=speedy;
			}else{
				this.turretYawMove+=speedy;
			}
		}else if(f3<-speedy){// -1
			if(f3<-180F){
				this.turretYawMove+=speedy;
			}else{
				this.turretYawMove-=speedy;
			}
		}
		if(f3>-2*speedy&&f3<2*speedy){
			aim_true = true;
			this.turretYawMove = y1;
		}else{
			aim_true = false;
		}
		this.turretYaw = this.turretYawMove;//yaw
		float f2 = (float) (x1 - this.turretPitch);// -180 ~ 0 ~ 180
		if(this.turretPitchMove<x1){
			if(x1<this.turretPitchMin)this.turretPitchMove+=speedx;
		}else if(this.turretPitchMove>x1){
			if(x1>this.turretPitchMax)this.turretPitchMove-=speedx;
		}
		if(f2<2*speedx&&f2>-2*speedx){
			this.turretPitchMove = x1;
		}
		this.turretPitch = this.xRot = this.turretPitchMove;//pitch
	}
	boolean hurtPassenger = true;
	int time = 0;
	public boolean aim_true = false;
	public boolean turret_speed = false;
	public boolean hudfollow = false;
	public void tick() {
		super.tick();
		if(this.level.isClientSide())
        {
            this.onClientUpdate();
        }
		if(changetime<10)++changetime;

		boolean fire_flag = false;
		if(this.turretYaw>360F||this.turretYaw<-360F){
			this.turretYaw = 0;
		}
		if(time<20)++time;
		if(this.getVehicle()!=null/* && this.time <10*/){
			int type = 0;
			int gun_cycle = 4;
			int i = this.getVehicle().getPassengers().indexOf(this);
			float fire_x = 0.89F;
			if(this.getRemain1()==2){
				fire_x = 0;
			}else if(this.getRemain1()==1){
				fire_x = -0.89F;
			}
			if(this.getVehicle() instanceof WeaponVehicleBase){
				WeaponVehicleBase vehicle = (WeaponVehicleBase)this.getVehicle();
				if(vehicle.getHealth()>0){
					if(vehicle.seatTurret[i]){
						this.yRot = vehicle.turretYaw;
					}else{
						this.yRot = vehicle.yRot;
					}
					if(i ==0){
						this.w1recoilp=vehicle.w1recoilp;
						this.w1recoilr=vehicle.w1recoilr;
						this.turretSpeed = vehicle.turretSpeed;
						this.turretYaw = vehicle.turretYaw;
						this.setYHeadRot(this.turretYaw);
						this.turretPitch = vehicle.xRot;
						this.canlock = vehicle.canlock;
						this.is_aa = vehicle.is_aa;
						this.minyaw = vehicle.minyaw;
						this.maxyaw = vehicle.maxyaw;
						this.turretPitchMax = vehicle.turretPitchMax;
						this.turretPitchMin = vehicle.turretPitchMin;
						this.seatProtect = vehicle.seatProtect-vehicle.enc_protect*0.1F;
						this.seatHide = vehicle.seatHide[0];
						this.canNightV=vehicle.canNightV;
					}
					//this.seatHide = vehicle.seatHide[i];
					this.seatCanFire = vehicle.seatCanFire[i];
					
					if(i ==0||hudfollow){
						this.render_hud_box = vehicle.render_hud_box;
						this.hud_box_obj = vehicle.hud_box_obj;
						this.hud_box_tex = vehicle.hud_box_tex;
						this.renderHudIcon = vehicle.renderHudIcon;
						this.hudIcon = vehicle.hudIcon;
						this.renderHudOverlay = vehicle.renderHudOverlay;
						this.hudOverlay = vehicle.hudOverlay;
						this.renderHudOverlayZoom = vehicle.renderHudOverlayZoom;
						this.hudOverlayZoom = vehicle.hudOverlayZoom;
					}
					
					if(this.getRemain1() <= 0){
						++reload1;
						if(reload1 == reload_time1 - reloadSoundStart1)this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), reloadSound1, SoundCategory.WEATHER, 2.0F, 1.0F);
						if(reload1 >= reload_time1){
							this.setRemain1(this.magazine);
							reload1 = 0;
						}
					}
					if(this.getRemain2() <= 0){
						++reload2;
						if(reload2 == reload_time2 - reloadSoundStart2)this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), reloadSound2, SoundCategory.WEATHER, 2.0F, 1.0F);
						if(reload2 >= reload_time2){
							this.setRemain2(this.magazine2);
							reload2 = 0;
						}
					}
					boolean weaponfire1 = false;
					boolean weaponfire2 = false;
					if (this.getControllingPassenger() != null) {
						if(startRidTime<15)++startRidTime;
						PlayerEntity player = (PlayerEntity) this.getControllingPassenger();
						/*float fix = 1.0F;
						if(vehicle.flyPitch!=0)fix = 1.4F;*/
						if(this.canlock && this.cooltime>this.ammo1 && this.cooltime2>this.ammo2){
							AI_MissileLock.SeatLock(this,player,this.is_aa, 1);
						}
						if(player.yHeadRot > 360F || player.yHeadRot < -360F){
							player.yHeadRot = 0;
							player.yRot = 0;
							player.yRotO = 0;
							player.yHeadRotO = 0;
						}
						if(vehicle.getChange()>0){
							turretFollow(player.yHeadRot , 2F, player.xRot, 1);
						}else{
							this.turretYaw = player.getYHeadRot();
							this.turretPitch= this.xRot = player.xRot;
						}
						this.setYHeadRot(this.turretYaw);
						
						//if(i>0)
						{
							if(this.fire1){
								weaponfire1 = true;
								this.fire1 = false;
							}
							if(this.fire2){
								weaponfire2 = true;
								this.fire2 = false;
							}
							if(this.keyr){
								if(changetime>8){
									int seat = i;
									if(i == vehicle.seatMaxCount-1){
										seat = -1;
									}
									for(int i1 = 0; i1 < vehicle.seatMaxCount; ++i1) {
										if(i1>seat && vehicle.getAnySeat(i1)!=null && ((EntityWMSeat)vehicle.getAnySeat(i1)).canDrive()){
											this.playSound(SoundEvents.IRON_DOOR_OPEN, 3.0F, 1.0F);
											if (!player.level.isClientSide){
												player.startRiding(vehicle.getAnySeat(i1));
											}
											this.playSound(SoundEvents.IRON_DOOR_CLOSE, 3.0F, 1.0F);
											changetime = 0;
											break;
										}
									}
									changetime = 0;
								}
								this.keyr = false;
							}
						}
					}else{
						startRidTime = 0;
						if(this.getNpcPassenger()!=null){
							if(this.getNpcPassenger() instanceof IMob){
								if(this.getTargetType()!=2)this.setTargetType(2);
							}else{
								if(this.getTargetType()!=3)this.setTargetType(3);
							}
							if(i>0 && this.weaponCount>0){
								CreatureEntity gunner = (CreatureEntity)this.getNpcPassenger();
								if(gunner.getTarget()!=null){
									this.setTarget(gunner.getTarget());
									if(gunner.getSensing().canSee(gunner.getTarget()) && this.distanceTo(gunner.getTarget())<this.attack_range_max)fire_flag = true;
								}else{
									this.setTarget(null);
								}
								if(this.getNavigation()!=null)this.getNavigation().stop();
								float targetpitch = gunner.xRot;
								if(gunner.xRot>0)targetpitch = gunner.xRot-4;
								if(this.getTarget()!=null){
									LivingEntity target = this.getTarget();
									if(target.isAlive() && target!=null){
										if(this.isthrow){
											double[] angles = new double[2];
											boolean flag = ThrowBullet.canReachTarget(this.bulletspeed[0], this.bulletgravity[0], 0.99,
													(int) this.getX(), (int) this.getEyeY(), (int) this.getZ(),
													(int) target.getX(), (int) target.getEyeY(), (int) target.getZ(),
													angles, changethrow&&vehicle.getMoveType()!=3);
											if (flag) {
												targetpitch = (float)-angles[1];
											}
										}
									}
								}
								if(turret_speed){
									turretFollow(gunner.yRot , 2F, targetpitch, 1);
								}else{
									this.turretYaw = gunner.yRot;//限制角度
									this.turretPitch = this.xRot = targetpitch;
								}
								this.setYHeadRot(this.turretYaw);
								if(fire_flag && (aim_true||!turret_speed)){
									{
										weaponfire1 = true;
										weaponfire2 = true;
										this.fire1 = true;
										this.fire2 = true;
									}
								}else{
									this.fire1 = false;
									this.fire2 = false;
								}
							}
						}else{
							if(this.getTargetType()!=0)this.setTargetType(0);
						}
					}
					
					if(weaponfire1){
						if(this.cooltime >= this.ammo1){
							this.counter1 = true;
							this.cooltime = 0;
						}
						if(this.counter1 && this.gun_count1 >= this.w1cycle && this.getRemain1() > 0){
							vehicle.setAnimFire(2+i);
							this.weaponActive1();
							this.setRemain1(this.getRemain1() - 1);
							this.gun_count1 = 0;
							this.counter1 = false;
							if(this.getControllingPassenger()!=null)WarMachineLib.proxy.onFireAnimation(this.w1recoilp,this.w1recoilr);
						}
					}
					if(weaponfire2){
						if(cooltime2 >= this.ammo2){
							this.counter2 = true;
							this.cooltime2 = 0;
						}
						if(this.counter2 && this.gun_count2 >= this.w2cycle && this.getRemain2()>0){
							this.weaponActive2();
							this.setRemain2(this.getRemain2() - 1);
							this.gun_count2 = 0;
							this.counter2 = false;
						}
					}
				}else{
					if(time>2){
						if(hurtPassenger){
							if(this.getAnyPassenger()!=null){
								if(this.getControllingPassenger()!=null){
									PlayerEntity player = (PlayerEntity)this.getAnyPassenger();
									player.hurt(DamageSource.IN_FIRE, 250);
								}else{
									this.getAnyPassenger().hurt(DamageSource.IN_FIRE, 250);
								}
								//this.getAnyPassenger().hurt(DamageSource.IN_FIRE, (10-vehicle.enc_protect*2)*0.1F*1*vehicle.getMaxHealth()/10F);
								this.getAnyPassenger().unRide();
								//ent.hurt(DamageSource.IN_FIRE, 250);
							}
							//hurtPassenger = false;
						}
						if (!this.level.isClientSide){
							this.dead = true;
							this.remove();
						}
						time = 0;
					}
				}
			}
		}else if(time>10){
			if (!this.level.isClientSide){
				this.dead = true;
				this.remove();
			}
			time = 0;
		}
	}
	
	public void setFire(int id)
    {
		if(id==1){
			this.fire1 = true;
		}
		if(id==2){
			this.fire2 = true;
		}
		if(id==3){
			this.keyf = true;
		}
		if(id==4){
			this.keylook = true;
		}
		if(id==5){
			this.keyg = true;
		}
		if(id==6){
			this.keyx = true;
		}
		if(id==7){
			this.keyv = true;
		}
		if(id==8){
			this.powerfire = true;
		}
		if(id==9){
			this.powerfire = false;
		}
		if(id==10){
			this.keyr = true;
		}
		if(id==11){
			this.keyc = true;
		}
		if(id==12){
			this.keyrun = true;
		}
	}
	
	public void positionRider(Entity passenger){
        if (this.hasPassenger(passenger))
        {
        	 int i = this.getPassengers().indexOf(passenger);
             {
				double ix = 0;
				double iy = 0;
				double iz = 0;
				float rote = 0;
				if (this.seatTurret) {
					rote = this.turretYaw;
				}else{
					rote = this.yRot;
				}
				float rpitch = this.turretPitch;
				double mgheight = 0;
				if(this.ridding_rotemgPitch)mgheight = /*this.firebaseZ[0] */ MathHelper.sin(passenger.xRot  * (1 * (float) Math.PI / 180)) *  0.38D;
				
				if(this.isOnGround()||!seatRotePitch)rpitch = 0;
				float f1 = rote * (2 * (float) Math.PI / 360);
				ix -= MathHelper.sin(f1) * seatPosZ[i];
				iz += MathHelper.cos(f1) * seatPosZ[i];
				ix -= MathHelper.sin(f1 - 1.57F) * seatPosX[i];
				iz += MathHelper.cos(f1 - 1.57F) * seatPosX[i];
				double ix2 = 0;
				double iz2 = 0;
				float f12 = this.turretYaw * (2 * (float) Math.PI / 360);
				ix2 -= MathHelper.sin(f12) * seatRoteZ[i];
				iz2 += MathHelper.cos(f12) * seatRoteZ[i];
				ix2 -= MathHelper.sin(f12 - 1.57F) * seatRoteX[i];
				iz2 += MathHelper.cos(f12 - 1.57F) * seatRoteX[i];
				double b = 0;
				double b2 = 0;
				double a = 0;
				double ax = 0;
				double az = 0;
				{
					b =  seatPosZ[i] * MathHelper.sin(rpitch  * (1 * (float) Math.PI / 180)) *  1.0D;
					a =  seatPosZ[i] * Math.abs(Math.cos(rpitch  * (1 * (float) Math.PI / 180))) *  1.0D;
					ax -= MathHelper.sin(rote * (2 * (float) Math.PI / 360)) * a;
					az += MathHelper.cos(rote * (2 * (float) Math.PI / 360)) * a;
					ax -= MathHelper.sin(rote * (2 * (float) Math.PI / 360) - 1.57F) * seatPosX[i];
					az += MathHelper.cos(rote * (2 * (float) Math.PI / 360) - 1.57F) * seatPosX[i];
				}
				Vector3d vec3d = new Vector3d(ax + ix2, seatPosY[i] + seatRoteY[i] + passenger.yRotO - b, az + iz2);
				float f11 = (float)(seatPosY[i] + passenger.getMyRidingOffset());
				passenger.setPos(this.getX() + vec3d.x,this.getY() + f11 + mgheight, this.getZ() + vec3d.z);
				this.applyYawToTurret(passenger);
             }
        }
    }
}