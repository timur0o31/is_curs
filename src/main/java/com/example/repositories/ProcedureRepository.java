package repository;

import java.util.List;
import model.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedureRepository extends JpaRepository<Procedure, Long> {
    List<Procedure> findByNameContainingIgnoreCase(String name);
}
