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
public class ItemSupport extends ItemSummon{
	public SAObjModel obj_model = null;
	public String obj_tex = "wmlib:textures/misc/vehicle_glint.png";
	public int id = 0;
	public int xp = 10;
	public int cool = 20;
	public int type = 0;// 1 once
	public EntitySA_SoldierBase soldier = null;
	public WeaponVehicleBase vehicle = null;
	public TameableEntity building = null;
	public ItemSupport(Item.Properties builder, int i, int t, int x, int c) {
		super(builder);
		this.id = i;
		this.type = t;
		this.xp=x;
		this.cool=c;
		this.infor3="经验值消耗:"+x;
		this.infor4="冷却时间:"+c/20F+"s";
	}
	public boolean isSummon = false;
	public String infor1 = null;
	public String infor2 = null;
	public String infor3 = null;
	public String infor4 = null;
	
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(!isSummon)tooltip.add(new TranslationTextComponent("advancearmy.infor.support1.desc").withStyle(TextFormatting.GREEN));//name
		if(type==0){
			if(infor1!=null)tooltip.add(new TranslationTextComponent(infor1).withStyle(TextFormatting.GREEN));//name
			if(infor2!=null)tooltip.add(new TranslationTextComponent(infor2).withStyle(TextFormatting.RED));//create
			if(infor3!=null)tooltip.add(new TranslationTextComponent(infor3).withStyle(TextFormatting.YELLOW));//describe1
			if(infor4!=null)tooltip.add(new TranslationTextComponent(infor4).withStyle(TextFormatting.YELLOW));//describe2
		}else{
			tooltip.add(new TranslationTextComponent("advancearmy.infor.support2.desc").withStyle(TextFormatting.RED));//create
		}
	}
	
	public void spawnCreature(World worldIn, PlayerEntity playerIn, int weaponid, boolean summon, double x, double y, double z, int summonid)
	{
		/*if (worldIn.isClientSide) return;
		SupportPoint point = new SupportPoint(AdvanceArmy.ENTITY_SPT, worldIn);
		if(playerIn!=null){
			if(playerIn.isCrouching() && playerIn.isCreative()){
			}else{
				point.tame(playerIn);
				if(playerIn.getTeam()!=null){
					playerIn.level.getScoreboard().addPlayerToTeam(point.getUUID().toString(), playerIn.getTeam());
				}
			}
		}
		point.setSummonID(id);
		point.moveTo(x + 0.5, y+1, z + 0.5, 0, 0);
		worldIn.addFreshEntity(point);*/
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
		/*ItemStack itemstack = context.getItemInHand();
		if (!context.getPlayer().abilities.instabuild && type == 3)
		{
			itemstack.shrink(1);
		}
		BlockPos pos = context.getClickedPos();*/
		//spawnCreature(world, context.getPlayer(), 0, false, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(),0);
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