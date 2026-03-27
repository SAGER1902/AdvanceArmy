package advancearmy.render;

import net.minecraft.util.math.vector.Matrix3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import advancearmy.entity.mob.EvilPortal;
import net.minecraft.client.renderer.culling.ClippingHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector3d;
//import wmlib.util.Vector3f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import com.mojang.blaze3d.platform.GlStateManager;
import wmlib.client.obj.SAObjModel;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Matrix4f;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.util.Random;
import java.util.stream.IntStream;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.util.Direction;
import java.util.List;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.texture.OverlayTexture;
import wmlib.client.render.SARenderState;
import net.minecraft.util.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import wmlib.client.render.SARenderHelper;
import wmlib.client.render.SARenderHelper.RenderTypeSA;
import wmlib.client.render.SARenderState;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.BlockPos;
@OnlyIn(Dist.CLIENT)
public class PortalRenderer extends EntityRenderer<EvilPortal> {
	public PortalRenderer(EntityRendererManager renderManagerIn) {
	  super(renderManagerIn);
      this.shadowRadius = 0.5F;
      this.glass = new ModelRenderer(64, 32, 0, 0);
      this.glass.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
      this.cube = new ModelRenderer(64, 32, 32, 0);
      this.cube.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
      this.base = new ModelRenderer(64, 32, 0, 16);
      this.base.addBox(-6.0F, 0.0F, -6.0F, 12.0F, 4.0F, 12.0F);
	}
	
   public static float getY(EvilPortal p_229051_0_, float p_229051_1_) {
      float f = (float)p_229051_0_.summontime + p_229051_1_;
      float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
      f1 = (f1 * f1 + f1) * 0.4F;
      return f1 - 1.4F;
   }
	private static final SAObjModel obj = new SAObjModel("wmlib:textures/misc/portal.obj");
	private static final ResourceLocation EvilPortalTex = new ResourceLocation("wmlib:textures/misc/portal1.png");
	private static final ResourceLocation EvilPortalTex2 = new ResourceLocation("wmlib:textures/misc/portal2.png");
	private static final ResourceLocation EvilPortalTex3 = new ResourceLocation("wmlib:textures/misc/portal3.png");
	private static final ResourceLocation END_CRYSTAL_LOCATION = new ResourceLocation("textures/entity/end_crystal/end_crystal.png");
	private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(END_CRYSTAL_LOCATION);
	public static final ResourceLocation CRYSTAL_BEAM_LOCATION = new ResourceLocation("textures/entity/end_crystal/end_crystal_beam.png");
	private static final RenderType BEAM = RenderType.entitySmoothCutout(CRYSTAL_BEAM_LOCATION);
	private static final float SIN_45 = (float)Math.sin((Math.PI / 4D));
	private final ModelRenderer cube;
	private final ModelRenderer glass;
	private final ModelRenderer base;
   
    public ResourceLocation getTextureLocation(EvilPortal entity)
    {
		return EvilPortalTex;
    }
	
