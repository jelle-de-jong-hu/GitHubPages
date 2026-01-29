package nl.hu.s4.project.lingo.data;

import nl.hu.s4.project.lingo.domain.Lingo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Lingo, Long> {
}
