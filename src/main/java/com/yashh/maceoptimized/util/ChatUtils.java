package com.yashh.maceoptimized.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatUtils {

    public static void sendActionBar(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), true);
        }
    }

    public static void sendChat(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), false);
        }
    }

    public static void sendDensitySwap() {
        sendChat("§6§l[§eYASHH MACE§6§l] §c§lDensity §7swap triggered! §i§7(§8Free Version§7)");
        sendChat(
                "§8 §7[§dPremium Version§7] §8§o\"Elytra Mace Assist, Wind Charge and Elytra Boost, Stun Slam Shield, Higher CPS MaceSpam for break Shield Durability, 360° anywhere PearlCatching, Resourcepack and Custom animations for Density and Breach with sound effects and much other...\"");
    }

    public static void sendBreachSwap() {
        sendChat("§6§l[§eYASHH MACE§6§l] §b§lBreach §7swap triggered! §i§7(§8Free Version§7)");
        sendChat("§7(§8upgrade to §dPremium §8for Elytra Mace Assist, 360° PearlCatch and more Advance feature§o!§r)");
    }

    public static void sendPearlCatch() {
        sendChat("§6§l[§eYASHH MACE§6§l] §t§lPearlCatch §7activated! §i§7(§8Free Version§7)");
        sendChat("§7(§8upgrade to §dPremium §8for Auto Elytra, 360° PearlCatch and more Advance feature§o!§r)");
    }

    public static void sendMaceSwap() {
        sendChat("§6§l[§eYASHH MACE§6§l] §q§lMace §7swap triggered! §i§7(§8Free Version§7)");
        sendChat("§7(§8upgrade to §dPremium §8for Elytra Mace Assist, 360° PearlCatch and more Advance feature§o!§r)");
    }

    // fallback generic
    public static void sendPrefixed(String message) {
        sendChat("§6§l[§eYASHH MACE§6§l] §r" + message);
    }
}