	private static final ResourceLocation glint2 = new ResourceLocation("wmlib:textures/misc/vehicle_made11.png");
	private static final ResourceLocation glint1 = new ResourceLocation("wmlib:textures/misc/vehicle_glint.png");
	public boolean shouldRender(EvilPortal p_225626_1_, ClippingHelper p_225626_2_, double p_225626_3_, double p_225626_5_, double p_225626_7_) {
      return true;
	}
	float iii = 0;
	private static final Random RANDOM = new Random(31100L);
	/*private static final List<RenderType> RENDER_TYPES = IntStream.range(0, 16).mapToObj((count) -> {
	  return SARenderState.endPortal(count + 1);}).collect(ImmutableList.toImmutableList());*/
    public void render(EvilPortal entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn)
    {
		super.render(entity, entityYaw, partialTicks, stack, buffer, packedLightIn);
		Minecraft mc = Minecraft.getInstance();
		long seed = entity.getUUID().hashCode();
		RANDOM.setSeed(seed*31100L);
		
		double dis = entity.distanceToSqr(mc.player);
		int i = this.getPasses(dis);
		float xx2 = 1F;
		Matrix4f matrix4f = stack.last().pose();
		
		{
			stack.pushPose();
			float f = getY(entity, partialTicks);
			float f1 = ((float)entity.rote + partialTicks) * 3.0F;
			stack.pushPose();
			stack.scale(2.0F, 2.0F, 2.0F);
			//stack.translate(0.0D, -0.5D, 0.0D);
			int i1 = OverlayTexture.NO_OVERLAY;
			
			IVertexBuilder ivertexbuilder = buffer.getBuffer(RENDER_TYPE);
			this.base.render(stack, ivertexbuilder, packedLightIn, i);
			
			stack.mulPose(Vector3f.YP.rotationDegrees(f1));
			stack.translate(0.0D, (double)(1.5F + f / 2.0F), 0.0D);
			stack.mulPose(new Quaternion(new Vector3f(SIN_45, 0.0F, SIN_45), 60.0F, true));
			
			/*IVertexBuilder back = buffer.getBuffer(SARenderState.endPortal(0, EvilPortalTex));
			this.glass.render(stack, back, packedLightIn, i1);*/
			//IVertexBuilder back1 = buffer.getBuffer(SARenderState.endPortal(0, glint2));
			//this.glass.render(stack, back1, packedLightIn, i1);
			
			float f2 = 0.875F;
			stack.scale(0.875F, 0.875F, 0.875F);
			stack.mulPose(new Quaternion(new Vector3f(SIN_45, 0.0F, SIN_45), 60.0F, true));
			stack.mulPose(Vector3f.YP.rotationDegrees(f1));
			
			/*IVertexBuilder back2 = buffer.getBuffer(SARenderState.endPortal(0, EvilPortalTex));
			this.glass.render(stack, back2, packedLightIn, i1);*/
			//IVertexBuilder back3 = buffer.getBuffer(SARenderState.endPortal(0, glint1));
			//this.glass.render(stack, back3, packedLightIn, i1);
			
			stack.scale(0.875F, 0.875F, 0.875F);
			stack.mulPose(new Quaternion(new Vector3f(SIN_45, 0.0F, SIN_45), 60.0F, true));
			stack.mulPose(Vector3f.YP.rotationDegrees(f1));
			
			IVertexBuilder eye = buffer.getBuffer(SARenderState.endPortal(0, EvilPortalTex));
			this.cube.render(stack, eye, packedLightIn, i1);
			IVertexBuilder eye1 = buffer.getBuffer(SARenderState.endPortal(15, EvilPortalTex3));
			this.cube.render(stack, eye1, packedLightIn, i1);
			
			stack.popPose();
			if(entity.getTarget()!=null){
				BlockPos blockpos = new BlockPos(entity.getTarget().getX(),entity.getTarget().getY(),entity.getTarget().getZ());
				if (blockpos != null) {
					float f3 = (float)blockpos.getX() + 0.5F;
					float f4 = (float)blockpos.getY() + 0.5F;
					float f5 = (float)blockpos.getZ() + 0.5F;
					float f6 = (float)((double)f3 - entity.getX());
					float f7 = (float)((double)f4 - entity.getY());
					float f8 = (float)((double)f5 - entity.getZ());
					stack.translate((double)f6, (double)f7, (double)f8);
					this.renderCrystalBeams(-f6, -f7 + f, -f8, partialTicks, entity.summontime, stack, buffer, packedLightIn);
				}
			}
			stack.popPose();
		}
		
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
		GL11.glTranslatef((float) xIn, (float) yIn+6F, (float) zIn);

		mc.getTextureManager().bind(EvilPortalTex);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		if(entity.getPortalType()>2){
			float r = (RANDOM.nextFloat() * 0.5F + 0.5F) * 0.15F;
			float g = (RANDOM.nextFloat() * 0.5F + 0.1F) * 0.15F;
			float b = (RANDOM.nextFloat() * 0.5F + 0.1F) * 0.15F;
			GL11.glColor3f(r, g, b);
		}else{
			float r = (RANDOM.nextFloat() * 0.5F + 0.5F) * 0.15F;
			float g = (RANDOM.nextFloat() * 0.5F + 0.3F) * 0.15F;
			float b = (RANDOM.nextFloat() * 0.5F + 0.3F) * 0.15F;
			GL11.glColor3f(r, g, b);
		}
		
		if(entity.deathTime > 0) {
			RenderSystem.scalef(1, (120F-entity.deathTime)/120F, 1);
			GL11.glColor4f((120F-entity.deathTime)/120F, (120F-entity.deathTime)/120F, (120F-entity.deathTime)/120F, (120F-entity.deathTime)/120F);
		}else{
			RenderSystem.scalef(1, entity.startTime/120F, 1);
		}
		
			float x =1;
            RenderSystem.matrixMode(5890);
            RenderSystem.pushMatrix();
            RenderSystem.loadIdentity();
            RenderSystem.translatef(0.5F, 0.5F, 0.0F);
            RenderSystem.scalef(0.5F, 0.5F, 1.0F);
            RenderSystem.translatef(17.0F / (float)x, (2.0F + (float)x / 1.5F) * ((float)(Util.getMillis() % 800000L) / 800000.0F), 0.0F);
            RenderSystem.rotatef(((float)(x * x) * 4321.0F + (float)x * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
            RenderSystem.scalef(4.5F - (float)x / 4.0F, 4.5F - (float)x / 4.0F, 1.0F);
            RenderSystem.mulTextureByProjModelView();
            RenderSystem.matrixMode(5888);
            RenderSystem.setupEndPortalTexGen();
		
		if(entity.getPortalType()==0)obj.renderPart("type1");
		if(entity.getPortalType()==1)obj.renderPart("type2");
		if(entity.getPortalType()==2)obj.renderPart("type3");
		if(entity.getPortalType()==3)obj.renderPart("type4");
		
            RenderSystem.matrixMode(5890);
            RenderSystem.popMatrix();
            RenderSystem.matrixMode(5888);
            RenderSystem.clearTexGen();
		
		//this.renderCube(xx2, 0.15F, matrix4f, buffer.getBuffer(RENDER_TYPES.get(0)));
		if(entity.getPortalType()>=2){
			mc.getTextureManager().bind(EvilPortalTex3);
		}else{
			mc.getTextureManager().bind(EvilPortalTex2);
		}
		//RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		for(int j = 1; j < i; ++j) {
			float f = (RANDOM.nextFloat() * 0.5F + 0.5F) * 2.0F / (float)(18 - j);
			float f1 = (RANDOM.nextFloat() * 0.5F + 0.2F) * 2.0F / (float)(18 - j);
			float f2 = (RANDOM.nextFloat() * 0.5F + 0.2F) * 2.0F / (float)(18 - j);
			GL11.glColor3f(f, f1, f2);
			float none =j;
            RenderSystem.matrixMode(5890);
            RenderSystem.pushMatrix();
            RenderSystem.loadIdentity();
            RenderSystem.translatef(0.5F, 0.5F, 0.0F);
            RenderSystem.scalef(0.5F, 0.5F, 1.0F);
            RenderSystem.translatef(17.0F / (float)none, (2.0F + (float)none / 1.5F) * ((float)(Util.getMillis() % 800000L) / 800000.0F)*50, 0.0F);
            RenderSystem.rotatef(((float)(none * none) * 4321.0F + (float)none * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
            RenderSystem.scalef(4.5F - (float)none / 4.0F, 4.5F - (float)none / 4.0F, 1.0F);
            RenderSystem.mulTextureByProjModelView();
            RenderSystem.matrixMode(5888);
            RenderSystem.setupEndPortalTexGen();
			
			if(entity.getPortalType()==0)obj.renderPart("type1");
			if(entity.getPortalType()==1)obj.renderPart("type2");
			if(entity.getPortalType()==2)obj.renderPart("type3");
			if(entity.getPortalType()==3)obj.renderPart("type4");
			
            RenderSystem.matrixMode(5890);
            RenderSystem.popMatrix();
            RenderSystem.matrixMode(5888);
            RenderSystem.clearTexGen();
			//this.renderCube(xx2, 2.0F / (float)(18 - j), matrix4f, buffer.getBuffer(RENDER_TYPES.get(j)));
		}
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
		GL11.glPopMatrix();
	}
   private void renderCube(float xx2, float size, Matrix4f stack, IVertexBuilder buffer) {
      float f = (RANDOM.nextFloat() * 0.5F + 0.1F) * size;
      float f1 = (RANDOM.nextFloat() * 0.5F + 0.4F) * size;
      float f2 = (RANDOM.nextFloat() * 0.5F + 0.5F) * size;
      this.renderFace(stack, buffer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, f, f1, f2);
      this.renderFace(stack, buffer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, f, f1, f2);
      this.renderFace(stack, buffer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, f, f1, f2);
      this.renderFace(stack, buffer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, f, f1, f2);
      this.renderFace(stack, buffer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f, f1, f2);
      this.renderFace(stack, buffer, 0.0F, 1.0F, xx2, xx2, 1.0F, 1.0F, 0.0F, 0.0F, f, f1, f2);
   }
	private void renderFace(Matrix4f stack, IVertexBuilder buffer,
	float x, float x2, float y, float y2,
	float z, float z2, float z3, float z4,
	float red, float green, float blue) {
		buffer.vertex(stack, x, y, z).color(red, green, blue, 1.0F).endVertex();
		buffer.vertex(stack, x2, y, z2).color(red, green, blue, 1.0F).endVertex();
		buffer.vertex(stack, x2, y2, z3).color(red, green, blue, 1.0F).endVertex();
		buffer.vertex(stack, x, y2, z4).color(red, green, blue, 1.0F).endVertex();
	}
	
	
	protected int getPasses(double distance) {
	  if (distance > 36864.0D) {
		 return 1;
	  } else if (distance > 25600.0D) {
		 return 3;
	  } else if (distance > 16384.0D) {
		 return 5;
	  } else if (distance > 9216.0D) {
		 return 7;
	  } else if (distance > 4096.0D) {
		 return 9;
	  } else if (distance > 1024.0D) {
		 return 11;
	  } else if (distance > 576.0D) {
		 return 13;
	  } else {
		 return distance > 256.0D ? 14 : 15;
	  }
	}
	
	
   public static void renderCrystalBeams(float p_229059_0_, float p_229059_1_, float p_229059_2_, float p_229059_3_, int p_229059_4_, MatrixStack p_229059_5_, IRenderTypeBuffer p_229059_6_, int p_229059_7_) {
      float f = MathHelper.sqrt(p_229059_0_ * p_229059_0_ + p_229059_2_ * p_229059_2_);
      float f1 = MathHelper.sqrt(p_229059_0_ * p_229059_0_ + p_229059_1_ * p_229059_1_ + p_229059_2_ * p_229059_2_);
      p_229059_5_.pushPose();
      p_229059_5_.translate(0.0D, 2.0D, 0.0D);
      p_229059_5_.mulPose(Vector3f.YP.rotation((float)(-Math.atan2((double)p_229059_2_, (double)p_229059_0_)) - ((float)Math.PI / 2F)));
      p_229059_5_.mulPose(Vector3f.XP.rotation((float)(-Math.atan2((double)f, (double)p_229059_1_)) - ((float)Math.PI / 2F)));
      IVertexBuilder ivertexbuilder = p_229059_6_.getBuffer(BEAM);
      float f2 = 0.0F - ((float)p_229059_4_ + p_229059_3_) * 0.01F;
      float f3 = MathHelper.sqrt(p_229059_0_ * p_229059_0_ + p_229059_1_ * p_229059_1_ + p_229059_2_ * p_229059_2_) / 32.0F - ((float)p_229059_4_ + p_229059_3_) * 0.01F;
      int i = 8;
      float f4 = 0.0F;
      float f5 = 0.75F;
      float f6 = 0.0F;
      MatrixStack.Entry matrixstack$entry = p_229059_5_.last();
      Matrix4f matrix4f = matrixstack$entry.pose();
      Matrix3f matrix3f = matrixstack$entry.normal();

      for(int j = 1; j <= 8; ++j) {
         float f7 = MathHelper.sin((float)j * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
         float f8 = MathHelper.cos((float)j * ((float)Math.PI * 2F) / 8.0F) * 0.75F;
         float f9 = (float)j / 8.0F;
         ivertexbuilder.vertex(matrix4f, f4 * 0.2F, f5 * 0.2F, 0.0F).color(0, 0, 0, 255).uv(f6, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229059_7_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
         ivertexbuilder.vertex(matrix4f, f4, f5, f1).color(255, 10, 10, 10).uv(f6, f3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229059_7_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
         ivertexbuilder.vertex(matrix4f, f7, f8, f1).color(255, 10, 10, 10).uv(f9, f3).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229059_7_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
         ivertexbuilder.vertex(matrix4f, f7 * 0.2F, f8 * 0.2F, 0.0F).color(0, 0, 0, 255).uv(f9, f2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229059_7_).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
         f4 = f7;
         f5 = f8;
         f6 = f9;
      }

      p_229059_5_.popPose();
   }
}