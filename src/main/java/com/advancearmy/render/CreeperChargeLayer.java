package advancearmy.render;


import net.minecraft.client.renderer.entity.IEntityRenderer;
//import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
//import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;

import advancearmy.entity.mob.ERO_Creeper;
@OnlyIn(Dist.CLIENT)
public class CreeperChargeLayer extends EnergyLayer<ERO_Creeper, CreeperModel<ERO_Creeper>> {
   private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
   private final CreeperModel<ERO_Creeper> model = new CreeperModel<>(2.0F);

   public CreeperChargeLayer(IEntityRenderer<ERO_Creeper, CreeperModel<ERO_Creeper>> p_i50947_1_) {
      super(p_i50947_1_);
   }

   protected float xOffset(float p_225634_1_) {
      return p_225634_1_ * 0.01F;
   }

   protected ResourceLocation getTextureLocation() {
      return POWER_LOCATION;
   }

   protected EntityModel<ERO_Creeper> model() {
      return this.model;
   }
}