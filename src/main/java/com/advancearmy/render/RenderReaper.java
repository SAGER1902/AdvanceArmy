package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.potion.Effects;
import advancearmy.entity.land.EntitySA_Reaper;
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
import wmlib.client.obj.SAObjModel;


import net.minecraft.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
@OnlyIn(Dist.CLIENT)
public class RenderReaper extends MobRenderer<EntitySA_Reaper, ModelNoneVehicle<EntitySA_Reaper>>
{
	private static final ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/reaper.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/reaper.obj");
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/mflash.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	
    public RenderReaper(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),1F);
        this.shadowStrength = 1F;
    }

    public ResourceLocation getTextureLocation(EntitySA_Reaper entity)
    {
		return tex;
    }
    
	public boolean shouldRender(EntitySA_Reaper p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
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
	
    private void render_part(EntitySA_Reaper entity, String name){
		{
			Minecraft.getInstance().getTextureManager().bind(tex);
			obj.renderPart(name);
			if(entity.getEnc()>0){
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

	public void renderleg(int id,EntitySA_Reaper entity, float x, float y, float z, String name, float partialTicks, float hox, float hoy, float hoz, float level,
		float x1, float y1, float z1){
    	float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);
    	GL11.glPushMatrix();
    	GL11.glTranslatef(x, y, z);
		float jumprote = 0;
		float xrote = 0;
    	float Ax = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		if(entity.getMovePitch()>0){
			Ax = 0;
			if(entity.getDeltaMovement().y<0){
				jumprote = -30F;
			}else{
				jumprote = 60F;
			}
		}
		if (entity.getStrafingMove()>0.1F){
			xrote = 10F;
		}else
		if (entity.getStrafingMove()<-0.1F){
			xrote = -10F;
			Ax = -Ax;
		}
		if(id==2)jumprote=-jumprote;
		GL11.glRotatef(jumprote * level, 0, 0, 1F);
    	GL11.glRotatef(Ax * (180F / (float)Math.PI) * level, hox, hoy, hoz);
		GL11.glRotatef(Ax * (180F / (float)Math.PI) * level*0.5F+xrote, 0, 0, 1F);
		GL11.glRotatef(30F*hoy, 0, 1, 0);
		GL11.glTranslatef(-x, -y, -z);
		render_part(entity,name);
		
		GL11.glTranslatef(x1, y1, z1);
		GL11.glRotatef(jumprote * level, 0, 0, 1F);
    	//GL11.glRotatef(Ax * (180F / (float)Math.PI) * level*1.25F, hox, hoy, hoz);
		if(Ax>0){
			GL11.glRotatef(-Ax * (180F / (float)Math.PI) * level*1.25F+xrote, 0, 0, 1F);
		}else{
			GL11.glRotatef(Ax * (180F / (float)Math.PI) * level*1.25F+xrote, 0, 0, 1F);
		}
		GL11.glTranslatef(-x1, -y1, -z1);
		render_part(entity,name+"1");
		GL11.glPopMatrix();
		
    }
	static int tick1 = 0;
	static int tick2 = 0;
	static int tick3 = 0;
	private void render_turret(EntitySA_Reaper entity, String name, float x, float y, float z, float rote, float pitch, float fire, float tick, int id){
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);//
		GL11.glRotatef(rote, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-x, -y, -z);//

		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);
		render_part(entity,name);
		GL11.glPopMatrix();//glend
		
		if(id==1){
			GL11.glTranslatef(x, y, z+1.65F);//
			GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(-x, -y, -z-1.65F);//
		}else if(id==2){
			GL11.glTranslatef(x, y, z+0.35F);//
			GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(-x, -y, -z-0.35F);//
		}else{
			GL11.glTranslatef(x, y, z);//
			GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(-x, -y, -z);//
		}

		if(id==1){
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(x, y, z);
			render_part(entity,"barrel");
			GL11.glPopMatrix();//glend
			GL11.glPushMatrix();//glstart
			Minecraft.getInstance().getTextureManager().bind(fire_tex);
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			GL11.glTranslatef(x, y, 4F);//
			if(fire < 3){
				float size = (float)(2+fire) / 3F;
				GlStateManager._scalef(size,size,size);
			}
			if(fire >= 3 && fire<5){
				float size = (float)(6-fire) / 4F;
				GlStateManager._scalef(size,size,size);
			}
			GL11.glTranslatef(-x, -y, -4F);//
			GL11.glTranslatef(x, y, 4F);
			if(fire<5){
				obj.renderPart("flash1");
			}
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			Minecraft.getInstance().getTextureManager().bind(tex);
			GL11.glPopMatrix();//glend
		}
		if(id==2){
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(x, y, z);
			render_part(entity,"barrel3");
			if(fire <4){
				GL11.glPushMatrix();//glstart
				Minecraft.getInstance().getTextureManager().bind(fire_tex);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
				if(entity.level.random.nextInt(4)==1){
					obj.renderPart("mat_1");
				}else if(entity.level.random.nextInt(4)==2){
					obj.renderPart("mat_2");
				}else if(entity.level.random.nextInt(4)==0){
					obj.renderPart("mat_3");
				}
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
				Minecraft.getInstance().getTextureManager().bind(tex);
				GL11.glPopMatrix();//glend
			}
			GL11.glPopMatrix();//glend
		}
		if(id==3){
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(x, y, z);
			render_part(entity,"barrel2");
			GL11.glPopMatrix();//glend
		}
		GL11.glPopMatrix();//glend
	}
	
	static boolean glow = true;
	static int shock =0;
    public void render(EntitySA_Reaper entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
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
    	Minecraft.getInstance().getTextureManager().bind(tex);
		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
		activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
		GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) xIn, (float) yIn, (float) zIn);
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		
		{
			GL11.glRotatef(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		}

		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		//float size1 = entity.level.random.nextInt(10) * 0.01F;
		renderleg(1,entity, -0.69F, 2.26F, 0.57F, "leg2", partialTicks, 1, -1F, 0F, 0.3F, -0.69F,2.16F,2.06F);
		renderleg(1,entity, -0.69F, 2.26F, -0.57F, "leg4", partialTicks, 1, 1F, 0F, 0.3F, -0.69F,2.16F,-2.06F);
		
		renderleg(2,entity, 0.69F, 2.26F, 0.57F, "leg1", partialTicks, -1, 1F, 0F, 0.4F, 0.69F,2.16F,2.06F);
		renderleg(2,entity, 0.69F, 2.26F, -0.57F, "leg3", partialTicks, -1, -1F, 0F, 0.4F, 0.69F,2.16F,-2.06F);
		
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * 0.0005F , 0);
		render_part(entity,"body");
		/*SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		obj.renderPart("body_light");
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);*/

		float rote1 = 180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
		float rote2 = 180.0F - entity.turretYaw_1 -(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks));
		float rote3 = 180.0F - entity.turretYaw_2 -(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks));
		float rote4 = 180.0F - entity.turretYaw_3 -(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks));
		if(entity.anim1<5){
			if(tick1<36F){
				++tick1;
			}else{
				tick1 = 0;
			}
		}
		if(entity.anim3<5){
			if(tick2<36F){
				++tick2;
			}else{
				tick2 = 0;
			}
		}
		if(entity.anim4<5){
			if(tick3<36F){
				++tick3;
			}else{
				tick3 = 0;
			}
		}
		render_turret(entity, "turret", 0F, 4.02F, 0F, rote1, (entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks), entity.anim1, tick1, 1);
		
		GL11.glTranslatef(0F, 0F, 0F);
		GL11.glRotatef(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0F, -0F, -0F);
		
		render_turret(entity, "turret3", -1.1F, 3.8F, -1.06F, rote2, entity.turretPitch_1, entity.anim3, tick2, 2);
		render_turret(entity, "turret3", 1.1F, 3.8F, -1.06F, rote3, entity.turretPitch_2, entity.anim4, tick3, 2);
		render_turret(entity, "turret2", 0, 5.67F, -1.37F, rote4, entity.turretPitch_3, entity.anim5, 0, 3);
		GlStateManager._shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		
	    matrixStackIn.popPose();
    }
	
    public float F6(LivingEntity entity, float partialTicks){
 		float f6 = 0;
 		if (!entity.isPassenger())
        {
            f6 = entity.animationPosition - entity.animationSpeed*0.25F * (1.0F - partialTicks);
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