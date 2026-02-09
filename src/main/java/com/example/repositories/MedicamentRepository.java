package repository;

import java.util.List;
import model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
    List<Medicament> findByNameContainingIgnoreCase(String name);
}
