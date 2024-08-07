package net.uhb217.blaststats;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.uhb217.blaststats.events.CrystalExplodeEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;
import static net.uhb217.blaststats.BlastStats.PREFIX;
import static net.uhb217.blaststats.BlastStatsModClient.Action.*;

public class BlastStatsModClient implements ClientModInitializer {
	public long lastTime = 0;
	private int sec = 3;
	public static Action action = IDLE;
	@Override
	public void onInitializeClient() {
		registerKeyBindings();
		CrystalExplodeEvent.EVENT.register(((world, player) -> {
			NbtCompound nbt = ((NBTConfigUtils) player).BlastStats$getPersistentData();
			if (nbt != null) {
				if (!nbt.contains("crystal_explosions"))
					nbt.putInt("crystal_explosions", 1);
				else
					nbt.putInt("crystal_explosions", nbt.getInt("crystal_explosions") + 1);
			}
			return ActionResult.PASS;
		}));
		HudRenderCallback.EVENT.register((ctx, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.currentScreen == null && client.player != null) {
				if (action == ANIMATE)
					animatedStartText(ctx);
				else if (action == COUNT) {
					//count seconds
					if (lastTime + 1000 <= System.currentTimeMillis()) {
						sec++;
						lastTime = System.currentTimeMillis();
					}
					//draw the amount of the explosions
					NbtCompound nbt = ((NBTConfigUtils) client.player).BlastStats$getPersistentData();
					if (nbt != null) {
						if (nbt.contains("crystal_explosions")) {
							ctx.drawTexture(Identifier.ofVanilla("textures/item/end_crystal.png"), 5, 5, 0, 0, 16, 16, 16, 16);
							drawScaledText(ctx, Text.literal(": " + nbt.getInt("crystal_explosions")), 23, 5, 0xfc03f8, 2.0f, true);
						}
					}
				}
			}
		});
	}
	private void animatedStartText(DrawContext ctx){
		int color = 0;
		if (lastTime + 1000 <= System.currentTimeMillis()) {
			sec = lastTime == 0? 0 : sec + 1;
			lastTime = System.currentTimeMillis();
		}
		switch (sec) {
			case 0: color = 0xcc612b;break;
			case 1: color = 0xd4b51c;break;
			case 2: color = 0xa9d41c;break;
			case 3: Text t = Text.literal("Go");drawScaledText(ctx, t, centeredX(t, ctx, 4.0f), centeredY(4.0f, ctx), 0x17f013, 4.0f, true);return;
			case 4: action = COUNT;NbtCompound nbt = ((NBTConfigUtils) MinecraftClient.getInstance().player).BlastStats$getPersistentData();if (nbt != null && nbt.contains("crystal_explosions")) nbt.putInt("crystal_explosions", 0);return;
		}
		Text t = Text.literal("Start in: " +(3-sec));
		drawScaledText(ctx, t, centeredX(t, ctx, 3.0f), centeredY(3.0f, ctx), color, 3.0f, true);
	}
	private void initAnimation() {
		lastTime = 0;
		sec = 0;
		action = ANIMATE;
	}
	private void endCount() {
		action = IDLE;
		NbtCompound nbt = ((NBTConfigUtils) MinecraftClient.getInstance().player).BlastStats$getPersistentData();
		int explosions = nbt != null && nbt.contains("crystal_explosions")? nbt.getInt("crystal_explosions") : 0;
		float perSec = BigDecimal.valueOf((double) explosions/(sec-4)).setScale(2,HALF_UP).floatValue();
		MinecraftClient.getInstance().player.sendMessage(Text.literal(PREFIX + "§5Total amount of explosions: " + explosions
		+ "\n               Explosions per second: " + perSec), false);
	}
	private float centeredX(Text text, DrawContext ctx, float scale){return (ctx.getScaledWindowWidth() - MinecraftClient.getInstance().textRenderer.getWidth(text) * scale) /2;}
	private float centeredY(float scale, DrawContext ctx) {return (ctx.getScaledWindowHeight() - MinecraftClient.getInstance().textRenderer.fontHeight * scale) / 2;}
	private void drawScaledText(@NotNull DrawContext ctx, Text text, float x, float y, int color, float scale, boolean shadow) {
		MatrixStack matrixStack = ctx.getMatrices();
		matrixStack.push();
		matrixStack.scale(scale, scale, 1.0f);
		MinecraftClient.getInstance().textRenderer.draw(text,x/scale,y/scale,color,shadow, ctx.getMatrices().peek().getPositionMatrix(),ctx.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
		matrixStack.pop();
	}
	private void registerKeyBindings() {
//		KeyBinding openLSScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
//				"Open the player tracker screen",
//				InputUtil.Type.KEYSYM,
//				GLFW.GLFW_KEY_R,
//				"Player Tracker Keys"
//		));
		KeyBinding startCounting = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Start counting crystal explosions",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				"Blast Stats Keys"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
//			if (openLSScreenKey.wasPressed() && client.world != null)
//				client.setScreen(new MyScreen(Text.literal("Hello").formatted(Formatting.DARK_AQUA)));
			 if (startCounting.wasPressed() && client.world != null) {
				switch (action){
					case IDLE: initAnimation();break;
					case ANIMATE: action = IDLE;MinecraftClient.getInstance().player.sendMessage(Text.literal(PREFIX + "Count has bin canceled").formatted(Formatting.DARK_RED));break;
					case COUNT: if(sec != 0)endCount();break;
				}
			}
		});
	}
	public enum Action {IDLE, ANIMATE, COUNT}
}