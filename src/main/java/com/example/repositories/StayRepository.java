package repository.stay;

import java.util.List;
import model.Stay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StayRepository extends JpaRepository<Stay, Long> {
    List<Stay> findByDoctorId(Long doctorId);

    List<Stay> findByRoom_Id(Long roomId);
}
