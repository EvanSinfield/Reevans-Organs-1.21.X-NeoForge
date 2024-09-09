package net.reevan.reevansorgans.market;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.reevan.reevansorgans.ReevansOrgans;

import java.util.Random;

@EventBusSubscriber(modid = ReevansOrgans.MODID)
public class MarketManager {
    public static final Random random = new Random();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        int chance = random.nextInt(1200);

        // 20 ticks a second, 1/1200 * 20 = 1/60 or ~every minute
        if (chance == 1) {
            updateMarket(event.getServer());
        }
    }

    public static void updateMarket(MinecraftServer server) {
        int upperBound;
        int lowerBound;
        int[] newPrices = new int[9];

        MarketSaveData marketSaveData = new MarketSaveData();
        marketSaveData = server.overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(marketSaveData::create, marketSaveData::load), "market");

        int[] prices = marketSaveData.getPrices();
        int[] demand = marketSaveData.getDemand();

        for (int i = 0; i < prices.length; i++) {
            upperBound = 8;
            lowerBound = -5;

            if (demand[i] > 0) {
                upperBound += (5 * demand[i]);
            } else if (demand[i] < 0) {
                lowerBound -= (5 * demand[i]);
            }

            newPrices[i] = prices[i] + random.nextInt(lowerBound, upperBound);
            demand[i] = 0;
        }

        marketSaveData.setPrices(newPrices);
        marketSaveData.setDemand(demand);
    }

    public static void register() {
        NeoForge.EVENT_BUS.register(MarketManager.class);
    }
}
