package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.potion.Effects;
import advancearmy.entity.EntitySA_TurretBase;
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
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.settings.PointOfView;

import net.minecraft.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import advancearmy.entity.EntitySA_Seat;
@OnlyIn(Dist.CLIENT)
public class RenderTurretBase extends MobRenderer<EntitySA_TurretBase, ModelNoneVehicle<EntitySA_TurretBase>>
{
	public ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/t90.png");
	public ResourceLocation gtex = null;
	public SAObjModel obj = new SAObjModel("advancearmy:textures/mob/kord.obj");

	public ResourceLocation fire_tex1 = new ResourceLocation("advancearmy:textures/entity/flash/tankmflash.png");
	public ResourceLocation fire_tex2 = new ResourceLocation("advancearmy:textures/entity/flash/muzzleflash3.png");
	private static final ResourceLocation ENCHANT_GLINT_LOCATION = new ResourceLocation("wmlib:textures/misc/vehicle_glint.png");
    public RenderTurretBase(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),1F);
        this.shadowStrength = 1F;
    }
    public ResourceLocation getTextureLocation(EntitySA_TurretBase entity)
    {
		return tex;
    }
	public boolean shouldRender(EntitySA_TurretBase p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
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
	
    private void render_part(EntitySA_TurretBase entity, String name){
		obj.renderPart(name);
		if(entity.getEnc()>0){
			setupGlintTexturing(8F);
			Minecraft.getInstance().getTextureManager().bind(ENCHANT_GLINT_LOCATION);
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			GL11.glPushMatrix();//glstart
			//GlStateManager._scalef(1.01F, 1.01F, 1.01F);
			obj.renderPart(name);
			GL11.glPopMatrix();//glend
			RenderSystem.matrixMode(5890);
			RenderSystem.popMatrix();
			RenderSystem.matrixMode(5888);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
			Minecraft.getInstance().getTextureManager().bind(tex);
		}
    }
	
    float iii;
	static boolean glow = true;
	static float shock =0;
    public void render(EntitySA_TurretBase entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		if(entity.obj!=null)obj = entity.obj;
		if(entity.tex!=null)tex = entity.tex;
		if(entity.guntex!=null)gtex=entity.guntex;
		if(entity.fire1tex!=null)fire_tex1=entity.fire1tex;
		if(entity.fire2tex!=null)fire_tex2=entity.fire2tex;
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
			if(entity.anim1<24*entity.w1barrelsize){
				if(!glow && this.shock<24*entity.w1barrelsize){
					++this.shock;
				}
				if(this.shock>=24*entity.w1barrelsize)glow = true;
				if(glow && this.shock>0){
					--this.shock;
				}
				if(this.shock<=0)glow = false;
			}else{
				this.shock=0;
			}
		}
		GL11.glRotatef(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		{
			GL11.glRotatef(180.0F - entity.turretYaw, 0.0F, 1.0F, 0.0F);
			if(entity.anim1<24*entity.w1barrelsize){
				float count = 24-entity.anim1;
				GL11.glRotatef(-this.shock *entity.w1barrelsize * count/50F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-this.shock *entity.w1barrelsize * count/60F, 0.0F, 1.0F, 0.0F);
			}
			GL11.glRotatef(-(180.0F - entity.turretYaw), 0.0F, 1.0F, 0.0F);
		}

		render_part(entity,"body");
		{
			//RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			obj.renderPart("body_light");
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
			//RenderSystem.enableDepthTest();
		}
		{//(entity.turretYawO + (entity.getTYaw() - entity.turretYawO) * partialTicks)
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(0F, 0F, 0F);
			GL11.glRotatef(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
			//GL11.glRotatef(180.0F - entity.turretYaw -(180.0F - entityYaw), 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0F, -0F, -0F);
			render_part(entity,"turret");
			//render_part(entity,"open");
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			obj.renderPart("head_light");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			GL11.glEnable(GL11.GL_LIGHTING);
			if(iii<360F){
				++iii;
			}else{
				iii = 0;
			}
			for(int t1 = 0; t1 < entity.radercount; ++t1){
				GL11.glPushMatrix();
				String tu1 = String.valueOf(t1 + 1);
				GL11.glTranslatef(entity.raderx[t1], entity.radery[t1], entity.raderz[t1]);//
				GL11.glRotatef(iii, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-entity.raderx[t1], -entity.radery[t1], -entity.raderz[t1]);//
				obj.renderPart("rader"+tu1);
				GL11.glPopMatrix();
			}
			
			float size2 = partialTicks * 0.3F + 1;
			
			GL11.glTranslatef(0F, entity.fireposY1, entity.firebaseZ);
			GL11.glRotatef((entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks), 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0F, -entity.fireposY1, -entity.firebaseZ);
			if(!entity.hidebarrel1 || entity.getFirstSeat()!=null && (entity.getFirstSeat().getControllingPassenger()!=null && 
			entity.getFirstSeat().getControllingPassenger() != mc.player||entity.getFirstSeat().getControllingPassenger()==null) || mc.options.getCameraType() != PointOfView.FIRST_PERSON){
				
				render_part(entity,"barrel");
				if(gtex!=null){
					mc.getTextureManager().bind(gtex);
					render_part(entity,"barrelgun");
					mc.getTextureManager().bind(tex);
				}
			}
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(0F, 0.09F, 1.55F);
			if(entity.turretPitch>-45){
				GL11.glRotatef(entity.turretPitch*0.8F, 1.0F, 0.0F, 0.0F);
			}else{
				GL11.glRotatef((-entity.turretPitch-90)*0.8F, 1.0F, 0.0F, 0.0F);
			}
			GL11.glTranslatef(0F, -0.09F, -1.55F);
			render_part(entity,"tripod");
			GL11.glPopMatrix();//glend
			GL11.glColor3f(1F, 1F, 1F);
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			obj.renderPart("barrel_light");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glColor3f(finalLight, finalLight, finalLight);
			if(entity.ammo){
				obj.renderPart("box");
				if(entity.count==0)obj.renderPart("ammo1");
				if(entity.count==1)obj.renderPart("ammo2");
				if(entity.count==2)obj.renderPart("ammo3");
				if(entity.anim1<4){
					if(entity.level.random.nextInt(4)==1){
						obj.renderPart("shell1");
					}
					if(entity.level.random.nextInt(4)==2){
						obj.renderPart("shell2");
					}
					if(entity.level.random.nextInt(4)==3){
						obj.renderPart("shell3");
					}
					if(entity.level.random.nextInt(4)==0){
						obj.renderPart("shell4");
					}
				}
			}
			if(entity.getHealth()>0){
				GL11.glPushMatrix();//glstart
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
				GL11.glColor3f(1F, 1F, 1F);
				mc.getTextureManager().bind(fire_tex2);
				GL11.glTranslatef(-entity.fireposX2, entity.fireposY2, entity.fireposZ2);
				GlStateManager._scalef(size2, size2, size2);
				
				if(entity.anim2<5)obj.renderPart("flash2");
				GL11.glPopMatrix();//glend
				
				GL11.glPushMatrix();//glstart
				mc.getTextureManager().bind(fire_tex1);
				GL11.glTranslatef(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
				if(entity.anim1 <4){
					GL11.glPushMatrix();//glstart
					GlStateManager._scalef(size2*1.5F, size2*1.5F, 1);
					obj.renderPart("flash");
					if(entity.level.random.nextInt(2)==1){
						obj.renderPart("mat_1");
					}else if(entity.level.random.nextInt(2)==2){
						obj.renderPart("mat_2");
					}else{
						obj.renderPart("mat_3");
					}
					Minecraft.getInstance().getTextureManager().bind(tex);
					GL11.glPopMatrix();//glend
				}
				
				if(entity.anim1 < 4){
					float size = (1 + partialTicks * 0.1F)*(float)(2+entity.anim1) / 3F;
					GlStateManager._scalef(size,size,size);
				}
				if(entity.anim1 >= 4 && entity.anim1<9){
					float size = (1 + partialTicks * 0.1F)*(float)(10-entity.anim1) / 4F;
					GlStateManager._scalef(size,size,size);
				}
				GL11.glTranslatef(-entity.fireposX1, -entity.fireposY1, -entity.fireposZ1);
				GL11.glTranslatef(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
				if(entity.anim1<9){
					obj.renderPart("flash1");
				}
				
				GL11.glPopMatrix();//glend
				mc.getTextureManager().bind(tex);
			}
			if(entity.anim1 >= 0 && entity.anim1 < 4*entity.w1barrelsize){
				GL11.glTranslatef(0.0F, 0.0F, -entity.anim1 * 0.3F*entity.w1barrelsize);
			}
			if(entity.anim1 >= 4 && entity.anim1 < 16*entity.w1barrelsize){
				GL11.glTranslatef(0.0F, 0.0F, -1.2F*entity.w1barrelsize);
				GL11.glTranslatef(0.0F, 0.0F, entity.anim1 * 0.1F*entity.w1barrelsize);
			}
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			GL11.glColor3f(finalLight, finalLight, finalLight);
			GL11.glEnable(GL11.GL_LIGHTING);
			render_part(entity,"barrel1");
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
 		if (!entity.isPassenger()){
            f6 = entity.animationPosition - entity.animationSpeed * (1.0F - partialTicks);
        }
 		return f6;
 	}
 	public float F5(LivingEntity entity, float partialTicks){
 		float f5 = 0;
 		if (!entity.isPassenger()){
            f5 = entity.animationSpeedOld + (entity.animationSpeed - entity.animationSpeedOld) * partialTicks;
            if (f5 > 1.0F){
                f5 = 1.0F;
            }
        }
 		return f5;
 	}
}