package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.potion.Effects;
import advancearmy.entity.sea.EntitySA_BattleShip;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import static advancearmy.entity.sea.EntitySA_BattleShip.*;
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.settings.PointOfView;

import net.minecraft.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
@OnlyIn(Dist.CLIENT)
public class RenderBattleShip extends MobRenderer<EntitySA_BattleShip, ModelNoneVehicle<EntitySA_BattleShip>>
{
	private static final ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/battleship.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/battleship.obj");
	
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/tankmflash.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	
    public RenderBattleShip(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),4F);
        this.shadowStrength = 4F;
    }

    public ResourceLocation getTextureLocation(EntitySA_BattleShip entity)
    {
		return tex;
    }
    
	public boolean shouldRender(EntitySA_BattleShip p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
      return true;
   }
	
   private static void setupGlintTexturing(float p_228548_0_) {
      RenderSystem.matrixMode(5890);
      RenderSystem.pushMatrix();
      RenderSystem.loadIdentity();
      long i = Util.getMillis() * 8L;
      float f = (float)(i % 110000L) / 110000.0F;
      float f1 = (float)(i % 30000L) / 30000.0F;
      RenderSystem.translatef(-f, f1, 0.0F);
      RenderSystem.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
      RenderSystem.scalef(p_228548_0_, p_228548_0_, p_228548_0_);
      RenderSystem.matrixMode(5888);
   }
	
    private void render_part(EntitySA_BattleShip entity, String name){
		{
			obj.renderPart(name);//
			if(entity.getOwner()!=null && entity.getHealth()<10){//
				setupGlintTexturing(8F);
				Minecraft.getInstance().getTextureManager().bind(ENCHANT_GLINT_LOCATION);
				//SARenderHelper.enableBlendMode(advancearmy.render.SARenderHelper.RenderTypeSA.ALPHA);//ADDITIVE
				//RenderSystem.disableDepthTest();
				//RenderSystem.depthMask(false);
				RenderSystem.enableBlend();
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
				GL11.glPushMatrix();//glstart
				if(entity.getOwner()==null)GlStateManager._scalef(1.01001F, 1.01001F, 1.01001F);
				obj.renderPart(name);
				GL11.glPopMatrix();//glend
				RenderSystem.matrixMode(5890);
				RenderSystem.popMatrix();
				RenderSystem.matrixMode(5888);
				RenderSystem.defaultBlendFunc();
				RenderSystem.disableBlend();
				Minecraft.getInstance().getTextureManager().bind(tex);
				//RenderSystem.depthMask(true);
				//RenderSystem.enableDepthTest();
				//SARenderHelper.disableBlendMode(advancearmy.render.SARenderHelper.RenderTypeSA.ALPHA);
			}else{
				//Minecraft.getInstance().getTextureManager().bind(POWER_LOCATION);
			}
		}
    }
	static int tick2 = 0;
	static int tick3 = 0;
	private void render_turret2(EntitySA_BattleShip entity, String name, float x, float y, float z, float bz, float rote, float pitch, float fire, float tick, int id){
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);//
		GL11.glRotatef(rote, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-x, -y, -z);//
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);
		render_part(entity,"t"+name);
		GL11.glPopMatrix();//glend
		
		GL11.glTranslatef(x, y, z+bz);//
		GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-x, -y, -z-bz);//
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);
		render_part(entity,"b"+name);
		GL11.glPopMatrix();//glend
		if(id>0){
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(x, y, z);
			render_part(entity,"b3");
			GL11.glPopMatrix();//glend
			
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(x, y, z);//
			GL11.glRotatef(tick*10F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-x, -y, -z);//
			GL11.glTranslatef(x, y, z);
			render_part(entity,"b31");
			GL11.glPopMatrix();//glend
			if(fire <4){
				float size2 = entity.level.random.nextInt(4) * 0.3F + 1;
				GL11.glPushMatrix();//glstart
				GL11.glTranslatef(x, y+0.1F, z);//
				GL11.glRotatef(tick*10F, 0.0F, 0.0F, 1.0F);
				GlStateManager._scalef(size2, size2, 1);
				GL11.glTranslatef(-x, -y-0.1F, -z);//
				GL11.glTranslatef(x, y+0.1F, z);
				Minecraft.getInstance().getTextureManager().bind(fire_tex);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
				if(entity.level.random.nextInt(3)==1){
					obj.renderPart("mat_1");
				}else if(entity.level.random.nextInt(3)==2){
					obj.renderPart("mat_2");
				}else{
					obj.renderPart("mat_3");
				}
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
				Minecraft.getInstance().getTextureManager().bind(tex);
				GL11.glPopMatrix();//glend
			}
		}
		GL11.glPopMatrix();//glend
	}

	private void render_turret(EntitySA_BattleShip entity, String name, float x, float y, float z, float rote, float pitch){
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);//
		GL11.glRotatef(rote, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-x, -y, -z);//
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);
		render_part(entity,"t"+name);
		GL11.glPopMatrix();//glend
		
		GL11.glTranslatef(x, y, z+2.6F);//
		GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-x, -y, -z-2.6F);//
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);
		render_part(entity,"b"+name);
		GL11.glPopMatrix();//glend
		GL11.glPopMatrix();//glend
	}
    float iii;
	static boolean glow = true;
	static int shock =0;
    public void render(EntitySA_BattleShip entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	    
		Minecraft mc = Minecraft.getInstance();
		/*if(entity.hasEffect(Effects.INVISIBILITY)){
			if(mc.player==entity.getOwner()){
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.2F);
			}else{
				return;
			}
		}*/
		matrixStackIn.pushPose();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);//
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);//
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);//
		GlStateManager._shadeModel(GL11.GL_SMOOTH);
		//GL11.glEnable(GL11.GL_BLEND);
		
		
		int blockLight = (packedLightIn & 0xFFFF) >> 4;
		int skyMask = packedLightIn >> 20 & 15;
		float rainLevel = mc.level.getRainLevel(partialTicks);
		float thunderLevel = mc.level.getThunderLevel(partialTicks);
		float sunAngle = mc.level.getSunAngle(partialTicks);
		float daylight = MathHelper.cos(sunAngle) * 0.5f + 0.5f;
		daylight = MathHelper.clamp(daylight, 0.0f, 1.0f);
		float weatherFactor = 1.0f - (rainLevel * 0.1f) - (thunderLevel * 0.2f);
		float skyLight = (skyMask / 15.0f) * daylight * weatherFactor;
		float blockLight1 = blockLight / 15.0f;
		float finalLight = Math.max(skyLight, blockLight1)+0.2f;
		if(entity.anim1<3)finalLight=1;
		if(entity.deathTime > 0)finalLight=0.1F;
		GL11.glColor3f(finalLight, finalLight, finalLight);
		if(entity.getTargetType()==2)GL11.glColor3f(0.7F, 0.4F, 0.4F);
		if(entity.deathTime > 0)GL11.glColor3f(0.1F, 0.1F, 0.1F);
		ActiveRenderInfo activeRenderInfoIn = Minecraft.getInstance().getEntityRenderDispatcher().camera;
		activeRenderInfoIn.setup(mc.level, (Entity)(mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity()), 
		!mc.options.getCameraType().isFirstPerson(), mc.options.getCameraType().isMirrored(), partialTicks);
		Vector3d avector3d = activeRenderInfoIn.getPosition();
		double camx = avector3d.x();
		double camy = avector3d.y();
		double camz = avector3d.z();
		double d0 = MathHelper.lerp((double)partialTicks, entity.xOld, entity.getX());
		double d1 = MathHelper.lerp((double)partialTicks, entity.yOld, entity.getY());
		double d2 = MathHelper.lerp((double)partialTicks, entity.zOld, entity.getZ());
		double xIn = d0 - camx;
		double yIn = d1 - camy;
		double zIn = d2 - camz;
    	mc.getTextureManager().bind(tex);
		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
		activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
		GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) xIn, (float) yIn, (float) zIn);
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);

		{//开炮抖动
			if(entity.anim1<24){
				if(!glow && this.shock<12){
					++this.shock;
				}
				if(this.shock>=12)glow = true;
				if(glow && this.shock>0){
					--this.shock;
				}
				if(this.shock<=0)glow = false;
			}else{
				this.shock=0;
			}
		}
		{
			GL11.glRotatef(180.0F - entity.turretYaw, 0.0F, 1.0F, 0.0F);
			if(entity.anim1<24){
				float count = 24-entity.anim1;
				GL11.glRotatef((-this.shock * count/50F)*0.1F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef((-this.shock * count/60F)*0.1F, 0.0F, 1.0F, 0.0F);
			}
			GL11.glRotatef(-(180.0F - entity.turretYaw), 0.0F, 1.0F, 0.0F);
		}
		
//倾斜系统
		{
			GL11.glRotatef(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		}

		float th = entity.throttle;
		if(th>=0){
			GL11.glRotatef(((entity.throttleMax-th)/(entity.throttleMax))*1.5F, 1.0F, 0.0F, 0.0F);
		}else if(th<0){
			GL11.glRotatef(((entity.throttleMin-th)/(entity.throttleMin))*1.5F, 1.0F, 0.0F, 0.0F);
		}
		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		GL11.glRotatef(Ax1 * (180F / (float)Math.PI) * 0.0002F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(Ax1 * (180F / (float)Math.PI) * 0.0002F, 0.0F, 1.0F, 0.0F);
		render_part(entity,"mat1");
		float rote1 = 180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		float rote2 = 180.0F - entity.turretYaw_1 -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		float rote3 = 180.0F - entity.turretYaw_2 -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		float rote4 = 180.0F - entity.turretYaw_3 -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		float rote5 = 180.0F - entity.turretYaw_4 -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		float rote6 = 180.0F - entity.turretYaw_5 -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		float rote7 = 180.0F - entity.turretYaw_6 -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		float rote8 = 180.0F - entity.turretYaw_7 -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		if(entity.anim7<5){
			if(tick2<36F){
				++tick2;
			}else{
				tick2 = 0;
			}
		}
		if(entity.anim8<5){
			if(tick3<36F){
				++tick3;
			}else{
				tick3 = 0;
			}
		}
		if(iii<360F){
			++iii;
		}else{
			iii = 0;
		}
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(0.0F, 0, 3.27F);
		GL11.glRotatef(iii, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0, -3.27F);
		obj.renderPart("rader");
		GL11.glPopMatrix();//glend
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(0.0F, 0, -1.26F);
		GL11.glRotatef(-iii, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0, 1.26F);
		obj.renderPart("rader2");
		GL11.glPopMatrix();//glend
		GlStateManager._shadeModel(GL11.GL_FLAT);
		render_turret(entity, "1", 0, 4.19F, 10.82F, rote1, entity.turretPitch);
		render_turret(entity, "1", 0, 3F, 17.89F, rote2, entity.turretPitch_1);
		render_turret(entity, "1", 0, 3.1F, -10.45F, rote3, entity.turretPitch_2);
		render_turret2(entity, "2", -4.38F, 3.1F, 4.37F, 0.6F, rote4, entity.turretPitch_3, 0, 0, 0);
		render_turret2(entity, "2", 4.38F, 3.1F, 4.37F, 0.6F, rote5, entity.turretPitch_4, 0, 0, 0);
		render_turret2(entity, "3", -3.63F, 6.9F, -1.3F, 0, rote6, entity.turretPitch_5, entity.anim7, tick2, 1);
		render_turret2(entity, "3", 3.63F, 6.9F, -1.3F, 0, rote7, entity.turretPitch_6, entity.anim8, tick3, 1);
		render_turret2(entity, "4", 0, 5.33F, -5.57F, 0, rote8, entity.turretPitch_7, 0, 0, 0);
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	    matrixStackIn.popPose();
    }
	
    public float F6(LivingEntity entity, float partialTicks){
 		float f6 = 0;
 		if (!entity.isPassenger())
        {
            f6 = entity.animationPosition - entity.animationSpeed * (1.0F - partialTicks);
        }
 		return f6;
 	}
 	public float F5(LivingEntity entity, float partialTicks){
 		float f5 = 0;
 		if (!entity.isPassenger())
        {
            f5 = entity.animationSpeedOld + (entity.animationSpeed - entity.animationSpeedOld) * partialTicks;
            if (f5 > 1.0F)
            {
                f5 = 1.0F;
            }
        }
 		return f5;
 	}
}