package advancearmy.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
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

import advancearmy.entity.mob.EntityAohuan;
import advancearmy.entity.mob.ERO_Pillager;
import advancearmy.entity.mob.ERO_Zombie;
import advancearmy.entity.mob.ERO_Skeleton;
import advancearmy.entity.mob.ERO_Creeper;
import advancearmy.entity.mob.DragonTurret;
import advancearmy.entity.mob.ERO_REB;
import advancearmy.entity.mob.ERO_Phantom;
import advancearmy.entity.mob.ERO_Ghast;
import advancearmy.entity.mob.EvilPortal;

import advancearmy.AdvanceArmy;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.entity.LivingEntity;
import wmlib.common.living.EntityWMVehicleBase;
import wmlib.common.item.ItemSummon;

import advancearmy.entity.map.CreatureRespawn;
import advancearmy.entity.map.ArmyMovePoint;
import advancearmy.entity.map.VehicleRespawn;

public class ItemSpawnMob extends Item{
	public int id=0;
	public ItemSpawnMob(Item.Properties builder, int i) {
		super(builder);
		this.id = i;
	}

	public String infor1 = null;
	public String infor2 = null;
	public String infor3 = null;
	public String infor4 = null;
	public String infor5 = null;
	public String infor6 = null;
	public String infor7 = null;
	public String infor8 = null;
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
			if(infor1!=null)tooltip.add(new TranslationTextComponent(infor1).withStyle(TextFormatting.GREEN));//name
			if(infor2!=null)tooltip.add(new TranslationTextComponent(infor2).withStyle(TextFormatting.RED));//create
			if(infor3!=null)tooltip.add(new TranslationTextComponent(infor3).withStyle(TextFormatting.YELLOW));//describe1
			if(infor4!=null)tooltip.add(new TranslationTextComponent(infor4).withStyle(TextFormatting.YELLOW));//describe2
			if(infor5!=null)tooltip.add(new TranslationTextComponent(infor5).withStyle(TextFormatting.YELLOW));//weapon
			if(infor6!=null)tooltip.add(new TranslationTextComponent(infor6).withStyle(TextFormatting.YELLOW));//health/seat
			if(infor7!=null)tooltip.add(new TranslationTextComponent(infor7).withStyle(TextFormatting.GREEN));//armor
			if(infor8!=null)tooltip.add(new TranslationTextComponent(infor8).withStyle(TextFormatting.AQUA));//turret_armor
	}
	public void spawnCreature(World worldIn, PlayerEntity playerIn, double par4, double par5, double par6)
	{
		if (worldIn.isClientSide) return;
		LivingEntity entity = null;
		if(id == 1){
			entity = new CreatureRespawn(AdvanceArmy.ENTITY_CRES, worldIn);
		}else if(id == 2){
			entity = new VehicleRespawn(AdvanceArmy.ENTITY_VRES, worldIn);
		}else if(id == 3){
			entity = new ArmyMovePoint(AdvanceArmy.ENTITY_MOVEP, worldIn);
		}else if(id == 4){
			entity = new ERO_Skeleton(AdvanceArmy.ENTITY_SKELETON, worldIn);
		}else if(id == 5){
			entity = new ERO_Pillager(AdvanceArmy.ENTITY_PI, worldIn);
		}else if(id == 6){
			entity = new ERO_Phantom(AdvanceArmy.ENTITY_PHA, worldIn);
		}else if(id == 7){
			entity = new ERO_Ghast(AdvanceArmy.ENTITY_GST, worldIn);
		}else if(id == 8){
			entity = new ERO_Creeper(AdvanceArmy.ENTITY_CREEPER, worldIn);
		}else if(id == 9){
			entity = new DragonTurret(AdvanceArmy.ENTITY_DT, worldIn);
		}else if(id == 10){
			entity = new ERO_REB(AdvanceArmy.ENTITY_REB, worldIn);
		}else if(id == 11){
			entity = new EntityAohuan(AdvanceArmy.ENTITY_AOHUAN, worldIn);
		}else if(id == 12){
			entity = new EvilPortal(AdvanceArmy.ENTITY_POR, worldIn);
		}else if(id == 13){
			entity = new ERO_Zombie(AdvanceArmy.ENTITY_EZOMBIE, worldIn);
		}
		if(entity!=null)spawn(entity, worldIn, playerIn, par4, par5, par6);
	}
	
	public void spawn(LivingEntity entity, World worldIn, PlayerEntity playerIn, double par4, double par5, double par6)
	{
		if(entity!=null){
			++par5;
			int var12 = MathHelper.floor((double) (playerIn.yRot * 4.0F / 360.0F) + 0.5D) & 3;
			entity.moveTo(par4 + 0.5, par5, par6 + 0.5, var12, 0.0F);
			worldIn.addFreshEntity(entity);
		} 
	}
   
   /**
    * Called when this item is used when targetting a Block
    */
   public ActionResultType useOn(ItemUseContext context) {
      World world = context.getLevel();
      if (world.isClientSide) {
         return ActionResultType.SUCCESS;
      } else {
         ItemStack itemstack = context.getItemInHand();
     	if (!context.getPlayer().abilities.instabuild)
        {
     		itemstack.shrink(1);
        }
     	BlockPos pos = context.getClickedPos();
    	spawnCreature(world, context.getPlayer(), (double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        return ActionResultType.SUCCESS;
      }
   }
}