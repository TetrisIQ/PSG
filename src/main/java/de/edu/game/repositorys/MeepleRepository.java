package de.edu.game.repositorys;

import de.edu.game.model.AbstractMeeple;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository for {@link AbstractMeeple}s
 */
public interface MeepleRepository extends JpaRepository<AbstractMeeple, Integer> {
}
