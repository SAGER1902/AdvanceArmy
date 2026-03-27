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

import advancearmy.entity.soldier.EntitySA_Conscript;
import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.entity.soldier.EntitySA_GI;
import advancearmy.entity.land.EntitySA_BMP2;
import advancearmy.entity.land.EntitySA_BMPT;
import advancearmy.entity.land.EntitySA_M109;
import advancearmy.entity.land.EntitySA_M2A2;
import advancearmy.entity.land.EntitySA_M2A2AA;
import advancearmy.entity.land.EntitySA_Bike;
import advancearmy.entity.land.EntitySA_Car;
import advancearmy.entity.land.EntitySA_T55;
import advancearmy.entity.land.EntitySA_FTK;
import advancearmy.entity.land.EntitySA_Hmmwv;
import advancearmy.entity.land.EntitySA_99G;
import advancearmy.entity.land.EntitySA_FTK_H;
import advancearmy.entity.land.EntitySA_LaserAA;
import advancearmy.entity.land.EntitySA_MASTDOM;
import advancearmy.entity.land.EntitySA_T90;
import advancearmy.entity.land.EntitySA_T72;
import advancearmy.entity.land.EntitySA_Reaper;
import advancearmy.entity.land.EntitySA_LAV;
import advancearmy.entity.land.EntitySA_LAVAA;
import advancearmy.entity.land.EntitySA_Sickle;
import advancearmy.entity.land.EntitySA_Tank;
import advancearmy.entity.land.EntitySA_Prism;

import advancearmy.entity.air.EntitySA_AH1Z;
import advancearmy.entity.air.EntitySA_Plane;
import advancearmy.entity.air.EntitySA_F35;
import advancearmy.entity.air.EntitySA_Helicopter;
import advancearmy.entity.sea.EntitySA_BattleShip;
import advancearmy.entity.air.EntitySA_Plane1;
import advancearmy.entity.air.EntitySA_Plane2;
import advancearmy.entity.air.EntitySA_A10a;
import advancearmy.entity.air.EntitySA_A10c;

import advancearmy.entity.land.EntitySA_APAGAT;
import advancearmy.entity.soldier.EntitySA_GAT;
import advancearmy.entity.land.EntitySA_MMTank;

import advancearmy.entity.air.EntitySA_SU33;
import advancearmy.entity.air.EntitySA_AH6;
import advancearmy.entity.air.EntitySA_MI24;
import advancearmy.entity.soldier.EntitySA_OFG;
import advancearmy.entity.turret.EntitySA_Mortar;
import advancearmy.entity.turret.EntitySA_M2hb;
import advancearmy.entity.turret.EntitySA_Kord;

import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.entity.land.EntitySA_Mirage;
import advancearmy.entity.land.EntitySA_Tesla;
import advancearmy.entity.turret.EntitySA_TOW;
import advancearmy.entity.turret.EntitySA_STIN;
import advancearmy.entity.soldier.EntitySA_ConscriptX;
import advancearmy.entity.soldier.EntitySA_RADS;
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
import net.minecraft.util.Direction;
import advancearmy.entity.building.SandBag;
import advancearmy.entity.building.SoldierMachine;
import advancearmy.entity.building.VehicleMachine;
import net.minecraft.entity.passive.TameableEntity;
import wmlib.client.obj.SAObjModel;
import net.minecraft.scoreboard.ScorePlayerTeam;

import net.minecraft.entity.ai.attributes.Attributes;
public class ItemSpawn extends ItemSummon{
	public SAObjModel obj_model = null;
	public String obj_tex = "wmlib:textures/misc/vehicle_glint.png";
	public int id = 0;
	public int xp = 10;
	public int cool = 20;
	public int type = 0;//0 vehicle 1 soldier 3 building
	public EntitySA_SoldierBase soldier = null;
	public WeaponVehicleBase vehicle = null;
	public TameableEntity building = null;
	public ItemSpawn(Item.Properties builder, int i, int t, int x, int c) {
		super(builder);
		this.id = i;
		this.type = t;
		this.xp=x;
		this.cool=c;
		this.inforx="建造价格:"+x;
		this.inforc="建造时间:"+c/20F+"s";
	}

