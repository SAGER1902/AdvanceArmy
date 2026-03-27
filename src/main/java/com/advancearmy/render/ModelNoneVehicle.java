package advancearmy.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.entity.Entity;
//import wmlib.common.living.EntityWMVehicleBase;
@OnlyIn(Dist.CLIENT)
public class ModelNoneVehicle<T extends Entity> extends AgeableModel<T> {
   public ModelNoneVehicle() {
	   super(false, 24.0F, 0.0F);
	   this.bone = new ModelRenderer(this);
   }

	private final ModelRenderer bone;
   
   public void prepareMobModel(T p_212843_1_, float p_212843_2_, float p_212843_3_, float p_212843_4_) {
	      super.prepareMobModel(p_212843_1_, p_212843_2_, p_212843_3_, p_212843_4_);
	}
   public void setupAnim(T p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {

	}

   protected Iterable<ModelRenderer> headParts() {
	  return ImmutableList.of();
   }

   protected Iterable<ModelRenderer> bodyParts() {
	  return ImmutableList.of(this.bone);
   }
}
