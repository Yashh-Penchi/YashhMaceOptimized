package com.yashh.maceoptimized.keybind;

import com.yashh.maceoptimized.config.ModConfig;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeybindManager {

    public static KeyBinding masterToggleKey;

    public static void register() {
        masterToggleKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Toggle Yashh MaceOptimized",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_K,
                        "Yashh MaceOptimized"
                )
        );
    }

    public static void handleInputs() {
        while (masterToggleKey.wasPressed()) {
            ModConfig.masterToggle = !ModConfig.masterToggle;
            Text statusText = ModConfig.masterToggle
                    ? Text.literal("§6MaceOptimized §aEnable")
                    : Text.literal("§6MaceOptimized §cDisabled");
            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.sendMessage(statusText, true);
            }
        }
    }
}
