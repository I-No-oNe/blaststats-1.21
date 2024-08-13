package net.uhb217.pvp_counter.utils;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uhb217.pvp_counter.client.Global;
import net.uhb217.pvp_counter.config.ConfigData;
import net.uhb217.pvp_counter.config.ConfigFile;

public class HudUtils implements Global {
    public static long lastTime = 0;
    public static int sec = 3;
    public static NbtCompound nbt;

    public static void screenLogic(DrawContext ctx) {
        if (mc.currentScreen == null && mc.player != null) {
            if (ConfigFile.getConfig().modState == ConfigData.Modes.REVERSE) {
                RenderUtils.animatedStartText(ctx);
            } else if (ConfigFile.getConfig().modState == ConfigData.Modes.ON) {
                if (lastTime + 1000 <= System.currentTimeMillis()) {
                    sec++;
                    lastTime = System.currentTimeMillis();
                }
                //draw the amount of the explosions
                nbt = ((NBTConfigUtils)mc.player).BlastStats$getPersistentData();
                if (nbt != null) {
                    if (nbt.contains("crystal_explosions")) {
                        ctx.drawTexture(Identifier.ofVanilla("textures/item/end_crystal.png"), 5, 5, 0, 0, 16, 16, 16, 16);
                        RenderUtils.drawScaledText(ctx, Text.literal(": " + nbt.getInt("crystal_explosions")), 23, 5, 0xfc03f8, 2.0f, true);
                    }
                }
            }
        }
    }
}