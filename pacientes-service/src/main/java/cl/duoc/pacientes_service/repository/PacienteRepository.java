package cl.duoc.pacientes_service.repository;

import cl.duoc.pacientes_service.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByRut(String rut);

    List<Paciente> findByApellidosContainingIgnoreCase(String apellidos);

    Optional<Paciente> findByEmail(String email);

    @Query("SELECT p FROM Paciente p WHERE p.fechaNacimiento > :fecha")
    List<Paciente> findPacientesNacidosDespuesDe(@Param("fecha") LocalDate fecha);
}