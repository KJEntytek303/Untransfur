package net.kjentytek303.untransfur.item;


//import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UntransfurSyringeItem extends Item implements net.ltxprogrammer.changed.item.SpecializedAnimations
{
	
	public UntransfurSyringeItem(Properties properties){
		super( properties );
	}
	@Override
	public AnimationHandler getAnimationHandler() {
		return new net.ltxprogrammer.changed.item.Syringe.SyringeAnimation(this);
	}
	
	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
		ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
		return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
	}
	
	@Override
	public int getUseDuration(@NotNull ItemStack pStack) {
		return 32;
	}
	
	//If stab, then kill //LATER
	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
		Player player = ( pLivingEntity instanceof Player ) ? (Player) pLivingEntity : null;
		if (player instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, pStack);
		}
		ChangedSounds.broadcastSound(pLivingEntity, ChangedSounds.SYRINGE_PRICK, 1, 1);
		if (player != null) {
			player.awardStat(Stats.ITEM_USED.get(this));
			if (!player.getAbilities().instabuild) {
				pStack.shrink(1);
			}
			
			pStack = new ItemStack(ChangedItems.SYRINGE.get());
			
			ProcessTransfur.removePlayerTransfurVariant(player);
			ProcessTransfur.setPlayerTransfurProgress(player, 0.0f);
		}
		return pStack;
	}
	
	
	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		pTooltipComponents.add(Component.translatable("item.untransfur.untransfur_syringe.desc"));
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
	}
}

