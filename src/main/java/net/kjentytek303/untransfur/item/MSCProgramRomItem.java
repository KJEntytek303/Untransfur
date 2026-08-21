package net.kjentytek303.untransfur.item;

import net.kjentytek303.untransfur.config.ServerCfg;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class MSCProgramRomItem extends Item {

	public MSCProgramRomItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
		CompoundTag program = pStack.getTag();
		if(program != null && program.contains("program")) {
			pTooltipComponents.add(Component.translatable("item.untransfur.msc_program_rom.tooltip", Component.translatable( program.get("program").getAsString())));
		} else {
			pTooltipComponents.add(Component.translatable("item.untransfur.msc_program_rom.tooltip", Component.translatable("item.untransfur.msc_program_rom.tooltip.empty" )));
		}
	}
}
