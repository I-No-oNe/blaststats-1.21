package net.uhb217.pvp_counter.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.uhb217.pvp_counter.client.Global;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

    public class VersionUtils implements Global {

        private static String getModVersion() {
            String fullVersionString = FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getVersion().getFriendlyString();
            String[] parts = fullVersionString.split("-");
            for (String part : parts) {
                if (part.matches("\\d+\\.\\d+")) {
                    return part;
                }
            }
            return "Unknown";
        }

        private static String getGithubVersion() {
            String version = "";
            try {
                URL url = new URL("https://api.github.com/repos/uhb217/blaststats-1.21/releases/latest");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JsonObject jsonObject = JsonParser.parseString(content.toString()).getAsJsonObject();
                version = jsonObject.get("tag_name:").getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return version;
        }

        public static boolean isUpdateAvailable() {
            return !getModVersion().equals(getGithubVersion());
        }
    }

