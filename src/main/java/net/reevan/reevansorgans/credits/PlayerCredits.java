package net.reevan.reevansorgans.credits;

import net.minecraft.server.level.ServerPlayer;

public class PlayerCredits {
    // TBH alot of this is redundant but im going to keep it for the sake of tidiness and its more readable

    public static void savePlayerCredits(ServerPlayer player, int credits) {
        player.getPersistentData().putInt("credits", credits);
    }

    public static int loadPlayerCredits(ServerPlayer player) {
        return player.getPersistentData().getInt("credits");
    }

    public static void addCredits(ServerPlayer player, int credits) {
        int balance = loadPlayerCredits(player);
        savePlayerCredits(player, balance + credits);
    }

    public static void removeCredits(ServerPlayer player, int credits) {
        int balance = loadPlayerCredits(player);
        savePlayerCredits(player, balance - credits);
    }
}
