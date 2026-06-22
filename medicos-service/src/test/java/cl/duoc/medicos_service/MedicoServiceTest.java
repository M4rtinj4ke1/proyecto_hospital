package cl.duoc.medicos_service;

import cl.duoc.medicos_service.clients.EspecialidadFeign;
import cl.duoc.medicos_service.dto.EspecialidadDTO;
import cl.duoc.medicos_service.dto.MedicoDTO;
import cl.duoc.medicos_service.exception.ResourceNotFoundException;
import cl.duoc.medicos_service.mapper.MedicoMapper;
import cl.duoc.medicos_service.model.Medico;
import cl.duoc.medicos_service.repository.MedicoRepository;
import cl.duoc.medicos_service.service.MedicoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para MedicoService")
public class MedicoServiceTest {

    @Mock
    private MedicoRepository repository;

    @Mock
    private MedicoMapper mapper;

    @Mock
    private EspecialidadFeign especialidadFeign;

    @InjectMocks
    private MedicoService service;

    private Medico medico;
    private MedicoDTO medicoDTO;
    private EspecialidadDTO especialidadDTO;
    private EspecialidadDTO especialidadNoDisponible;

    @BeforeEach
    void setUp() {
        medico = new Medico(1L, "11111111-1", "Pedro", "Soto Muñoz",
                "pedro.soto@hospital.cl", 1L);

        especialidadDTO = new EspecialidadDTO(1L, "Cardiología",
                "Especialidad del corazón y sistema cardiovascular");

        especialidadNoDisponible = new EspecialidadDTO(1L, "No disponible", "");

        medicoDTO = new MedicoDTO(1L, "11111111-1", "Pedro", "Soto Muñoz",
                "pedro.soto@hospital.cl", especialidadDTO);
    }



    @Test
    @DisplayName("findAll - Debe retornar lista de médicos con especialidad")
    void findAll_deberiaRetornarListaConEspecialidad() {

        when(repository.findAll()).thenReturn(List.of(medico));
        when(especialidadFeign.findById(1L)).thenReturn(especialidadDTO);
        when(mapper.toDTO(medico, especialidadDTO)).thenReturn(medicoDTO);


        List<MedicoDTO> resultado = service.findAll();


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Pedro", resultado.get(0).getNombre());
        assertEquals("Cardiología", resultado.get(0).getEspecialidad().getNombre());

        verify(repository).findAll();
        verify(especialidadFeign).findById(1L);
        verify(mapper).toDTO(medico, especialidadDTO);
    }

    @Test
    @DisplayName("findAll - Debe retornar 'No disponible' cuando especialidades-service falla")
    void findAll_cuandoFeignFalla_deberiaRetornarEspecialidadNoDisponible() {

        MedicoDTO medicoDTONoDisponible = new MedicoDTO(1L, "11111111-1", "Pedro",
                "Soto Muñoz", "pedro.soto@hospital.cl", especialidadNoDisponible);

        when(repository.findAll()).thenReturn(List.of(medico));
        when(especialidadFeign.findById(1L)).thenThrow(new RuntimeException("Service unavailable"));
        when(mapper.toDTO(medico, especialidadNoDisponible)).thenReturn(medicoDTONoDisponible);


        List<MedicoDTO> resultado = service.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("No disponible", resultado.get(0).getEspecialidad().getNombre());

        verify(repository).findAll();
        verify(especialidadFeign).findById(1L);
        verify(mapper).toDTO(medico, especialidadNoDisponible);
    }



