package net.uhb217.pvp_counter.client;

import net.minecraft.client.MinecraftClient;

public interface Global {
MinecraftClient mc = MinecraftClient.getInstance();
String modId = "pvp-counter";
String PREFIX = "§6PVP-Counter: §r";
}
