package de.bitwars.models.gameTick.dao;

import de.bitwars.models.game.dao.GameDAO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "game_tickets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameTickDAO {

    @Id
    @GeneratedValue
    private Long id;
    private int tick;

    @Column(columnDefinition = "TEXT")
    private String gameStateJson;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameDAO game;
}
