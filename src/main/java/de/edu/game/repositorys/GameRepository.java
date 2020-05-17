package de.edu.game.repositorys;

import de.edu.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    default Game getTheGame() {
        return this.findAll().get(0);
    }

}
