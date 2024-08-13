package net.uhb217.pvp_counter.client;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.uhb217.pvp_counter.utils.VersionUtils;


public class Version implements Global {

    static boolean isChecked = false;

    public static void updateChecker() {
        try {
            if (!isChecked &  mc.player != null) {
                if (VersionUtils.isUpdateAvailable()) {
                    mc.player.sendMessage(Text.of(PREFIX + "Download the new version of Auto Disconnect from Modrinth!"));
                    Text literal = Text.literal("§b https://modrinth.com/mod/auto-disconnect");
                    ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/auto-disconnect");
                    MutableText text = literal.copy();
                    Text chatMessage = (text.fillStyle(text.getStyle().withClickEvent(event)));
                    mc.player.sendMessage(chatMessage, false);
                    isChecked = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}