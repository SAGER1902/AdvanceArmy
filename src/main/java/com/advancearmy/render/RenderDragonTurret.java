package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import advancearmy.entity.mob.DragonTurret;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import net.minecraft.client.renderer.entity.MobRenderer;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.BlockPos;
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

import static advancearmy.entity.mob.DragonTurret.*;
import wmlib.client.obj.SAObjModel;
import net.minecraft.world.LightType;

import net.minecraft.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
@OnlyIn(Dist.CLIENT)
public class RenderDragonTurret extends MobRenderer<DragonTurret, ModelNone<DragonTurret>>
{
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/dragonturret.obj");
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/mflash3.png");
	private static final ResourceLocation CRYSTAL = new ResourceLocation("textures/entity/end_crystal/end_crystal.png");
	private static final ResourceLocation tex = new ResourceLocation("textures/entity/enderdragon/dragon.png");
	
    public RenderDragonTurret(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),3F);
        this.shadowStrength = 3F;
    }

    public ResourceLocation getTextureLocation(DragonTurret entity)
    {
		return tex;
    }
    
	public boolean shouldRender(DragonTurret p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
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
	
    private void render_part(DragonTurret entity, String name){
		{
			obj.renderPart(name);
		}
    }

    float iii;
	float tick;
    float rote;
	static boolean glow = true;
	static int shock =0;
    public void render(DragonTurret entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	    matrixStackIn.pushPose();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);//
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);//
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);//
		GlStateManager._shadeModel(GL11.GL_SMOOTH);
		
		Minecraft mc = Minecraft.getInstance();
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
		
		
		
		
		if(entity.deathTime > 0) {
			GL11.glColor4f(0.1F, 0.1F, 0.1F, 1F);
		}
		else {
			
		}

		if(entity.deathTime > 0){
			GL11.glColor4f(0.1F, 0.1F, 0.1F, 1F);
		}

		if(entity.getVehicle()!=null){
			rote = entity.getVehicle().yRot;
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0, -3.2F, 0F);
		}else{
			rote = entity.yRot;
		}

		{
			GL11.glRotatef(180.0F - rote, 0.0F, 1.0F, 0.0F);
		}
		Minecraft.getInstance().getTextureManager().bind(CRYSTAL);
		render_part(entity,"mat1");
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		obj.renderPart("mat1_light");
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		Minecraft.getInstance().getTextureManager().bind(tex);
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(0F, 1F, 0F);
		GL11.glRotatef(entity.turretPitch, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(0F, -1F, -0F);
		render_part(entity,"mat4_2");
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		obj.renderPart("mat4_2_light");
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		Minecraft.getInstance().getTextureManager().bind(fire_tex);
		GL11.glTranslatef(0, 2.14F, 6.3F);
		float size = 1F;
		int time = DragonTurret.fire_tick2;
		if(time > 0 && time < 10){
			if(time>=0 && time<=4){
				size = time * 0.5F + 1;
			}
			if(time>4 && time<=8){//10 11 12 ... 19 <---
				size = (8 - time) * 0.3F + 1;
			}
		}
		GlStateManager._scalef(size, size, size);
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		if(DragonTurret.fire_tick<8)obj.renderPart("flash2");
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		GL11.glPopMatrix();//glend
		
		Minecraft.getInstance().getTextureManager().bind(tex);
		{
			GL11.glTranslatef(0F, 0F, 0F);
			{
				GL11.glRotatef(180.0F - entity.turretYaw -(180.0F - rote), 0.0F, 1.0F, 0.0F);
			}
			GL11.glTranslatef(-0F, -0F, -0F);
			
			if(entity.getVehicle()!=null)GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			
			render_part(entity,"mat4_1");
			render_part(entity,"mat5_1");
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			obj.renderPart("head");
			render_part(entity,"mat5_light");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			if(tick<360F){
				++tick;
			}else{
				tick = 0;
			}
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(0.0F, 0, -0.91F);
			GL11.glRotatef(tick, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, 0, 0.91F);
			obj.renderPart("rote");
			GL11.glPopMatrix();//glend
			GL11.glTranslatef(0F, 2.55F, 1F);
			GL11.glRotatef(entity.turretPitch, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0F, -2.55F, -1F);
			render_part(entity,"mat5");
				
			Minecraft.getInstance().getTextureManager().bind(fire_tex);
			if(DragonTurret.fire_tick2 <4){
				if(iii<360F){
					++iii;
				}else{
					iii = 0;
				}
				GL11.glTranslatef(0.0F, 2.55F, 0);
				GL11.glRotatef(iii, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(0.0F, -2.55F, 0);
				
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
			
			Minecraft.getInstance().getTextureManager().bind(tex);
			render_part(entity,"gun1");
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			if(DragonTurret.fire_tick<5)obj.renderPart("gun2");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		}

		GlStateManager._shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
		
	    matrixStackIn.popPose();
    }
}