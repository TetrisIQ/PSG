package de.edu.game.repositorys;

import de.edu.game.model.AbstractMeeple;
import de.edu.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JpaRepository for the {@link Game}
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    /**
     * Get the only existing Game
     * @return The {@link Game}
     */
    default Game getTheGame() {
        return this.findAll().get(0);
    }

}
