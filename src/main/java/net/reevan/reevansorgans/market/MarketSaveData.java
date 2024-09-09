package net.reevan.reevansorgans.market;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class MarketSaveData extends SavedData {
    // index for each organ are alphabetical. i.e. brain-0, heart-1, etc
    private int[] prices = {50, 50, 50, 50, 50, 50, 50, 50, 50};
    private int[] demand = {0, 0, 0, 0, 0, 0, 0, 0, 0};  // this will be positive if organs are bought, negative if sold

    public MarketSaveData create() {
        return new MarketSaveData();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putIntArray("prices", prices);
        compoundTag.putIntArray("demand", demand);

        return compoundTag;
    }

    public MarketSaveData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        MarketSaveData data = this.create();
        // Load saved data
        data.prices = tag.getIntArray("prices");
        data.demand = tag.getIntArray("demand");

        return data;
    }

    public void setPrices(int[] newPrices) {
        this.prices = newPrices;
        setDirty();
    }

    public void setDemand(int[] demand) {
        this.demand = demand;
        setDirty();
    }

    public void setDemand(int index, int amount) {
        this.demand[index] = amount;
        setDirty();
    }

    public int[] getPrices() {
        return prices;
    }

    public int[] getDemand() {
        return demand;
    }

//    public MarketSaveData getMarketData(ServerLevel world) {
//        return world.getDataStorage().computeIfAbsent(new Factory<>(this::create, this::load), "market");
//    }
}
