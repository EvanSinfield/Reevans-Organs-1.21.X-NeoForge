package net.reevan.reevansorgans.item;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.reevan.reevansorgans.ReevansOrgans;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ReevansOrgans.MODID);

    public static final DeferredItem<Item> BRAIN = ITEMS.register("brain",
            () -> new Item(new Item.Properties().durability(100)));

    public static final DeferredItem<Item> HEART = ITEMS.register("heart",
            () -> new Item(new Item.Properties().durability(100)));

    public static final DeferredItem<Item> INTESTINES = ITEMS.register("intestines",
            () -> new Item(new Item.Properties().durability(100)));

    public static final DeferredItem<Item> KIDNEY = ITEMS.register("kidney",
            () -> new Item(new Item.Properties().durability(100)));

    public static final DeferredItem<Item> LIVER = ITEMS.register("liver",
            () -> new Item(new Item.Properties().durability(100)));

    public static final DeferredItem<Item> LUNGS = ITEMS.register("lungs",
            () -> new Item(new Item.Properties().durability(100)));

    public static final DeferredItem<Item> PANCREAS = ITEMS.register("pancreas",
            () -> new Item(new Item.Properties().durability(100)));

    public static final DeferredItem<Item> STOMACH = ITEMS.register("stomach",
            () -> new Item(new Item.Properties().durability(100)));

    public static final DeferredItem<Item> THYROID = ITEMS.register("thyroid",
            () -> new Item(new Item.Properties().durability(100)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
