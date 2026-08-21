package net.kjentytek303.untransfur.item;


//import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.kjentytek303.untransfur.config.ServerCfg;
import net.kjentytek303.untransfur.init.InitMobEffects;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.item.Syringe;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.process.TransfurEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UntransfurSyringeItem extends Item implements SpecializedAnimations
{
	
	public UntransfurSyringeItem(Properties properties){
		super( properties );
	}
	@Override
	public AnimationHandler getAnimationHandler() {
		return new Syringe.SyringeAnimation(this);
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

	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
		Player player = ( pLivingEntity instanceof Player ) ? (Player) pLivingEntity : null;
		if (player instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, pStack);
		}
		ChangedSounds.broadcastSound(pLivingEntity, ChangedSounds.SYRINGE_PRICK, 1, 1);
		if (player == null) {
			return pStack;
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		switch (ServerCfg.UNTRANSFUR_HANDLE_MODE.get()) {
			case SIMPLE -> {
				var event = new TransfurEvents.UntransfurPlayerByItemEvent( player, pStack, player, ProcessTransfur.getPlayerTransfurVariant(player), null );
				if(MinecraftForge.EVENT_BUS.post(event)){ break; }
				TransfurEvents.finalizeUntransfurPlayerEvent(event);
			}
			case ORGANICS_ONLY -> {
				if( ProcessTransfur.isPlayerNotLatex(player) ) {
					var event = new TransfurEvents.UntransfurPlayerByItemEvent( player, pStack, player, ProcessTransfur.getPlayerTransfurVariant(player), null );
					if(MinecraftForge.EVENT_BUS.post(event)){ break; }
					TransfurEvents.finalizeUntransfurPlayerEvent(event);
					break;
				}
				player.addEffect(new MobEffectInstance(InitMobEffects.UNSAFE_UNTRANSFUR.get(), 40, 1));
			}

			case COMPLEX -> {
				if( ProcessTransfur.isPlayerNotLatex(player)) {
					player.addEffect(new MobEffectInstance(InitMobEffects.UNSAFE_UNTRANSFUR.get(), 40, 1));
					break;
				}
				//TODO: add Flinston Solution effect.
			}
		}
		if (!player.getAbilities().instabuild) {
			pStack.shrink(1);
		}
		pStack = new ItemStack(ChangedItems.SYRINGE.get());
		return pStack;
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
		if( ServerCfg.UNTRANSFUR_HANDLE_MODE.get() != ServerCfg.UntransfurHandleMode.SIMPLE ) {
			pTooltipComponents.add(Component.translatable("item.untransfur.untransfur_syringe.desc"));
		}
	}
}