package net.uhb217.pvp_counter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.uhb217.pvp_counter.client.Global;
import net.uhb217.pvp_counter.client.Keybinds;
import net.uhb217.pvp_counter.config.ConfigFile;
import net.uhb217.pvp_counter.config.ConfigScreen;
import net.uhb217.pvp_counter.events.CrystalExplodeEvent;
import net.uhb217.pvp_counter.utils.HudUtils;
import net.uhb217.pvp_counter.utils.RenderUtils;

public class PVPCounterClient implements ClientModInitializer, Global {
	public static boolean firstPress = false;

	@Override
	public void onInitializeClient() {
		/*Loading Config*/
		ConfigFile.loadConfig();
		/*Loading Keybinds*/
		Keybinds.registerKeybinds();
		/*Loading Events*/
		CrystalExplodeEvent.EVENT.register(((world, player) -> ActionResult.PASS));
		HudRenderCallback.EVENT.register((ctx, tickDelta) -> HudUtils.screenLogic(ctx));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (Keybinds.startCounting.wasPressed() && !firstPress && mc.world != null) {
				firstPress = true;
				switch (ConfigFile.getConfig().modState) {
					case OFF:
						RenderUtils.initAnimation();
						firstPress = false;
						break;
					case REVERSE:
						mc.player.sendMessage(Text.literal(PREFIX + "Count has been canceled").formatted(Formatting.DARK_RED));
						break;
					case ON:
						RenderUtils.endCount();
						break;
				}
			}
//			idk why but u need to open the gui via the mod menu and then close it and then press it ingame
			if (Keybinds.openGui.isPressed() && mc != null){
				mc.setScreen(ConfigScreen.configScreen);
			}
		});
	}
}
