package de.bitwars.api.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bitwars.api.models.PubSubMessage;
import de.bitwars.live.GameLiveController;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/live")
@ApplicationScoped
public class GameLiveSocket {
    private static final Logger log = LoggerFactory.getLogger(GameLiveSocket.class);

    @Inject
    GameLiveController gameLiveController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        log.info("onOpen {}", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        this.gameLiveController.unsubscribeAll(session);
        log.info("onClose {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        this.gameLiveController.unsubscribeAll(session);
        log.error("onError {}", session.getId(), throwable);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("onMessage {} {}", session.getId(), message);

        try {
            PubSubMessage pubSubMessage = objectMapper.readValue(message, PubSubMessage.class);
            String pubSubMessageString = pubSubMessage.getMessage().toString();
            log.info("pubSubMessage {} {}: {}", session.getId(), pubSubMessage.getTopic(), pubSubMessageString);

            String[] topic = pubSubMessage.getTopic().split("_");
            if (topic.length == 2 && topic[0].equals("game")) {
                Long gameId = Long.parseLong(topic[1]);

                if (pubSubMessageString.equals("subscribe")) {
                    this.gameLiveController.subscribe(gameId, session);
                } else if (pubSubMessageString.equals("unsubscribe")) {
                    this.gameLiveController.unsubscribe(gameId, session);
                }
            }
        } catch (JsonProcessingException | NumberFormatException e) {
            log.error("onMessage {}", session.getId(), e);
        }
    }
}
