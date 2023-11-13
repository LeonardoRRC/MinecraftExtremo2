package me.meowquantum.com.mineextermo2.Managers;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UUIDFetcher {

    public static UUID getPremiumUUID(String playerName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Si la respuesta es OK (HTTP 200), entonces el jugador tiene una UUID premium.
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonElement element = JsonParser.parseReader(reader);
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    String id = object.get("id").getAsString();
                    return UUID.fromString(
                            id.replaceFirst(
                                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                                    "$1-$2-$3-$4-$5"
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra la UUID premium, devuelve null.
    }

    public static UUID getOfflineUUID(String playerName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
    }
}
