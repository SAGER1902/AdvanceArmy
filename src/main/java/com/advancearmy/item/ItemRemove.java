package advancearmy.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.soldier.EntitySA_ConscriptX;
import advancearmy.entity.map.SupportPoint;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.client.util.ITooltipFlag;
import advancearmy.AdvanceArmy;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.entity.LivingEntity;
import wmlib.common.living.WeaponVehicleBase;
import wmlib.common.item.ItemSummon;
import net.minecraft.entity.ILivingEntityData;
import wmlib.common.block.BlockRegister;
import net.minecraft.world.server.ServerWorld;

import advancearmy.entity.building.SoldierMachine;
import advancearmy.entity.building.VehicleMachine;
import net.minecraft.entity.passive.TameableEntity;
import wmlib.client.obj.SAObjModel;
public class ItemRemove extends Item{
	public ItemRemove(Item.Properties builder) {
		super(builder);
	}
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("advancearmy.interact.itemremove.desc").withStyle(TextFormatting.RED));//create
	}
	public boolean enc = false;
  	public boolean isFoil(ItemStack p_77636_1_) {
	  return enc;
	}
	public ActionResultType useOn(ItemUseContext context) {
	  World world = context.getLevel();
	  if (world.isClientSide) {
		 return ActionResultType.SUCCESS;
	  } else {
		return ActionResultType.SUCCESS;
	  }
	}
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity target, LivingEntity entity) {
	  return false;
	}
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
	  ItemStack itemstack = playerIn.getItemInHand(handIn);
	  if (worldIn.isClientSide) {
		 return new ActionResult<>(ActionResultType.PASS, itemstack);
	  } else {
		 return new ActionResult<>(ActionResultType.FAIL, itemstack);
	  }
	}
}