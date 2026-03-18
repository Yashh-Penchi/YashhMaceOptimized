package com.yashh.maceoptimized;
import com.yashh.maceoptimized.combat.MaceSwapHandler;
//import com.yashh.maceoptimized.combat.DensitySwapHandler;
import com.yashh.maceoptimized.combat.PearlCatchHandler;
//import com.yashh.maceoptimized.hud.IndicatorRenderer;
import com.yashh.maceoptimized.keybind.KeybindManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class MaceOptimizedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeybindManager.register();
//        IndicatorRenderer.register();

        // Inhe register karna zaroori hai
        MaceSwapHandler.register();
        PearlCatchHandler.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                KeybindManager.handleInputs();
            }
        });
    }
}