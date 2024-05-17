package de.bitwars.games;

import de.bitwars.games.moduels.GameMapBU;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class MapController {

    private static final Logger log = LoggerFactory.getLogger(MapController.class);

    public GameMapBU loadFromUrl(String Url) {
        log.info("Loading Map from {}", Url);
        return Config.defaultMap;
    }
}
