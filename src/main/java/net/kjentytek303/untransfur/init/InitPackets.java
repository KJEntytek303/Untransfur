package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.network.LivingEntityVariables;
import net.kjentytek303.untransfur.network.UntransfurPacketHandler;
import net.kjentytek303.untransfur.network.UntransfurVariables;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;


public class InitPackets {
	public static void addPackets(FMLCommonSetupEvent event ) {
		Untransfur.addNetworkMessage(UntransfurVariables.SyncPacket.class,
			UntransfurVariables.SyncPacket::serialize,
			UntransfurVariables.SyncPacket::new,
			UntransfurPacketHandler::handlePlayerVariableSync,
			NetworkDirection.PLAY_TO_CLIENT
		);
		Untransfur.addNetworkMessage(LivingEntityVariables.SyncPacket.class,
			LivingEntityVariables.SyncPacket::serialize,
			LivingEntityVariables.SyncPacket::new,
			UntransfurPacketHandler::handleLivingEntityVariableSync,
			NetworkDirection.PLAY_TO_CLIENT
		);
	}
}
