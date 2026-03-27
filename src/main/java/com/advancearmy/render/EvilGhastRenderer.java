package advancearmy.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.GhastModel;
import advancearmy.entity.mob.ERO_Ghast;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import wmlib.client.obj.SAObjModel;
import net.minecraft.client.renderer.entity.MobRenderer;
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
public class EvilGhastRenderer extends MobRenderer<ERO_Ghast, GhastModel<ERO_Ghast>> {
   private static final ResourceLocation GHAST_LOCATION = new ResourceLocation("advancearmy:textures/mob/ghast.png");
   private static final ResourceLocation GHAST_BOOMER = new ResourceLocation("advancearmy:textures/mob/ghast2.png");
   private static final ResourceLocation GHAST_LASER = new ResourceLocation("advancearmy:textures/mob/ghast3.png");
   private static final ResourceLocation GHAST_SHOOTING_LOCATION = new ResourceLocation("advancearmy:textures/mob/ghast_shooting.png");

   public EvilGhastRenderer(EntityRendererManager p_i46174_1_) {
      super(p_i46174_1_, new GhastModel<>(), 1.5F);
   }

   public ResourceLocation getTextureLocation(ERO_Ghast entity) {
	   if(entity.getAIType()<=4){
		   if(entity.isCharging()){
			   return GHAST_SHOOTING_LOCATION;
		   }else{
			   return GHAST_LOCATION;
		   }
	   }else if(entity.getAIType()<=6){
		   if(entity.isCharging()){
			   return GHAST_SHOOTING_LOCATION;
		   }else{
			   return GHAST_BOOMER;
		   }
	   }else{
		   if(entity.isCharging()){
			   return GHAST_SHOOTING_LOCATION;
		   }else{
			   return GHAST_LASER;
		   }
	   }
      ///return entity.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LOCATION;
   }

   protected void scale(ERO_Ghast p_225620_1_, MatrixStack p_225620_2_, float p_225620_3_) {
      float f = 1.0F;
      float f1 = 4.5F;
      float f2 = 4.5F;
      p_225620_2_.scale(4.5F, 4.5F, 4.5F);
   }
}