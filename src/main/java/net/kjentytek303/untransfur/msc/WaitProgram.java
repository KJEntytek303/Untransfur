package net.kjentytek303.untransfur.msc;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;

import static net.minecraft.world.item.Items.GOLD_INGOT;
import static net.minecraft.world.item.Items.GOLD_NUGGET;
import static net.minecraft.world.item.Items.IRON_INGOT;
import static net.minecraft.world.item.Items.IRON_NUGGET;


public class WaitProgram extends MSCScheduledCommand {
	public static final ResourceLocation ID = Untransfur.modResource("wait");
	public WaitProgram() {
		super(ID);
	}

	public boolean test(MSCControllerBlockEntity bentity) {
		return true;
	}

	public Boolean apply( MSCControllerBlockEntity bentity, ItemStack argument) {
		return false;
	}

	@Override
	@Contract(pure = true)
	public MSCCommandInstance asInstance(ItemStack argument ) {
		var ret = super.asInstance(argument);
		//TODO I should probably move this into a datapack.
		Item item = argument.getItem();
		if (item.equals(GOLD_NUGGET)) {
			ret.additional_delay = 10 * 20;
		}
		else if (item.equals(GOLD_INGOT)) {
			ret.additional_delay = 30 * 20;
		}
		else if (item.equals(ItemStack.EMPTY)) {
			ret.additional_delay = 60 * 20;
		}
		else if (item.equals(IRON_NUGGET)) {
			ret.additional_delay = 2 * 60 * 20;
		}
		else if (item.equals(IRON_INGOT)) { //15 min stasis on default cfg.
			ret.additional_delay = 5 * 60 * 20;
		}
		else {
			ret.additional_delay = 0;
		}
		return ret;
	}
}
