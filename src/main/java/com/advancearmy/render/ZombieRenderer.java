package advancearmy.render;

//import net.minecraft.client.renderer.entity.model.ZombieModel;
//import net.minecraft.entity.monster.ZombieEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.ERO_Zombie;

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
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
@OnlyIn(Dist.CLIENT)
public class ZombieRenderer extends AbstractZombieRenderer<CreatureEntity, ZombieModel<CreatureEntity>> {
   public ZombieRenderer(EntityRendererManager p_i46127_1_) {
      super(p_i46127_1_, new ZombieModel<>(0.0F, false), new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true));
   }
    float iii;
    public void render(CreatureEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
}