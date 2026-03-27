package advancearmy.entity.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
//import net.minecraft.init.MobEffects;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.ProjectileManager;
import com.mrcrayfish.guns.common.ShootTracker;
import com.mrcrayfish.guns.common.SpreadTracker;
import com.mrcrayfish.guns.common.container.AttachmentContainer;
import com.mrcrayfish.guns.common.container.WorkbenchContainer;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.crafting.WorkbenchRecipes;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.interfaces.IProjectileFactory;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IColored;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageBulletTrail;
import com.mrcrayfish.guns.network.message.MessageGunSound;
import com.mrcrayfish.guns.network.message.MessageShoot;
import com.mrcrayfish.guns.tileentity.WorkbenchTileEntity;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.enchantment.EnchantmentHelper;
import com.mrcrayfish.guns.item.IAmmo;
import com.mrcrayfish.guns.init.ModSounds;

import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraft.particles.IParticleData;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import advancearmy.entity.EntitySA_SoldierBase;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;

public abstract class AI_EntityWeapon_CGM
{
	public static void fireFromGun(EntitySA_SoldierBase entity, World world, ItemStack heldItem, Gun modifiedGun){
		if(heldItem.getItem() instanceof GunItem && Gun.hasAmmo(heldItem)/* && entity.getOwner()!=null && entity.getOwner() instanceof PlayerEntity*/){
			GunItem item = (GunItem) heldItem.getItem();
			if(modifiedGun != null)
			{
				int count = modifiedGun.getGeneral().getProjectileAmount();
				Gun.Projectile projectileProps = modifiedGun.getProjectile();
				ProjectileEntity[] spawnedProjectiles = new ProjectileEntity[count];
				for(int i = 0; i < count; i++)//开火发包
				{
					IProjectileFactory factory = ProjectileManager.getInstance().getFactory(projectileProps.getItem());
					//请确保家具枪的配置文件中的 improvedHitboxes = false ，家具枪的improvedHitboxes功能会导致由非玩家生物射出子弹时会造成崩溃
					ProjectileEntity projectileEntity = factory.create(world, entity/*.getOwner()*/, heldItem, item, modifiedGun);
					projectileEntity.setWeapon(heldItem);
					projectileEntity.setAdditionalDamage(Gun.getAdditionalDamage(heldItem));
					world.addFreshEntity(projectileEntity);
					spawnedProjectiles[i] = projectileEntity;
					projectileEntity.tick();
				}
				if(!projectileProps.isVisible())//子弹轨迹
				{
					IParticleData data = GunEnchantmentHelper.getParticle(heldItem);
					MessageBulletTrail messageBulletTrail = new MessageBulletTrail(spawnedProjectiles, projectileProps, entity.getId(), data);
					PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getX(), entity.getY(), entity.getZ(), Config.COMMON.network.projectileTrackingRange.get(), entity.level.dimension())), messageBulletTrail);
				}
				
				ResourceLocation fireSound = getFireSound(heldItem, modifiedGun);
				if(fireSound != null)//播放声音
				{
					double posX = entity.getX();
					double posY = entity.getY() + entity.getEyeHeight();
					double posZ = entity.getZ();
					float volume = GunModifierHelper.getFireSoundVolume(heldItem);
					float pitch = 0.9F + world.random.nextFloat() * 0.2F;
					double radius = GunModifierHelper.getModifiedFireSoundRadius(heldItem, Config.SERVER.gunShotMaxDistance.get());
					boolean muzzle = modifiedGun.getDisplay().getFlash() != null;
					MessageGunSound messageSound = new MessageGunSound(fireSound, SoundCategory.PLAYERS, (float) posX, (float) posY, (float) posZ, volume, pitch, entity.getId(), muzzle, false);
					PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(posX, posY, posZ, radius, entity.level.dimension());
					PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> targetPoint), messageSound);
				}
			}
		}
	}
    private static ResourceLocation getFireSound(ItemStack stack, Gun modifiedGun)
    {
        ResourceLocation fireSound = null;
        if(GunModifierHelper.isSilencedFire(stack))
        {
            fireSound = modifiedGun.getSounds().getSilencedFire();
        }
        else if(stack.isEnchanted())
        {
            fireSound = modifiedGun.getSounds().getEnchantedFire();
        }
        if(fireSound != null)
        {
            return fireSound;
        }
        return modifiedGun.getSounds().getFire();
    }
}