package com.shynieke.statues.network;

import com.shynieke.statues.Reference;
import com.shynieke.statues.network.handler.ClientPayloadHandler;
import com.shynieke.statues.network.handler.ServerPayloadHandler;
import com.shynieke.statues.network.message.PlayerStatueScreenData;
import com.shynieke.statues.network.message.PlayerStatueSyncData;
import com.shynieke.statues.network.message.StatueTableData;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class StatuesNetworking {
	public static void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(Reference.MOD_ID);

		registrar.playToClient(PlayerStatueScreenData.ID, PlayerStatueScreenData.CODEC, ClientPayloadHandler.getInstance()::handleData);
		registrar.playToServer(StatueTableData.ID, StatueTableData.CODEC, ServerPayloadHandler.getInstance()::handleTableData);
		registrar.playToServer(PlayerStatueSyncData.ID, PlayerStatueSyncData.CODEC, ServerPayloadHandler.getInstance()::handleSyncData);
	}
}
