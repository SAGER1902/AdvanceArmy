package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.potion.Effects;
import advancearmy.entity.land.EntitySA_LAVAA;
import advancearmy.entity.EntitySA_LandBase;
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
import net.minecraft.client.settings.PointOfView;

import net.minecraft.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
@OnlyIn(Dist.CLIENT)
public class RenderLAV extends MobRenderer<EntitySA_LandBase, ModelNoneVehicle<EntitySA_LandBase>>
{
	private static final ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/lav25.png");
	private static SAObjModel obj = new SAObjModel("advancearmy:textures/mob/lav25.obj");
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/muzzleflash3.png");
	private static final ResourceLocation tankmflash = ResourceLocation.tryParse("advancearmy:textures/entity/flash/tankmflash.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	
    public RenderLAV(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),2.5F);
        this.shadowStrength = 2.5F;
    }

    public ResourceLocation getTextureLocation(EntitySA_LandBase entity)
    {
		return tex;
    }
    
	public boolean shouldRender(EntitySA_LandBase p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
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
	
    private void render_part(EntitySA_LandBase entity, String name){
		{
			obj.renderPart(name);//
			if(entity.getEnc()>0){//
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

	private void render_wheel(EntitySA_LandBase entity, String name, float x, float y, float z){
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);
		GL11.glRotatef((float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-x, -y, -z);
		GL11.glTranslatef(x, y, z);
		obj.renderPart(name);
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(-x, y, z);
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef((float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(x, -y, -z);
		GL11.glTranslatef(-x, y, z);
		obj.renderPart(name);
		GL11.glPopMatrix();//glend
	}
    float iii;

    public void render(EntitySA_LandBase entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	    Minecraft mc = Minecraft.getInstance();
		if(entity.obj!=null)obj = entity.obj;
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
		
		{
			GL11.glRotatef(180.0F - (entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
			
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
		
		float range = 0;
		{
			range = 0.0002F;
		}
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(1.12F, 0.55F, 2.19F);
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		GL11.glRotatef(entity.rote_wheel, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef((float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-1.12F, -0.55F, -2.19F);
		GL11.glTranslatef(1.12F, 0.55F, 2.19F);
		obj.renderPart("wheel");
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(-1.12F, 0.55F, 2.19F);
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		GL11.glRotatef(entity.rote_wheel, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef((float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(1.12F, -0.55F, -2.19F);
		GL11.glTranslatef(-1.12F, 0.55F, 2.19F);
		obj.renderPart("wheel");
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(1.12F, 0.55F, 0.79F);
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		GL11.glRotatef(entity.rote_wheel, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef((float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-1.12F, -0.55F, -0.79F);
		GL11.glTranslatef(1.12F, 0.55F, 0.79F);
		obj.renderPart("wheel");
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(-1.12F, 0.55F, 0.79F);
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		GL11.glRotatef(entity.rote_wheel, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef((float)entity.thpera*10F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(1.12F, -0.55F, -0.79F);
		GL11.glTranslatef(-1.12F, 0.55F, 0.79F);
		obj.renderPart("wheel");
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
		render_wheel(entity, "wheel", 1.12F, 0.55F, -0.91F);
		GL11.glPopMatrix();//glend
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * range, 0);
		render_wheel(entity, "wheel", 1.12F, 0.55F, -2.18F);
		GL11.glPopMatrix();//glend
		{
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(0.15F, 0F, -1.08F);
			GL11.glRotatef(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.15F, -0F, 1.08F);
			render_part(entity,"turret");
			
			GL11.glTranslatef(0, entity.fireposY1, -entity.fireposZ1);
			GL11.glRotatef((entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks), 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0, -entity.fireposY1, entity.fireposZ1);
			render_part(entity,"barrel");
			if(entity.anim1 <4){
				if(entity instanceof EntitySA_LAVAA){
					if(iii<360F){
						++iii;
					}else{
						iii = 0;
					}
					GL11.glTranslatef(0.24F, 2.82F, 0);
					GL11.glRotatef(iii*10F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(-0.24F, -2.82F, 0);
					mc.getTextureManager().bind(fire_tex);
				}else{
					mc.getTextureManager().bind(tankmflash);
				}
				GL11.glPushMatrix();//glstart
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
				if(entity.level.random.nextInt(2)==1){
					obj.renderPart("mat_1");
				}else if(entity.level.random.nextInt(2)==2){
					obj.renderPart("mat_2");
				}else{
					obj.renderPart("mat_3");
				}
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
				GL11.glPopMatrix();//glend
			}
			GL11.glEnable(GL11.GL_LIGHTING);
			mc.getTextureManager().bind(tex);
			obj.renderPart("barrel_rote");
			
			if(entity.anim1 >= 0 && entity.anim1 < 4){
				GL11.glTranslatef(0.0F, 0.0F, -entity.anim1 * 0.3F*0.2F);
			}
			if(entity.anim1 >= 4 && entity.anim1 < 8){
				GL11.glTranslatef(0.0F, 0.0F, -1.2F*0.2F);
				GL11.glTranslatef(0.0F, 0.0F, entity.anim1 * 0.1F*0.2F);
			}
			obj.renderPart("barrel_1");
			GL11.glPopMatrix();//glend
		}
		GL11.glColor3f(1F, 1F, 1F);
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