package net.kjentytek303.untransfur.config;

import net.minecraftforge.common.ForgeConfigSpec;


public class ServerCfg {
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec.ConfigValue<Integer> MSC_MAX_STASIS_DURATION;
	public static final ForgeConfigSpec.ConfigValue<Boolean> MSC_PLAYER_GRIEF_FAILSAFE;

	public static final ForgeConfigSpec.ConfigValue<Integer> MSC_CRASHES_AFTER_STASIS_EXTENSIONS;

	static {
		BUILDER.comment("Max stasis duration for the MSC in seconds.");
		BUILDER.comment("Default duration for the Stasis Chamber is 120s");
		BUILDER.comment("Due to player requests, MSC's default is 300s (5 min)");
		MSC_MAX_STASIS_DURATION = BUILDER.defineInRange("msc.max_stasis_duration", 300, 0, 1000000000);

		BUILDER.comment("Stasis Chamber has a build-in griefing prevention mechanism, to prevent other players");
		BUILDER.comment("from constantly extending stasis. We allow server owners to disable this with MSC.");
		BUILDER.comment("Default: true");
		MSC_PLAYER_GRIEF_FAILSAFE = BUILDER.define("msc.player_griefing_prevention", true);

		BUILDER.comment("How many stasis extension attempts can be made with redstone, before MSC controller crashes?");
		BUILDER.comment("0 disables redstone stasis extensions");
		BUILDER.comment("-1 means unlimited extensions");
		BUILDER.comment("Default: 3");
		MSC_CRASHES_AFTER_STASIS_EXTENSIONS = BUILDER.defineInRange("msc.redstone_crashes_after_stasis_extensions", 3, -1, 1000000000);

		SPEC = BUILDER.build();
	}


}
