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
import advancearmy.entity.EntitySA_Seat;
import wmlib.client.obj.SAObjModel;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
//import wmlib.client.render.SARenderState;
@OnlyIn(Dist.CLIENT)
public class RenderSeat extends MobRenderer<EntitySA_Seat, ModelNone<EntitySA_Seat>>
{
	private static final ResourceLocation tex = new ResourceLocation("wmlib","textures/marker/seat.png");
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/marker/seat.obj");
    public RenderSeat(EntityRendererManager context) {
        super(context, new ModelNone(),0.5F);
    }
    public ResourceLocation getTextureLocation(EntitySA_Seat entity)
    {
		return tex;
    }
    public void render(EntitySA_Seat entity, float entityYaw, float partialTicks, MatrixStack poseStack, IRenderTypeBuffer buffer, int packedLight)
    {
	    poseStack.pushPose();
		GL11.glPushMatrix();
		Minecraft mc = Minecraft.getInstance();
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		if(mc.player.getVehicle()==null){
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
			//GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
			EntityRendererManager manager = mc.getEntityRenderDispatcher();
			GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
			mc.getTextureManager().bind(tex);
			//GlStateManager._disableCull();//
			//GlStateManager._depthMask(false);//
			
			SARenderHelper.enableBlendMode(RenderTypeSA.ADDITIVE);//ADDITIVE
			//RenderSystem.disableDepthTest();
			//RenderSystem.enableBlend();
			//RenderSystem.depthMask(false);
			//RenderSystem.disableAlphaTest();
			if(entity.getVehicle()!=null && entity.canRid()){
				if(entity.getVehicle() instanceof LivingEntity && ((LivingEntity)entity.getVehicle()).getHealth()>0){
					GL11.glTranslatef(0, (float)entity.seatPosY[0], 0);
					int i = entity.getVehicle().getPassengers().indexOf(entity);
					if(i==0)obj.renderPart("mat1");
					if(i>0){
						if(entity.weaponCount==0){
							obj.renderPart("mat3");
						}else{
							obj.renderPart("mat2");
						}
					}
				}
			}
			//RenderSystem.depthMask(true);
			//RenderSystem.enableAlphaTest();
			//RenderSystem.disableBlend();
			//RenderSystem.enableDepthTest();
			SARenderHelper.disableBlendMode(RenderTypeSA.ADDITIVE);//
			//GlStateManager._depthMask(true);
			//GlStateManager._enableCull();
		}
		//GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	    poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}