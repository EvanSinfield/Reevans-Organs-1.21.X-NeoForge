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

    public int credits = 0;

    public MarketMenu(int containerId, Inventory playerInventory, MarketBlockEntity blockEntity, ContainerData containerData) {
        super((MenuType)ModMenuTypes.MARKET_MENU.get(), containerId);
        this.blockEntity = Objects.requireNonNull(blockEntity);
        this.inventory = blockEntity.getInventory();
        this.containerData = containerData;
        this.level = playerInventory.player.level();
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 108 + X_OFFSET + col * 18, 142));
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 108 + X_OFFSET + col * 18, 84 + row * 18));
            }
        }

        this.addSlot(new SlotItemHandler(this.inventory, 0, 115 + X_OFFSET, 58));
    }

    public ItemStack getOrganSlot() {
        return this.inventory.getStackInSlot(0);
    }

    public MarketMenu(int i, Inventory inventory, RegistryFriendlyByteBuf data) {
        this(i, inventory, getTileEntity(inventory, data), new SimpleContainerData(1));
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 1;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.levelAccess, player, (Block) ModBlocks.MARKET_BLOCK.get());
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

    public void setOrganSlot(ItemStack itemStack) {
        this.inventory.insertItem(0, itemStack, false);
    }

    public void emptyOrganSlot() {
        this.inventory.extractItem(0, 1, false);
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
