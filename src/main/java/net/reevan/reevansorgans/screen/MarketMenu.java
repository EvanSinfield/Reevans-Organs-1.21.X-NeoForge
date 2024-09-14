package net.reevan.reevansorgans.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.reevan.reevansorgans.block.ModBlocks;
import net.reevan.reevansorgans.block.entity.MarketBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MarketMenu extends AbstractContainerMenu {
    public final MarketBlockEntity blockEntity;
    public final ItemStackHandler inventory;
    private final ContainerData containerData;
    private final ContainerLevelAccess levelAccess;
    protected final Level level;
    private final int X_OFFSET = -50;  // its wider than normal so it has an offset

    public MarketMenu(int containerId, Inventory playerInventory, MarketBlockEntity blockEntity, ContainerData containerData) {
        super((MenuType)ModMenuTypes.MARKET_MENU.get(), containerId);
        this.blockEntity = Objects.requireNonNull(blockEntity);
        this.inventory = blockEntity.getInventory();
        this.containerData = containerData;
        this.level = playerInventory.player.level();
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

         this.addSlot(new SlotItemHandler(this.inventory, 0, 115 + X_OFFSET, 58));

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 108 + X_OFFSET + col * 18, 142));
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 108 + X_OFFSET + col * 18, 84 + row * 18));
            }
        }
    }

    public ItemStack getOrganSlot() {
        return this.inventory.getStackInSlot(0);
    }

    public MarketMenu(int i, Inventory inventory, RegistryFriendlyByteBuf data) {
        this(i, inventory, getTileEntity(inventory, data), new SimpleContainerData(1));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;  // TODO: check if this crashes everything
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.levelAccess, player, (Block) ModBlocks.MARKET_BLOCK.get());  // TODO set this up properly
    }

    private static MarketBlockEntity getTileEntity(Inventory playerInventory, FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        BlockEntity tileAtPos = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof MarketBlockEntity) {
            return (MarketBlockEntity) tileAtPos;
        } else {
            throw new IllegalStateException("Tile entity is not correct! " + String.valueOf(tileAtPos));
        }
    }
}
