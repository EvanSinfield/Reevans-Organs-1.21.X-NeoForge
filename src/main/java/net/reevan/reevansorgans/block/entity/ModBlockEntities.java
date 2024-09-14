package net.reevan.reevansorgans.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.reevan.reevansorgans.ReevansOrgans;
import net.reevan.reevansorgans.block.ModBlocks;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ReevansOrgans.MODID);

    public static final Supplier<BlockEntityType<MarketBlockEntity>> MARKET_BE =
            BLOCK_ENTITIES.register("market_be", () -> BlockEntityType.Builder.of(
                    MarketBlockEntity::new, ModBlocks.MARKET_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
