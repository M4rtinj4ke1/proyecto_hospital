package cl.duoc.medicos_service.repository;

import cl.duoc.medicos_service.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByRut(String rut);

    List<Medico> findByEspecialidadId(Long especialidadId);

    List<Medico> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT m FROM Medico m WHERE m.especialidadId = :id ORDER BY m.apellidos ASC")
    List<Medico> findByEspecialidadOrdenado(@Param("id") Long especialidadId);
}