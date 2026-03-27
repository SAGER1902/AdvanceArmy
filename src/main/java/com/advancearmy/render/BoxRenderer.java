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
import advancearmy.entity.map.RewardBox;
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
public class BoxRenderer extends MobRenderer<RewardBox, ModelNone<RewardBox>>{
	private ResourceLocation tex = new ResourceLocation("advancearmy:textures/entity/box1.jpg");
	private SAObjModel obj = new SAObjModel("advancearmy:textures/entity/box1.obj");
	
	private static final ResourceLocation tex2 = new ResourceLocation("advancearmy:textures/entity/box2.jpg");
	private static final SAObjModel obj2 = new SAObjModel("advancearmy:textures/entity/box2.obj");
	
	private static final ResourceLocation tex3 = new ResourceLocation("advancearmy:textures/entity/box3.png");
	private static final SAObjModel obj3 = new SAObjModel("advancearmy:textures/entity/box3.obj");
	
	private static final ResourceLocation tex4 = new ResourceLocation("advancearmy:textures/entity/box4.png");
	private static final SAObjModel obj4 = new SAObjModel("advancearmy:textures/entity/box4.obj");
	
	
    public ResourceLocation getTextureLocation(RewardBox entity)
    {
		return tex;
    }
    public BoxRenderer(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),0F);
    }
	
    float iii;
	static boolean glow = true;
	static float shock =0;
    public void render(RewardBox entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.pushPose();
		Minecraft mc = Minecraft.getInstance();
		if(entity.getBoxID()>2 && entity.getBoxID()<5){
			obj=obj2;
			tex=tex2;
		}else if(entity.getBoxID()>4 && entity.getBoxID()<7){
			obj=obj3;
			tex=tex3;
		}else if(entity.getBoxID()>6){
			obj=obj4;
			tex=tex4;
		}
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
				
				Minecraft.getInstance().getTextureManager().bind(tex);

				
				GL11.glEnable(GL11.GL_DEPTH_TEST);//
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glEnable(GL11.GL_LIGHTING);//
				GL11.glEnable(GL11.GL_COLOR_MATERIAL);//
				obj.renderPart("body");
				GL11.glDisable(GL12.GL_RESCALE_NORMAL);
				GL11.glDisable(GL11.GL_LIGHTING);
				SARenderHelper.enableBlendMode(RenderTypeSA.ALPHA);//ADDITIVE
				obj.renderPart("body_light");
				SARenderHelper.disableBlendMode(RenderTypeSA.ALPHA);//
			}
		}
		GL11.glPopMatrix();//glend
		matrixStackIn.popPose();
	}
}