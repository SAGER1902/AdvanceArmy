package advancearmy.render;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.client.settings.PointOfView;
import net.minecraft.world.World;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.IVertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Util;
import advancearmy.entity.land.EntitySA_99G;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import wmlib.client.render.SARenderState;
import wmlib.client.obj.SAObjModel;
import net.minecraft.potion.Effects;
@OnlyIn(Dist.CLIENT)
public class Render99 extends MobRenderer<EntitySA_99G, ModelNoneVehicle<EntitySA_99G>>
{
	public ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/99g.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/99.obj");
	private static final ResourceLocation tankmflash = new ResourceLocation("advancearmy:textures/entity/flash/tankmflash.png");
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/muzzleflash3.png");
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	
    public Render99(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),4F);
	}

    public ResourceLocation getTextureLocation(EntitySA_99G entity)
    {
    	return tex;
	}
    
	public boolean shouldRender(EntitySA_99G p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
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
	
    private void render_part(EntitySA_99G entity, String name){
		{
			obj.renderPart(name);//
			if(entity.getEnc()>0){//
				setupGlintTexturing(8F);
				Minecraft.getInstance().getTextureManager().bind(ENCHANT_GLINT_LOCATION);
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
			}else{
				//Minecraft.getInstance().getTextureManager().bind(POWER_LOCATION);
			}
		}
    }

    float iii;
	private float recoilTime = 0f; // 用于三角函数的时间参数
	private float recoilIntensity = 0f; // 震动强度
	private float[] randomFactors = new float[3]; // 存储随机因素
	public static double noise(double x, double y) {
		return Math.sin(x * 10 + y * 5) * 0.5 + 0.5; // 简化版本
	}

    public void render(EntitySA_99G entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
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
		
		if(entity.anim1 < 12) {
			if(entity.anim1 == 0) {
				recoilTime = 0f;
				recoilIntensity = 1.0f; // 初始强度
				for(int i = 0; i < 3; i++) {
					randomFactors[i] = (entity.level.random.nextFloat() - 0.5f) * 0.4f;
				}
			}
			float progress = entity.anim1 / 12.0f;
			recoilTime += 0.2f; // 控制振荡速度
			recoilIntensity = (1.0f - progress) * (0.5f + 0.5f * (float)Math.sin(recoilTime));
			float highFreqShake = (float)Math.sin(recoilTime * 3f) * 0.1f * (1.0f - progress);
		} else {
			recoilIntensity = 0f;
		}
		{
			GL11.glRotatef(180.0F - entity.turretYaw, 0.0F, 1.0F, 0.0F);
			if(entity.anim1 < 12) {
				float progress = entity.anim1 / 12.0f;
				// 使用不同的三角函数创建多轴震动
				float pitchShake = (float)Math.sin(recoilTime) * recoilIntensity * (1.0f + randomFactors[0]);
				float yawShake = (float)Math.sin(recoilTime * 0.8f + 0.5f) * recoilIntensity * (0.7f + randomFactors[1]);
				float rollShake = (float)Math.cos(recoilTime * 1.2f) * recoilIntensity * 0.3f * randomFactors[2];
				
				GL11.glRotatef(pitchShake * 6f *entity.w1barrelsize, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-yawShake * 5f *entity.w1barrelsize, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(rollShake * 4f *entity.w1barrelsize, 0.0F, 0.0F, 1.0F);
				
				float pushBack = (float)Math.cos(recoilTime * 0.5f) * recoilIntensity * 0.02f;
				GL11.glTranslatef(0, 0, -pushBack);
				if(recoilIntensity > 0.3f) {
					float noiseX = (float)this.noise(recoilTime * 10f, 0) * 0.005f * recoilIntensity;
					float noiseY = (float)this.noise(0, recoilTime * 10f) * 0.005f * recoilIntensity;
					float noiseZ = (float)this.noise(recoilTime * 10f, recoilTime * 10f) * 0.005f * recoilIntensity;
					GL11.glTranslatef(noiseX, noiseY, noiseZ);
				}
			}
			GL11.glRotatef(-(180.0F - entity.turretYaw), 0.0F, 1.0F, 0.0F);
		}
		{
			GL11.glRotatef(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		}
		
        GL11.glRotatef(entity.currentTilt, 1.0F, 0.0F, 0.0F);
		
		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		GL11.glRotatef(Ax1 * (180F / (float)Math.PI) * 0.0002F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(Ax1 * (180F / (float)Math.PI) * 0.0002F, 0.0F, 1.0F, 0.0F);

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
		
		GlStateManager._pushMatrix();
		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.translatef(0, -entity.throttleRight, 0);
		RenderSystem.matrixMode(5888);
		//mc.getTextureManager().bind(tex);
		obj.renderPart("track_r");
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		
		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		RenderSystem.translatef(0, -entity.throttleLeft, 0);
		RenderSystem.matrixMode(5888);
		//mc.getTextureManager().bind(tex);
		obj.renderPart("track_l");
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		GlStateManager._popMatrix();
		
		float range = 0;
		{
			range = 0.0005F;
		}
		for(int t1 = 0; t1 < entity.wheelcount; ++t1){
			GL11.glPushMatrix();
			String tu1 = String.valueOf(t1 + 1);
			GL11.glTranslatef(entity.wheelx[t1], entity.wheely[t1], entity.wheelz[t1]);//
			GL11.glRotatef(entity.thpera*entity.wheel_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(entity.thpera*entity.wheel_rotey, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(entity.thpera*entity.wheel_rotez, 0.0F, 0.0F, 1.0F);
			if(t1%2==0){
				GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * range, 0);
			}else{
				GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * -range, 0);
			}
			GL11.glTranslatef(-entity.wheelx[t1], -entity.wheely[t1], -entity.wheelz[t1]);//
			render_part(entity, "wheel_" + tu1);
			GL11.glPopMatrix();
		}
		
		{
			float size2 = entity.level.random.nextInt(4) * 0.3F + 1;
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(0F, 0F, 0F);
			GL11.glRotatef(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0F, -0F, -0F);
			
			render_part(entity,"turret");
			
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(-0.98F, 3.98F, -1.4F);
			GL11.glRotatef(180.0F - (entity.turretYawO1 + (entity.turretYaw_1 - entity.turretYawO1) * partialTicks)-(180.0F - entity.turretYaw), 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.98F, -3.98F, 1.4F);
			
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(-0.98F, 3.98F, -1.4F);
			obj.renderPart("t1");
			GL11.glPopMatrix();//glend
			
			GL11.glTranslatef(-0.98F, 3.98F, -1.4F);
				GL11.glRotatef((entity.turretPitchO1 + (entity.turretPitch_1 - entity.turretPitchO1) * partialTicks), 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.98F, -3.98F, 1.4F);
			
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(-0.98F, 3.98F, -1.4F);
			obj.renderPart("b1");
			mc.getTextureManager().bind(fire_tex);
			
			GL11.glColor3f(1, 1, 1);
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			GlStateManager._scalef(size2*1.5F, size2*1.5F, 1);
			if(entity.anim3<5)obj.renderPart("flash3");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			GL11.glColor3f(finalLight, finalLight, finalLight);
			if(entity.getTargetType()==2)GL11.glColor3f(0.7F, 0.4F, 0.4F);
			GL11.glPopMatrix();//glend
			
			GL11.glPopMatrix();//glend
			mc.getTextureManager().bind(tex);
			
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(1.23F, 3.89F, -1.2F);
			GL11.glRotatef(180.0F - (entity.turretYawO2 + (entity.turretYaw_2 - entity.turretYawO2) * partialTicks)-(180.0F - entity.turretYaw), 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-1.23F, -3.89F, 1.2F);
			
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(1.23F, 3.89F, -1.2F);
			obj.renderPart("t2");
			GL11.glPopMatrix();//glend
			
			GL11.glTranslatef(1.23F, 3.89F, -1.2F);
			GL11.glRotatef((entity.turretPitchO2 + (entity.turretPitch_2 - entity.turretPitchO2) * partialTicks), 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(-1.23F, -3.89F, 1.2F);
			
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(1.23F, 3.89F, -1.2F);
			obj.renderPart("b2");
			GL11.glColor3f(1, 1, 1);
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			obj.renderPart("b2f");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			GL11.glColor3f(finalLight, finalLight, finalLight);
			if(entity.getTargetType()==2)GL11.glColor3f(0.7F, 0.4F, 0.4F);
			GL11.glPopMatrix();//glend
			
			GL11.glPopMatrix();//glend
			
			GL11.glTranslatef(0F, 2.41F, 2F);
			GL11.glRotatef((entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks), 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0F, -2.41F, -2F);
			render_part(entity,"mat5");
			if(entity.getHealth()>0){
				mc.getTextureManager().bind(fire_tex);
				GL11.glPushMatrix();//glstart
				GL11.glTranslatef(-0.23F, 2.41F, 2.96F);
				GlStateManager._scalef(size2, size2, size2);
				GL11.glColor3f(1, 1, 1);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
				if(entity.anim2<8)obj.renderPart("flash2");
				GL11.glPopMatrix();//glend
				GL11.glPushMatrix();//glstart
				GL11.glTranslatef(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
				if(entity.anim1 < 8){
					float size = (1 + partialTicks * 0.1F)*(float)(4+entity.anim1) / 4F;
					GlStateManager._scalef(size,size,size);
				}
				if(entity.anim1 >= 8 && entity.anim1<18){
					float size = (1 + partialTicks * 0.1F)*(float)(20-entity.anim1) / 6F;
					GlStateManager._scalef(size,size,size);
				}
				GL11.glTranslatef(-entity.fireposX1, -entity.fireposY1, -entity.fireposZ1);
				GL11.glTranslatef(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
				if(entity.anim1<18)obj.renderPart("flash1");
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
				GL11.glColor3f(finalLight, finalLight, finalLight);
				if(entity.getTargetType()==2)GL11.glColor3f(0.7F, 0.4F, 0.4F);
				GL11.glPopMatrix();//glend
			}
			{
				if(entity.anim1 >= 0 && entity.anim1 < 4){
					GL11.glTranslatef(0.0F, 0.0F, -entity.anim1 * 0.4F);
				}
				if(entity.anim1 >= 4 && entity.anim1 < 15){
					GL11.glTranslatef(0.0F, 0.0F, -1.2F);
					GL11.glTranslatef(0.0F, 0.0F, entity.anim1 * 0.1F);
				}
			}
			mc.getTextureManager().bind(tex);
			render_part(entity,"mat6");
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