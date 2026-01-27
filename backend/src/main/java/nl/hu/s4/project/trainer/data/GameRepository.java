package nl.hu.s4.project.trainer.data;

import nl.hu.s4.project.trainer.domain.LingoTrainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<LingoTrainer, Long> {
}
