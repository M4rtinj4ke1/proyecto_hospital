package cl.duoc.historial_service;

import cl.duoc.historial_service.clients.MedicoFeign;
import cl.duoc.historial_service.clients.PacienteFeign;
import cl.duoc.historial_service.dto.HistorialDTO;
import cl.duoc.historial_service.dto.MedicoDTO;
import cl.duoc.historial_service.dto.PacienteDTO;
import cl.duoc.historial_service.exception.ResourceNotFoundException;
import cl.duoc.historial_service.mapper.HistorialMapper;
import cl.duoc.historial_service.model.Historial;
import cl.duoc.historial_service.repository.HistorialRepository;
import cl.duoc.historial_service.service.HistorialService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para HistorialService")
public class HistorialServiceTest {

    @Mock
    private HistorialRepository repository;

    @Mock
    private HistorialMapper mapper;

    @Mock
    private PacienteFeign pacienteFeign;

    @Mock
    private MedicoFeign medicoFeign;

    @InjectMocks
    private HistorialService service;

    private Historial historial;
    private HistorialDTO historialDTO;
    private PacienteDTO pacienteDTO;
    private MedicoDTO medicoDTO;

    @BeforeEach
    void setUp() {
        historial = new Historial(1L, 1L, 1L, LocalDate.of(2026, 5, 10), "Gripe", "Reposo");

        pacienteDTO = new PacienteDTO(1L, "12345678-9", "Juan", "González", LocalDate.of(1990, 5, 15), "juan.gonzalez@email.com");

        medicoDTO = new MedicoDTO(1L, "98765432-1", "Carla", "Pérez", "carla.perez@email.com", null);

        historialDTO = new HistorialDTO(1L, LocalDate.of(2026, 5, 10), "Gripe", "Reposo", pacienteDTO, medicoDTO);
    }

    @Test
    @DisplayName("findAll - Debe retornar lista de historiales correctamente")
    void findAll_deberiaRetornarListaDeHistoriales() {
        when(repository.findAll()).thenReturn(List.of(historial));
        when(pacienteFeign.findById(1L)).thenReturn(pacienteDTO);
        when(medicoFeign.findById(1L)).thenReturn(medicoDTO);
        when(mapper.toDTO(historial, pacienteDTO, medicoDTO)).thenReturn(historialDTO);

        List<HistorialDTO> resultado = service.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Gripe", resultado.get(0).getDiagnostico());

        verify(repository).findAll();
        verify(pacienteFeign).findById(1L);
        verify(medicoFeign).findById(1L);
    }

    @Test
    @DisplayName("findById - Debe retornar historial cuando existe")
    void findById_cuandoExiste_deberiaRetornarHistorialDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(historial));
        when(pacienteFeign.findById(1L)).thenReturn(pacienteDTO);
        when(medicoFeign.findById(1L)).thenReturn(medicoDTO);
        when(mapper.toDTO(historial, pacienteDTO, medicoDTO)).thenReturn(historialDTO);

        HistorialDTO resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Reposo", resultado.getTratamiento());

        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("findById - Debe lanzar ResourceNotFoundException cuando el historial no existe")
    void findById_cuandoNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(99L)
        );

        assertTrue(exception.getMessage().contains("99"));

        verify(repository).findById(99L);
        verifyNoInteractions(pacienteFeign, medicoFeign, mapper);
    }

    @Test
    @DisplayName("findById - Debe lanzar ResourceNotFoundException cuando el paciente no existe en el servicio externo")
    void findById_cuandoPacienteNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findById(1L)).thenReturn(Optional.of(historial));
        when(pacienteFeign.findById(1L)).thenThrow(new RuntimeException("Paciente no disponible"));

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(1L)
        );

        assertTrue(exception.getMessage().contains("Paciente"));

        verify(pacienteFeign).findById(1L);
    }

    @Test
    @DisplayName("save - Debe guardar y retornar el historial creado")
    void save_deberiaGuardarYRetornarHistorialDTO() {
        HistorialDTO dtoSinId = new HistorialDTO(null, LocalDate.of(2026, 5, 10), "Gripe", "Reposo", pacienteDTO, medicoDTO);

        when(pacienteFeign.findById(1L)).thenReturn(pacienteDTO);
        when(medicoFeign.findById(1L)).thenReturn(medicoDTO);
        when(mapper.toEntity(dtoSinId)).thenReturn(historial);
        when(repository.save(historial)).thenReturn(historial);
        when(repository.findById(1L)).thenReturn(Optional.of(historial));
        when(mapper.toDTO(historial, pacienteDTO, medicoDTO)).thenReturn(historialDTO);

        HistorialDTO resultado = service.save(dtoSinId);

        assertNotNull(resultado);
        assertEquals("Gripe", resultado.getDiagnostico());

        verify(repository).save(historial);
    }

    @Test
    @DisplayName("update - Debe lanzar ResourceNotFoundException cuando el historial no existe")
    void update_cuandoNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.update(99L, historialDTO)
        );

        assertTrue(exception.getMessage().contains("99"));

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deleteById - Debe lanzar ResourceNotFoundException cuando el historial no existe")
    void deleteById_cuandoNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteById(99L)
        );

        assertTrue(exception.getMessage().contains("99"));

        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("findByPaciente - Debe retornar lista de historiales del paciente")
    void findByPaciente_cuandoExisten_deberiaRetornarLista() {
        when(repository.findByPacienteId(1L)).thenReturn(List.of(historial));
        when(pacienteFeign.findById(1L)).thenReturn(pacienteDTO);
        when(medicoFeign.findById(1L)).thenReturn(medicoDTO);
        when(mapper.toDTO(historial, pacienteDTO, medicoDTO)).thenReturn(historialDTO);

        List<HistorialDTO> resultado = service.findByPaciente(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(repository).findByPacienteId(1L);
    }

    @Test
    @DisplayName("findByPaciente - Debe lanzar ResourceNotFoundException cuando no hay historiales")
    void findByPaciente_cuandoNoExisten_deberiaLanzarResourceNotFoundException() {
        when(repository.findByPacienteId(99L)).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByPaciente(99L)
        );

        assertTrue(exception.getMessage().contains("99"));

        verify(repository).findByPacienteId(99L);
    }

    @Test
    @DisplayName("findByMedico - Debe retornar lista de historiales del médico")
    void findByMedico_cuandoExisten_deberiaRetornarLista() {
        when(repository.findByMedicoId(1L)).thenReturn(List.of(historial));
        when(pacienteFeign.findById(1L)).thenReturn(pacienteDTO);
        when(medicoFeign.findById(1L)).thenReturn(medicoDTO);
        when(mapper.toDTO(historial, pacienteDTO, medicoDTO)).thenReturn(historialDTO);

        List<HistorialDTO> resultado = service.findByMedico(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(repository).findByMedicoId(1L);
    }

    @Test
    @DisplayName("findByMedico - Debe lanzar ResourceNotFoundException cuando no hay historiales")
    void findByMedico_cuandoNoExisten_deberiaLanzarResourceNotFoundException() {
        when(repository.findByMedicoId(99L)).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByMedico(99L)
        );

        assertTrue(exception.getMessage().contains("99"));

        verify(repository).findByMedicoId(99L);
    }
}