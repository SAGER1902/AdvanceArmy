package advancearmy.render;

import com.mojang.blaze3d.matrix.MatrixStack;
//import net.minecraft.client.renderer.entity.layers.CreeperChargeLayer;
//import net.minecraft.client.renderer.entity.model.CreeperModel;
//import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.ERO_Creeper;

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
import wmlib.client.obj.SAObjModel;
import net.minecraft.entity.Entity;

import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
@OnlyIn(Dist.CLIENT)
public class CreeperRenderer extends MobRenderer<ERO_Creeper, CreeperModel<ERO_Creeper>> {
   private static final ResourceLocation CREEPER_LOCATION = new ResourceLocation("advancearmy:textures/mob/evil_creeper.png");
   public CreeperRenderer(EntityRendererManager p_i46186_1_) {
      super(p_i46186_1_, new CreeperModel<>(), 0.5F);
      this.addLayer(new CreeperChargeLayer(this));
   }

    float iii;
    public void render(ERO_Creeper entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
   
   protected void scale(ERO_Creeper p_225620_1_, MatrixStack p_225620_2_, float p_225620_3_) {
      float f = p_225620_1_.getSwelling(p_225620_3_);
      float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
      f = MathHelper.clamp(f, 0.0F, 1.0F);
      f = f * f;
      f = f * f;
      float f2 = (1.0F + f * 0.4F) * f1;
      float f3 = (1.0F + f * 0.1F) / f1;
      p_225620_2_.scale(f2, f3, f2);
   }

   protected float getWhiteOverlayProgress(ERO_Creeper p_225625_1_, float p_225625_2_) {
      float f = p_225625_1_.getSwelling(p_225625_2_);
      return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
   }

   public ResourceLocation getTextureLocation(ERO_Creeper p_110775_1_) {
      return CREEPER_LOCATION;
   }
}