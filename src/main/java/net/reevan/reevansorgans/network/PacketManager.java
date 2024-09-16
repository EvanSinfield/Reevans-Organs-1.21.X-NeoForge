package net.reevan.reevansorgans.network;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.reevan.reevansorgans.ReevansOrgans;
import org.slf4j.Logger;

@EventBusSubscriber(modid = ReevansOrgans.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PacketManager {
    private static final Logger LOGGER = LogUtils.getLogger();


    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.NETWORK);

        registrar.playToServer(
                RequestPrice.TYPE,
                RequestPrice.STREAM_CODEC,
                ServerPayloadHandler::handleDataOnNetwork
        );

        registrar.playToClient(
                CurrentOrganPrice.TYPE,
                CurrentOrganPrice.STREAM_CODEC,
                ClientPayloadHandler::handleDataOnNetwork
        );

        LOGGER.info("Packets registered");
    }
}
