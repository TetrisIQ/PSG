package de.edu.game.repositorys;

import de.edu.game.model.AbstractMeeple;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeepleRepository extends JpaRepository<AbstractMeeple, Integer> {
}
