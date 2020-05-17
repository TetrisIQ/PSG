package de.edu.game.repositorys;

import de.edu.game.model.Map;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapRepository extends JpaRepository<Map, Integer> {
    default Map getTheMap() {
        return this.findAll().get(0);
    }

}
