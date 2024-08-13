package net.uhb217.pvp_counter.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static KeyBinding openGui;
    public static KeyBinding startCounting;

    public static void registerKeybinds() {
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open Settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "PVP Counter"
        ));
        startCounting = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Start counting crystal explosions",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                "PVP Counter"
        ));
    }
}
