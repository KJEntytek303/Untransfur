package net.kjentytek303.untransfur.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class UntransfurPacketHandler {
	public static void handlerVariableSync( UntransfurVariables.SyncPacket message, Supplier<NetworkEvent.Context> context_supplier) {
		NetworkEvent.Context context = context_supplier.get();
		context.enqueueWork( () -> {
			Player player = Minecraft.getInstance().player;
			assert player != null;
			if( player.isDeadOrDying() ) {
				return;
			}

			var untf_vars = UntransfurVariables.of(player);
			if (untf_vars != null ) {
				untf_vars.copyFrom(message.data);
			}
		});
		context.setPacketHandled(true);
	}
}
