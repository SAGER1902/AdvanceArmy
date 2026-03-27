package advancearmy.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.TameableEntity;
import advancearmy.entity.EntitySA_SoldierBase;
public abstract class SoldierTargetGoalSA extends Goal {
   protected final EntitySA_SoldierBase mob;
   protected final boolean mustSee;
   private final boolean mustReach;
   private int reachCache;
   private int reachCacheTime;
   private int unseenTicks;
   protected LivingEntity targetMob;
   protected int unseenMemoryTicks = 60;

   public SoldierTargetGoalSA(EntitySA_SoldierBase soldier, boolean isCanSee) {
      this(soldier, isCanSee, false);
   }

   public SoldierTargetGoalSA(EntitySA_SoldierBase soldier, boolean isCanSee, boolean isReach) {
      this.mob = soldier;
      this.mustSee = isCanSee;
      this.mustReach = isReach;
   }

   public boolean canContinueToUse() {
      LivingEntity livingentity = this.mob.getTarget();
      if (livingentity == null) {
         livingentity = this.targetMob;
      }
      if (livingentity == null) {
         return false;
      } else if (!livingentity.isAlive()) {
         return false;
      } else {
         Team team = this.mob.getTeam();
         Team team1 = livingentity.getTeam();
         if (team != null && team1 == team) {
            return false;
         } else {
            double d0 = this.getFollowDistance();
            if (this.mob.distanceToSqr(livingentity) > d0 * d0) {
               return false;
            } else {
               if (this.mustSee) {
                  if (this.mob.getSensing().canSee(livingentity)) {
                     this.unseenTicks = 0;
                  } else if (++this.unseenTicks > this.unseenMemoryTicks) {
                     return false;
                  }
               }
               if (livingentity instanceof PlayerEntity && ((PlayerEntity)livingentity).abilities.invulnerable) {
                  return false;
               } else {
                  this.mob.setTarget(livingentity);
				  //this.mob.setAttacking(true);
                  return true;
               }
            }
         }
      }
   }
	protected double getAADistance() {
		return this.mob.attack_range_max;//attack_height_max
	}
	protected double getFollowDistance() {
		return this.mob.attack_range_max;
	}

	public void start() {
		this.reachCache = 0;
		this.reachCacheTime = 0;
		this.unseenTicks = 0;
	}

   public void stop() {
      this.mob.setTarget((LivingEntity)null);
      this.targetMob = null;
   }

   protected boolean canAttack(@Nullable LivingEntity attackentity, EntityPredicate predicate) {
      if (attackentity == null) {
         return false;
      } else if (!predicate.test(this.mob, attackentity)) {
         return false;
      } else if (!this.mob.isWithinRestriction(attackentity.blockPosition())) {
         return false;
      } else {
         if (this.mustReach) {
            if (--this.reachCacheTime <= 0) {
               this.reachCache = 0;
            }

            if (this.reachCache == 0) {
               this.reachCache = this.canReach(attackentity) ? 1 : 2;
            }

            if (this.reachCache == 2) {
               return false;
            }
         }
         return true;
      }
   }

   private boolean canReach(LivingEntity attackentity) {
      this.reachCacheTime = 10 + this.mob.getRandom().nextInt(5);
      Path path = this.mob.getNavigation().createPath(attackentity, 0);
      if (path == null) {
         return false;
      } else {
         PathPoint pathpoint = path.getEndNode();
         if (pathpoint == null) {
            return false;
         } else {
            int i = pathpoint.x - MathHelper.floor(attackentity.getX());
            int j = pathpoint.z - MathHelper.floor(attackentity.getZ());
            return (double)(i * i + j * j) <= 2.25D;
         }
      }
   }

   public SoldierTargetGoalSA setUnseenMemoryTicks(int count) {
      this.unseenMemoryTicks = count;
      return this;
   }
}