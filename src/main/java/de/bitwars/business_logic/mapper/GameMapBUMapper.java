package de.bitwars.business_logic.mapper;

import de.bitwars.business_logic.GameMapLoader;
import de.bitwars.business_logic.moduels.GameMapBU;
import de.bitwars.models.gameMap.dao.GameMapDAO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameMapBUMapper {


    public GameMapBU toGameBU(GameMapDAO gameMapDAO) {
        GameMapBU jsonValue = GameMapLoader.mapJsonToGameMapBU(gameMapDAO.getJsonString());
        jsonValue.setName(gameMapDAO.getName());
        return jsonValue;
    }


}
