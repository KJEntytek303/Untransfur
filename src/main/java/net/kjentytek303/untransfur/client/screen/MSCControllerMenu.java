package net.kjentytek303.untransfur.client.screen;

import net.kjentytek303.untransfur.block_entity.MSCControllerBlockEntity;
import net.ltxprogrammer.changed.block.entity.StasisChamberBlockEntity;
import net.ltxprogrammer.changed.world.inventory.StasisChamberMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;


public class MSCControllerMenu extends StasisChamberMenu {
	public MSCControllerMenu(int id, Inventory inventory, FriendlyByteBuf extra) {
		super(id, inventory, extra);
	}
	public MSCControllerMenu(int id, Inventory inventory, @Nullable StasisChamberBlockEntity blockEntity, ContainerData dataAccess) {
		super(id, inventory, blockEntity, dataAccess);
	}


	/*
	protected MSCControllerMenu(@Nullable MenuType<?> pMenuType, int pContainerId) {
		super(pMenuType, pContainerId);
	}

	public final MSCControllerBlockEntity block_entity;
	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 *
	 * @param pPlayer
	 * @param pIndex
	 *
	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		return null;
	}
	/**
	 * Determines whether supplied player can use this container
	 *
	 * @param pPlayer
	 *
	@Override
	public boolean stillValid(Player pPlayer) {
		return false;
	}*/
}
