package repository;

import java.util.List;
import model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByDoctorId(Long doctorId);

    List<Session> findByProcedure_Id(Long procedureId);
}
