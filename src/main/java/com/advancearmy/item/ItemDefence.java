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

import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.soldier.EntitySA_ConscriptX;
import advancearmy.entity.map.DefencePoint;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.client.util.ITooltipFlag;
import advancearmy.AdvanceArmy;
import net.minecraft.util.text.TranslationTextComponent;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.scoreboard.ScorePlayerTeam;
public class ItemDefence extends ItemSummon{
	public int id = 0;
	public int xp = 10;
	public int cool = 20;
	public int type = 0;//0 vehicle 1 soldier
	public String name = "天呐，那是接近的";
	public EntitySA_SoldierBase soldier = null;
	public WeaponVehicleBase vehicle = null;
	public TameableEntity building = null;
	public ItemDefence(Item.Properties builder, int i, String n) {
		super(builder);
		this.id = i;
		this.name=n;
	}

	public String infor1 = "advancearmy.infor.defence1.desc";
	public String infor2 = "advancearmy.infor.defence2.desc";
	public String infor3 = "advancearmy.infor.defence3.desc";
	public String infor4 = "advancearmy.infor.defence4.desc";
	public String infor5 = "advancearmy.infor.defence5.desc";
	public String infor6 = "advancearmy.infor.defence6.desc";
	public String infor7 = "advancearmy.infor.defence7.desc";
	public String infor8 = "advancearmy.infor.defence8.desc";
	public String infor9 = "advancearmy.infor.defence9.desc";
	
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(infor1!=null)tooltip.add(new TranslationTextComponent(infor1).withStyle(TextFormatting.GREEN));//name
		if(infor2!=null)tooltip.add(new TranslationTextComponent(infor2).withStyle(TextFormatting.RED));//create
		if(infor3!=null)tooltip.add(new TranslationTextComponent(infor3).withStyle(TextFormatting.YELLOW));//describe1
		if(infor4!=null)tooltip.add(new TranslationTextComponent(infor4).withStyle(TextFormatting.YELLOW));//describe2
		if(infor5!=null)tooltip.add(new TranslationTextComponent(infor5).withStyle(TextFormatting.RED));//weapon
		if(infor6!=null)tooltip.add(new TranslationTextComponent(infor6).withStyle(TextFormatting.RED));//health/seat
		if(infor7!=null)tooltip.add(new TranslationTextComponent(infor7).withStyle(TextFormatting.GREEN));//armor
		if(infor8!=null)tooltip.add(new TranslationTextComponent(infor8).withStyle(TextFormatting.AQUA));//turret_armor
		if(infor8!=null)tooltip.add(new TranslationTextComponent(infor9).withStyle(TextFormatting.RED));//turret_armor
	}
	
	public void spawnCreature(World worldIn, PlayerEntity playerIn, int weaponid, boolean summon, double x, double y, double z, int summonid)
	{
		if (worldIn.isClientSide) return;
		if(id!=7){
			DefencePoint point = new DefencePoint(AdvanceArmy.ENTITY_DPT, worldIn);
			if(playerIn!=null){
				/*if(playerIn.isCrouching() && playerIn.isCreative()){
				}else*/{
					point.tame(playerIn);
					if(playerIn.getTeam()!=null && playerIn.getTeam() instanceof ScorePlayerTeam){
						playerIn.level.getScoreboard().addPlayerToTeam(point.getUUID().toString(), (ScorePlayerTeam)playerIn.getTeam());
					}
				}
			}
			point.setSummonID(id);
			point.moveTo(x + 0.5, y+1, z + 0.5, 0, 0);
			worldIn.addFreshEntity(point);
			point.setCustomName(new StringTextComponent(name));
		}else{
			/*if(playerIn.level.isClientSide)*/playerIn.sendMessage(new TranslationTextComponent("这个挑战还没做完!", new Object[0]), playerIn.getUUID());
		}
	}

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
		spawnCreature(world, context.getPlayer(), 0, false, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(),0);
		return ActionResultType.SUCCESS;
	  }
	}
}