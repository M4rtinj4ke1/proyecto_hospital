package cl.duoc.medicos_service;

import cl.duoc.medicos_service.controller.MedicoController;
import cl.duoc.medicos_service.dto.EspecialidadDTO;
import cl.duoc.medicos_service.dto.MedicoDTO;
import cl.duoc.medicos_service.exception.ResourceNotFoundException;
import cl.duoc.medicos_service.service.MedicoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicoController.class)
@DisplayName("Pruebas en la capa Controller de médicos")
public class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private MedicoDTO medicoDTO;
    private MedicoDTO medicoDTOSinId;

    @BeforeEach
    void setUp() {
        EspecialidadDTO especialidad = new EspecialidadDTO(1L, "Cardiología",
                "Especialidad del corazón y sistema cardiovascular");

        medicoDTO = new MedicoDTO(1L, "11111111-1", "Pedro", "Soto Muñoz",
                "pedro.soto@hospital.cl", especialidad);

        medicoDTOSinId = new MedicoDTO(null, "11111111-1", "Pedro", "Soto Muñoz",
                "pedro.soto@hospital.cl", especialidad);
    }

    @Test
    @DisplayName("GET /api/medicos - Debe retornar 200 OK con lista de médicos")
    void findAll_deberiaRetornar200ConLista() throws Exception {
        when(service.findAll()).thenReturn(List.of(medicoDTO));

        mockMvc.perform(get("/api/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rut").value("11111111-1"))
                .andExpect(jsonPath("$[0].nombre").value("Pedro"))
                .andExpect(jsonPath("$[0].especialidad.nombre").value("Cardiología"));
    }

    @Test
    @DisplayName("GET /api/medicos - Debe retornar 200 OK con lista vacía")
    void findAll_deberiaRetornar200ConListaVacia() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @DisplayName("GET /api/medicos/{id} - Debe retornar 200 OK cuando el médico existe")
    void findById_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.findById(1L)).thenReturn(medicoDTO);

        mockMvc.perform(get("/api/medicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Pedro"))
                .andExpect(jsonPath("$.especialidad.nombre").value("Cardiología"));
    }

    @Test
    @DisplayName("GET /api/medicos/{id} - Debe retornar 404 cuando el médico no existe")
    void findById_cuandoNoExiste_deberiaRetornar404() throws Exception {

        when(service.findById(99L)).thenThrow(
                new ResourceNotFoundException("Médico no encontrado con ID: 99"));

        mockMvc.perform(get("/api/medicos/99"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("POST /api/medicos - Debe retornar 201 CREATED con el médico creado")
    void save_deberiaRetornar201ConMedicoCreado() throws Exception {
        when(service.save(any(MedicoDTO.class))).thenReturn(medicoDTO);

        mockMvc.perform(post("/api/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicoDTOSinId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("11111111-1"))
                .andExpect(jsonPath("$.nombre").value("Pedro"));
    }


    @Test
    @DisplayName("PUT /api/medicos/{id} - Debe retornar 200 OK con médico actualizado")
    void update_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.update(eq(1L), any(MedicoDTO.class))).thenReturn(medicoDTO);

        mockMvc.perform(put("/api/medicos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Pedro"));
    }

    @Test
    @DisplayName("PUT /api/medicos/{id} - Debe retornar 404 cuando el médico no existe")
    void update_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.update(eq(99L), any(MedicoDTO.class)))
                .thenThrow(new ResourceNotFoundException("Médico no encontrado con ID: 99"));

        mockMvc.perform(put("/api/medicos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicoDTO)))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("DELETE /api/medicos/{id} - Debe retornar 204 NO CONTENT")
    void deleteById_deberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/api/medicos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/medicos/{id} - Debe retornar 404 cuando el médico no existe")
    void deleteById_cuandoNoExiste_deberiaRetornar404() throws Exception {
        doThrow(new ResourceNotFoundException("Médico no encontrado con ID: 99"))
                .when(service).deleteById(99L);

        mockMvc.perform(delete("/api/medicos/99"))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("GET /api/medicos/rut/{rut} - Debe retornar 200 OK cuando el RUT existe")
    void findByRut_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.findByRut("11111111-1")).thenReturn(medicoDTO);

        mockMvc.perform(get("/api/medicos/rut/11111111-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("11111111-1"))
                .andExpect(jsonPath("$.nombre").value("Pedro"));
    }

    @Test
    @DisplayName("GET /api/medicos/rut/{rut} - Debe retornar 404 cuando el RUT no existe")
    void findByRut_cuandoNoExiste_deberiaRetornar404() throws Exception {

        when(service.findByRut("00000000-0"))
                .thenThrow(new ResourceNotFoundException("Médico no encontrado con RUT: 00000000-0"));

        mockMvc.perform(get("/api/medicos/rut/00000000-0"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/medicos/especialidad/{id} - Debe retornar 200 OK con lista")
    void findByEspecialidad_cuandoExisten_deberiaRetornar200() throws Exception {
        when(service.findByEspecialidad(1L)).thenReturn(List.of(medicoDTO));

        mockMvc.perform(get("/api/medicos/especialidad/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especialidad.nombre").value("Cardiología"));
    }

    @Test
    @DisplayName("GET /api/medicos/especialidad/{id} - Debe retornar 404 cuando no hay médicos")
    void findByEspecialidad_cuandoNoExisten_deberiaRetornar404() throws Exception {
        when(service.findByEspecialidad(99L))
                .thenThrow(new ResourceNotFoundException("No se encontraron médicos"));

        mockMvc.perform(get("/api/medicos/especialidad/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/medicos/nombre/{nombre} - Debe retornar 200 OK con lista")
    void findByNombre_cuandoExisten_deberiaRetornar200() throws Exception {
        when(service.findByNombre("Pedro")).thenReturn(List.of(medicoDTO));

        mockMvc.perform(get("/api/medicos/nombre/Pedro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Pedro"));
    }

    @Test
    @DisplayName("GET /api/medicos/nombre/{nombre} - Debe retornar 404 cuando no hay coincidencias")
    void findByNombre_cuandoNoExisten_deberiaRetornar404() throws Exception {
        // Given
        when(service.findByNombre("Desconocido"))
                .thenThrow(new ResourceNotFoundException("No se encontraron médicos con nombre: Desconocido"));

        // When & Then
        mockMvc.perform(get("/api/medicos/nombre/Desconocido"))
                .andExpect(status().isNotFound());
    }
}