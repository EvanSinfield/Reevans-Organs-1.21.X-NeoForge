package net.reevan.reevansorgans.market;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.reevan.reevansorgans.ReevansOrgans;
import org.slf4j.Logger;

import java.util.Random;

@EventBusSubscriber(modid = ReevansOrgans.MODID)
public class MarketManager {
    public static final Random random = new Random();
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        int chance = random.nextInt(120000);

        // 20 ticks a second, 1/1200 * 20 = 1/60 or ~every minute
        if (chance == 1) {
            marketCrash(event.getServer());
        } else if (chance <= 100) {
            updateMarket(event.getServer());
        }
    }

    public static void updateMarket(MinecraftServer server) {
        final double[] VOLATILITY = {0.75, 1.0, 1.05, 1.1, 0.9, 0.85, 0.95, 1.25, 1.15};  // constants: 0.75 - 1.25 applies after update

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

            // previous price +/- random based on demand, * volatility
            newPrices[i] = prices[i] + (int)(random.nextInt(lowerBound, upperBound) * VOLATILITY[i]);
            demand[i] = 0;

            if (newPrices[i] < 0) {
                newPrices[i] = 0;
            }
        }

        marketSaveData.setPrices(newPrices);
        marketSaveData.setDemand(demand);

//        LOGGER.info("Market prices: {}", newPrices);
    }

    public static void marketCrash(MinecraftServer server) {
        LOGGER.debug("Market crash");

        MarketSaveData marketSaveData = new MarketSaveData();
        marketSaveData = server.overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(marketSaveData::create, marketSaveData::load), "market");

        int organIndex = random.nextInt(0, 8);
        int[] newPrices = new int[9];

        LOGGER.debug("Selected Index: {}", organIndex);

        for (int i = 0; i < 9; i++) {
            if (i == organIndex) {
                newPrices[i] = marketSaveData.getPrices()[i] / random.nextInt(10, 30);
            } else {
                newPrices[i] = marketSaveData.getPrices()[i];
            }
        }

        LOGGER.debug("new Price: {}", newPrices);

        marketSaveData.setPrices(newPrices);
    }

    public static void register() {
        NeoForge.EVENT_BUS.register(MarketManager.class);
    }
}
