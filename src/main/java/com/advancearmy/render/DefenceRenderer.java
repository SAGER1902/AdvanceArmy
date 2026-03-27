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
import advancearmy.entity.map.DefencePoint;
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
public class DefenceRenderer extends MobRenderer<DefencePoint, ModelNone<DefencePoint>>{
	/*private static final ResourceLocation dtex = new ResourceLocation("wmlib:textures/hud/count.png");
	private static final SAObjModel digit = new SAObjModel("wmlib:textures/hud/digit.obj");
	*/
	private static final ResourceLocation tex = new ResourceLocation("wmlib:textures/marker/point1.png");
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/marker/point1.obj");
	
	private static final ResourceLocation tex1 = new ResourceLocation("wmlib:textures/marker/soldier_type.png");
	private static final SAObjModel obj1 = new SAObjModel("wmlib:textures/marker/defence.obj");
	
    public ResourceLocation getTextureLocation(DefencePoint entity)
    {
		return tex;
    }
    public DefenceRenderer(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),0F);
    }
	
    float iii;
	static boolean glow = true;
	static float shock =0;
    public void render(DefencePoint entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.pushPose();
		Minecraft mc = Minecraft.getInstance();

		GL11.glPushMatrix();
		{
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
				
				//DefencePoint entity = (DefencePoint)entity;
				//RenderSystem.enableBlend();

				Minecraft.getInstance().getTextureManager().bind(tex1);
				//GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_DEPTH_TEST);//
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glEnable(GL11.GL_LIGHTING);//
				GL11.glEnable(GL11.GL_COLOR_MATERIAL);//
				obj1.renderPart("body");
				GL11.glColor4f(0.6F, 1, 0.6F, 1);
				if(entity.flag_time==0)obj1.renderPart("flag1");
				if(entity.flag_time==1)obj1.renderPart("flag2");
				if(entity.flag_time==2)obj1.renderPart("flag3");
				if(entity.flag_time==3)obj1.renderPart("flag4");
				if(entity.flag_time==4)obj1.renderPart("flag5");
				if(entity.flag_time==5)obj1.renderPart("flag6");
				
				Minecraft.getInstance().getTextureManager().bind(tex);
				/*if(!glow && iii<10F){
					++iii;
				}else{
					glow = true;
				}
				if(glow && iii>0){
					--iii;
				}else{
					glow = false;
				}
				GL11.glScalef(1+iii*0.02F, 1+iii*0.02F, 1+iii*0.02F);
				if(entity.isAttack){
					obj.renderPart("support2");
				}else{
					obj.renderPart("support1");
				}*/
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				GL11.glDisable(GL11.GL_LIGHTING);
		
				GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0, 5, 0);
				GL11.glRotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0, -5, 0);
				GL11.glTranslatef(0, 5, 0);
				if(entity.isAttack){
					obj.renderPart("show2");
				}else{
					obj.renderPart("show1");
				}
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
		
		GL11.glPopMatrix();//glend
		matrixStackIn.popPose();
	}
}