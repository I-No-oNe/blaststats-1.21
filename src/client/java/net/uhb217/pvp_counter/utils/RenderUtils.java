package net.uhb217.pvp_counter.utils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.uhb217.pvp_counter.config.ConfigData;
import net.uhb217.pvp_counter.config.ConfigFile;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;
import static net.uhb217.pvp_counter.utils.HudUtils.*;

public class RenderUtils {
    private static long lastTime = 0;
    private static int sec = 0;

    public static void animatedStartText(DrawContext ctx) {
        int color = 0;
        if (lastTime + 1000 <= System.currentTimeMillis()) {
            sec = lastTime == 0 ? 0 : sec + 1;
            lastTime = System.currentTimeMillis();
        }
        Text t;
        switch (sec) {
            case 0:
                color = 0xcc612b;
                break;
            case 1:
                color = 0xd4b51c;
                break;
            case 2:
                color = 0xa9d41c;
                break;
            case 3:
                t = Text.literal("Go");
                drawScaledText(ctx, t, centeredX(t, ctx, 4.0f), centeredY(4.0f, ctx), 0x17f013, 4.0f, true);
                return;
            case 4:
                ConfigFile.getConfig().modState = ConfigData.Modes.ON; // Assign mode
                NbtCompound nbt = ((NBTConfigUtils) mc.player).BlastStats$getPersistentData();
                if (nbt != null && nbt.contains("crystal_explosions")) {
                    nbt.putInt("crystal_explosions", 0);
                }
                return;
        }
        t = Text.literal("Start in: " + (3 - sec));
        drawScaledText(ctx, t, centeredX(t, ctx, 3.0f), centeredY(3.0f, ctx), color, 3.0f, true);
    }

    public static void initAnimation() {
        lastTime = 0;
        sec = 0;
        ConfigFile.getConfig().modState = ConfigData.Modes.REVERSE; // Assign mode
    }

    public static void endCount() {
        ConfigFile.getConfig().modState = ConfigData.Modes.OFF; // Assign mode
        NbtCompound nbt = ((NBTConfigUtils) mc.player).BlastStats$getPersistentData();
        int explosions = (nbt != null && nbt.contains("crystal_explosions")) ? nbt.getInt("crystal_explosions") : 0;
        float perSec = BigDecimal.valueOf((double) explosions / (sec - 4)).setScale(2, HALF_UP).floatValue();
        mc.player.sendMessage(Text.literal(PREFIX + "§5Total amount of explosions: " + explosions
                + "\n               Explosions per second: " + perSec), false);
    }

    private static float centeredX(Text text, DrawContext ctx, float scale) {
        return (ctx.getScaledWindowWidth() - mc.textRenderer.getWidth(text) * scale) / 2;
    }

    private static float centeredY(float scale, DrawContext ctx) {
        return (ctx.getScaledWindowHeight() - mc.textRenderer.fontHeight * scale) / 2;
    }

    public static void drawScaledText(@NotNull DrawContext ctx, Text text, float x, float y, int color, float scale, boolean shadow) {
        MatrixStack matrixStack = ctx.getMatrices();
        matrixStack.push();
        matrixStack.scale(scale, scale, 1.0f);
        mc.textRenderer.draw(text, x / scale, y / scale, color, shadow, ctx.getMatrices().peek().getPositionMatrix(), ctx.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        matrixStack.pop();
    }
}
