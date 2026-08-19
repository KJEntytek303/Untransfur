package net.kjentytek303.untransfur.init;

import net.kjentytek303.untransfur.Untransfur;
import net.kjentytek303.untransfur.network.UntransfurPacketHandler;
import net.kjentytek303.untransfur.network.UntransfurVariables;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;


public class InitPackets {
	public static void addPackets(FMLCommonSetupEvent event ) {
		Untransfur.addNetworkMessage(UntransfurVariables.SyncPacket.class,
			UntransfurVariables.SyncPacket::serialize,
			UntransfurVariables.SyncPacket::new,
			UntransfurPacketHandler::handlerVariableSync,
			NetworkDirection.PLAY_TO_CLIENT
		);
	}
}
