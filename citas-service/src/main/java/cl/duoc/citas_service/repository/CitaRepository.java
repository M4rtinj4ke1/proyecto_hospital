package cl.duoc.citas_service.repository;

import cl.duoc.citas_service.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByFecha(LocalDate fecha);

    @Query("SELECT c FROM Cita c WHERE c.fecha BETWEEN :inicio AND :fin")
    List<Cita> findCitasEntreFechas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}