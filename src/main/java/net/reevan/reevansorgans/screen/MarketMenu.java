package net.reevan.reevansorgans.screen;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MarketMenu extends AbstractContainerMenu {
    public MarketMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    public MarketMenu(int containerId, Inventory inventory) {
        super(ModMenuTypes.MARKET_MENU.get(), containerId);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;  // TODO set this up properly
    }
}
