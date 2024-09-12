package net.reevan.reevansorgans.block;


import io.netty.resolver.DefaultHostsFileEntriesResolver;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.reevan.reevansorgans.ReevansOrgans;
import net.reevan.reevansorgans.item.ModItems;

import java.util.function.Supplier;

// god help
public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ReevansOrgans.MODID);

    public static final DeferredBlock<Block> MARKET_BLOCK = registerBlock("market_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(1.5f)
                    .lightLevel(state -> 3)
                    .sound(SoundType.METAL)
            ));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
         DeferredBlock<T> toReturn = BLOCKS.register(name, block);
         registerBlockItem(name, toReturn);
         return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
         BLOCKS.register(eventBus);

    }
}
