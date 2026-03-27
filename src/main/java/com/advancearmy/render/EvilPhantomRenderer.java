package advancearmy.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.layers.PhantomEyesLayer;
import net.minecraft.client.renderer.entity.model.PhantomModel;
import advancearmy.entity.mob.ERO_Phantom;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import wmlib.client.obj.SAObjModel;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import net.minecraft.util.math.vector.Vector3d;

@OnlyIn(Dist.CLIENT)
public class EvilPhantomRenderer extends MobRenderer<ERO_Phantom, PhantomModel<ERO_Phantom>> {
	private static final ResourceLocation PHANTOM_LOCATION = new ResourceLocation("advancearmy:textures/mob/phantom.png");
	private static final ResourceLocation PHANTOM_EXP = new ResourceLocation("advancearmy:textures/mob/phantom2.png");
	private static final ResourceLocation PHANTOM_LASER = new ResourceLocation("advancearmy:textures/mob/phantom3.png");
	/*private static final SAObjModel gun1 = new SAObjModel("advancearmy:textures/gun/model/ar/m16a4.obj");
	private static final ResourceLocation gun2 = new ResourceLocation("advancearmy:textures/gun/model/ar/m16a1.png");*/
	public EvilPhantomRenderer(EntityRendererManager p_i48829_1_) {
	  super(p_i48829_1_, new PhantomModel<>(), 0.75F);
	  //this.addLayer(new EvilPhantomEyesLayer<>(this));
	}
   public ResourceLocation getTextureLocation(ERO_Phantom entity) {
	   if(entity.getEvilPhantomSize()<3){
		   return PHANTOM_EXP;
	   }else if(entity.getEvilPhantomSize()<6){
		   return PHANTOM_LOCATION;
	   }else{
		   return PHANTOM_LASER;
	   }
      ///return entity.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LOCATION;
   }
   protected void scale(ERO_Phantom entity, MatrixStack p_225620_2_, float p_225620_3_) {
      int i = entity.getEvilPhantomSize();
      float f = 1.0F + 0.15F * (float)i;
      p_225620_2_.scale(f, f, f);
      p_225620_2_.translate(0.0D, 1.3125D, 0.1875D);
   }

   protected void setupRotations(ERO_Phantom entity, MatrixStack p_225621_2_, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
      super.setupRotations(entity, p_225621_2_, p_225621_3_, p_225621_4_, p_225621_5_);
      p_225621_2_.mulPose(Vector3f.XP.rotationDegrees(entity.xRot));
   }
     /*float iii;
    public void render(ERO_Phantom entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		
	    matrixStackIn.pushPose();
		//GlStateManager._enableBlend();
		GL11.glPushMatrix();
		//SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);//
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);//
		//GlStateManager._shadeModel(GL11.GL_SMOOTH);
		//GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		//GL11.glEnable(GL11.GL_BLEND);
		Minecraft mc = Minecraft.getInstance();
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
    	Minecraft.getInstance().getTextureManager().bind(PHANTOM_LOCATION);
		net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
		activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
		GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef((float) xIn, (float) yIn, (float) zIn);
		
		
		
		
		{
			 
		}
		
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		
		if(iii < 360F){
			iii = iii + 0.1F;
		}else{
			iii = 0F;
		}
		float f = ((float)(entity.getId() * 3) + partialTicks) * 0.13F;
		Minecraft minecraft = Minecraft.getInstance();
		GL11.glRotatef(180.0F - entity.yRot, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-(5.0F + MathHelper.cos(f * 2.0F) * 5.0F) * ((float)Math.PI / 180F), 1.0F, 0.0F, 0.0F);
		SAObjModel gun_model = gun1;
		GL11.glPushMatrix();//glstart
		GL11.glScalef(0.1875F, 0.1875F, 0.1875F);
		GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(-0.375F * 5.33F, 0 , -0.85F * 5.33F + 0.2F);//x,z,y
		minecraft.getTextureManager().bind(gun2);
		gun_model.renderPart("mat1");
		gun_model.renderPart("mat2");
		gun_model.renderPart("mat3");
		gun_model.renderPart("mat10");
		gun_model.renderPart("mat25");
		gun_model.renderPart("mat31");
		gun_model.renderPart("mat32");
		GL11.glPopMatrix();//glend
		
		GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		//GlStateManager._shadeModel(GL11.GL_SMOOTH);
		//GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
		//SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
		//GlStateManager._disableBlend();
	    matrixStackIn.popPose();
	}*/
}