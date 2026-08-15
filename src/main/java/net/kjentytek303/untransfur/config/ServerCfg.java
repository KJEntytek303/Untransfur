package net.kjentytek303.untransfur.config;

import net.minecraftforge.common.ForgeConfigSpec;


public class ServerCfg {
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec.ConfigValue<Integer> MSC_MAX_STASIS_DURATION;
	public static final ForgeConfigSpec.ConfigValue<Boolean> MSC_PLAYER_GRIEF_FAILSAFE;
	public static final ForgeConfigSpec.ConfigValue<Integer> MSC_CRASHES_AFTER_STASIS_EXTENSIONS;
	public static final ForgeConfigSpec.ConfigValue<Integer> MSC_MAX_COMMAND_SCHEDULE;

	public static final ForgeConfigSpec.ConfigValue<UntransfurHandleMode> UNTRANSFUR_HANDLE_MODE;

	static {
		BUILDER.comment("Max stasis duration for the MSC in seconds.");
		BUILDER.comment("Default duration for the Stasis Chamber is 120s");
		BUILDER.comment("Due to player requests, MSC's default is 300s (5 min)");
		MSC_MAX_STASIS_DURATION = BUILDER.defineInRange("msc.max_stasis_duration", 300, 0, Integer.MAX_VALUE);

		BUILDER.comment("Stasis Chamber has a build-in griefing prevention mechanism, to prevent other players");
		BUILDER.comment("from constantly extending stasis. We allow server owners to disable this with MSC.");
		BUILDER.comment("Default: true");
		MSC_PLAYER_GRIEF_FAILSAFE = BUILDER.define("msc.player_griefing_prevention", true);

		BUILDER.comment("How many stasis extension attempts can be made with redstone, before MSC controller crashes?");
		BUILDER.comment("0 disables redstone stasis extensions");
		BUILDER.comment("-1 means unlimited extensions");
		BUILDER.comment("Default: 3");
		MSC_CRASHES_AFTER_STASIS_EXTENSIONS = BUILDER.defineInRange("msc.redstone_crashes_after_stasis_extensions", 3, -1, Integer.MAX_VALUE);

		BUILDER.comment("How many commands can the MSC queue keep at the same time.");
		BUILDER.comment("Exceeding this value will trigger a failsafe MSC crash.");
		BUILDER.comment("Default: 16");
		MSC_MAX_COMMAND_SCHEDULE = BUILDER.defineInRange( "msc.max_command_queue", 16, 3, Integer.MAX_VALUE);

		BUILDER.comment("How should Untransfurring be handled?");
		BUILDER.comment("SIMPLE - Same as in 1.0. MSC untransfur doesn't require special procedures.");
		BUILDER.comment("ORGANICS_ONLY - Same as SIMPLE, but only if the player is an organic. MSC untransfur doesn't require additional procedures.");
		BUILDER.comment("COMPLEX - Untransfurring requires an MSC and the player must follow MSC procedures described in lore");
		BUILDER.comment("Default: SIMPLE");
		UNTRANSFUR_HANDLE_MODE = BUILDER.defineEnum("untf_handle_mode", UntransfurHandleMode.SIMPLE);

		SPEC = BUILDER.build();
	}

	public enum UntransfurHandleMode {
		SIMPLE,
		ORGANICS_ONLY,
		COMPLEX
	}


}
