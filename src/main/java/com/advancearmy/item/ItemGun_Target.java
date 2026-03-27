package advancearmy.item;
import wmlib.common.item.ItemGun;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import advancearmy.entity.map.SupportPoint;
import advancearmy.AdvanceArmy;
import net.minecraft.util.math.vector.Vector3d;
import wmlib.common.bullet.EntityMissile;
import net.minecraft.util.CooldownTracker;
import wmlib.api.IRaderItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import advancearmy.event.SASoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.client.util.ITooltipFlag;
public class ItemGun_Target extends ItemGun implements IRaderItem {
   public ItemGun_Target(Item.Properties builder) {
      super(builder);
   }

   public boolean fire_flag = true;
   public int fire_count = 0;
   //int id = 6;
   public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
	   super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
   }
   
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("advancearmy.infor.targetgun1.desc").withStyle(TextFormatting.GREEN));//name
	}
   
   public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
	  /*if (entityLiving instanceof PlayerEntity) {
		 PlayerEntity player = (PlayerEntity)entityLiving;
	  }*/
   }

	public void FireBullet(ItemStack stack, World worldIn, PlayerEntity player) 
	{
		if(player.getOffhandItem()!=null){
			ItemStack held = player.getOffhandItem();
			Item items = held.getItem();
			if(items instanceof ItemSupport){
				ItemSupport support = (ItemSupport)items;
				if(player!=null){
					if(player.getScore()>=support.xp||player.isCreative()||support.type==1){
						CooldownTracker tracker = player.getCooldowns();
						if(!tracker.isOnCooldown(support)||support.type==1){
							if(!player.isCreative() && support.type==0){
								player.giveExperiencePoints(-support.xp);
								player.getCooldowns().addCooldown(this, support.cool);
							}
							//worldIn.playSound(player, player.getX(), player.getY(), player.getZ(),WMSoundEvent.getSound(this.fire_sound), SoundCategory.NEUTRAL, 3.0F, 1.0F);
							Vector3d lock = Vector3d.directionFromRotation(player.xRot, player.yRot);
							int range = 2;
							int ix = 0;
							int iy = 0;
							int iz = 0;
							
							int posXm = 0;
							int posYm = 0;
							int posZm = 0;
							boolean isGroundAim = false;
							for(int x2 = 0; x2 < 120; ++x2) {
								ix = (int) (player.getX() + lock.x * x2);
								iy = (int) (player.getY() + 1.5 + lock.y * x2);
								iz = (int) (player.getZ() + lock.z * x2);
								if(ix != 0 && iz != 0 && iy != 0){
									BlockPos blockpos = new BlockPos(player.getX() + lock.x * x2,player.getY() + 1.5 + lock.y * x2,player.getZ() + lock.z * x2);
									BlockState iblockstate = player.level.getBlockState(blockpos);
									if (!iblockstate.isAir(player.level, blockpos)){
										posXm = ix;
										posYm = iy+1;
										posZm = iz;
										isGroundAim = true;
										break;
									}else{
									}
								}
							}
							if(support.id==1)player.sendMessage(new TranslationTextComponent("SWUN探索者小队即将到达", new Object[0]), player.getUUID());
							if(support.id==2)player.sendMessage(new TranslationTextComponent("超时空传送正在启动", new Object[0]), player.getUUID());
							if(support.id==3)player.sendMessage(new TranslationTextComponent("超时空传送正在启动", new Object[0]), player.getUUID());
							if(support.id==4)player.sendMessage(new TranslationTextComponent("轨道空投启动", new Object[0]), player.getUUID());
							if(support.id==5)player.sendMessage(new TranslationTextComponent("轨道空投启动", new Object[0]), player.getUUID());
							if(support.id==6)player.sendMessage(new TranslationTextComponent("A-10攻击机即将前往轰炸！", new Object[0]), player.getUUID());
							if(support.id==7)player.sendMessage(new TranslationTextComponent("F35战机即将前往轰炸！", new Object[0]), player.getUUID());
							if(support.id==8)player.sendMessage(new TranslationTextComponent("3架A-10攻击机即将前往轰炸！", new Object[0]), player.getUUID());
							if(support.id==9)player.sendMessage(new TranslationTextComponent("3架F35战机即将前往轰炸！", new Object[0]), player.getUUID());
							if(support.id==10)player.sendMessage(new TranslationTextComponent("开~炮！！！！", new Object[0]), player.getUUID());
							if(support.id==11){
								EntityMissile bullet = new EntityMissile(worldIn, player, null, player);
								bullet.modid="advancearmy";
								bullet.fly_sound="advancearmy.missile_fly2";
								bullet.timemax=700;
								bullet.power = 700;
								bullet.setGravity(0.01F);
								bullet.setExLevel(12);
								bullet.hitEntitySound=SASoundEvent.artillery_impact.get();
								bullet.hitBlockSound=SASoundEvent.artillery_impact.get();
								bullet.setBulletType(7);
								bullet.setModel("advancearmy:textures/entity/bullet/kh29l.obj");
								bullet.setTex("advancearmy:textures/entity/bullet/kh29t.png");
								bullet.setFX("BigMissileTrail");
								//bullet.flame = true;
								bullet.moveTo(player.getX(), player.getY()+100, player.getZ(), player.yRot, player.xRot);
								bullet.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 7F, 1F);
								if (!worldIn.isClientSide) worldIn.addFreshEntity(bullet);
								player.sendMessage(new TranslationTextComponent("警告！KH29导弹已发射！！", new Object[0]), player.getUUID());
							}
							if(support.id==12){
								EntityMissile bullet = new EntityMissile(worldIn, player, posXm, posYm-1, posZm, player);
								bullet.modid="advancearmy";
								bullet.fly_sound="advancearmy.missile_fly3";
								bullet.timemax=700;
								bullet.power = 1000;
								bullet.setGravity(0.01F);
								bullet.setExLevel(20);
								bullet.hitEntitySound=SASoundEvent.missile_hit1.get();
								bullet.hitBlockSound=SASoundEvent.missile_hit1.get();
								bullet.setBulletType(8);
								bullet.setModel("advancearmy:textures/entity/bullet/3m22.obj");
								bullet.setTex("advancearmy:textures/entity/bullet/3m22.png");
								bullet.setFX("BigMissileTrail");
								//bullet.flame = true;
								bullet.moveTo(player.getX(), player.getY()+100, player.getZ(), player.yRot, player.xRot);
								bullet.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 9F, 1F);
								player.sendMessage(new TranslationTextComponent("警告！3M22导弹已发射！！", new Object[0]), player.getUUID());
								if (!worldIn.isClientSide) worldIn.addFreshEntity(bullet);
							}
							if(support.id==13){
								EntityMissile bullet = new EntityMissile(worldIn, player, null, player);
								bullet.modid="advancearmy";
								bullet.fly_sound="advancearmy.missile_fly2";
								bullet.timemax=700;
								bullet.power = 1100;
								bullet.setGravity(0.01F);
								bullet.setExLevel(22);
								bullet.hitEntitySound=SASoundEvent.missile_hit1.get();
								bullet.hitBlockSound=SASoundEvent.missile_hit1.get();
								bullet.setBulletType(7);
								bullet.setModel("advancearmy:textures/entity/bullet/agm158.obj");
								bullet.setTex("advancearmy:textures/entity/bullet/agm158.png");
								bullet.setFX("BigMissileTrail");
								//bullet.flame = true;
								bullet.moveTo(player.getX(), player.getY()+100, player.getZ(), player.yRot, player.xRot);
								bullet.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 7F, 1F);
								player.sendMessage(new TranslationTextComponent("警告！AGM-158导弹已发射！！", new Object[0]), player.getUUID());
								if (!worldIn.isClientSide) worldIn.addFreshEntity(bullet);
							}
							if(support.id==14){
								EntityMissile bullet = new EntityMissile(worldIn, player, posXm, posYm-1, posZm, player);
								bullet.modid="advancearmy";
								bullet.fly_sound="advancearmy.missile_fly1";
								bullet.timemax=700;
								bullet.power = 1200;
								bullet.setGravity(0.01F);
								bullet.setExLevel(25);
								bullet.hitEntitySound=SASoundEvent.missile_hit1.get();
								bullet.hitBlockSound=SASoundEvent.missile_hit1.get();
								bullet.setBulletType(8);
								bullet.setModel("advancearmy:textures/entity/bullet/kh58.obj");
								bullet.setTex("advancearmy:textures/entity/bullet/kh58.png");
								bullet.setFX("BigMissileTrail");
								//bullet.flame = true;
								bullet.moveTo(player.getX(), player.getY()+100, player.getZ(), player.yRot, player.xRot);
								bullet.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 7F, 1F);
								player.sendMessage(new TranslationTextComponent("警告！KH58导弹已发射！！", new Object[0]), player.getUUID());
								if (!worldIn.isClientSide) worldIn.addFreshEntity(bullet);
							}
							if(support.id==15){
								if(isGroundAim){
									player.sendMessage(new TranslationTextComponent("警告！战术核弹已发射！！", new Object[0]), player.getUUID());
									worldIn.playSound(player, player.getX(), player.getY(), player.getZ(),this.getSound("advancearmy","advancearmy.nuclear_worn"), SoundCategory.NEUTRAL, 10.0F, 1.0F);
								}else{
									player.sendMessage(new TranslationTextComponent("需要地面坐标！！！", new Object[0]), player.getUUID());
								}
							}
							if(support.id==16)player.sendMessage(new TranslationTextComponent("超时空传送正在启动", new Object[0]), player.getUUID());
							if(posXm!=0 || posYm!=0 || posZm!=0){
								SupportPoint point = new SupportPoint(AdvanceArmy.ENTITY_SPT, worldIn);
								if(player!=null){
									/*if(player.isCrouching() && player.isCreative()){
									}else*/{
										point.tame(player);
										if(player.getTeam()!=null && player.getTeam() instanceof ScorePlayerTeam){
											player.level.getScoreboard().addPlayerToTeam(point.getUUID().toString(), (ScorePlayerTeam)player.getTeam());
										}
									}
								}
								point.setSummonID(support.id);
								point.moveTo(posXm, posYm, posZm, 0, 0);
								worldIn.addFreshEntity(point);
							}
							if(support.type==1)held.shrink(1);
							worldIn.playSound(player, player.getX(), player.getY(), player.getZ(),this.getSound("advancearmy","advancearmy.command_say"), SoundCategory.NEUTRAL, 10.0F, 1.0F);
						}
					}else{
						worldIn.playSound(player, player.getX(), player.getY(), player.getZ(),this.getSound("advancearmy","advancearmy.command_say"), SoundCategory.NEUTRAL, 10.0F, 1.0F);
						player.sendMessage(new TranslationTextComponent("没有足够的经验值!", new Object[0]), player.getUUID());
					}
				}
			}
		}
	}
}