    @Test
    @DisplayName("findById - Debe retornar médico cuando existe")
    void findById_cuandoExiste_deberiaRetornarMedicoDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadFeign.findById(1L)).thenReturn(especialidadDTO);
        when(mapper.toDTO(medico, especialidadDTO)).thenReturn(medicoDTO);

        MedicoDTO resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pedro", resultado.getNombre());
        assertEquals("Cardiología", resultado.getEspecialidad().getNombre());

        verify(repository).findById(1L);
        verify(especialidadFeign).findById(1L);
        verify(mapper).toDTO(medico, especialidadDTO);
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
        verifyNoInteractions(especialidadFeign);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("findById - Debe retornar 'No disponible' cuando especialidades-service falla")
    void findById_cuandoFeignFalla_deberiaRetornarEspecialidadNoDisponible() {

        MedicoDTO medicoDTONoDisponible = new MedicoDTO(1L, "11111111-1", "Pedro",
                "Soto Muñoz", "pedro.soto@hospital.cl", especialidadNoDisponible);

        when(repository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadFeign.findById(1L)).thenThrow(new RuntimeException("Service unavailable"));
        when(mapper.toDTO(medico, especialidadNoDisponible)).thenReturn(medicoDTONoDisponible);


        MedicoDTO resultado = service.findById(1L);


        assertNotNull(resultado);
        assertEquals("No disponible", resultado.getEspecialidad().getNombre());

        verify(repository).findById(1L);
        verify(especialidadFeign).findById(1L);
    }


    @Test
    @DisplayName("save - Debe guardar y retornar el médico creado")
    void save_deberiaGuardarYRetornarMedicoDTO() {
        MedicoDTO dtoSinId = new MedicoDTO(null, "11111111-1", "Pedro",
                "Soto Muñoz", "pedro.soto@hospital.cl", especialidadDTO);

        when(mapper.toEntity(dtoSinId)).thenReturn(medico);
        when(repository.save(medico)).thenReturn(medico);
        when(repository.findById(1L)).thenReturn(Optional.of(medico));
        when(especialidadFeign.findById(1L)).thenReturn(especialidadDTO);
        when(mapper.toDTO(medico, especialidadDTO)).thenReturn(medicoDTO);


        MedicoDTO resultado = service.save(dtoSinId);


        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pedro", resultado.getNombre());

        verify(mapper).toEntity(dtoSinId);
        verify(repository).save(medico);
    }


    @Test
    @DisplayName("update - Debe actualizar y retornar el médico modificado")
    void update_cuandoExiste_deberiaActualizarYRetornarMedicoDTO() {

        when(repository.findById(1L)).thenReturn(Optional.of(medico));
        when(mapper.toEntity(medicoDTO)).thenReturn(medico);
        when(repository.save(medico)).thenReturn(medico);
        when(especialidadFeign.findById(1L)).thenReturn(especialidadDTO);
        when(mapper.toDTO(medico, especialidadDTO)).thenReturn(medicoDTO);

        MedicoDTO resultado = service.update(1L, medicoDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pedro", resultado.getNombre());

        verify(repository, times(2)).findById(1L);
        verify(repository).save(medico);
    }

    @Test
    @DisplayName("update - Debe lanzar ResourceNotFoundException cuando el médico no existe")
    void update_cuandoNoExiste_deberiaLanzarResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.update(99L, medicoDTO)
        );

        assertTrue(exception.getMessage().contains("99"));

        verify(repository).findById(99L);
        verify(repository, never()).save(any());
    }


    @Test
    @DisplayName("deleteById - Debe eliminar correctamente cuando el médico existe")
    void deleteById_cuandoExiste_deberiaEliminar() {
        when(repository.findById(1L)).thenReturn(Optional.of(medico));

        service.deleteById(1L);

        verify(repository).findById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById - Debe lanzar ResourceNotFoundException cuando el médico no existe")
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
    @DisplayName("findByRut - Debe retornar médico cuando el RUT existe")
    void findByRut_cuandoExiste_deberiaRetornarMedicoDTO() {
        when(repository.findByRut("11111111-1")).thenReturn(Optional.of(medico));
        when(especialidadFeign.findById(1L)).thenReturn(especialidadDTO);
        when(mapper.toDTO(medico, especialidadDTO)).thenReturn(medicoDTO);

        MedicoDTO resultado = service.findByRut("11111111-1");

        assertNotNull(resultado);
        assertEquals("11111111-1", resultado.getRut());
        assertEquals("Cardiología", resultado.getEspecialidad().getNombre());

        verify(repository).findByRut("11111111-1");
        verify(especialidadFeign).findById(1L);
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
        verifyNoInteractions(especialidadFeign);
    }


    @Test
    @DisplayName("findByEspecialidad - Debe retornar lista cuando existen médicos")
    void findByEspecialidad_cuandoExisten_deberiaRetornarLista() {
        when(repository.findByEspecialidadId(1L)).thenReturn(List.of(medico));
        when(especialidadFeign.findById(1L)).thenReturn(especialidadDTO);
        when(mapper.toDTO(medico, especialidadDTO)).thenReturn(medicoDTO);

        List<MedicoDTO> resultado = service.findByEspecialidad(1L);


        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cardiología", resultado.get(0).getEspecialidad().getNombre());

        verify(repository).findByEspecialidadId(1L);
    }

    @Test
    @DisplayName("findByEspecialidad - Debe lanzar ResourceNotFoundException cuando no hay médicos")
    void findByEspecialidad_cuandoNoExisten_deberiaLanzarResourceNotFoundException() {
        when(repository.findByEspecialidadId(99L)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findByEspecialidad(99L));

        verify(repository).findByEspecialidadId(99L);
    }


    @Test
    @DisplayName("findByNombre - Debe retornar lista cuando existen médicos con ese nombre")
    void findByNombre_cuandoExisten_deberiaRetornarLista() {
        when(repository.findByNombreContainingIgnoreCase("Pedro")).thenReturn(List.of(medico));
        when(especialidadFeign.findById(1L)).thenReturn(especialidadDTO);
        when(mapper.toDTO(medico, especialidadDTO)).thenReturn(medicoDTO);

        List<MedicoDTO> resultado = service.findByNombre("Pedro");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Pedro", resultado.get(0).getNombre());

        verify(repository).findByNombreContainingIgnoreCase("Pedro");
    }

    @Test
    @DisplayName("findByNombre - Debe lanzar ResourceNotFoundException cuando no hay coincidencias")
    void findByNombre_cuandoNoExisten_deberiaLanzarResourceNotFoundException() {
        when(repository.findByNombreContainingIgnoreCase("Desconocido")).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> service.findByNombre("Desconocido"));

        verify(repository).findByNombreContainingIgnoreCase("Desconocido");
    }
}