	public String infor1 = null;
	public String infor2 = "advancearmy.infor.spawn1.desc";
	public String infor3 = null;
	public String infor4 = null;
	public String infor5 = null;
	public String infor6 = null;
	public String inforx = null;
	public String inforc = null;
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(type==0){
			if(infor1!=null)tooltip.add(new TranslationTextComponent(infor1).withStyle(TextFormatting.GREEN));//name
			if(infor2!=null)tooltip.add(new TranslationTextComponent(infor2).withStyle(TextFormatting.RED));//create
			if(infor3!=null)tooltip.add(new TranslationTextComponent(infor3).withStyle(TextFormatting.YELLOW));//describe1
			if(infor4!=null)tooltip.add(new TranslationTextComponent(infor4).withStyle(TextFormatting.YELLOW));//describe2
			if(inforx!=null)tooltip.add(new TranslationTextComponent(inforx).withStyle(TextFormatting.AQUA));//describe1
			if(inforc!=null)tooltip.add(new TranslationTextComponent(inforc).withStyle(TextFormatting.AQUA));//describe2
			if(infor5!=null)tooltip.add(new TranslationTextComponent(infor5).withStyle(TextFormatting.YELLOW));//weapon
			if(infor6!=null)tooltip.add(new TranslationTextComponent(infor6).withStyle(TextFormatting.YELLOW));//health/seat
			/*if(infor7!=null)tooltip.add(new TranslationTextComponent(infor7).withStyle(TextFormatting.GREEN));//armor
			if(infor8!=null)tooltip.add(new TranslationTextComponent(infor8).withStyle(TextFormatting.AQUA));//turret_armor*/
			tooltip.add(new TranslationTextComponent("advancearmy.infor.pickaxe_fix.desc").withStyle(TextFormatting.GREEN));
		}else{
			if(infor1!=null)tooltip.add(new TranslationTextComponent(infor1).withStyle(TextFormatting.GREEN));//name
			if(infor2!=null)tooltip.add(new TranslationTextComponent(infor2).withStyle(TextFormatting.RED));//create
			if(infor3!=null)tooltip.add(new TranslationTextComponent(infor3).withStyle(TextFormatting.YELLOW));//describe1
			if(infor4!=null)tooltip.add(new TranslationTextComponent(infor4).withStyle(TextFormatting.YELLOW));//describe2
			if(infor5!=null)tooltip.add(new TranslationTextComponent(infor5).withStyle(TextFormatting.RED));//weapon
			if(infor6!=null)tooltip.add(new TranslationTextComponent(infor6).withStyle(TextFormatting.RED));//health
		}
	}
	
	public void spawnCreature(World worldIn, PlayerEntity playerIn, int weaponid, boolean summon, double x, double y, double z, int summonid)
	{
		if (worldIn.isClientSide) return;
		if(id == 1){
			soldier = new EntitySA_Soldier(AdvanceArmy.ENTITY_SOLDIER, worldIn);
			if(weaponid==1){
				if(soldier.level.random.nextInt(3)==1){
					soldier.setWeapon(1);
				}else if(soldier.level.random.nextInt(3)==2){
					soldier.setWeapon(11);
				}else{
					soldier.setWeapon(12);
				}
			}else if(weaponid==2){
				if(soldier.level.random.nextInt(4)==1){
					soldier.setWeapon(4);
				}else if(soldier.level.random.nextInt(4)==2){
					soldier.setWeapon(13);
				}else if(soldier.level.random.nextInt(4)==3){
					soldier.setWeapon(3);
				}else{
					soldier.setWeapon(6);
				}
			}else if(weaponid==3){
				if(soldier.level.random.nextInt(2)==1){
					soldier.setWeapon(14);
				}else{
					soldier.setWeapon(2);
				}
			}else if(weaponid==4){
				soldier.setWeapon(8);
			}else if(weaponid==5){
				soldier.setWeapon(5);
			}
		}else if(id == 2){
			vehicle = new EntitySA_Tank(AdvanceArmy.ENTITY_TANK, worldIn);
		}else if(id == 3){
			soldier = new EntitySA_Conscript(AdvanceArmy.ENTITY_CONS, worldIn);
			//soldier.setWeapon(weaponid);
		}else if(id == 4){
			vehicle = new EntitySA_FTK(AdvanceArmy.ENTITY_FTK, worldIn);
		}else if(id == 5){
			vehicle = new EntitySA_T55(AdvanceArmy.ENTITY_T55, worldIn);
		}else if(id == 6){
			vehicle = new EntitySA_Prism(AdvanceArmy.ENTITY_PRISM, worldIn);
		}else if(id == 7){
			vehicle = new EntitySA_Helicopter(AdvanceArmy.ENTITY_HELI, worldIn);
		}else if(id == 8){
			vehicle = new EntitySA_Plane(AdvanceArmy.ENTITY_PLANE, worldIn);
		}else if(id == 9){
			vehicle = new EntitySA_T72(AdvanceArmy.ENTITY_T72, worldIn);
		}else if(id == 10){
			vehicle = new EntitySA_T90(AdvanceArmy.ENTITY_T90, worldIn);
		}else if(id == 11){
			vehicle = new EntitySA_LAVAA(AdvanceArmy.ENTITY_LAVAA, worldIn);
		}else if(id == 12){
			vehicle = new EntitySA_LaserAA(AdvanceArmy.ENTITY_LAA, worldIn);
		}else if(id == 13){
			vehicle = new EntitySA_Sickle(AdvanceArmy.ENTITY_SICKLE, worldIn);
		}else if(id == 14){
			vehicle = new EntitySA_MASTDOM(AdvanceArmy.ENTITY_MAST, worldIn);
		}else if(id == 15){
			vehicle = new EntitySA_M2A2(AdvanceArmy.ENTITY_M2A2, worldIn);
		}else if(id == 16){
			vehicle = new EntitySA_99G(AdvanceArmy.ENTITY_99G, worldIn);
		}else if(id == 17){
			vehicle = new EntitySA_M109(AdvanceArmy.ENTITY_M109, worldIn);
		}else if(id == 18){
			vehicle = new EntitySA_FTK_H(AdvanceArmy.ENTITY_FTK_H, worldIn);
		}else if(id == 19){
			vehicle = new EntitySA_AH1Z(AdvanceArmy.ENTITY_AH1Z, worldIn);
		}else if(id == 20){
			vehicle = new EntitySA_Car(AdvanceArmy.ENTITY_CAR, worldIn);
		}else if(id == 21){
			vehicle = new EntitySA_Hmmwv(AdvanceArmy.ENTITY_HMMWV, worldIn);
		}else if(id == 22){
			vehicle = new EntitySA_F35(AdvanceArmy.ENTITY_F35, worldIn);
		}else if(id == 23){
			vehicle = new EntitySA_BattleShip(AdvanceArmy.ENTITY_BSHIP, worldIn);
		}else if(id == 24){
			vehicle = new EntitySA_M2A2AA(AdvanceArmy.ENTITY_M2A2AA, worldIn);
		}else if(id == 25){
			vehicle = new EntitySA_LAV(AdvanceArmy.ENTITY_LAV, worldIn);
		}else if(id == 26){
			vehicle = new EntitySA_APAGAT(AdvanceArmy.ENTITY_APAGAT, worldIn);
		}else if(id == 27){
			vehicle = new EntitySA_BMP2(AdvanceArmy.ENTITY_BMP2, worldIn);
		}else if(id == 28){
			vehicle = new EntitySA_BMPT(AdvanceArmy.ENTITY_BMPT, worldIn);
		}else if(id == 29){
			vehicle = new EntitySA_A10a(AdvanceArmy.ENTITY_A10A, worldIn);
		}else if(id == 30){
			vehicle = new EntitySA_A10c(AdvanceArmy.ENTITY_A10C, worldIn);
		}else if(id == 31){
			vehicle = new EntitySA_AH6(AdvanceArmy.ENTITY_AH6, worldIn);
		}else if(id == 32){
			vehicle = new EntitySA_MI24(AdvanceArmy.ENTITY_MI24, worldIn);
		}else if(id == 33){
			vehicle = new EntitySA_SU33(AdvanceArmy.ENTITY_SU33, worldIn);
		}else if(id == 34){
			soldier = new EntitySA_OFG(AdvanceArmy.ENTITY_OFG, worldIn);
		}else if(id == 35){
			vehicle = new EntitySA_Mortar(AdvanceArmy.ENTITY_MORTAR, worldIn);
		}else if(id == 36){
			vehicle = new EntitySA_M2hb(AdvanceArmy.ENTITY_M2HB, worldIn);
		}else if(id == 37){
			vehicle = new EntitySA_Kord(AdvanceArmy.ENTITY_KORD, worldIn);
		}else if(id == 38){
			soldier = new EntitySA_GI(AdvanceArmy.ENTITY_GI, worldIn);
		}else if(id == 39){
			soldier = new EntitySA_ConscriptX(AdvanceArmy.ENTITY_CONSX, worldIn);
		}else if(id == 40){
			vehicle = new EntitySA_TOW(AdvanceArmy.ENTITY_TOW, worldIn);
		}else if(id == 41){
			vehicle = new EntitySA_STIN(AdvanceArmy.ENTITY_STIN, worldIn);
		}else if(id == 42){
			building = new SoldierMachine(AdvanceArmy.ENTITY_SMAC, worldIn);
		}else if(id == 43){
			building = new VehicleMachine(AdvanceArmy.ENTITY_VMAC, worldIn);
		}else if(id == 44){
			soldier = new EntitySA_RADS(AdvanceArmy.ENTITY_RADS, worldIn);
		}else if(id == 45){
			soldier = new EntitySA_GAT(AdvanceArmy.ENTITY_GAT, worldIn);
		}else if(id == 46){
			vehicle = new EntitySA_Reaper(AdvanceArmy.ENTITY_REAPER, worldIn);
		}else if(id == 47){
			vehicle = new EntitySA_MMTank(AdvanceArmy.ENTITY_MMTANK, worldIn);
		}else if(id == 48){
			building = new SandBag(AdvanceArmy.ENTITY_SANDBAG, worldIn);
		}else if(id == 49){
			building = new SandBag(AdvanceArmy.ENTITY_SANDBAG, worldIn);
		}else if(id == 50){
			building = new SandBag(AdvanceArmy.ENTITY_SANDBAG, worldIn);
		}else if(id == 51){
			vehicle = new EntitySA_Mirage(AdvanceArmy.ENTITY_MIRAGE, worldIn);
		}else if(id == 52){
			vehicle = new EntitySA_Tesla(AdvanceArmy.ENTITY_TESLA, worldIn);
		}else if(id == 53){
			vehicle = new EntitySA_Bike(AdvanceArmy.ENTITY_BIKE, worldIn);
		}
		
		
		if(summon){
			if(soldier!=null)spawnsoldier(soldier, worldIn, playerIn, x, y, z, summonid);
			if(vehicle!=null)spawntank(vehicle, worldIn, playerIn, x, y, z, summonid);
		}else{
			if(playerIn!=null){
				if(playerIn.getScore()>=xp||playerIn.isCreative()){
					if(!playerIn.isCreative()){
						playerIn.giveExperiencePoints(-xp);
						playerIn.getCooldowns().addCooldown(this, cool);
					}
					if(soldier!=null)spawnsoldier(soldier, worldIn, playerIn, x, y, z, summonid);
					if(vehicle!=null)spawntank(vehicle, worldIn, playerIn, x, y, z, summonid);
					if(building!=null)spawnbuilding(building, worldIn, playerIn, x, y, z);
				}else{
					//if(playerIn.level.isClientSide)playerIn.sendMessage(new TranslationTextComponent("ExperiencePoints not enough!", new Object[0]), playerIn.getUUID());
				}
			} 
		}
	}
	
	public void spawnsoldier(EntitySA_SoldierBase ent, World worldIn, PlayerEntity playerIn, double x, double y, double z, int summonid)
	{
		if(ent!=null){
			if(playerIn!=null){
				if(playerIn.isCrouching() && playerIn.isCreative()){
				}else{
					ent.tame(playerIn);
					if(playerIn.getTeam()!=null && playerIn.getTeam() instanceof ScorePlayerTeam){
						playerIn.level.getScoreboard().addPlayerToTeam(ent.getUUID().toString(), (ScorePlayerTeam)playerIn.getTeam());
					}
				}
			}
			++y;
			if(summonid==1){
				ent.setMoveType(2);
				ent.setMovePosX((int)x);
				ent.setMovePosZ((int)z+6);
			}else{
				soldier.setMoveType(1);
			}
			ent.special_cool = 300;
			ent.moveTo(x + 0.5, y, z + 0.5, 0, 0);
			worldIn.addFreshEntity(ent);
		} 
	}
	public void spawnbuilding(TameableEntity ent, World worldIn, PlayerEntity playerIn, double x, double y, double z)
	{
		if(ent!=null){
			if(playerIn!=null){
				if(playerIn.isCrouching() && playerIn.isCreative()){
				}else{
					ent.tame(playerIn);
					if(playerIn.getTeam()!=null && playerIn.getTeam() instanceof ScorePlayerTeam){
						playerIn.level.getScoreboard().addPlayerToTeam(ent.getUUID().toString(), (ScorePlayerTeam)playerIn.getTeam());
					}
				}
			}
			++y;
			ent.moveTo(x + 0.5, y, z + 0.5, 0, 0);
			if(ent instanceof SandBag){
				if(playerIn!=null)((SandBag)ent).setYawRote(90-(int)playerIn.yHeadRot);
				if(this.id==48){
					
				}else if(this.id==49){
					ent.getAttribute(Attributes.MAX_HEALTH).setBaseValue(60);
					ent.setHealth(60);
				}else if(this.id==50){
					ent.getAttribute(Attributes.MAX_HEALTH).setBaseValue(90);
					ent.setHealth(90);
				}
			}
			worldIn.addFreshEntity(ent);
		}
	}
	public void spawntank(WeaponVehicleBase entity, World worldIn, PlayerEntity playerIn, double x, double y, double z, int summonid)
	{
		if(entity!=null){
			if(playerIn!=null){
				if(playerIn.isCrouching() && playerIn.isCreative()){
				}else{
					entity.tame(playerIn);
					if(playerIn.getTeam()!=null && playerIn.getTeam() instanceof ScorePlayerTeam){
						playerIn.level.getScoreboard().addPlayerToTeam(entity.getUUID().toString(), (ScorePlayerTeam)playerIn.getTeam());
					}
				}
				//entity.turretYaw = entity.yBodyRot= entity.yRot = entity.yHeadRot = -((float) Math.atan2(x - playerIn.getX(), z - playerIn.getZ())) * 180.0F/ (float) Math.PI;
			}
			++y;
			entity.moveTo(x + 0.5, y, z + 0.5, 0, 0);
			/*if (!(worldIn instanceof ServerWorld)) {
				//return false;
			}else{
				ServerWorld serverworld = (ServerWorld)worldIn;
				entity.finalizeSpawn(serverworld, worldIn.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData)null, (CompoundNBT)null);
			}*/
			entity.setRemain1(entity.magazine);
			entity.setRemain2(entity.magazine2);
			entity.setRemain3(entity.magazine3);
			entity.setRemain4(entity.magazine4);
			entity.setMoveType(1);
			entity.setArmyType2(summonid);
			worldIn.addFreshEntity(entity);
		} 
	}
   
	public ActionResultType useOn(ItemUseContext context) {
	  World world = context.getLevel();
		ItemStack offitem = context.getPlayer().getOffhandItem();
		Item item2 = offitem.getItem();
	  if (world.isClientSide && (item2==null||!(item2 instanceof ItemSupport))) {
		 if(!context.getPlayer().isCreative() && type!=3){
			 if(type==0){
				context.getPlayer().sendMessage(new TranslationTextComponent("advancearmy.infor.spawn2.desc", new Object[0]), context.getPlayer().getUUID());
			 }else{
				context.getPlayer().sendMessage(new TranslationTextComponent("advancearmy.infor.spawn3.desc", new Object[0]), context.getPlayer().getUUID());
			 }	 
		 }
		 return ActionResultType.SUCCESS;
	  } else {
		  if(context.getPlayer().isCreative()||type == 3||item2!=null && item2 instanceof ItemSupport && ((ItemSupport)item2).isSummon){
				ItemStack itemstack = context.getItemInHand();
				/*if (!context.getPlayer().abilities.instabuild && type == 3)
				{
					itemstack.shrink(1);
				}*/
				BlockPos blockpos = context.getClickedPos();
				 Direction direction = context.getClickedFace();
				 BlockState blockstate = world.getBlockState(blockpos);
				 BlockPos blockpos1;
				 if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
					blockpos1 = blockpos;
				 } else {
					blockpos1 = blockpos.relative(direction);
				 }

				int weaponid = 0;
				if(blockstate.is(BlockRegister.ASSULT.get()))weaponid=1;
				if(blockstate.is(BlockRegister.RECON.get()))weaponid=2;
				if(blockstate.is(BlockRegister.ENGINEER.get()))weaponid=3;
				if(blockstate.is(BlockRegister.MEDIC.get()))weaponid=4;
				if(blockstate.is(BlockRegister.SUPPORT.get()))weaponid=5;
				spawnCreature(world, context.getPlayer(), weaponid, false, (double)blockpos1.getX(), (double)blockpos1.getY(), (double)blockpos1.getZ(),0);
				if(type == 3)itemstack.shrink(1);
		  }
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