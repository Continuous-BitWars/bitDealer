package de.bitwars.games.moduels.player;

import de.bitwars.api.models.clients.Board;
import de.bitwars.api.models.clients.PlayerAction;
import de.bitwars.games.mapper.GameBUMapper;
import de.bitwars.games.moduels.ActionProvider;
import de.bitwars.games.moduels.GameBU;
import de.bitwars.games.moduels.PlayerActionBU;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RemotePlayer implements ActionProvider {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(1);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(1);
    private static final Logger log = LoggerFactory.getLogger(RemotePlayer.class);

    private final long id;
    private final String name;
    private final String url;
    private final String color;

    GameBUMapper gameBUMapper;

    public RemotePlayer(long id, String name, String url, String color, GameBUMapper gameBUMapper) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.color = color;
        this.gameBUMapper = gameBUMapper;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public List<PlayerActionBU> requestStep(GameBU gameBU) {
        log.debug("Send Request to Player {} {}", getId(), getUrl());

        try (var client = createClient()) {
            Board board = this.gameBUMapper.toBoard(gameBU, this.id);

            final List<PlayerAction> response = client.getPlayerActionForBoard(board);

            return response.stream().map(playerAction -> gameBUMapper.toPlayerActionBU(playerAction)).toList();
        } catch (final Exception e) {
            log.error("Error while requesting bet from player {}", url, e);
        }
        return List.of();
    }


    private RemotePlayerClient createClient() {
        return RestClientBuilder.newBuilder()
                .baseUri(URI.create(this.url))
                .connectTimeout(CONNECT_TIMEOUT.getSeconds(), TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT.getSeconds(), TimeUnit.SECONDS)
                .build(RemotePlayerClient.class);
    }
}
