package net.reevan.reevansorgans.block.entity.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

public class MarketBlockItemHandler implements IItemHandler {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private final IItemHandler itemHandler;
    private final Direction direction;

    public MarketBlockItemHandler(IItemHandler itemHandler, @Nullable Direction direction) {
        this.itemHandler = itemHandler;
        this.direction = direction;
    }

    @Override
    public int getSlots() {
        return this.itemHandler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.itemHandler.getStackInSlot(i);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack itemStack, boolean simulate) {
        if (this.direction != null && !this.direction.equals(Direction.UP)) {
            return slot == 7 ? this.itemHandler.insertItem(slot, itemStack, simulate) : itemStack;
        } else {
            return slot < 6 ? this.itemHandler.insertItem(slot, itemStack, simulate) : itemStack;
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.direction != null && !this.direction.equals(Direction.UP)) {
            return slot == 8 ? this.itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        } else {
            return slot < 6 ? this.itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int i) {
        return this.itemHandler.getSlotLimit(i);
    }

    @Override
    public boolean isItemValid(int i, ItemStack itemStack) {
        return this.itemHandler.isItemValid(i, itemStack);
    }
}
