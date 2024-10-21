package de.bitwars.models.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import de.bitwars.models.game.moduels.GameMapBU;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

@ApplicationScoped
public class MapController {

    private static final Logger log = LoggerFactory.getLogger(MapController.class);

    public GameMapBU loadFromUrl(String url) {
        log.info("Loading Map from {}", url);
        ObjectMapper mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        try {
            return mapper.readValue(new URL(url), GameMapBU.class);
        } catch (Exception e) {
            log.error("Failed to load Map from {}", url, e);
        }
        return Config.defaultMap;
    }
}
