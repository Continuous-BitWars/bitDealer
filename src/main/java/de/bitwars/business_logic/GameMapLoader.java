package de.bitwars.business_logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import de.bitwars.business_logic.moduels.GameMapBU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GameMapLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMapLoader.class);

    public static String fetchJsonFromUrl(String url) {
        LOGGER.info("Fetching JSON from {}", url);
        try (Scanner scanner = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            LOGGER.error("Failed to fetch JSON from {}", url, e);
            return null;
        }
    }

    public static GameMapBU mapJsonToGameMapBU(String json) {
        ObjectMapper mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        try {
            return mapper.readValue(json, GameMapBU.class);
        } catch (Exception e) {
            LOGGER.error("Failed to map JSON to GameMapBU", e);
            return Config.defaultMap;
        }
    }

    public static GameMapBU loadGameMapBUFromUrl(String url) {
        String json = fetchJsonFromUrl(url);
        if (json != null) {
            return mapJsonToGameMapBU(json);
        } else {
            return Config.defaultMap;
        }
    }
}
