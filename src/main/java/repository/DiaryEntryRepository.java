package repository;

import java.util.List;
import model.DiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {
    List<DiaryEntry> findByMedicalCard_Id(Long medicalCardId);
}
