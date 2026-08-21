package net.kjentytek303.untransfur.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class UntransfurPacketHandler {
	public static void handlePlayerVariableSync( UntransfurVariables.SyncPacket message, Supplier<NetworkEvent.Context> context_supplier) {
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

	public static void handleLivingEntityVariableSync( LivingEntityVariables.SyncPacket message, Supplier<NetworkEvent.Context> ctx_supplier) {
		NetworkEvent.Context context = ctx_supplier.get();
		context.enqueueWork( () -> {
			Player player = Minecraft.getInstance().player;
			assert player != null;
			if( player.isDeadOrDying() ) {
				return;
			}

			var living_entity_vars = LivingEntityVariables.of(player);
			if (living_entity_vars != null ) {
				living_entity_vars.copyFrom(message.data);
			}
		});
		context.setPacketHandled(true);
	}
}
