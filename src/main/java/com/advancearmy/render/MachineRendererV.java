package advancearmy.render;
import java.util.List;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import com.mojang.blaze3d.systems.RenderSystem;
import wmlib.client.obj.SAObjModel;
import net.minecraft.entity.player.PlayerEntity;
import advancearmy.entity.building.VehicleMachine;
import advancearmy.item.ItemSpawn;

import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import wmlib.client.event.RenderEntityEvent;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;

import net.minecraft.util.Util;
@OnlyIn(Dist.CLIENT)
public class MachineRendererV extends MobRenderer<VehicleMachine, ModelNone<VehicleMachine>>{
	private static final ResourceLocation dtex = new ResourceLocation("wmlib:textures/hud/count.png");
	private static final SAObjModel model = new SAObjModel("wmlib:textures/hud/digit.obj");
	
	private static final ResourceLocation tex = new ResourceLocation("advancearmy:textures/mob/building/vehiclebuilding.png");
	private static final SAObjModel obj = new SAObjModel("advancearmy:textures/mob/building/vehiclebuilding.obj");
	
	private static final ResourceLocation glint = new ResourceLocation("wmlib:textures/misc/vehicle_made1.png");
    public ResourceLocation getTextureLocation(VehicleMachine entity)
    {
		return tex;
    }
    public MachineRendererV(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),0F);
    }
	
         
   private void renderArmWithItem(VehicleMachine p_229135_1_, ItemStack p_229135_2_, ItemCameraTransforms.TransformType p_229135_3_, HandSide p_229135_4_, MatrixStack stack, IRenderTypeBuffer p_229135_6_, int p_229135_7_) {
      if (!p_229135_2_.isEmpty()) {
         stack.pushPose();
         //this.getParentModel().translateToHand(p_229135_4_, stack);
         //stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
         stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
         stack.translate(2, 2, 0);
         Minecraft.getInstance().getItemInHandRenderer().renderItem(p_229135_1_, p_229135_2_, p_229135_3_, true, stack, p_229135_6_, p_229135_7_);
         stack.popPose();
      }
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
   
    float iii;
	static boolean glow = true;
	static float shock =0;
	public SAObjModel show = null;
    public void render(VehicleMachine entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.pushPose();
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
		GL11.glColor4f(finalLight, finalLight, finalLight, 1.0f);
		/*PlayerEntity entityplayer = mc.player;
		ItemStack itemstack1 = entityplayer.getItemBySlot(EquipmentSlotType.HEAD);
        Item item1 = itemstack1.getItem();*/
		GL11.glPushMatrix();
		/*if(item1 == Items.DIAMOND_HELMET)*/{
			if(entity.getMainHandItem()!=null){
				ItemStack this_heldItem = entity.getMainHandItem();
				Item item = this_heldItem.getItem();
				if(item instanceof ItemSpawn){
					ItemSpawn ispawn = (ItemSpawn)item;
					if(ispawn.obj_model!=null)this.show=ispawn.obj_model;
				}
			}
			{
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
				
				net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup = net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, activeRenderInfoIn, partialTicks);
				activeRenderInfoIn.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
				GL11.glRotatef(cameraSetup.getRoll(), 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(activeRenderInfoIn.getXRot(), 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(activeRenderInfoIn.getYRot() + 180.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef((float) xIn, (float) yIn, (float) zIn);
				EntityRendererManager manager = mc.getEntityRenderDispatcher();
				
				GL11.glRotatef(entity.getYawRoteNBT(), 0.0F, 1.0F, 0.0F);
				//VehicleMachine entity = (VehicleMachine)entity;
				float size1 = 2F;
				int count = entity.getMoney();
				GL11.glPushMatrix();
				
				int f = entity.finish_time;
				int ready = 0;
				if(f>0)ready = (entity.getBuild() * 100)/f;
				
				//RenderSystem.enableBlend();

				Minecraft.getInstance().getTextureManager().bind(tex);
				GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_DEPTH_TEST);//
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_LIGHTING);//
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);//
		GlStateManager._shadeModel(GL11.GL_SMOOTH);
				
				obj.renderPart("head");
				obj.renderPart("body");
				
		GlStateManager._shadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		
				if(count<=0||entity.isCover){
					GL11.glColor4f(1F, 0.3F, 0.3F, 1);
				}else{
					GL11.glColor4f(0.6F, 1, 0.6F, 1);
				}
				SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//
				obj.renderPart("head_light");
				obj.renderPart("body_light");
				if(entity.getMainHandItem()!=null)obj.renderPart("cover");
				
				if(entity.getBuild()>0){
					if(count>0&&!entity.isCover){
						if(!glow && this.shock<45){
							++this.shock;
						}
						if(this.shock>=45)glow = true;
						if(glow && this.shock>-45){
							--this.shock;
						}
						if(this.shock<=-45)glow = false;
					}
					GL11.glTranslatef(0F, 2F, 0.9F);
					GL11.glRotatef(shock,1,0,0);
					GL11.glTranslatef(0F, -2F, -0.9F);
					obj.renderPart("barrel");
				}
				SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
				GL11.glPopMatrix();//glend
				
				if(count<=0||entity.isCover){
					GL11.glColor4f(1F, 0.3F, 0.3F, (float)entity.getBuild()/entity.finish_time);
				}else{
					GL11.glColor4f(0.6F, 1, 0.6F, (float)entity.getBuild()/entity.finish_time);
				}
				
				if(this.show!=null){
					GL11.glPushMatrix();
					GL11.glTranslatef(0F, 0F, 6F);
					setupGlintTexturing(8F);
					Minecraft.getInstance().getTextureManager().bind(glint);
					//RenderSystem.depthMask(false);
					//RenderSystem.enableBlend();
					//RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
					GL11.glPushMatrix();//glstart
					//GlStateManager._scalef(1.01F, 1.01F, 1.01F);
					SARenderHelper.enableBlendMode(RenderTypeSA.ALPHA);//
					show.renderAll();
					SARenderHelper.disableBlendMode(RenderTypeSA.ALPHA);//
					GL11.glPopMatrix();//glend
					RenderSystem.matrixMode(5890);
					RenderSystem.popMatrix();
					RenderSystem.matrixMode(5888);
					//RenderSystem.defaultBlendFunc();
					//RenderSystem.disableBlend();
					//RenderSystem.depthMask(true);
					GL11.glPopMatrix();//glend
				}
				if(count<=0){
					GL11.glColor4f(1F, 0.3F, 0.3F, 1);
				}else{
					GL11.glColor4f(0.6F, 1, 0.6F, 1);
				}
				
				GL11.glRotatef(-entity.getYawRoteNBT(), 0.0F, 1.0F, 0.0F);
				GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0, 0, 0);
				GL11.glRotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0, 0, 0);
				GL11.glTranslatef(0F, 4F, 0F);
				Minecraft.getInstance().getTextureManager().bind(dtex);
				SARenderHelper.enableBlendMode(RenderTypeSA.ALPHA);//
				{
					GL11.glPushMatrix();
					renderCount(count);
					GL11.glPopMatrix();
				}
				
				GL11.glPushMatrix();
				GL11.glTranslatef(-2F, 1F, 0F);
				renderCount(entity.getVehicleC());
				GL11.glPopMatrix();
				
				{
					GL11.glTranslatef(0F, 1F, 0F);
					renderCount(ready);
					GlStateManager._translatef(0.3F*size1,0,0);
					model.renderPart("rate");//
				}
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				//RenderSystem.disableBlend();
				SARenderHelper.disableBlendMode(RenderTypeSA.ALPHA);//
				GL11.glPopMatrix();
			}
		}
		
		if(entity.getMainHandItem()!=null){
			ItemStack this_heldItem = entity.getMainHandItem();
			this.renderArmWithItem(entity, this_heldItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);
		}
		
		GL11.glPopMatrix();//glend
		matrixStackIn.popPose();
	}
	void renderCount(int count){
		float size1 = 2F;
		int num = count;
		int shiwei=0,baiwei=0,qianwei=0,gewei=0;
		qianwei = num / 1000;
		baiwei = (num % 1000) / 100;
		shiwei = (num / 10 ) % 10;
		gewei = (num %100) % 10;
		String t1 = String.valueOf(gewei);
		String t2 = String.valueOf(shiwei);
		String t3 = String.valueOf(baiwei);
		String t4 = String.valueOf(qianwei);
		if(count<10){
			model.renderPart("obj" + t1);//
		}else if(count<100)
		{
			model.renderPart("obj" + t2);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t1);//
		}else if(count<1000)
		{
			model.renderPart("obj" + t3);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t2);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t1);//
		}else if(count<10000)
		{
			model.renderPart("obj" + t4);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t3);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t2);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t1);//
		}else{
			model.renderPart("obj9");//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj9");//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj9");//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj9");//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("add");//+
		}
	}
}