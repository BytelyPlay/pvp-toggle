package org.bytelyplay.pvptoggle.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

public class Messages {
    public static Component ATTACK_DENIED = Component
            .literal("You can't attack this player because either one of your PVPs are disabled.")
            .withColor(CommonColors.RED);
}
