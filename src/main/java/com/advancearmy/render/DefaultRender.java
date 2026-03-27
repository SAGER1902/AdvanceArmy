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
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import com.mojang.blaze3d.systems.RenderSystem;
import wmlib.client.obj.SAObjModel;
import net.minecraft.entity.player.PlayerEntity;
import advancearmy.entity.map.ArmyMovePoint;
import advancearmy.entity.map.CreatureRespawn;
import advancearmy.entity.map.VehicleRespawn;
import advancearmy.entity.building.SandBag;

import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import wmlib.client.event.RenderEntityEvent;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.inventory.EquipmentSlotType;
@OnlyIn(Dist.CLIENT)
public class DefaultRender extends MobRenderer<MobEntity, ModelNone<MobEntity>>{
	private static final ResourceLocation tex = ResourceLocation.tryParse("wmlib:textures/hud/count.png");
	private static final SAObjModel model = new SAObjModel("wmlib:textures/hud/digit.obj");
	private static final ResourceLocation sbtex = ResourceLocation.tryParse("wmlib:textures/entity/sandbag.png");
	private static final SAObjModel sb = new SAObjModel("wmlib:textures/entity/sandbag.obj");
	private static final SAObjModel icon = new SAObjModel("wmlib:textures/entity/icon.obj");
	private static final ResourceLocation tex1 = ResourceLocation.tryParse("advancearmy:textures/item/maptool_respawnc.png");
	private static final ResourceLocation tex2 = ResourceLocation.tryParse("advancearmy:textures/item/maptool_respawnv.png");
	private static final ResourceLocation tex3 = ResourceLocation.tryParse("advancearmy:textures/item/maptool_movepoint.png");
    public ResourceLocation getTextureLocation(MobEntity entity)
    {
		return tex;
    }
    public DefaultRender(EntityRendererManager renderManagerIn)
    {
    	super(renderManagerIn, new ModelNone(),0F);
    }
	private void renderArmWithItem(MobEntity p_229135_1_, ItemStack p_229135_2_, ItemCameraTransforms.TransformType p_229135_3_, HandSide p_229135_4_, MatrixStack stack, IRenderTypeBuffer p_229135_6_, int p_229135_7_) {
		if (!p_229135_2_.isEmpty()) {
			stack.pushPose();
			//this.getParentModel().translateToHand(p_229135_4_, stack);
			/*stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
			stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
			boolean flag = p_229135_4_ == HandSide.LEFT;
			stack.translate((double)((float)(flag ? -1 : 1) / 16.0F), 0.125D, -0.625D);*/
			stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
			stack.translate(0, 1, 0);
			Minecraft.getInstance().getItemInHandRenderer().renderItem(p_229135_1_, p_229135_2_, p_229135_3_, true, stack, p_229135_6_, p_229135_7_);
			stack.popPose();
		}
	}
	
