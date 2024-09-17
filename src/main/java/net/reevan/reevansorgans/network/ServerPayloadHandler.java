package net.reevan.reevansorgans.network;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.reevan.reevansorgans.market.MarketSaveData;
import org.slf4j.Logger;

public class ServerPayloadHandler {
    private static final Logger LOGGER = LogUtils.getLogger();  // FIXME: logger

    public static void handlePriceRequest(final RequestPrice data, final IPayloadContext ctx) {
        LOGGER.info("Received price request");
        LOGGER.info("index: {}", data.index());

        MarketSaveData marketSaveData = new MarketSaveData();
        marketSaveData = ctx.player().getServer().overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(marketSaveData::create, marketSaveData::load), "market");

        PacketDistributor.sendToPlayer((ServerPlayer)ctx.player(), new CurrentOrganPrice(data.index(),
                marketSaveData.getPrices()[data.index()]));
    }

    public static void handleDemand(final ChangeOrganDemand data, final IPayloadContext ctx) {
        LOGGER.info("Received demand update");
        LOGGER.info("index: {}  demand: {}", data.index(), data.demand());

        MarketSaveData marketSaveData = new MarketSaveData();
        marketSaveData = ctx.player().getServer().overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(marketSaveData::create, marketSaveData::load), "market");

        marketSaveData.setDemand(data.index(), data.demand());
    }
}
