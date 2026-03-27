package advancearmy.render;
import java.util.List;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
//import net.minecraft.client.renderer.entity.model.ZombieModel;
//import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Husk;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.CreatureEntity;
@OnlyIn(Dist.CLIENT)
public abstract class AbstractZombieRenderer<T extends CreatureEntity, M extends ZombieModel<T>> extends BipedRenderer<T, M> {
	private static final ResourceLocation HUSK_ENEMY = new ResourceLocation("advancearmy:textures/mob/evil_husk.png");
	private static final ResourceLocation ZOMBIE_ENEMY = new ResourceLocation("advancearmy:textures/mob/evil_zombie.png");
	protected AbstractZombieRenderer(EntityRendererManager p_i50974_1_, M p_i50974_2_, M p_i50974_3_, M p_i50974_4_) {
		super(p_i50974_1_, p_i50974_2_, 0.5F);
		this.addLayer(new BipedArmorLayer<>(this, p_i50974_3_, p_i50974_4_));
	}

	public ResourceLocation getTextureLocation(CreatureEntity ent) {
		if(ent instanceof ERO_Husk){
			return HUSK_ENEMY;
		}else{
			return ZOMBIE_ENEMY;
		}
	}

	protected boolean isShaking(T p_230495_1_) {
		return false;
		//return p_230495_1_.isUnderWaterConverting();
	}
	

	public void render(T entity/*T entity*/, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
}