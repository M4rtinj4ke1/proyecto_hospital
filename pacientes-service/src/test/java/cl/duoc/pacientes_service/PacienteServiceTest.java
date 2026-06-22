package cl.duoc.pacientes_service;

import cl.duoc.pacientes_service.dto.PacienteDTO;
import cl.duoc.pacientes_service.exception.ResourceNotFoundException;
import cl.duoc.pacientes_service.mapper.PacienteMapper;
import cl.duoc.pacientes_service.model.Paciente;
import cl.duoc.pacientes_service.repository.PacienteRepository;
import cl.duoc.pacientes_service.service.PacienteService;

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
@DisplayName("Pruebas unitarias para PacienteService")
public class PacienteServiceTest {

    @Mock
    private PacienteRepository repository;

    @Mock
    private PacienteMapper mapper;

    @InjectMocks
    private PacienteService service;

    private Paciente paciente;
    private PacienteDTO pacienteDTO;
    private PacienteDTO pacienteDTOSinId;

    @BeforeEach
    void setUp() {
        paciente = new Paciente(
                1L,
                "12345678-9",
                "Juan",
                "González",
                LocalDate.of(1990, 5, 15),
                "juan.gonzalez@email.com"
        );

        pacienteDTO = new PacienteDTO(
                1L,
                "12345678-9",
                "Juan",
                "González",
                LocalDate.of(1990, 5, 15),
                "juan.gonzalez@email.com"
        );

        pacienteDTOSinId = new PacienteDTO(
                null,
                "12345678-9",
                "Juan",
                "González",
                LocalDate.of(1990, 5, 15),
                "juan.gonzalez@email.com"
        );
    }


    @Test
    @DisplayName("findAll - Debe retornar lista de pacientes correctamente")
    void findAll_deberiaRetornarListaDePacientes() {
        when(repository.findAll()).thenReturn(List.of(paciente));
        when(mapper.toDTO(paciente)).thenReturn(pacienteDTO);

        List<PacienteDTO> resultado = service.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        assertEquals("12345678-9", resultado.get(0).getRut());

        verify(repository).findAll();
        verify(mapper).toDTO(paciente);
    }

    @Test
    @DisplayName("findAll - Debe retornar lista vacía cuando no hay pacientes")
    void findAll_deberiaRetornarListaVacia() {
        when(repository.findAll()).thenReturn(List.of());

        List<PacienteDTO> resultado = service.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(repository).findAll();
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("findById - Debe retornar paciente cuando existe")
    void findById_cuandoExiste_deberiaRetornarPacienteDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(mapper.toDTO(paciente)).thenReturn(pacienteDTO);

        PacienteDTO resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("juan.gonzalez@email.com", resultado.getEmail());

        verify(repository).findById(1L);
        verify(mapper).toDTO(paciente);
    }

