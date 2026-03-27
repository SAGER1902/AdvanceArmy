package advancearmy.render;

import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
//import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.ERO_Skeleton;

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
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
@OnlyIn(Dist.CLIENT)
public class SkeletonRenderer extends BipedRenderer<ERO_Skeleton, SkeletonModel<ERO_Skeleton>> {
   private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("advancearmy:textures/mob/evil_skeleton.png");
   public SkeletonRenderer(EntityRendererManager p_i46143_1_) {
      super(p_i46143_1_, new SkeletonModel<>(), 0.5F);
      this.addLayer(new BipedArmorLayer<>(this, new SkeletonModel(0.5F, true), new SkeletonModel(1.0F, true)));
   }

    float iii;
    public void render(ERO_Skeleton entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
   
   public ResourceLocation getTextureLocation(ERO_Skeleton p_110775_1_) {
      return SKELETON_LOCATION;
   }
}