package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.potion.Effects;
import advancearmy.entity.soldier.EntitySA_ConscriptX;
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
import net.minecraftforge.common.util.Constants;

import net.minecraftforge.fml.ModList;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.client.renderer.model.IBakedModel;

import wmlib.common.living.EntityWMSeat;
@OnlyIn(Dist.CLIENT)
public class RenderConscriptX extends MobRenderer<EntitySA_ConscriptX, ModelNone<EntitySA_ConscriptX>>
{
	private static final ResourceLocation tex1 = new ResourceLocation("advancearmy:textures/mob/soldier/conscript.png");
	private ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/soldier/conscriptx.jpg");
    private SAObjModel obj = new SAObjModel("advancearmy:textures/mob/soldier/conscriptx.obj");
	private static final SAObjModel obj1 = new SAObjModel("advancearmy:textures/mob/soldier/conscript.obj");
    private static final ResourceLocation gun2 = new ResourceLocation("advancearmy:textures/gun/gun.png");
	private static final ResourceLocation gun1 = new ResourceLocation("advancearmy:textures/gun/ak470.jpg");
    private static final SAObjModel sward = new SAObjModel("advancearmy:textures/mob/soldier/minesward.obj");
    private static final ResourceLocation swardtex = new ResourceLocation("advancearmy:textures/mob/soldier/warminer.png");
	
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/mflash.png");
	private static final ResourceLocation cloud_tex = new ResourceLocation("advancearmy:textures/entity/flash/thruster_b.png");
	
