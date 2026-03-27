package advancearmy.util;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.GroundPathHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.MobEntity;
import advancearmy.AdvanceArmy;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.item.Item;

import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraft.entity.CreatureEntity;

import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.BossInfo;

import wmlib.api.IEnemy;
import net.minecraft.entity.player.ServerPlayerEntity;
import wmlib.common.living.WeaponVehicleBase;
import advancearmy.entity.EntitySA_LandBase;
import advancearmy.event.SASoundEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;

import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import wmlib.api.IHealthBar;
import wmlib.api.IArmy;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.Direction;
import net.minecraft.network.play.server.SDestroyEntitiesPacket;
import advancearmy.entity.EntitySA_SoldierBase;
import advancearmy.item.ItemRemove;
public class InteractSoldier{
	public static ActionResultType interactSoldier(EntitySA_SoldierBase soldier, PlayerEntity player, Hand hand) {
		ItemStack heldItem = player.getItemInHand(hand);
		ItemStack this_heldItem = soldier.getMainHandItem();
		Item item = heldItem.getItem();
		if(soldier.getOwner()==null&&item == Items.GOLD_INGOT && heldItem!=null && !heldItem.isEmpty()){
			soldier.tame(player);
			player.sendMessage(new TranslationTextComponent("Ok, I'll follow you", new Object[0]), player.getUUID());
			heldItem.shrink(1);
			return ActionResultType.PASS;
		}
		if(item instanceof ItemRemove && soldier.getOwner()==player){
			player.playSound(SoundEvents.ANVIL_DESTROY,1F,1F);
			if(!soldier.level.isClientSide){
				soldier.remove();
				if (player instanceof ServerPlayerEntity) {
					((ServerPlayerEntity) player).connection.send(new SDestroyEntitiesPacket(soldier.getId()));
				}
				return ActionResultType.SUCCESS;
			}
			if(soldier.level.isClientSide)player.sendMessage(new TranslationTextComponent("Remvoed", new Object[0]), player.getUUID());
			return ActionResultType.SUCCESS;
		}
		if(player.isCreative()||player == soldier.getOwner()){
			if(soldier.getMoveType() == 1) {
				soldier.setMoveType(0);
				soldier.setRemain2(0);
				return ActionResultType.PASS;
			}
			else if(soldier.getMoveType() == 0) {
				soldier.setMoveType(3);
				soldier.setRemain2(2);
				return ActionResultType.PASS;
			}
			else if(soldier.getMoveType() == 3) {
				soldier.setMoveType(1);
				soldier.setRemain2(0);
				return ActionResultType.PASS;
			}
		}
		return ActionResultType.PASS;
    }
}
