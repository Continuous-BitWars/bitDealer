package de.bitwars.live;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bitwars.api.models.PubSubMessage;
import de.bitwars.api.models.clients.Board;
import de.bitwars.models.game.mapper.GameBUMapper;
import de.bitwars.models.game.moduels.GameBU;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GameLiveController {

    @Inject
    GameBUMapper gameBUMapper;

    private static final Logger log = LoggerFactory.getLogger(GameLiveController.class);
    private final Map<Long, List<Session>> sessions = Collections.synchronizedMap(new HashMap<>());
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void subscribe(Long gameId, Session session) {
        if (!this.sessions.containsKey(gameId)) {
            this.sessions.put(gameId, new ArrayList<>());
        }
        if (!this.sessions.get(gameId).contains(session)) {
            this.sessions.get(gameId).add(session);
        }
        sendSuccessToSession(gameId, session);
    }

    public void unsubscribe(Long gameId, Session session) {
        if (sessions.containsKey(gameId)) {
            sessions.get(gameId).remove(session);
        }
        sendSuccessToSession(gameId, session);
    }

    public void unsubscribeAll(Session session) {
        this.sessions.values().forEach(sessions1 -> sessions1.remove(session));
        log.debug("Unsubscribing all sessions: {}", session.getId());
    }

    public void broadcastToTopic(long gameId, String message) {
        if (sessions.get(gameId) != null) {
            sessions.get(gameId).forEach(s -> {
                s.getAsyncRemote().sendObject(message, result -> {
                    if (result.getException() != null) {
                        log.error("Unable to send message: " + result.getException());
                    }
                });
            });
        } else {
            log.debug("No session found for gameId: {}", gameId);
        }
    }

    public void broadcastGameStep(GameBU gameBU) {
        try {
            Board board = this.gameBUMapper.toBoard(gameBU, 0);
            PubSubMessage pubSubMessage = new PubSubMessage("game_" + gameBU.getId(), board);
            String message = objectMapper.writeValueAsString(pubSubMessage);

            this.broadcastToTopic(gameBU.getId(), message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendSuccessToSession(Long gameId, Session session) {
        return;
        /*
        int count = Optional.ofNullable(this.sessions.get(gameId)).orElse(Collections.emptyList()).size();
        log.info("topic info {}: {}", gameId, count);

        PubSubMessage response = new PubSubMessage("game_" + gameId, "success");
        try {
            session.getAsyncRemote().sendObject(this.objectMapper.writeValueAsString(response), result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Unable to send message: " + e.getMessage());
        }
        */
    }
}