    @Test
    @DisplayName("findById - Debe lanzar ResourceNotFoundException cuando no existe")
    void findById_cuandoNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(99L)
        );

        assertTrue(exception.getMessage().contains("99"));

        verify(repository).findById(99L);
        verifyNoInteractions(mapper);
    }


    @Test
    @DisplayName("save - Debe guardar y retornar el DTO del paciente creado")
    void save_deberiaGuardarYRetornarPacienteDTO() {
        when(mapper.toEntity(pacienteDTOSinId)).thenReturn(paciente);
        when(repository.save(paciente)).thenReturn(paciente);
        when(mapper.toDTO(paciente)).thenReturn(pacienteDTO);

        PacienteDTO resultado = service.save(pacienteDTOSinId);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("González", resultado.getApellidos());

        verify(mapper).toEntity(pacienteDTOSinId);
        verify(repository).save(paciente);
        verify(mapper).toDTO(paciente);
    }

    @Test
    @DisplayName("update - Debe actualizar y retornar el paciente modificado")
    void update_cuandoExiste_deberiaActualizarYRetornarPacienteDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(mapper.toEntity(pacienteDTO)).thenReturn(paciente);
        when(repository.save(paciente)).thenReturn(paciente);
        when(mapper.toDTO(paciente)).thenReturn(pacienteDTO);

        PacienteDTO resultado = service.update(1L, pacienteDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());

        verify(repository).findById(1L);
        verify(repository).save(paciente);
        verify(mapper).toDTO(paciente);
    }

    @Test
    @DisplayName("update - Debe lanzar ResourceNotFoundException cuando el paciente no existe")
    void update_cuandoNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.update(99L, pacienteDTO)
        );

        assertTrue(exception.getMessage().contains("99"));

        verify(repository).findById(99L);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deleteById - Debe lanzar ResourceNotFoundException cuando el paciente no existe")
    void deleteById_cuandoNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteById(99L)
        );

        assertTrue(exception.getMessage().contains("99"));

        verify(repository).findById(99L);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("findByRut - Debe retornar paciente cuando el RUT existe")
    void findByRut_cuandoExiste_deberiaRetornarPacienteDTO() {
        when(repository.findByRut("12345678-9")).thenReturn(Optional.of(paciente));
        when(mapper.toDTO(paciente)).thenReturn(pacienteDTO);

        PacienteDTO resultado = service.findByRut("12345678-9");

        assertNotNull(resultado);
        assertEquals("12345678-9", resultado.getRut());
        assertEquals("Juan", resultado.getNombre());

        verify(repository).findByRut("12345678-9");
        verify(mapper).toDTO(paciente);
    }

    @Test
    @DisplayName("findByRut - Debe lanzar ResourceNotFoundException cuando el RUT no existe")
    void findByRut_cuandoNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findByRut("00000000-0")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByRut("00000000-0")
        );

        assertTrue(exception.getMessage().contains("00000000-0"));

        verify(repository).findByRut("00000000-0");
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("findByApellidos - Debe retornar lista de pacientes que coinciden con el apellido")
    void findByApellidos_cuandoExisten_deberiaRetornarLista() {
        when(repository.findByApellidosContainingIgnoreCase("González")).thenReturn(List.of(paciente));
        when(mapper.toDTO(paciente)).thenReturn(pacienteDTO);

        List<PacienteDTO> resultado = service.findByApellidos("González");
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("González", resultado.get(0).getApellidos());

        verify(repository).findByApellidosContainingIgnoreCase("González");
        verify(mapper).toDTO(paciente);
    }

    @Test
    @DisplayName("findByApellidos - Debe lanzar ResourceNotFoundException cuando no hay coincidencias")
    void findByApellidos_cuandoNoExisten_deberiaLanzarResourceNotFoundException() {
        when(repository.findByApellidosContainingIgnoreCase("Desconocido")).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByApellidos("Desconocido")
        );

        assertTrue(exception.getMessage().contains("Desconocido"));

        verify(repository).findByApellidosContainingIgnoreCase("Desconocido");
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("findByEmail - Debe retornar paciente cuando el email existe")
    void findByEmail_cuandoExiste_deberiaRetornarPacienteDTO() {
        when(repository.findByEmail("juan.gonzalez@email.com")).thenReturn(Optional.of(paciente));
        when(mapper.toDTO(paciente)).thenReturn(pacienteDTO);

        PacienteDTO resultado = service.findByEmail("juan.gonzalez@email.com");

        assertNotNull(resultado);
        assertEquals("juan.gonzalez@email.com", resultado.getEmail());
        assertEquals("Juan", resultado.getNombre());

        verify(repository).findByEmail("juan.gonzalez@email.com");
        verify(mapper).toDTO(paciente);
    }

    @Test
    @DisplayName("findByEmail - Debe lanzar ResourceNotFoundException cuando el email no existe")
    void findByEmail_cuandoNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findByEmail("noexiste@email.com")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByEmail("noexiste@email.com")
        );

        assertTrue(exception.getMessage().contains("noexiste@email.com"));

        verify(repository).findByEmail("noexiste@email.com");
        verifyNoInteractions(mapper);
    }
}