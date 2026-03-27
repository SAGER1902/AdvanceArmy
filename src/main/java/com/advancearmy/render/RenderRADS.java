package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.potion.Effects;
import advancearmy.entity.soldier.EntitySA_RADS;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import wmlib.client.obj.SAObjModel;

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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.client.renderer.model.IBakedModel;
import com.mojang.blaze3d.systems.RenderSystem;
import wmlib.common.living.EntityWMSeat;
@OnlyIn(Dist.CLIENT)
public class RenderRADS extends MobRenderer<EntitySA_RADS, ModelNone<EntitySA_RADS>>
{
	public ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/soldier/radsoldier.png");
    private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/soldier/radsoldier.obj");
	public ResourceLocation gtex = new ResourceLocation("advancearmy:textures/gun/fsp.png");
    private static final SAObjModel gobj = new SAObjModel("advancearmy:textures/gun/fsp.obj");
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/rad_beam.png");
	
    public RenderRADS(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),0.5F);
    }

    public ResourceLocation getTextureLocation(EntitySA_RADS entity)
    {
		return tex;
    }
    private void render_light(EntitySA_RADS entity, String name){
		GL11.glPushMatrix();//glstart
		/*float lastx = RenderHelper.lastBrightnessX;
		float lasty = RenderHelper.lastBrightnessY;*/
		//RenderHelper.setLightmapTextureCoords(RenderHelper.lightmapTexUnit, 240f, 240f);
		RenderHelper.turnBackOn();
		obj.renderPart(name);
		RenderHelper.turnOff();
		//RenderHelper.setLightmapTextureCoords(RenderHelper.lightmapTexUnit, lastx, lasty);
		GL11.glPopMatrix();//glend
    }
	
    float iii;
    public void render(EntitySA_RADS entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		if(entity.hide){
			
		}else{
		Minecraft mc = Minecraft.getInstance();
		/*if(entity.hasEffect(Effects.INVISIBILITY)){
			if(mc.player==entity.getOwner()){
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.2F);
			}else{
				return;
			}
		}*/
	    matrixStackIn.pushPose();
		//GlStateManager._enableBlend();
		GL11.glPushMatrix();
		//SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);//
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);//
		GlStateManager._shadeModel(GL11.GL_SMOOTH);
		//GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		//GL11.glEnable(GL11.GL_BLEND);
		
    	float limbSwing = this.F6(entity, partialTicks);
		float limbSwingAmount = this.F5(entity, partialTicks);
		
		
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
		
		
		if(entity.anim1<3)GL11.glColor3f(1, 1, 1);
		

		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		if(entity.getVehicle()!=null)GL11.glScalef(0.8F, 0.8F, 0.8F);
		
		if(iii < 360F){
			iii = iii + 0.1F;
		}else{
			iii = 0F;
		}
		GL11.glRotatef(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		
		if(entity.getHealth()==0){
			int dt = entity.deathTime;
			if(dt>8)dt=8;
			GL11.glTranslatef(0F, 0.15F, -1.5F);
			GL11.glRotatef(-90F*dt/8F, 1.0F, 0.0F, 0.0F);//
		}else{
			if(entity.getRemain2()==1){
				GL11.glTranslatef(0F, 0.15F, -1.5F);
				GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);//
			}
		}
		
		{
			GL11.glPushMatrix();//glstart
			this.renderlegs(entity, limbSwing, limbSwingAmount, partialTicks);
			GL11.glPopMatrix();//glend
		}
		if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {//
			GL11.glTranslatef(0F, -0.5F, 0.0F);
		}
		float Ax1 = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		GL11.glTranslatef( 0, Ax1 * (180F / (float)Math.PI) * 0.001F, 0);//
    	{
    		GL11.glTranslatef(0F, 0.75F, 0.0F);
			GL11.glRotatef(Ax1 * (180F / (float)Math.PI) * 0.05F, - 1.0F, 0.0F, 0.0F);
    		if(entity.getRemain2()==2)GL11.glRotatef(20, 1.0F, 0.0F, 0.0F);
			if(entity.anim1<5)GL11.glRotatef(-entity.anim1, 1.0F, 0.0F, 0.0F);
    		GL11.glTranslatef(0F, -0.75F, 0.0F);
    	}

		{
			GL11.glPushMatrix();//glstart
			GL11.glTranslatef(0F, 1.5F, 0.0F);
			GL11.glRotatef(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks) -(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
			if(entity.getRemain2()==1){//
				GL11.glRotatef(entity.xRot-60F, 1.0F, 0.0F, 0.0F);
			}else{
				GL11.glRotatef(entity.xRot, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain2()==2 && entity.getRemain1()!=0 && entity.isAttacking())GL11.glRotatef(15F, 0.0F, 0.0F, 1.0F);//
			GL11.glTranslatef(0F, -1.5F, 0.0F);
			obj.renderPart("head");
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
			obj.renderPart("head_light");
			obj.renderPart("head_light");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
			GL11.glPopMatrix();//glend
		}
		
		{
			GL11.glPushMatrix();//glstart
			this.renderbody(entity, limbSwing, limbSwingAmount, partialTicks, matrixStackIn, bufferIn);
			GL11.glPopMatrix();//glend
		}
		
		GL11.glColor3f(1F, 1F, 1F);
		//GL11.glDisable(GL11.GL_BLEND);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GlStateManager._shadeModel(GL11.GL_SMOOTH);
		//GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
		//SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
		//GlStateManager._disableBlend();
	    matrixStackIn.popPose();
		
		}
    }
	
	
	
    public float move_size = 1F;//
    private void renderlegs(EntitySA_RADS entity, float limbSwing, float limbSwingAmount, float partialTicks){
		float Ax = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		float motion =  Ax * (180F / (float)Math.PI) * this.move_size;
    	GL11.glPushMatrix();//glstart
    	{
			GL11.glTranslatef(0.15F, 0.625F, 0.0F);
			if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {
				GL11.glTranslatef(0F, -0.5F, 0.0F);
				GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20, 0.0F, 0.0F, 1.0F);
			}else {
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);//
			}
			if(entity.getRemain2()==2 && entity.isAttacking())GL11.glRotatef(-30F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(-0.15F, -0.625F, 0.0F);
			obj.renderPart("knee_l");
			
			GL11.glTranslatef(0.15F, 0.4F, 0.05F);
			if(motion<0){
				GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);
			}else{
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain2()==2 && entity.isAttacking())GL11.glRotatef(25F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(-0.15F, -0.4F, -0.05F);
			obj.renderPart("leg_l");
			
		}
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		{
			GL11.glTranslatef(-0.15F, 0.625F, 0.0F);
			if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {
				GL11.glTranslatef(0F, -0.5F, 0.0F);
				GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
			}else {
				GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);//
			}
			if(entity.getRemain2()==2 && entity.isAttacking())GL11.glRotatef(15F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.15F, -0.625F, 0.0F);
			obj.renderPart("knee_r");
			
			GL11.glTranslatef(-0.15F, 0.4F, 0.05F);
			if(motion>0){
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
			}else{
				GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain2()==2 && entity.isAttacking())GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.15F, -0.4F, -0.05F);
			obj.renderPart("leg_r");
			
		}
		GL11.glPopMatrix();//glend
	}
    
    private void renderbody(EntitySA_RADS entity, float limbSwing, float limbSwingAmount, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn){
		Minecraft minecraft = Minecraft.getInstance();
		GL11.glPushMatrix();//glstart
    	obj.renderPart("body");
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
		obj.renderPart("body_light");
		obj.renderPart("body_light");
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
		GL11.glPopMatrix();//glend
		float arm_l_rotex = 0;
		float arm_l_rotez = 0;
		float arm_l_rotey = 0;
		float arm_r_rotex = 0;
		float arm_r_rotez = 0;
		float arm_r_rotey = 0;
		boolean move_arm = false;
		 
    	GL11.glPushMatrix();//glstart
    	{	
			float Ax = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
			GL11.glTranslatef(0.37F, 1.375F, 0.0F);
			if(entity.getRemain2()==2 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
			GL11.glRotatef(-30, 1.0F, 0.0F, 0.0F);
			}else if(entity.getRemain2()==2){
			GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				if(entity.getRemain2()!=1)arm_l_rotex = -20;
				arm_l_rotez = -50;
			}
			else if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)||entity.sit_aim && !entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_l_rotex = -90 + entity.xRot;
				arm_l_rotez = -30;
			}else if(entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_l_rotex = -30;
			}
			else if(entity.deathTime > 0){
				if(entity.getRemain2()!=1)arm_l_rotex = -30;
				arm_l_rotez = -40;
			}else {
				arm_l_rotex = Ax * (180F / (float)Math.PI) * 1.5F;
				move_arm = true;
			}
			
			if(entity.getRemain2()==1){//
				GL11.glRotatef(arm_l_rotex+180F, 1.0F, 0.0F, 0.0F);
			}else{
				GL11.glRotatef(arm_l_rotex, 1.0F, 0.0F, 0.0F);
			}
			GL11.glRotatef(arm_l_rotey, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(arm_l_rotez, 0.0F, 0.0F, 1.0F);
			
			GL11.glTranslatef(-0.37F, -1.375F, 0.0F);
			
			obj.renderPart("elbow_l");
			if(move_arm && !entity.sit_aim){
				GL11.glTranslatef(0.37F, 1.12F, -0.07F);
				if(Ax<0){
					GL11.glRotatef(arm_l_rotex, 1.0F, 0.0F, 0.0F);
				}else{
					GL11.glRotatef(arm_l_rotex, -1.0F, 0.0F, 0.0F);
				}
				GL11.glTranslatef(-0.37F, -1.12F, 0.07F);
			}
			obj.renderPart("arm_l");
		}
		GL11.glPopMatrix();//glend

		GL11.glPushMatrix();//glstart
		{
			float Ax = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
			GL11.glTranslatef(-0.37F, 1.375F, 0.0F);
			if(entity.getRemain2()==2 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
			GL11.glRotatef(-30, 1.0F, 0.0F, 0.0F);
			}else if(entity.getRemain2()==2){
			GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				arm_r_rotez = 10;
				if(entity.getRemain2()!=1)arm_r_rotex = -20;
			}
			else 
			if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)||entity.sit_aim && !entity.isPassenger()) {
				if(entity.anim1<5){
	    			GL11.glRotatef(-entity.anim1, 1.0F, 0.0F, 0.0F);
	    		}
				if(entity.getRemain2()!=1)arm_r_rotex = -90 + entity.xRot;
			}else if(entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_r_rotex = -30;
			}
			else if(entity.deathTime > 0){
				if(entity.getRemain2()!=1)arm_r_rotex = -20;
			}
			else {
				arm_r_rotex = Ax * (180F / (float)Math.PI)*0.8F;
				move_arm = true;
			}
			
			if(entity.getRemain2()==1 && !entity.sit_aim){//
				GL11.glRotatef(arm_r_rotex+180F, 1.0F, 0.0F, 0.0F);
			}else{
				GL11.glRotatef(arm_r_rotex, 1.0F, 0.0F, 0.0F);
			}
			GL11.glRotatef(arm_r_rotey, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(arm_r_rotez, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(0.37F, -1.375F, 0.0F);

			obj.renderPart("elbow_r");
			GL11.glTranslatef(-0.37F, 1.12F, -0.07F);
			if(move_arm && !entity.sit_aim){
				if(Ax<0){
					GL11.glRotatef(arm_r_rotex, 1.0F, 0.0F, 0.0F);
				}else{
					GL11.glRotatef(arm_r_rotex, -1.0F, 0.0F, 0.0F);
				}
			}
			if(!entity.isAttacking() && !entity.sit_aim)GL11.glRotatef(- 15, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.37F, -1.12F, 0.07F);
			obj.renderPart("arm_r");
			if(entity.canfire){
				GL11.glPushMatrix();//glstart
				GL11.glScalef(0.1875F, 0.1875F, 0.1875F);
				if(entity.getWeaponId()==2){
					//GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
				}else{
					GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
				}
				GL11.glTranslatef(-1.9F, 0 , -4.33F);//x,z,y
				minecraft.getTextureManager().bind(gtex);
				gobj.renderPart("mat1");
				
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
				gobj.renderPart("mat1_light");
				gobj.renderPart("mat1_light");
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
				
				GL11.glPopMatrix();//glend
				
				GL11.glPushMatrix();//glstart
				GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
				minecraft.getTextureManager().bind(fire_tex);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
				GL11.glTranslatef(-entity.fireposX, 0.23F, entity.fireposZ);
				float size = entity.level.random.nextInt(3) * 0.25F + 1;
				GlStateManager._scalef(size, size, size);
				if(entity.anim1<8 && entity.getRemain1() > 0 && entity.isAttacking())
				{
					GL11.glPushMatrix();
					GL11.glScalef(entity.anim1/4F, entity.anim1/4F, 1);
					obj.renderPart("mat_1");
					GL11.glPopMatrix();
					
					GL11.glPushMatrix();
					GL11.glScalef(entity.anim1/2F, entity.anim1/4F, 1);
					obj.renderPart("mat_2");
					GL11.glPopMatrix();
					
					GL11.glPushMatrix();
					GL11.glScalef(entity.anim1, entity.anim1, 1);
					obj.renderPart("mat_3");
					GL11.glPopMatrix();
				}
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
				minecraft.getTextureManager().bind(tex);
				GL11.glPopMatrix();//glend
			}
		}
		GL11.glPopMatrix();//glend
	}
    
    private void render_color(EntitySA_RADS entity, String name){
    	GL11.glPushMatrix();//glstart
			/*GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			{
				{
					GlStateManager.setupOutline();
					GlStateManager.setupOverlayColor(entity.getTeamColor(), 16);//16777215
				}*/
				obj.renderPart(name);
				/*{
					GlStateManager.teardownOutline();
					GlStateManager.teardownOverlayColor();
				}
			}
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);*/
    	GL11.glPopMatrix();//glend
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