package advancearmy.render;
import advancearmy.entity.mob.ERO_Phantom;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.PhantomModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
@OnlyIn(Dist.CLIENT)
public class EvilPhantomEyesLayer<T extends ERO_Phantom> extends AbstractEyesLayer<T, PhantomModel<T>> {
   private static final RenderType PHANTOM_EYES = RenderType.eyes(new ResourceLocation("advancearmy:textures/mob/phantom_eyes.png"));

   public EvilPhantomEyesLayer(IEntityRenderer<T, PhantomModel<T>> p_i50928_1_) {
      super(p_i50928_1_);
   }

   public RenderType renderType() {
      return PHANTOM_EYES;
   }
}