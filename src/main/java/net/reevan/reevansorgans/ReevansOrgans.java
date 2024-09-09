package net.reevan.reevansorgans;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.reevan.reevansorgans.block.ModBlocks;
import net.reevan.reevansorgans.item.ModCreativeTab;
import net.reevan.reevansorgans.item.ModItems;
import net.reevan.reevansorgans.market.MarketManager;
import net.reevan.reevansorgans.market.MarketSaveData;
import net.reevan.reevansorgans.network.CurrentOrganPrice;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.Random;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ReevansOrgans.MODID)
public class ReevansOrgans
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "reevansorgans";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ReevansOrgans(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModCreativeTab.register(modEventBus);

        MarketManager.register();

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // im going to keep this for future reference
//        MarketSaveData marketSaveData = new MarketSaveData();
//        marketSaveData = event.getServer().overworld().getDataStorage().computeIfAbsent(
//                new SavedData.Factory<>(marketSaveData::create, marketSaveData::load), "market");
//
//        LOGGER.info("Server started");
//        LOGGER.info("Market save data: {}", marketSaveData);
//        LOGGER.info("Market prices: {}", marketSaveData.getPrices());
//        LOGGER.info("Market demand: {}", marketSaveData.getDemand());
    }

     // this is useful for testing but not needed
//    @SubscribeEvent
//    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//
//    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            LOGGER.info("Client setup");
        }
    }
}
