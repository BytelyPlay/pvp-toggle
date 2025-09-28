package org.bytelyplay.pvptoggle.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

public class Messages {
    public static Component ATTACK_DENIED = Component
            .literal("You can't attack this player because either one of your PVPs are disabled.")
            .withColor(CommonColors.RED);
    public static Component ONLY_PLAYERS_CAN_EXECUTE = Component
            .literal("Only players can execute this command.")
            .withColor(CommonColors.YELLOW);
    public static Component ENABLED_PVP = Component
            .literal("Enabled PVP")
            .withColor(CommonColors.RED);
    public static Component DISABLED_PVP = Component
            .literal("Disabled PVP")
            .withColor(CommonColors.RED);
}
