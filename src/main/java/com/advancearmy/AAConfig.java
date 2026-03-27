package advancearmy;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Collections;
import java.util.List;

public class AAConfig
{
    /**
     * Common related config options
     */
    public static class Common
    {
        public final Spawn spawn;
        public Common(ForgeConfigSpec.Builder builder)
        {
            builder.push("common");
            {
                this.spawn = new Spawn(builder);
            }
            builder.pop();
        }
    }

    public static class Spawn
    {
        public final ForgeConfigSpec.BooleanValue spawn_eromob;
		public final ForgeConfigSpec.BooleanValue spawn_army;
		public final ForgeConfigSpec.BooleanValue vehicle_break_block;
		public final ForgeConfigSpec.IntValue ranger_count;
		public final ForgeConfigSpec.IntValue conscript_count;
		public final ForgeConfigSpec.IntValue ero_reb_count;
		public final ForgeConfigSpec.IntValue ero_pillager_count;
		public final ForgeConfigSpec.IntValue ero_phantom_count;
		public final ForgeConfigSpec.IntValue ero_ghost_count;
		public final ForgeConfigSpec.IntValue ero_creeper_count;
		public final ForgeConfigSpec.IntValue ero_skeleton_count;
		public final ForgeConfigSpec.IntValue ero_zombie_count;
		public final ForgeConfigSpec.IntValue ero_husk_count;
		
        public Spawn(ForgeConfigSpec.Builder builder)
        {
			this.vehicle_break_block = builder.comment("是否开启载具移动破坏方块").define("vehicle_break_block", true);
            builder.comment("生物生成选项").push("spawn_option");
            {
                this.spawn_eromob = builder.comment("是否生成侵蚀怪物").define("spawn_eromob", true);
				this.spawn_army = builder.comment("是否生成友军部队").define("spawn_friend", true);
            }
            builder.pop();
			builder.comment("生物生成权重").push("spawn_weight");
				this.ranger_count = builder.comment("游骑兵生成权重").defineInRange("ranger_count", 10, 0, 100);
				this.conscript_count = builder.comment("动员兵生成权重").defineInRange("conscript_count", 10, 0, 100);
				this.ero_reb_count = builder.comment("侵蚀叛军生成权重").defineInRange("ero_reb_count", 20, 0, 100);
				this.ero_pillager_count = builder.comment("侵蚀掠夺者生成权重").defineInRange("ero_pillager_count", 15, 0, 100);
				this.ero_phantom_count = builder.comment("侵蚀幻翼生成权重").defineInRange("ero_phantom_count", 15, 0, 100);
				this.ero_ghost_count = builder.comment("侵蚀恶魂生成权重").defineInRange("ero_ghost_count", 5, 0, 100);
				this.ero_creeper_count = builder.comment("侵蚀爬行者生成权重").defineInRange("ero_creeper_count", 20, 0, 100);
				this.ero_skeleton_count = builder.comment("侵蚀骷髅生成权重").defineInRange("ero_skeleton_count", 20, 0, 100);
				this.ero_zombie_count = builder.comment("侵蚀僵尸生成权重").defineInRange("ero_zombie_count", 20, 0, 100);
				this.ero_husk_count = builder.comment("无魂空壳生成权重").defineInRange("ero_husk_count", 1, 0, 100);
			builder.pop();
        }
    }
    static final ForgeConfigSpec commonSpec;
    public static final AAConfig.Common COMMON;
    static
    {
        /*final Pair<Common, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(AAConfig.Common::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();*/
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
    /*public static void saveClientConfig()
    {
        clientSpec.save();
    }*/
}
