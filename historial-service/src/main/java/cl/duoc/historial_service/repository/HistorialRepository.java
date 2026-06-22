package cl.duoc.historial_service.repository;

import cl.duoc.historial_service.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {

    List<Historial> findByPacienteId(Long pacienteId);

    List<Historial> findByMedicoId(Long medicoId);
}