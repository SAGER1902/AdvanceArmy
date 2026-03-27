package advancearmy.event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import advancearmy.entity.mob.DragonTurret;
import advancearmy.entity.soldier.EntitySA_Soldier;
import advancearmy.AdvanceArmy;
public class SAEntityEvent {
	@SubscribeEvent
	public void extraSpawnEvent(EntityJoinWorldEvent event) {//Turret
		if(event.getEntity()!=null && event.getWorld()!=null && event.getWorld() instanceof ServerWorld){
			ServerWorld serverworld = (ServerWorld)event.getWorld();
			if(event.getEntity() instanceof EnderDragonEntity){
				EnderDragonEntity living = (EnderDragonEntity) event.getEntity();
				int ra = living.level.random.nextInt(3);
				if(ra==1){
					DragonTurret pha = new DragonTurret(AdvanceArmy.ENTITY_DT, serverworld);
					pha.setPos(living.getX(), living.getY()+1, living.getZ());
					serverworld.addFreshEntityWithPassengers(pha);
					pha.startRiding(living);
				}
			}
			/*if(event.getEntity() instanceof VillagerEntity){
				VillagerEntity living = (VillagerEntity) event.getEntity();
				int ra = living.level.random.nextInt(5);
				if(ra==1){
					EntitySA_Soldier pha = new EntitySA_Soldier(AdvanceArmy.ENTITY_SOLDIER, serverworld);
					pha.setPos(living.getX(), living.getY()+1, living.getZ());
					serverworld.addFreshEntityWithPassengers(pha);
					//pha.startRiding(living);
				}
			}*/
		}
	}
}
