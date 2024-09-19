package net.reevan.reevansorgans.network;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.reevan.reevansorgans.item.ModItems;
import net.reevan.reevansorgans.market.MarketSaveData;
import net.reevan.reevansorgans.network.packets.BuySellOrgan;
import net.reevan.reevansorgans.network.packets.CurrentOrganPrice;
import net.reevan.reevansorgans.network.packets.RequestPrice;
import net.reevan.reevansorgans.screen.MarketMenu;
import org.slf4j.Logger;

import java.util.Random;

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

    public static void handleDemand(final BuySellOrgan data, final IPayloadContext ctx) {
        Random random = new Random();
        LOGGER.info("Received demand update");
        LOGGER.info("index: {}  demand: {}", data.index(), data.demand());

        MarketSaveData marketSaveData = new MarketSaveData();
        marketSaveData = ctx.player().getServer().overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(marketSaveData::create, marketSaveData::load), "market");

        marketSaveData.setDemand(data.index(), data.demand());

        AbstractContainerMenu menu = ctx.player().containerMenu;
        if (menu instanceof MarketMenu) {
            if (data.demand() > 0) {
                ItemStack organ;

                switch (data.index()) {
                    case 0:
                        organ = ModItems.BRAIN.toStack();
                        break;
                    case 1:
                        organ = ModItems.HEART.toStack();
                        break;
                    case 2:
                        organ = ModItems.INTESTINES.toStack();
                        break;
                    case 3:
                        organ = ModItems.KIDNEY.toStack();
                        break;
                    case 4:
                        organ = ModItems.LIVER.toStack();
                        break;
                    case 5:
                        organ = ModItems.LUNGS.toStack();
                        break;
                    case 6:
                        organ = ModItems.PANCREAS.toStack();
                        break;
                    case 7:
                        organ = ModItems.STOMACH.toStack();
                        break;
                    case 8:
                        organ = ModItems.THYROID.toStack();
                        break;
                    default:  // this will never be called but stops it complaining
                        organ = ItemStack.EMPTY;
                }

                organ.setDamageValue(random.nextInt(0, 99));
                ((MarketMenu) menu).setOrganSlot(organ);

                LOGGER.debug("organ bought");

                int credits = ctx.player().getPersistentData().getInt("credits");
                ctx.player().getPersistentData().putInt("credits", credits - marketSaveData.getPrices()[data.index()]);
            } else {
                int credits = ctx.player().getPersistentData().getInt("credits");
                int sellPrice = Math.round((100 - ((MarketMenu) menu).getOrganSlot().getDamageValue()) / 100f
                        * marketSaveData.getPrices()[data.index()] * random.nextFloat(0.75f, 1.25f));

                ctx.player().getPersistentData().putInt("credits", credits + sellPrice);
                ((MarketMenu) menu).emptyOrganSlot();
            }
        }
    }
}
