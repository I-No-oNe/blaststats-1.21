package net.uhb217.blaststats.utils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static net.uhb217.blaststats.BlastStats.MOD_ID;
import static net.uhb217.blaststats.BlastStats.PREFIX;

public class ModVersionChecker {

    public static boolean checked = false;
    private static final String REPO_URL = "https://github.com/uhb217/blaststats-1.21/releases/latest";
    private static final MinecraftClient client = MinecraftClient.getInstance();
    public static void updateChecker() {
        if (!checked && client.player != null) {
            try {
                String latestVersion = getLatestVersion();
                if (latestVersion != null && isUpdateAvailable(latestVersion)) {
                    client.player.sendMessage(Text.of(PREFIX + "Download the new version of BlastStats from Modrinth!"));
                    Text literal = Text.literal("§a https://modrinth.com/mod/blaststats");
                    ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/mod/blaststats");
                    MutableText text = literal.copy();
                    client.player.sendMessage(text.fillStyle(text.getStyle().withClickEvent(event)));
                }
                checked = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static String getLatestVersion() throws Exception {
        URL url = new URL(REPO_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        conn.disconnect();

        String jsonString = sb.toString();

        String versionTag = "\"tag_name\":\"";
        int startIndex = jsonString.indexOf(versionTag);
        if (startIndex != -1) {
            int endIndex = jsonString.indexOf('"', startIndex + versionTag.length());
            if (endIndex != -1) {
                return jsonString.substring(startIndex + versionTag.length(), endIndex);
            }
        }

        return null;
    }
    public static boolean isUpdateAvailable(String latestVersion) {
        return !getModVersion().equals(latestVersion);
    }
    public static String getModVersion() {
        String fullVersionString = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
        String[] parts = fullVersionString.split("-");
        for (String part : parts) {
            if (part.matches("\\d+\\.\\d+")) {
                return part;
            }
        }
        return "Unknown";
    }
}