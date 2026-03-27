package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.potion.Effects;
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
import advancearmy.entity.EntitySA_HeliBase;
import wmlib.client.obj.SAObjModel;
import net.minecraft.world.LightType;
import net.minecraft.client.settings.PointOfView;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Util;
@OnlyIn(Dist.CLIENT)
public class RenderHeliBase extends MobRenderer<EntitySA_HeliBase, ModelNoneVehicle<EntitySA_HeliBase>>
{
	public ResourceLocation rotor1 = new ResourceLocation("advancearmy:textures/mob/ah64rotor.png");
	public ResourceLocation rotor2 = new ResourceLocation("advancearmy:textures/mob/ah64rotor2.png");
	public ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/ah64.png");
	public SAObjModel obj = new SAObjModel("advancearmy:textures/mob/ah64.obj");
	public SAObjModel mg = new SAObjModel("advancearmy:textures/mob/30mm.obj");
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/mflash.png");
	public ResourceLocation dtex = new ResourceLocation("advancearmy:textures/mob/drive.png");
	
	private static final ResourceLocation nighttex = ResourceLocation.tryParse("wmlib:textures/hud/night.png");
	
    public RenderHeliBase(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNoneVehicle(),4F);
        this.shadowStrength = 4F;
    }

    public ResourceLocation getTextureLocation(EntitySA_HeliBase entity)
    {
		return tex;
    }
    
	public boolean shouldRender(EntitySA_HeliBase p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
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
   private static final ResourceLocation ENCHANT_GLINT_LOCATION = new ResourceLocation("wmlib:textures/misc/vehicle_glint.png");
    private void render_part(EntitySA_HeliBase entity, String name){
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
	
	private void render_turret(EntitySA_HeliBase entity, float x, float y, float z, float bz, float rote, float pitch, float fire, float tick, int id){
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
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(x, y, z);//
		GL11.glRotatef(tick*10F, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(-x, -y, -z);
		GL11.glTranslatef(x, y, z);
		mg.renderPart("rote");
		if(fire <4){
			GL11.glColor3f(1F, 1F, 1F);
			GL11.glPushMatrix();//glstart
			Minecraft.getInstance().getTextureManager().bind(fire_tex);
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
    public void render(EntitySA_HeliBase entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		if(entity.obj!=null)obj = entity.obj;
		if(entity.mgobj!=null)mg = entity.mgobj;
		if(entity.rotortex1!=null)rotor1 = entity.rotortex1;
		if(entity.rotortex2!=null)rotor2 = entity.rotortex2;
		if(entity.tex!=null)tex = entity.tex;
		if(entity.drivetex!=null)dtex = entity.drivetex;
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
		
		ActiveRenderInfo activeRenderInfoIn = mc.getEntityRenderDispatcher().camera;
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
			/*
			
			*/
			//GL11.glRotatef(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
			/*if(!entity.isOnGround())*/{
				GL11.glRotatef(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0, (float)entity.seatPosY[0]+1.32F, (float)entity.seatPosZ[0]);
				GL11.glRotatef(entity.flyPitch, 1.0F, 0.0F, 0.0F);
				//GL11.glRotatef(entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks, 1.0F, 0.0F, 0.0F);
				//GL11.glRotatef(entity.xRot, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(entity.flyRoll, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(0, (float)-entity.seatPosY[0]-1.32F, (float)-entity.seatPosZ[0]);
			}
			//entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks
			
		}
		if(!entity.isZoom||mc.options.getCameraType() != PointOfView.FIRST_PERSON){
			render_part(entity,"body");
			render_part(entity,"seat");
			if(dtex!=null)mc.getTextureManager().bind(dtex);
			render_part(entity,"drive");
			mc.getTextureManager().bind(tex);
			if(entity.movePower>=5F && !entity.isOnGround()){
				render_part(entity,"close");
			}else{
				render_part(entity,"gear");
			}
			if(entity.getRemain1()>0)obj.renderPart("missile_1");
			if(entity.getRemain1()>1)obj.renderPart("missile_2");
			if(entity.getRemain1()>2)obj.renderPart("missile_3");
			if(entity.getRemain1()>3)obj.renderPart("missile_4");
			//SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
			{
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.enableBlend();
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
				/*if(entity.getControllingPassenger()!=null && entity.getControllingPassenger().getControllingPassenger()!=mc.player)*/obj.renderPart("head");
				obj.renderPart("body_light");
				RenderSystem.defaultBlendFunc();
				RenderSystem.disableBlend();
				RenderSystem.depthMask(true);
				RenderSystem.enableDepthTest();
			}
			//SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		}
		/*if(entity.getHealth()>0)*/{
			for(int t1 = 0; t1 < entity.rotorcount; ++t1){
				GL11.glPushMatrix();
				String tu1 = String.valueOf(t1 + 1);
				GL11.glTranslatef(entity.rotorx[t1], entity.rotory[t1], entity.rotorz[t1]);//
				GL11.glRotatef(entity.thpera*entity.rotor_rotex[t1], 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(entity.thpera*entity.rotor_rotey[t1], 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(entity.thpera*entity.rotor_rotez[t1], 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(-entity.rotorx[t1], -entity.rotory[t1], -entity.rotorz[t1]);//
				if(entity.movePower<4||!entity.change_roter){
					render_part(entity,"pera" + tu1);
				}else{
					RenderSystem.depthMask(false);
					RenderSystem.enableBlend();
					if(t1==0)mc.getTextureManager().bind(rotor1);
					if(t1==1)mc.getTextureManager().bind(rotor2);
					obj.renderPart("rote" + tu1);
					RenderSystem.disableBlend();
					RenderSystem.depthMask(true);
				}
				GL11.glPopMatrix();
			}
			mc.getTextureManager().bind(tex);
		
			GL11.glPushMatrix();//glstart
			mc.getTextureManager().bind(fire_tex);
			if(entity.getRemain1()%2==0){
				GL11.glTranslatef(entity.fireposX1, entity.fireposY1, entity.fireposZ1);
			}else{
				GL11.glTranslatef(-entity.fireposX1, entity.fireposY1, entity.fireposZ1);
			}
			float size = 1F;
			int time = entity.anim2;
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
			if(entity.anim2<8 && (entity.isAttacking()||entity.getControllingPassenger()!=null))obj.renderPart("flash1");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
			GL11.glPopMatrix();//glend
			mc.getTextureManager().bind(tex);
		}
		{
			float size2 = partialTicks * 0.3F + 1;
			float rotet = 180.0F - (entity.turretYawO1 + (entity.turretYaw_1 - entity.turretYawO1) * partialTicks)-(180.0F - entity.yRot);
			float rotetp = entity.turretPitchO1 + (entity.turretPitch_1 - entity.turretPitchO1) * partialTicks -entity.flyPitch;
			if(entity.anim3<3){
				if(iii<360){
					++iii;
				}else{
					iii=0;
				}
			}
			//seat turret
			if(entity.mgobj!=null){
				if(entity.mgtex!=null)mc.getTextureManager().bind(entity.mgtex);
				render_turret(entity, entity.mgx, entity.mgy, entity.mgz, entity.mgbz, rotet, rotetp, entity.anim3, iii, 1);
				mc.getTextureManager().bind(tex);
			}
		}
		//GL11.glDisable(GL11.GL_BLEND);
		GlStateManager._shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		//GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	    matrixStackIn.popPose();
    }
}