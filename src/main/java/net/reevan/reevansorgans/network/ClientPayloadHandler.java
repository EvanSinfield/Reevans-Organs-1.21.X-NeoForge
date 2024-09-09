package net.reevan.reevansorgans.network;

import com.mojang.logging.LogUtils;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public class ClientPayloadHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void handleDataOnMain(final CurrentOrganPrice data, final IPayloadContext ctx) {
        LOGGER.info("Received organ price");
        LOGGER.info("index: {}, price: {}", data.index(), data.price());
    }
}
