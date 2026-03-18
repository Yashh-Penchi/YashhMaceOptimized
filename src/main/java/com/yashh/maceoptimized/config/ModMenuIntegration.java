package com.yashh.maceoptimized.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("§6§lYashh MaceOptimized §r§7Settings"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // Single category — sab kuch ek hi page pe vertical
            ConfigCategory all = builder.getOrCreateCategory(Text.literal("Settings"));

            // --- GENERAL ---
            all.addEntry(entryBuilder.startTextDescription(Text.literal("§e§l— General —")).build());

            all.addEntry(entryBuilder.startBooleanToggle(Text.literal("Master Toggle"), ModConfig.masterToggle)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Enable/Disable entire mod"))
                    .setSaveConsumer(v -> ModConfig.masterToggle = v)
                    .build());

            all.addEntry(entryBuilder.startIntSlider(Text.literal("Revert Delay (Ticks)"), ModConfig.revertDelayTicks, 1, 20)
                    .setDefaultValue(3)
                    .setTooltip(Text.literal("How many ticks before swapping back to original slot"))
                    .setSaveConsumer(v -> ModConfig.revertDelayTicks = v)
                    .build());

            // --- MACE SWAP ---
            all.addEntry(entryBuilder.startTextDescription(Text.literal("§b§l— Mace Swap —")).build());

            all.addEntry(entryBuilder.startBooleanToggle(Text.literal("Density Swap"), ModConfig.densityEnabled)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Auto swap to Density Mace when falling"))
                    .setSaveConsumer(v -> ModConfig.densityEnabled = v)
                    .build());

            all.addEntry(entryBuilder.startBooleanToggle(Text.literal("Breach Swap"), ModConfig.breachEnabled)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Auto swap to Breach Mace when target has armor"))
                    .setSaveConsumer(v -> ModConfig.breachEnabled = v)
                    .build());

            // --- PEARL CATCH ---
            all.addEntry(entryBuilder.startTextDescription(Text.literal("§d§l— Pearl Catch —")).build());

            all.addEntry(entryBuilder.startBooleanToggle(Text.literal("Pearl Catch Enabled"), ModConfig.pearlCatchEnabled)
                    .setDefaultValue(true)
                    .setTooltip(Text.literal("Auto swap to Wind Charge after throwing Ender Pearl"))
                    .setSaveConsumer(v -> ModConfig.pearlCatchEnabled = v)
                    .build());

            all.addEntry(entryBuilder.startBooleanToggle(Text.literal("Auto Pearl Catch"), ModConfig.autoPearlCatch)
                    .setDefaultValue(false)
                    .setTooltip(Text.literal("OFF = swap only, you click manually | ON = auto use Wind Charge after delay"))
                    .setSaveConsumer(v -> ModConfig.autoPearlCatch = v)
                    .build());

            all.addEntry(entryBuilder.startIntSlider(Text.literal("Auto Catch Delay (Ticks)"), ModConfig.autoPearlCatchDelay - 2, 1, 7)
                    .setDefaultValue(2)
                    .setTooltip(Text.literal("Ticks after swap before auto using Wind Charge\nRange: 1-7"))
                    .setSaveConsumer(v -> ModConfig.autoPearlCatchDelay = v + 2)
                    .build());

            return builder.build();
        };
    }
}
