package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.potion.Effects;
import advancearmy.entity.land.EntitySA_Ember;
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
import net.minecraft.client.settings.PointOfView;
import wmlib.common.living.EntityWMSeat;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Util;
@OnlyIn(Dist.CLIENT)
public class RenderEmber extends MobRenderer<EntitySA_Ember, ModelNone<EntitySA_Ember>>
{
	private static final ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/hj3.png");
    private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/ember_ii.obj");
	private static final ResourceLocation fire_tex = new ResourceLocation("advancearmy:textures/entity/flash/flash.png");
	private static final ResourceLocation cloud_tex = new ResourceLocation("advancearmy:textures/entity/flash/thruster_b.png");
	private static final ResourceLocation wave1 = ResourceLocation.tryParse("advancearmy:textures/entity/flash/sward_wave1.png");
	private static final ResourceLocation wave2 = ResourceLocation.tryParse("advancearmy:textures/entity/flash/sward_wave2.png");
	private static final ResourceLocation wave3 = ResourceLocation.tryParse("advancearmy:textures/entity/flash/sward_wave3.png");
	
	private static final ResourceLocation gun_tex = new ResourceLocation("advancearmy:textures/gun/hj_gun1_t.png");
	private static final ResourceLocation sward_tex = new ResourceLocation("advancearmy:textures/mob/wanderer2.png");
	public ResourceLocation dtex = new ResourceLocation("wmlib:textures/hud/box.png");
    public RenderEmber(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),2.5F);
    }
    public ResourceLocation getTextureLocation(EntitySA_Ember entity)
    {
		return tex;
    }
    private void render_light(EntitySA_Ember entity, String name){
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
	public static final ResourceLocation ENCHANT_GLINT_LOCATION = ResourceLocation.tryParse("wmlib:textures/misc/vehicle_made1.png");
	private static void renderEnchantGlint(String name, int color) {
		RenderSystem.matrixMode(5890);
		RenderSystem.pushMatrix();
		RenderSystem.loadIdentity();
		long i = Util.getMillis() * 8L;
		float f = (float)(i % 110000L) / 110000.0F;
		float f1 = (float)(i % 30000L) / 30000.0F;
		RenderSystem.translatef(-f, f1, 0.0F);
		RenderSystem.rotatef(10.0F, 0.0F, 0.0F, 1.0F);
		RenderSystem.scalef(1.01F, 1.01F, 1.01F);
		RenderSystem.matrixMode(5888);

		Minecraft.getInstance().getTextureManager().bind(ENCHANT_GLINT_LOCATION);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		if(color==1)GL11.glColor4f(1, 0.6F, 0.6F, 1);
		GL11.glPushMatrix();//glstart
		obj.renderPart(name);
		GL11.glPopMatrix();//glend
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.matrixMode(5890);
		RenderSystem.popMatrix();
		RenderSystem.matrixMode(5888);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
		Minecraft.getInstance().getTextureManager().bind(tex);
	}
	
    public float kneex = 1.8F;
	public float kneey = 4.2F;
	public float kneez = 0F;
	public float legx = 1.8F;
	public float legy = 3.03F;
	public float legz = 1.19F;
	public float bodyx = 0.0F;
	public float bodyy = 4.37F;
	public float bodyz = 0.0F;
	public float headx = 0.0F;
	public float heady = 5.2F;
	public float headz = 1.2F;
	public float elbowx = 1.8F;
	public float elbowy = 7.2F;
	public float elbowz = 0.09F;
	public float armx = 1.8F;
	public float army = 5.69F;
	public float armz = 0.09F;
	public float move_size = 0.4F;
	public float leg_rotez = 5;
	public float leg_rotex = 2;
	public float saber_x = 1.9F;
	public float saber_y = 1F;
	public float saber_z = -4.5F;
	public float saber_rote = 45;
	public float fire_x = 1.95F;
	public float fire_y = 4.8F;
	public float fire_z = 2.6F;
	
    float iii;
    public void render(EntitySA_Ember entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		Minecraft mc = Minecraft.getInstance();

		matrixStackIn.pushPose();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);//
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);//
		GlStateManager._shadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);

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
		
		if(iii < 360F){
			iii = iii + 10F;
		}else{
			iii = 0F;
		}
		GL11.glRotatef(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks), 0.0F, 1.0F, 0.0F);
		
		GL11.glPushMatrix();//glstart
		
		this.renderlegs(entity, limbSwing, limbSwingAmount, partialTicks);
		GL11.glPopMatrix();//glend

		float Ax1 = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;//
		/*if(!entity.isOnGround())*/GL11.glTranslatef( 0, Ax1 * (180F / (float)Math.PI) * 0.001F, 0);//
		{
			GL11.glPushMatrix();//glstart
			float body_rotex = 0;
			float body_rotey = 0;
			GL11.glTranslatef(bodyx, bodyy, bodyz);
			if(entity.move_mode == 1)GL11.glRotatef(15, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 2)GL11.glRotatef(-15, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 3)GL11.glRotatef(5, 0.0F, 0.0F, 1.0F);
			if(entity.move_mode == 4)GL11.glRotatef(5, 0.0F, 0.0F, -1.0F);
			if(entity.attackAnim>0){
				body_rotex = -20 + entity.attackAnim * 3;
				GL11.glPushMatrix();//glstart
				GL11.glRotatef(180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks)), 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0, 9.49F, -2.13F);
				GL11.glRotatef((entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks), 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0, - 9.49F, 2.13F);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glColor4f(1F, 1F, 1F, (8F-entity.attackAnim)/8F);
				GL11.glTranslatef(0, 0, entity.attackAnim*0.5F);//
				mc.getTextureManager().bind(wave1);
				if(entity.getMovePosY()==1){
					if(entity.getMoveMode()>0)mc.getTextureManager().bind(wave3);
					if(entity.attackAnim<8)obj.renderPart("wave1");
				}else if(entity.getMovePosY()==3){
					if(entity.getMoveMode()>0)mc.getTextureManager().bind(wave2);
					if(entity.attackAnim<8)obj.renderPart("wave2");
					body_rotey = 30-entity.attackAnim * 4;
				}else{
					if(entity.getMoveMode()>0)mc.getTextureManager().bind(wave2);
					if(entity.attackAnim<8)obj.renderPart("wave3");
					body_rotey = -30+entity.attackAnim * 8;;
				}
				GL11.glColor4f(1F, 1F, 1F, 1F);
				GL11.glEnable(GL11.GL_LIGHTING);//
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);
				mc.getTextureManager().bind(tex);
				GL11.glPopMatrix();//glend
			}
			float rote = 180.0F - (entity.turretYawO + (entity.turretYaw - entity.turretYawO) * partialTicks) -(180.0F - (entity.yHeadRotO + (entity.yHeadRot - entity.yHeadRotO) * partialTicks));
			GL11.glRotatef(rote, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(Ax1 * (180F / (float)Math.PI) * 0.001F, - 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(body_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(body_rotey, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-bodyx, -bodyy, -bodyz);
			
			GL11.glPushMatrix();
			GL11.glRotatef(-rote, 0.0F, 1.0F, 0.0F);
			obj.renderPart("waist");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("waist",0);
			GL11.glPopMatrix();
			
			this.renderbody(entity, limbSwing, limbSwingAmount, partialTicks, matrixStackIn, bufferIn);
			if(!entity.isOnGround()/* && entity.getMoveTypeype()==5*/){
				GL11.glRotatef(saber_rote, 1.0F, 0.0F, 0.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				this.render_cloud(mc,-saber_x,saber_y,saber_z,"cloud",iii);//
				this.render_cloud(mc,saber_x,saber_y,saber_z,"cloud",iii);//
			}
			GL11.glPopMatrix();//glend
		}
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GlStateManager._shadeModel(GL11.GL_FLAT);
		GL11.glPopMatrix();
		matrixStackIn.popPose();
    }
	
    private void renderlegs(EntitySA_Ember entity, float limbSwing, float limbSwingAmount, float partialTicks){
		float Ax = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
		float motion =  Ax * (180F / (float)Math.PI) * this.move_size;
		if(!entity.isOnGround()){
			motion = 15F;;
		}
    	GL11.glPushMatrix();//glstart
    	{
			GL11.glTranslatef(kneex, kneey, kneez);
			GL11.glRotatef(leg_rotez, 0.0F, 0.0F, 1.0F);
			if(entity.move_mode == 1)GL11.glRotatef(15, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 2)GL11.glRotatef(-15, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 3) {
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(10, 0.0F, 0.0F, 1.0F);
			}else if(entity.move_mode == 4) {
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-10, 0.0F, 0.0F, 1.0F);
			}else{
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
			}
			if(entity.isAttacking())GL11.glRotatef(leg_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef( - kneex,  - kneey, -  kneez);
			obj.renderPart("knee_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("knee_l",0);
			GL11.glTranslatef(legx, legy, legz);
			if(entity.move_mode == 3) {
				GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);//同理
				GL11.glRotatef(10, 0.0F, 0.0F, 1.0F);
			}else if(entity.move_mode == 4) {
				GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-10, 0.0F, 0.0F, 1.0F);
			}else{
				if(motion<0){
					GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);
				}else{
					GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
				}
			}
			if(entity.isAttacking())GL11.glRotatef(-leg_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(- legx, - legy, - legz);
			obj.renderPart("leg_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("leg_l",0);
			obj.renderPart("feet_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("feet_l",0);
		}
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		{
			GL11.glTranslatef(-kneex, kneey, kneez);
			GL11.glRotatef(-leg_rotez, 0.0F, 0.0F, 1.0F);
			if(entity.move_mode == 1)GL11.glRotatef(15, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 2)GL11.glRotatef(-15, 1.0F, 0.0F, 0.0F);
			if(entity.move_mode == 3) {
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(10, 0.0F, 0.0F, 1.0F);
			}else if(entity.move_mode == 4) {
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-10, 0.0F, 0.0F, 1.0F);
			}else{
				GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);//
			}
			if(entity.isAttacking())GL11.glRotatef(leg_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef( kneex,  - kneey, -  kneez);
			obj.renderPart("knee_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("knee_r",0);
			GL11.glTranslatef(-legx, legy, legz);
			if(entity.move_mode == 3) {
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(10, 0.0F, 0.0F, 1.0F);
			}else if(entity.move_mode == 4) {
				GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-10, 0.0F, 0.0F, 1.0F);
			}else{
				if(motion>0){
					GL11.glRotatef(motion * 1F, 1.0F, 0.0F, 0.0F);
				}else{
					GL11.glRotatef(motion * 1F, -1.0F, 0.0F, 0.0F);
				}
			}
			if(entity.isAttacking())GL11.glRotatef(-leg_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(legx, - legy, - legz);
			obj.renderPart("leg_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("leg_r",0);
			obj.renderPart("feet_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("feet_r",0);
		}
		GL11.glPopMatrix();//glend
	}
	
	float rote;
    private void renderbody(EntitySA_Ember entity, float limbSwing, float limbSwingAmount, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn){
		Minecraft minecraft = Minecraft.getInstance();
		GL11.glPushMatrix();//glstart
    	obj.renderPart("body");
		if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("body",0);
		/*if(minecraft.options.getCameraType() == PointOfView.FIRST_PERSON && entity.getControllingPassenger()!=null && entity.getControllingPassenger().getControllingPassenger()==minecraft.player){
			minecraft.getTextureManager().bind(dtex);
			obj.renderPart("box");
			float b1 = 25-entity.blacktime;
			float b2 = 50-entity.blacktime;
			float b3 = 75-entity.blacktime;
			float b4 = 100-entity.blacktime;
			renderBlack("black1",b1*0.04F);
			renderBlack("black2",b2*0.04F);
			renderBlack("black3",b3*0.04F);
			renderBlack("black4",b4*0.04F);
		}*/
		
		if(entity.getArmyType2()==1){
			minecraft.getTextureManager().bind(gun_tex);
			obj.renderPart("gun_wait");
		}else{
			minecraft.getTextureManager().bind(sward_tex);
			obj.renderPart("sward_wait");
			if(entity.getMoveMode()==1)this.renderEnchantGlint("sward_wait",1);
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
			obj.renderPart("sward_wait_light");
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
		}
		minecraft.getTextureManager().bind(tex);
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
		obj.renderPart("body_light");
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
		GL11.glPopMatrix();//glend
		
		float size = partialTicks * 0.3F + 1;
		GL11.glPushMatrix();//动画开头
		GL11.glTranslatef(0, 9.49F, -2.13F);
		GL11.glRotatef((entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks), 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(0, - 9.49F, 2.13F);
		obj.renderPart("gun2");
		if(entity.getMoveMode()==1)this.renderEnchantGlint("gun2",1);
		SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);
		obj.renderPart("gun2_light");
		minecraft.getTextureManager().bind(fire_tex);	
		//GL11.glRotatef(90, 1.0F, 0.0F, 0.0F);
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(1.31F, 9.17F, 0.57F);
		GlStateManager._scalef(size, size, size);
		if(entity.anim2<5){
			if(entity.level.random.nextInt(6)<2){
				this.render_light(entity, "flash1");
			}else if(entity.level.random.nextInt(6)<4){
				this.render_light(entity, "flash2");
			}else{
				this.render_light(entity, "flash3");
			}
		}
		GL11.glPopMatrix();//glend
		GL11.glPushMatrix();//glstart
		GL11.glTranslatef(-1.31F, 9.17F, 0.57F);
		GlStateManager._scalef(size, size, size);
		if(entity.anim2<5){
			if(entity.level.random.nextInt(6)<2){
				this.render_light(entity, "flash1");
			}else if(entity.level.random.nextInt(6)<4){
				this.render_light(entity, "flash2");
			}else{
				this.render_light(entity, "flash3");
			}
		}
		minecraft.getTextureManager().bind(tex);
		GL11.glPopMatrix();//glend
		SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
		GL11.glPopMatrix();

		float arm_l_rotex = 0;
		float arm_l_rotez = 0;
		float arm_l_rotey = 0;
		float arm_r_rotex = 0;
		float arm_r_rotez = 0;
		float arm_r_rotey = 0;
    	GL11.glPushMatrix();//glstart
    	{
			float Ax = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
			GL11.glTranslatef(elbowx, elbowy, elbowz);
			if(entity.getArmyType2()==2){
				GL11.glRotatef(15, 1.0F, 0.0F, 0.0F);
			}
			if(entity.getArmyType2()==1){
				if(entity.attackAnim>0){
					
					if(entity.getMovePosY()==1){
						arm_l_rotex = -140 + entity.attackAnim * 17;
					}else if(entity.getMovePosY()==3){
						arm_l_rotex = -140 + entity.attackAnim * 14;
						arm_l_rotey = -5+entity.attackAnim * 2;
						arm_l_rotez = 50-entity.attackAnim * 8;//
					}else{
						arm_l_rotex = -140 + entity.attackAnim * 13;
						arm_l_rotey = 5-entity.attackAnim * 2;
						arm_l_rotez = -50+entity.attackAnim * 10;//
					}
				}
			}else{
				arm_l_rotex = -41 + (entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks);
			}
			
			GL11.glRotatef(arm_l_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(arm_l_rotey, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(arm_l_rotez, 0.0F, 0.0F, 1.0F);
			
			if(entity.anim1<5){
				GL11.glTranslatef(armx, army, armz);
				GL11.glRotatef(-entity.anim1, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-armx, -army, -armz);
			}
			
			GL11.glTranslatef(- elbowx, - elbowy, - elbowz);
			
			obj.renderPart("elbow_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("elbow_l",0);
			if(entity.getArmyType2()==1 && entity.attackAnim==0){
				GL11.glTranslatef(armx, army, armz);
				GL11.glRotatef(15, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-armx, -army, -armz);
			}else{
				if(entity.anim1<5){
					GL11.glTranslatef(armx, army, armz);
					GL11.glRotatef(entity.anim1, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef(-armx, -army, -armz);
				}
			}
			obj.renderPart("arm_l");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("arm_l",0);
			GL11.glPushMatrix();//glstart
			if(entity.getArmyType2()==1){
				minecraft.getTextureManager().bind(sward_tex);
				obj.renderPart("sward_l");
				if(entity.getMoveMode()==1)this.renderEnchantGlint("sward_l",1);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
				obj.renderPart("sward_l_light");
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
			}else{
				minecraft.getTextureManager().bind(gun_tex);
				obj.renderPart("gun_l");
			}
			GL11.glPopMatrix();//glend
			
			if(entity.getArmyType2()==0){
				GL11.glPushMatrix();//glstart
				GL11.glRotatef(41, 1.0F, 0.0F, 0.0F);
				minecraft.getTextureManager().bind(fire_tex);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
				GL11.glTranslatef(fire_x, fire_y, fire_z);
				GlStateManager._scalef(size, size, size);
				if(entity.anim1<5){
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
			minecraft.getTextureManager().bind(tex);
		}
		GL11.glPopMatrix();//glend
		
		GL11.glPushMatrix();//glstart
		{
			float Ax = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
			GL11.glTranslatef(-elbowx, elbowy, elbowz);
			if(entity.getArmyType2()==2){
				GL11.glRotatef(15, 1.0F, 0.0F, 0.0F);
			}

			if(entity.getArmyType2()==1){
				if(entity.attackAnim>0){
					arm_r_rotex = -140 + entity.attackAnim * 17;
					if(entity.getMovePosY()==1){
						arm_r_rotex = -140 + entity.attackAnim * 17;
					}else if(entity.getMovePosY()==3){
						arm_r_rotex = -140 + entity.attackAnim * 14;
						arm_r_rotey = -5+entity.attackAnim * 2;
						arm_r_rotez = 50-entity.attackAnim * 8;//
					}else{
						arm_r_rotex = -140 + entity.attackAnim * 12;
						arm_r_rotey = 5-entity.attackAnim * 2;
						arm_r_rotez = -50+entity.attackAnim * 8;//
					}
				}
			}else{
				//if(EntitySA_Ember.gun_count1<5)GL11.glRotatef(-EntitySA_Ember.gun_count1, 1.0F, 0.0F, 0.0F);
				arm_r_rotex = -41 + (entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks);
			}

			GL11.glRotatef(arm_r_rotex, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(arm_r_rotey, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(arm_r_rotez, 0.0F, 0.0F, 1.0F);
			
			if(entity.anim1<5){
				GL11.glTranslatef(-armx, army, armz);
				GL11.glRotatef(entity.anim1, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(armx, -army, -armz);
			}
			
			GL11.glTranslatef(elbowx, - elbowy, - elbowz);
			obj.renderPart("elbow_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("elbow_r",0);
			if(entity.getArmyType2()==1 && entity.attackAnim==0){
				GL11.glTranslatef(-armx, army, armz);
				GL11.glRotatef(-15, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(armx, -army, -armz);
			}else{
				if(entity.anim1<5){
					GL11.glTranslatef(-armx, army, armz);
					GL11.glRotatef(-entity.anim1, 0.0F, 1.0F, 0.0F);
					GL11.glTranslatef(armx, -army, -armz);
				}
			}
			obj.renderPart("arm_r");
			if(entity.shieldHealth>0 && entity.hurtTime>0)this.renderEnchantGlint("arm_r",0);
			GL11.glPushMatrix();//glstart
			if(entity.getArmyType2()==1){
				minecraft.getTextureManager().bind(sward_tex);
				obj.renderPart("sward_r");
				if(entity.getMoveMode()==1)this.renderEnchantGlint("sward_r",1);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
				obj.renderPart("sward_r_light");
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
			}else{
				minecraft.getTextureManager().bind(gun_tex);
				obj.renderPart("gun_r");
			}
			GL11.glPopMatrix();//glend
			
			if(entity.getArmyType2()==0){
				GL11.glPushMatrix();//glstart
				GL11.glRotatef(41, 1.0F, 0.0F, 0.0F);
				minecraft.getTextureManager().bind(fire_tex);
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
				GL11.glTranslatef(-fire_x, fire_y, fire_z);
				GlStateManager._scalef(size, size, size);
				if(entity.anim1<5){
					if(entity.level.random.nextInt(6)<2){
						this.render_light(entity, "mat_1");
					}else if(entity.level.random.nextInt(6)<4){
						this.render_light(entity, "mat_2");
					}else if(entity.level.random.nextInt(6)==4){
						this.render_light(entity, "mat_3");
					}
				}
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
				GL11.glPopMatrix();//glend
			}
			minecraft.getTextureManager().bind(tex);
		}
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