    public RenderConscriptX(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),0.5F);
    }

    public ResourceLocation getTextureLocation(EntitySA_ConscriptX entity)
    {
		return tex;
    }
    private void render_light(EntitySA_ConscriptX entity, String name){
		GL11.glPushMatrix();//glstart
		RenderHelper.turnBackOn();
		obj.renderPart(name);
		RenderHelper.turnOff();
		GL11.glPopMatrix();//glend
    }
	
	public void render_cloud(Minecraft mc, float x,float y,float z,String name,float tick){//
		GL11.glPushMatrix();//
		mc.getTextureManager().bind(cloud_tex);//
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
		GL11.glTranslatef(x, y, z);//
		GL11.glTranslatef(0, 0, 0);//
		GL11.glRotatef(tick, 0.0F, 1.0F, 0.0F);//
		GL11.glTranslatef(0, 0, 0);//
		obj.renderPart(name);
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
		GL11.glPopMatrix();//
	}
	
    public float kneex = 0.25F;
	public float kneey = 1.36F;
	public float kneez = 0F;
	
	public float legx = 0.25F;//
	public float legy = 0.7F;
	public float legz = 0F;
	
	public float bodyx = 0.0F;//
	public float bodyy = 1.38F;
	public float bodyz = 0.0F;
	
	public float headx = 0.0F;//
	public float heady = 2.7F;
	public float headz = 0F;
	
	public float elbowx = 0.65F;//
	public float elbowy = 2.3F;
	public float elbowz = 0.09F;
	
	public float armx = 0.65F;//
	public float army = 1.7F;
	public float armz = 0F;
    
	public float move_size = 1F;

	public float leg_rotez = 2;//
	public float leg_rotex = 2;//
	
	public float saber_x = 0.56F;
	public float saber_y = 2.23F;
	public float saber_z = -0.47F;
	public float saber_rote = 0;
	
	public float gun_x = -0.65F;
	public float gun_y = 0.1F;
	public float gun_z = -1.5F;
	
	public float fire_x = -0.65F;
	public float fire_y = 0.3F;
	public float fire_z = 0.2F;
	
    float iii;
    public void render(EntitySA_ConscriptX entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		if(entity.hide){
			
		}else{
			/*if(entity.getRemain2()>5){
				obj = obj1;
				tex = tex1;
				this.kneex = 0.15F;
				this.kneey = 0.625F;
				this.kneez = 0F;
				this.legx = 0.15F;
				this.legy = 0.4F;
				this.legz = 0.05F;
				this.bodyx = 0.0F;
				this.bodyy = 0.75F;
				this.bodyz = 0.0F;
				this.headx = 0.0F;//
				this.heady = 1.5F;
				this.headz = 0F;
				this.elbowx = 0.37F;//
				this.elbowy = 1.375F;
				this.elbowz = 0F;
				this.armx = 0.37F;
				this.army = 1.12F;
				this.armz = -0.07F;
				this.move_size = 1F;
				this.leg_rotez = 2;//
				this.leg_rotex = 2;//
				this.fire_x = -0.36F;
				this.fire_y = 1.75F;
				this.fire_z = 1.8F;
				this.gun_x = -0.375F;
				this.gun_y = 0;
				this.gun_z = -0.85F;
			}else{
				
			}*/
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
			if(entity.anim1<3)GL11.glColor3f(1, 1, 1);
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
			
			if(iii < 360F){
				iii = iii + 10F;
			}else{
				iii = 0F;
			}
			GL11.glRotatef(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
			
			if(entity.getRemain2()==1){
				GL11.glTranslatef(0F, 0.15F, -1.5F);
				GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);//
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
				GL11.glPushMatrix();//glstart
				float body_rotex = 0;
				float body_rotey = 0;
				GL11.glTranslatef(bodyx, bodyy, bodyz);
				if(entity.getRemain2()==2)GL11.glRotatef(12, 1.0F, 0.0F, 0.0F);
				if(entity.attackAnim>0){
					body_rotex = -20 + entity.attackAnim * 3;
					if(entity.getMovePosY()==1){
						
					}else if(entity.getMovePosY()==3){
						body_rotey = 30-entity.attackAnim * 4;
					}else{
						body_rotey = -30+entity.attackAnim * 8;;
					}
				}
		    	GL11.glRotatef(Ax1 * (180F / (float)Math.PI) * 0.05F, - 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(body_rotex, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(body_rotey, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-bodyx, -bodyy, -bodyz);
				{
					GL11.glPushMatrix();//glstart
					GL11.glTranslatef(headx, heady, headz);
					GL11.glRotatef(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks) -(180.0F - (entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
					if(entity.getRemain2()==1){//
						GL11.glRotatef(entity.xRot-60F, 1.0F, 0.0F, 0.0F);
					}else{
						GL11.glRotatef(entity.xRot, 1.0F, 0.0F, 0.0F);
					}
					if(entity.getMovePosY()==0 && entity.getRemain2()==2 && entity.getRemain1()!=0 && entity.isAttacking())GL11.glRotatef(15F, 0.0F, 0.0F, 1.0F);//
					GL11.glTranslatef(- headx, - heady, - headz);
					obj.renderPart("head");
					obj.renderPart("head_light");
					GL11.glPopMatrix();//glend
				}
				this.renderbody(entity, limbSwing, limbSwingAmount, partialTicks, matrixStackIn, bufferIn);
				if(!entity.isOnGround() && entity.getAction()==5){
					this.render_cloud(mc,-saber_x,saber_y,saber_z,"cloud",iii);//
					this.render_cloud(mc,saber_x,saber_y,saber_z,"cloud",iii);//
				}
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
	
    private void renderlegs(EntitySA_ConscriptX entity, float limbSwing, float limbSwingAmount, float partialTicks){
		float Ax = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		float motion =  Ax * (180F / (float)Math.PI) * this.move_size;
    	GL11.glPushMatrix();//glstart
    	{
			GL11.glTranslatef(kneex, kneey, kneez);
			if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {
				GL11.glTranslatef(0F, -0.5F, 0.0F);
				GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20, 0.0F, 0.0F, 1.0F);
			}else {
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);//
			}
			if(entity.getRemain2()==2 && entity.isAttacking())GL11.glRotatef(-30F, 1.0F, 0.0F, 0.0F);
			if(entity.isAttacking())GL11.glRotatef(leg_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef( - kneex,  - kneey, -  kneez);
			obj.renderPart("knee_l");
			
			GL11.glTranslatef(legx, legy, legz);
			if(motion<0){
				GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);
			}else{
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain2()==2 && entity.isAttacking())GL11.glRotatef(25F, 1.0F, 0.0F, 0.0F);
			if(entity.isAttacking())GL11.glRotatef(-leg_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(- legx, - legy, - legz);
			obj.renderPart("leg_l");
			//this.render_light(entity, "leg_l_light");
		}
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		{
			GL11.glTranslatef(-kneex, kneey, kneez);
			if(entity.isPassenger()||entity.sit_aim && entity.getRemain2()!=1) {
				GL11.glTranslatef(0F, -0.5F, 0.0F);
				GL11.glRotatef(-90, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
			}else {
				GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);//
			}
			if(entity.getRemain2()==2 && entity.isAttacking())GL11.glRotatef(15F, 1.0F, 0.0F, 0.0F);
			if(entity.isAttacking())GL11.glRotatef(leg_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef( kneex,  - kneey, -  kneez);
			obj.renderPart("knee_r");
			
			GL11.glTranslatef(-legx, legy, legz);
			if(motion>0){
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
			}else{
				GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);
			}
			if(entity.getRemain2()==2 && entity.isAttacking())GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
			if(entity.isAttacking())GL11.glRotatef(-leg_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(legx, - legy, - legz);
			obj.renderPart("leg_r");
			//this.render_light(entity, "leg_r_light");
		}
		GL11.glPopMatrix();//glend
	}
	float rote;
    private void renderbody(EntitySA_ConscriptX entity, float limbSwing, float limbSwingAmount, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn){
		Minecraft minecraft = Minecraft.getInstance();
		GL11.glPushMatrix();//glstart
    	obj.renderPart("body");
		obj.renderPart("jet");
		obj.renderPart("waist");
		minecraft.getTextureManager().bind(gun2);
		obj.renderPart("wait1");
		if(entity.getMovePosY()>0){

		}else{
			minecraft.getTextureManager().bind(swardtex);
			obj.renderPart("wait2");
		}
		minecraft.getTextureManager().bind(tex);
		//this.render_light(entity, "body_light");
		GL11.glPopMatrix();//glend

		float arm_l_rotex = 0;
		float arm_l_rotez = 0;
		float arm_l_rotey = 0;
		float arm_r_rotex = 0;
		float arm_r_rotez = 0;
		float arm_r_rotey = 0;
		boolean turret = false;
		if (entity.getVehicle() instanceof EntityWMSeat && entity.getVehicle() != null) {
			EntityWMSeat vehicle = (EntityWMSeat) entity.getVehicle();
			turret = true;
		}
		boolean move_arm = false;
    	GL11.glPushMatrix();//glstart
    	{
			float Ax = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
			GL11.glTranslatef(elbowx, elbowy, elbowz);
			if(entity.getRemain2()==2 && entity.getMovePosY()==0 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
				GL11.glRotatef(-40, 1.0F, 0.0F, 0.0F);
			}else if(entity.getRemain2()==2 && entity.getMovePosY()==0){
				GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
			}
			
			if(entity.getMovePosY()>0){
				arm_l_rotex = -15+Ax * (180F / (float)Math.PI) * 1.5F;
			}else if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				arm_l_rotex = -20;
				arm_l_rotez = -50;
			}else if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)) {
				if(entity.getRemain2()!=1)arm_l_rotex = -90 + entity.xRot;
				arm_l_rotez = -45;
			}else if(entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_l_rotex = -30;
			}else if(entity.deathTime > 0){
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
			
			GL11.glTranslatef(- elbowx, - elbowy, - elbowz);
			
			obj.renderPart("elbow_l");
			if(move_arm){
				GL11.glTranslatef(armx, army, armz);
				if(Ax<0){
					GL11.glRotatef(arm_l_rotex, 1.0F, 0.0F, 0.0F);
				}else{
					GL11.glRotatef(arm_l_rotex, -1.0F, 0.0F, 0.0F);
				}
				GL11.glRotatef(arm_l_rotez/2, 0.0F, 0.0F, 1.0F);
				GL11.glTranslatef(-armx, -army, -armz);
			}
			obj.renderPart("arm_l");
		}
		GL11.glPopMatrix();//glend
		
		
		GL11.glPushMatrix();//glstart
		{
			float Ax = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
			GL11.glTranslatef(-elbowx, elbowy, elbowz);
			if(entity.getRemain2()==2 && !(entity.isAttacking()) && !(entity.getRemain1()==0)){
				GL11.glRotatef(-30, 1.0F, 0.0F, 0.0F);
			}else if(entity.getRemain2()==2){
				GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
			}

			if(entity.getMovePosY()>0){
				if(entity.attackAnim>0){
					arm_r_rotex = -160 + entity.attackAnim * 20;
					if(entity.getMovePosY()==1){
						
					}else if(entity.getMovePosY()==3){
						arm_r_rotey = -5+entity.attackAnim * 2;
						arm_r_rotez = 50-entity.attackAnim * 10;//
					}else{
						arm_r_rotey = 5-entity.attackAnim * 2;
						arm_r_rotez = -50+entity.attackAnim * 12;//
					}
				}
				//move_arm = true;
			}else if(entity.getRemain1()==0||entity.getRemain2()==2 && !(entity.isAttacking())){
				arm_r_rotez = 10;
				if(entity.getRemain2()!=1)arm_r_rotex = -20;
			}else if(entity.isAttacking() && !(entity.deathTime > 0) && !(entity.getRemain1()==0)) {
				if(entity.anim1<5)GL11.glRotatef(-entity.anim1, 1.0F, 0.0F, 0.0F);
				if(entity.getRemain2()!=1)arm_r_rotex = -90 + entity.xRot;
				arm_r_rotez = 10;
			}else if(entity.isPassenger()) {
				if(entity.getRemain2()!=1)arm_r_rotex = -30;
			}else if(entity.deathTime > 0){
				if(entity.getRemain2()!=1)arm_r_rotex = -20;
			}else {
				arm_r_rotex = Ax * (180F / (float)Math.PI)*0.8F;
				move_arm = true;
			}
			
			if(entity.getRemain2()==1){//
				GL11.glRotatef(arm_r_rotex+180F, 1.0F, 0.0F, 0.0F);
			}else{
				GL11.glRotatef(arm_r_rotex, 1.0F, 0.0F, 0.0F);
			}
			GL11.glRotatef(arm_r_rotey, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(arm_r_rotez, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(elbowx, - elbowy, - elbowz);
			obj.renderPart("elbow_r");
			if(move_arm){
				GL11.glTranslatef(-armx, army, armz);
				if(Ax<0){
					GL11.glRotatef(arm_r_rotex, 1.0F, 0.0F, 0.0F);
				}else{
					GL11.glRotatef(arm_r_rotex, -1.0F, 0.0F, 0.0F);
				}
				GL11.glTranslatef(armx, -army, -armz);
			}
			obj.renderPart("arm_r");

			if(!turret){
				GL11.glPushMatrix();//glstart
				GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
				if(entity.getMovePosY()>0){
					if(entity.isAttacking()){
						if(rote<2){
							++rote;
						}else{
							rote = 0;
						}
					}
					GL11.glTranslatef(gun_x, gun_y, gun_z+0.4F);
					minecraft.getTextureManager().bind(swardtex);
					obj.renderPart("base");
					if(rote==0)obj.renderPart("mat1");
					if(rote==1)obj.renderPart("mat2");
					if(rote==2)obj.renderPart("mat3");
				}else{
					GL11.glTranslatef(gun_x, gun_y, gun_z);
					minecraft.getTextureManager().bind(gun1);
					obj.renderPart("gun1");
				}
				GL11.glPopMatrix();//glend
				if(entity.getMovePosY()==0){
					GL11.glPushMatrix();//glstart
					GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
					minecraft.getTextureManager().bind(fire_tex);
					SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
					GL11.glTranslatef(fire_x, fire_y, fire_z);
					float size = entity.level.random.nextInt(3) * 0.25F + 1;
					GlStateManager._scalef(size, size, size);
					if(entity.anim1<3 && entity.getRemain1() > 0 && entity.isAttacking())
					{
						if(entity.level.random.nextInt(6)<2){
							this.render_light(entity, "mat_1");
						}else if(entity.level.random.nextInt(6)<4){
							this.render_light(entity, "mat_2");
						}else{
							this.render_light(entity, "mat_3");
						}
					}
					SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
					GL11.glPopMatrix();//glend
				}
			}
		}
		minecraft.getTextureManager().bind(tex);
		GL11.glPopMatrix();//glend
		this.render_light(entity, "jet_light");
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