package com.yashh.maceoptimized.combat;

import com.yashh.maceoptimized.config.ModConfig;
import com.yashh.maceoptimized.util.ChatUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class MaceSwapHandler {
    private static int revertTimer = -1;
    private static int originalSlot = -1;
    private static boolean isSwapping = false;

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!ModConfig.masterToggle || !world.isClient) return ActionResult.PASS;
            if (hand != Hand.MAIN_HAND || !(entity instanceof LivingEntity) || isSwapping) return ActionResult.PASS;

            LivingEntity target = (LivingEntity) entity;

            boolean targetHasArmor = false;
            for (EquipmentSlot slot : new EquipmentSlot[]{
                    EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                    EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                if (!target.getEquippedStack(slot).isEmpty()) {
                    targetHasArmor = true;
                    break;
                }
            }

            boolean isFalling = (player.fallDistance >= 1.5f && player.getVelocity().y < 0);

            ItemStack held = player.getMainHandStack();
            boolean holdingWeapon = held.getItem() instanceof AxeItem
                    || held.isOf(Items.WOODEN_SWORD)
                    || held.isOf(Items.STONE_SWORD)
                    || held.isOf(Items.IRON_SWORD)
                    || held.isOf(Items.GOLDEN_SWORD)
                    || held.isOf(Items.DIAMOND_SWORD)
                    || held.isOf(Items.NETHERITE_SWORD);
            if (!holdingWeapon) return ActionResult.PASS;

            int foundSlot = -1;
            int fallbackSlot = -1;

            for (int i = 0; i < 9; i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (!stack.isOf(Items.MACE)) continue;
                if (fallbackSlot < 0) fallbackSlot = i;
                if (isFalling && ModConfig.densityEnabled) {
                    if (getEnchantmentLevel(stack, "density") > 0) { foundSlot = i; break; }
                } else if (targetHasArmor && ModConfig.breachEnabled) {
                    if (getEnchantmentLevel(stack, "breach") > 0) { foundSlot = i; break; }
                }
            }

            int finalSlot = isFalling
                    ? (foundSlot != -1 ? foundSlot : fallbackSlot)
                    : foundSlot;

            if (finalSlot == -1 || player.getInventory().getSelectedSlot() == finalSlot) return ActionResult.PASS;

            originalSlot = player.getInventory().getSelectedSlot();
            isSwapping = true;
            player.getInventory().setSelectedSlot(finalSlot);
            if (MinecraftClient.getInstance().getNetworkHandler() != null) {
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(
                        new UpdateSelectedSlotC2SPacket(finalSlot));
            }

            // Chat notification
            if (isFalling && foundSlot != -1) {
                ChatUtils.sendDensitySwap();
            } else if (isFalling) {
                ChatUtils.sendMaceSwap();
            } else if (targetHasArmor) {
                ChatUtils.sendBreachSwap();
            }

            revertTimer = ModConfig.revertDelayTicks + 1;
            return ActionResult.PASS;
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            if (revertTimer > 0) {
                revertTimer--;
            } else if (revertTimer == 0 && originalSlot != -1) {
                client.player.getInventory().setSelectedSlot(originalSlot);
                if (client.getNetworkHandler() != null) {
                    client.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(originalSlot));
                }
                originalSlot = -1;
                revertTimer = -1;
                isSwapping = false;
            }
        });
    }

    private static int getEnchantmentLevel(ItemStack stack, String enchantName) {
        if (stack == null || stack.isEmpty()) return 0;
        ItemEnchantmentsComponent enchants = stack.get(DataComponentTypes.ENCHANTMENTS);
        if (enchants == null) return 0;
        for (RegistryEntry<Enchantment> entry : enchants.getEnchantments()) {
            if (entry.getIdAsString().toLowerCase().contains(enchantName.toLowerCase())) {
                return enchants.getLevel(entry);
            }
        }
        return 0;
    }
}
