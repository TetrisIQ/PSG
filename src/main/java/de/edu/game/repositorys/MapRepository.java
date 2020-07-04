package de.edu.game.repositorys;

import de.edu.game.model.Map;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository for the {@link Map}
 */
public interface MapRepository extends JpaRepository<Map, Integer> {

    /**
     * Get the only existing {@link Map}
     *
     * @return returns the only existing @{@link Map}
     */
    default Map getTheMap() {
        return this.findAll().get(0);
    }

}
