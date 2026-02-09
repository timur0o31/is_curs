package repository;

import java.util.List;
import model.DiningTable;
import model.Diet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
    List<DiningTable> findByDiet(Diet diet);
}
