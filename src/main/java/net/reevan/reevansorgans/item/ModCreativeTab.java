package net.reevan.reevansorgans.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.reevan.reevansorgans.ReevansOrgans;
import net.reevan.reevansorgans.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB, ReevansOrgans.MODID);

    public static final Supplier<CreativeModeTab> ORGANS_TAB = CREATIVE_MODE_TAB.register("organs_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BRAIN.get()))
                    .title(Component.translatable("creativetab.organs_tab"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.BRAIN);
                        output.accept(ModItems.HEART);
                        output.accept(ModItems.INTESTINES);
                        output.accept(ModItems.KIDNEY);
                        output.accept(ModItems.LIVER);
                        output.accept(ModItems.LUNGS);
                        output.accept(ModItems.PANCREAS);
                        output.accept(ModItems.STOMACH);
                        output.accept(ModItems.THYROID);
                        output.accept(ModBlocks.MARKET_BLOCK);
                    })).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
