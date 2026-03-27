package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.World;
@OnlyIn(Dist.CLIENT)
public class RenderTankBase extends MobRenderer<EntitySA_LandBase, ModelNoneVehicle<EntitySA_LandBase>>
{
	public ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/t90.png");
	public SAObjModel obj = new SAObjModel("advancearmy:textures/mob/t90.obj");
	public SAObjModel mg = new SAObjModel("advancearmy:textures/mob/kord.obj");

	private static final ResourceLocation tankmflash = new ResourceLocation("advancearmy:textures/entity/flash/tankmflash.png");
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/muzzleflash3.png");
	private static final ResourceLocation ENCHANT_GLINT_LOCATION = new ResourceLocation("wmlib:textures/misc/vehicle_glint.png");
	private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
	
	public ResourceLocation ptex = new ResourceLocation("advancearmy:textures/entity/dun1.png");
	
    public RenderTankBase(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),4F);
        this.shadowStrength = 4F;
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
	
	private void render_turret(EntitySA_LandBase entity, float x, float y, float z, float bz, float rote, float pitch, float fire, float tick, int id){
		float size2 = entity.level.random.nextInt(4) * 0.3F + 1;
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);//
		GL11.glRotatef(rote, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-x, -y, -z);//
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);
		mg.renderPart("turret");
		GL11.glPopMatrix();//glend
		
		GL11.glTranslatef(x, y, z+bz);//
		GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-x, -y, -z-bz);//
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);
		mg.renderPart("barrel");
		if(entity.ammo){
			mg.renderPart("box");
			if(EntitySA_LandBase.count==0)mg.renderPart("ammo1");
			if(EntitySA_LandBase.count==1)mg.renderPart("ammo2");
			if(EntitySA_LandBase.count==2)mg.renderPart("ammo3");
			if(fire<4){
				if(entity.level.random.nextInt(4)==1){
					mg.renderPart("shell1");
				}
				if(entity.level.random.nextInt(4)==2){
					mg.renderPart("shell2");
				}
				if(entity.level.random.nextInt(4)==3){
					mg.renderPart("shell3");
				}
				if(entity.level.random.nextInt(4)==0){
					mg.renderPart("shell4");
				}
			}
		}
		GL11.glPopMatrix();//glend
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);
		if(fire <4){
			GL11.glPushMatrix();//glstart
			Minecraft.getInstance().getTextureManager().bind(fire_tex);
			GL11.glColor3f(1, 1, 1);
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			GlStateManager._scalef(size2*1.5F, size2*1.5F, 1);
			mg.renderPart("flash");
			if(entity.level.random.nextInt(2)==1){
				mg.renderPart("mat_1");
			}else if(entity.level.random.nextInt(2)==2){
				mg.renderPart("mat_2");
			}else{
				mg.renderPart("mat_3");
			}
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			Minecraft.getInstance().getTextureManager().bind(tex);
			GL11.glPopMatrix();//glend
		}
		GL11.glPopMatrix();//glend
		GL11.glPopMatrix();//glend
	}

	
	

    float iii;
	float barrel_r;
	private float recoilTime = 0f; // 用于三角函数的时间参数
	private float recoilIntensity = 0f; // 震动强度
	private float[] randomFactors = new float[3]; // 存储随机因素
	public static double noise(double x, double y) {
		return Math.sin(x * 10 + y * 5) * 0.5 + 0.5; // 简化版本
	}
    public void render(EntitySA_LandBase entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		Minecraft mc = Minecraft.getInstance();
		/*if(entity.hasEffect(Effects.INVISIBILITY)){
			if(mc.player==entity.getOwner()){
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.2F);
			}else{
				return;
			}
		}else*/{
		
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
		
		
		if(entity.obj!=null)obj = entity.obj;
		if(entity.mgobj!=null)mg = entity.mgobj;
		if(entity.tex!=null)tex = entity.tex;
	    matrixStackIn.pushPose();
		GL11.glPushMatrix();
		
		
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);//
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);//
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);//
		GlStateManager._shadeModel(GL11.GL_SMOOTH);

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
		float wsize = 12*entity.w1barrelsize;
		if(entity.anim1 < wsize) {
			if(entity.anim1 == 0) {
				recoilTime = 0f;
				recoilIntensity = 1.0f; // 初始强度
				for(int i = 0; i < 3; i++) {
					randomFactors[i] = (entity.level.random.nextFloat() - 0.5f) * 0.4f;
				}
			}
			float progress = entity.anim1 / wsize;
			recoilTime += 0.2f*entity.w1barrelsize; // 控制振荡速度
			recoilIntensity = (1.0f - progress) * (0.5f + 0.5f * (float)Math.sin(recoilTime));
			float highFreqShake = (float)Math.sin(recoilTime * 3f) * 0.1f * (1.0f - progress);
		} else {
			recoilIntensity = 0f;
		}
		{
			GL11.glRotatef(180.0F - entity.turretYaw, 0.0F, 1.0F, 0.0F);
			if(entity.anim1 < wsize) {
				float progress = entity.anim1 / wsize;
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
		

		GL11.glRotatef(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		

		GL11.glRotatef(entity.currentTilt, 1.0F, 0.0F, 0.0F);
		
		

		float limbSwing = this.F6(entity, partialTicks);//
		float limbSwingAmount = this.F5(entity, partialTicks);//
		float Ax1 = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		GL11.glTranslatef(0, Ax1 * (180F / (float)Math.PI) * 0.0002F , 0);
		GL11.glRotatef(Ax1 * (180F / (float)Math.PI) * 0.0002F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(Ax1 * (180F / (float)Math.PI) * 0.0002F, 0.0F, 1.0F, 0.0F);
		
		
		if(entity.duntex!=null)ptex = entity.duntex;
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
		
		if(entity.tracktex!=null)mc.getTextureManager().bind(entity.tracktex);
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
		if(entity.tracktex!=null)mc.getTextureManager().bind(tex);
		
		float range = 0;
		{
			range = 0.0005F;
		}
		for(int t1 = 0; t1 < entity.wheelcount; ++t1){
			GL11.glPushMatrix();
			String tu1 = String.valueOf(t1 + 1);
			GL11.glTranslatef(entity.wheelx[t1], entity.wheely[t1], entity.wheelz[t1]);//
			if(entity.wheelturn[t1])GL11.glRotatef(entity.rote_wheel, 0.0F, 1.0F, 0.0F);
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
		{//(entity.turretYawO + (entity.getTYaw() - entity.turretYawO) * partialTicks)
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(0F, 0F, 0F);
			GL11.glRotatef(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
			//GL11.glRotatef(180.0F - entity.turretYaw -(180.0F - entityYaw), 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0F, -0F, -0F);
			render_part(entity,"mat4");
		
			GL11.glColor3f(1, 1, 1);
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			obj.renderPart("head_light");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			GL11.glColor3f(finalLight, finalLight, finalLight);
			if(entity.getTargetType()==2)GL11.glColor3f(0.7F, 0.4F, 0.4F);
			
			render_part(entity,"close");
			
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
			float rotet = 180.0F - (entity.turretYawO1 + (entity.turretYaw_1 - entity.turretYawO1) * partialTicks)-(180.0F - entity.turretYaw);
			float rotetp = entity.turretPitchO1 + (entity.turretPitch_1 - entity.turretPitchO1) * partialTicks;
			//seat turret
			if(entity.mgtex!=null){
				mc.getTextureManager().bind(entity.mgtex);
				render_turret(entity, entity.mgx, entity.mgy, entity.mgz, entity.mgbz, rotet, rotetp, entity.anim3, 0, 1);
				GL11.glColor3f(finalLight, finalLight, finalLight);
				if(entity.getTargetType()==2)GL11.glColor3f(0.7F, 0.4F, 0.4F);
				
				mc.getTextureManager().bind(tex);
			}
			
			float pitch = (entity.turretPitchO + (entity.turretPitch - entity.turretPitchO) * partialTicks);
			if(pitch<entity.w1pitchlimit)pitch=entity.w1pitchlimit;
			
			GL11.glTranslatef(0F, entity.fireposY1, entity.firebaseZ);
			GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0F, -entity.fireposY1, -entity.firebaseZ);
			render_part(entity,"mat5");
			GL11.glColor3f(1, 1, 1);
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			obj.renderPart("barrel_light");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			GL11.glColor3f(finalLight, finalLight, finalLight);
			if(entity.getTargetType()==2)GL11.glColor3f(0.7F, 0.4F, 0.4F);
			
			GL11.glPushMatrix();//glstart
			if(entity.anim1 < 2){
				if(barrel_r<360F){
					++barrel_r;
				}else{
					barrel_r = 0;
				}
			}
			GL11.glTranslatef(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
			GL11.glRotatef(barrel_r*20F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(-entity.fireposX1, -entity.fireposY1, -entity.fireposZ1);
			obj.renderPart("rote");
			GL11.glPopMatrix();//glend
			
			if(entity.getHealth()>0){
				GL11.glPushMatrix();//glstart
				mc.getTextureManager().bind(fire_tex);
				GL11.glTranslatef(-entity.fireposX2, entity.fireposY2, entity.fireposZ2);
				GlStateManager._scalef(size2, size2, size2);
				GL11.glColor3f(1, 1, 1);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
				if(entity.anim2<5)obj.renderPart("flash2");

				GL11.glPopMatrix();//glend
				GL11.glPushMatrix();//glstart
				mc.getTextureManager().bind(tankmflash);

				GL11.glTranslatef(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
				if(entity.anim1 < 4){
					float size = (1 + partialTicks * 0.1F)*(float)(2+entity.anim1) / 3F;
					GlStateManager._scalef(size,size,size);
				}
				if(entity.anim1 >= 4 && entity.anim1<9){
					float size = (1 + partialTicks * 0.1F)*(float)(10-entity.anim1) / 4F;
					GlStateManager._scalef(size,size,size);
				}
				if(entity.anim1<9){
					obj.renderPart("flash1");
					if(entity.level.random.nextInt(2)==1){
						obj.renderPart("mat_1");
					}else if(entity.level.random.nextInt(2)==2){
						obj.renderPart("mat_2");
					}else{
						obj.renderPart("mat_3");
					}
				}
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
				GL11.glColor3f(finalLight, finalLight, finalLight);
				if(entity.getTargetType()==2)GL11.glColor3f(0.7F, 0.4F, 0.4F);
				
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