    public void render(MobEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.pushPose();
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity entityplayer = mc.player;
		ItemStack itemstack1 = entityplayer.getItemBySlot(EquipmentSlotType.HEAD);
        Item item1 = itemstack1.getItem();
		GL11.glPushMatrix();
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
			
		if(entity instanceof SandBag){
			Minecraft.getInstance().getTextureManager().bind(sbtex);
			GL11.glEnable(GL11.GL_DEPTH_TEST);//
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_LIGHTING);//
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);//
			GlStateManager._shadeModel(GL11.GL_SMOOTH);
			int blockLight = (packedLightIn & 0xFFFF) >> 4;
			int skyMask = packedLightIn >> 20 & 15;
			float rainLevel = mc.level.getRainLevel(partialTicks);
			float thunderLevel = mc.level.getThunderLevel(partialTicks);
			float sunAngle = mc.level.getSunAngle(partialTicks);
			float daylight = MathHelper.cos(sunAngle) * 0.5f + 0.5f;
			daylight = MathHelper.clamp(daylight, 0.0f, 1.0f);
			float weatherFactor = 1.0f - (rainLevel * 0.1f) - (thunderLevel * 0.2f);
			float skyLight = (skyMask / 15.0f) * daylight * weatherFactor;
			float blockLight1 = blockLight / 15.0f;
			float finalLight = Math.max(skyLight, blockLight1)+0.2f;
			GL11.glRotatef(((SandBag)entity).getYawRote(), 0.0F, 1F, 0F);
			GL11.glColor3f(finalLight, finalLight, finalLight);
			if(entity.getHealth()>60){
				sb.renderPart("mat1");
			}else if(entity.getHealth()>30&&entity.getHealth()<=60){
				sb.renderPart("mat2");
			}else if(entity.getHealth()<=30){
				sb.renderPart("mat3");
			}
			GL11.glColor3f(1F, 1F, 1F);
			GlStateManager._shadeModel(GL11.GL_FLAT);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glDisable(GL11.GL_LIGHTING);
		}
		
		if(item1 != Items.DIAMOND_HELMET && entityplayer.isCreative()){
			if(entity.getMainHandItem()!=null){
				ItemStack this_heldItem = entity.getMainHandItem();
				ItemStack this_heldItem2 = entity.getOffhandItem();
				if(entity instanceof VehicleRespawn)this.renderArmWithItem(entity, this_heldItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStackIn, bufferIn, packedLightIn);
				//this.renderArmWithItem(entity, this_heldItem2, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStackIn, bufferIn, packedLightIn);
			}
			if(entity instanceof CreatureRespawn){
				EntityRendererManager manager = mc.getEntityRenderDispatcher();
				CreatureRespawn point = (CreatureRespawn)entity;
				SARenderHelper.enableBlendMode(RenderTypeSA.ALPHA);//
				Minecraft.getInstance().getTextureManager().bind(tex1);
				icon.renderPart("mat1");
				Minecraft.getInstance().getTextureManager().bind(tex);
				if(point.isEnemyRespawn){
					icon.renderPart("red");
					GL11.glColor4f(1, 0.6F, 0.6F, 1);
				}else{
					icon.renderPart("green");
					GL11.glColor4f(0.6F, 1, 0.6F, 1);
				}
				GL11.glPushMatrix();
				GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0, 0, 0);
				GL11.glRotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0, 0, 0);
				GL11.glTranslatef(0F, 1F, 0F);
				Minecraft.getInstance().getTextureManager().bind(tex);
				renderCount(point.getRespawnCount());
				GL11.glPopMatrix();
				SARenderHelper.disableBlendMode(RenderTypeSA.ALPHA);//
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
			if(entity instanceof VehicleRespawn){
				EntityRendererManager manager = mc.getEntityRenderDispatcher();
				VehicleRespawn point = (VehicleRespawn)entity;
				SARenderHelper.enableBlendMode(RenderTypeSA.ALPHA);//
				Minecraft.getInstance().getTextureManager().bind(tex2);
				icon.renderPart("mat1");
				Minecraft.getInstance().getTextureManager().bind(tex);
				icon.renderPart("white");
				GL11.glPushMatrix();
				GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0, 0, 0);
				GL11.glRotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0, 0, 0);
				GL11.glTranslatef(0F, 1F, 0F);
				Minecraft.getInstance().getTextureManager().bind(tex);
				renderCount(point.getVehicleID());
				GL11.glPopMatrix();
				SARenderHelper.disableBlendMode(RenderTypeSA.ALPHA);//
			}
			if(entity instanceof ArmyMovePoint){
				EntityRendererManager manager = mc.getEntityRenderDispatcher();
				ArmyMovePoint point = (ArmyMovePoint)entity;
				float size1 = 2F;
				int count = point.getMoveId();
				GL11.glPushMatrix();
				int connectid = 2;
				if(point.isEnemyPoint && point.pointType==0)connectid = 3;
				if(point.pointType==1)connectid=4;
				if(point.pointType==2)connectid=5;
				if(point.pointType==3)connectid=6;
				List<Entity> list = point.level.getEntities(point, point.getBoundingBox().inflate(point.connectRange, point.connectRange*2F, point.connectRange));
				for(int k2 = 0; k2 < list.size(); ++k2) {
					Entity ent = list.get(k2);
					if(ent instanceof ArmyMovePoint && ((ArmyMovePoint)ent).getHealth()>0){
						ArmyMovePoint point1 = (ArmyMovePoint)ent;
						if(point.pointType==point1.pointType && (count+1 == point1.getMoveId())){
							if(point1.isEnemyPoint){
								if(point.isEnemyPoint)RenderEntityEvent.renderBeam(point, connectid, (float)point1.getX(),(float)point1.getY()+1,(float)point1.getZ());
							}else{
								if(!point.isEnemyPoint)RenderEntityEvent.renderBeam(point, connectid, (float)point1.getX(),(float)point1.getY()+1,(float)point1.getZ());
							}
						}
					}
				}
				
				//GL11.glDisable(GL11.GL_CULL_FACE);
				//RenderSystem.enableBlend();
				SARenderHelper.enableBlendMode(RenderTypeSA.ALPHA);//
				
				Minecraft.getInstance().getTextureManager().bind(tex3);
				icon.renderPart("mat1");
				Minecraft.getInstance().getTextureManager().bind(tex);
				if(point.isEnemyPoint){
					icon.renderPart("red");
					GL11.glColor4f(1, 0.6F, 0.6F, 1);
				}else{
					icon.renderPart("green");
					GL11.glColor4f(0.6F, 1, 0.6F, 1);
				}
				
				GlStateManager._rotatef(-manager.camera.getYRot()+180F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0, 0, 0);
				GL11.glRotatef(-manager.camera.getXRot(), 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0, 0, 0);
				GL11.glTranslatef(0F, 1F, 0F);
				
				renderCount(count);
				
				if(point.pointType==0)model.renderPart("man");//
				if(point.pointType==1)model.renderPart("tank");//
				if(point.pointType==2)model.renderPart("heli");//
				if(point.pointType==3)model.renderPart("jet");//
				
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				//RenderSystem.disableBlend();
				SARenderHelper.disableBlendMode(RenderTypeSA.ALPHA);//
				GL11.glPopMatrix();
			}
		}
		GL11.glPopMatrix();//glend
		matrixStackIn.popPose();
	}
	void renderCount(int count){
		float size1 = 2F;
		int num = count;
		int shiwei=0,baiwei=0,qianwei=0,gewei=0;
		qianwei = num / 1000;
		baiwei = (num % 1000) / 100;
		shiwei = (num / 10 ) % 10;
		gewei = (num %100) % 10;
		String t1 = String.valueOf(gewei);
		String t2 = String.valueOf(shiwei);
		String t3 = String.valueOf(baiwei);
		String t4 = String.valueOf(qianwei);
		if(count<10){
			model.renderPart("obj" + t1);//
		}else if(count<100)
		{
			model.renderPart("obj" + t2);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t1);//
		}else if(count<1000)
		{
			model.renderPart("obj" + t3);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t2);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t1);//
		}else if(count<10000)
		{
			model.renderPart("obj" + t4);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t3);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t2);//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj" + t1);//
		}else{
			model.renderPart("obj9");//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj9");//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj9");//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("obj9");//
			GL11.glTranslatef(0.4F*size1,0,0);
			model.renderPart("add");//+
		}
	}
}