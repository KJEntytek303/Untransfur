package net.kjentytek303.untransfur.util;

import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ContainerWithIndex {
	public List<Integer> slots = new ArrayList<>();
	public IItemHandler item_handler;

	public ContainerWithIndex(@NotNull IItemHandler item_handler) {
		this.item_handler = item_handler;
	}
}