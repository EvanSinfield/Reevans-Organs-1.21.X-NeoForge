package net.reevan.reevansorgans.network;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.reevan.reevansorgans.screen.MarketScreen;
import org.slf4j.Logger;

public class ClientPayloadHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void handleDataOnNetwork(final CurrentOrganPrice data, final IPayloadContext ctx) {
        LOGGER.info("Received organ price");
        LOGGER.info("index: {}, price: {}", data.index(), data.price());

        Screen screen = Minecraft.getInstance().screen;

        if (screen instanceof MarketScreen) {
            ((MarketScreen) screen).setOrganPrice(data.price());
        }
    }
}
