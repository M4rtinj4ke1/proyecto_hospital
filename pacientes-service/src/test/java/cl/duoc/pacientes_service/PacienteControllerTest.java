package cl.duoc.pacientes_service;

import cl.duoc.pacientes_service.controller.PacienteController;
import cl.duoc.pacientes_service.dto.PacienteDTO;
import cl.duoc.pacientes_service.exception.ResourceNotFoundException;
import cl.duoc.pacientes_service.service.PacienteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PacienteController.class)
@DisplayName("Pruebas en la capa Controller de pacientes")
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PacienteService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PacienteDTO pacienteDTO;
    private PacienteDTO pacienteDTOSinId;

    @BeforeEach
    void setUp() {
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

    // ─── GET /api/pacientes ─────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/pacientes - Debe retornar 200 OK con lista de pacientes")
    void findAll_deberiaRetornar200ConLista() throws Exception {
        // Given
        when(service.findAll()).thenReturn(List.of(pacienteDTO));

        // When & Then
        mockMvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellidos").value("González"))
                .andExpect(jsonPath("$[0].email").value("juan.gonzalez@email.com"));
    }

    @Test
    @DisplayName("GET /api/pacientes - Debe retornar 200 OK con lista vacía")
    void findAll_deberiaRetornar200ConListaVacia() throws Exception {
        // Given
        when(service.findAll()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ─── GET /api/pacientes/{id} ────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/pacientes/{id} - Debe retornar 200 OK cuando el paciente existe")
    void findById_cuandoExiste_deberiaRetornar200() throws Exception {
        // Given
        when(service.findById(1L)).thenReturn(pacienteDTO);

        // When & Then
        mockMvc.perform(get("/api/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    @DisplayName("GET /api/pacientes/{id} - Debe retornar 404 cuando el paciente no existe")
    void findById_cuandoNoExiste_deberiaRetornar404() throws Exception {
        // Given
        when(service.findById(99L)).thenThrow(new ResourceNotFoundException("Paciente no encontrado con ID: 99"));

        // When & Then
        mockMvc.perform(get("/api/pacientes/99"))
                .andExpect(status().isNotFound());
    }

    // ─── POST /api/pacientes ────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/pacientes - Debe retornar 201 CREATED con el paciente creado")
    void save_deberiaRetornar201ConPacienteCreado() throws Exception {
        // Given
        when(service.save(any(PacienteDTO.class))).thenReturn(pacienteDTO);

        // When & Then
        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteDTOSinId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan.gonzalez@email.com"));
    }

    @Test
    @DisplayName("POST /api/pacientes - Debe retornar 400 cuando los datos son inválidos")
    void save_conDatosInvalidos_deberiaRetornar400() throws Exception {
        // Given
        PacienteDTO dtoInvalido = new PacienteDTO(null, "", "", "", null, "no-es-email");

        // When & Then
        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    // ─── PUT /api/pacientes/{id} ────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/pacientes/{id} - Debe retornar 200 OK con paciente actualizado")
    void update_cuandoExiste_deberiaRetornar200() throws Exception {
        // Given
        when(service.update(eq(1L), any(PacienteDTO.class))).thenReturn(pacienteDTO);

        // When & Then
        mockMvc.perform(put("/api/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    @DisplayName("PUT /api/pacientes/{id} - Debe retornar 404 cuando el paciente no existe")
    void update_cuandoNoExiste_deberiaRetornar404() throws Exception {
        // Given
        when(service.update(eq(99L), any(PacienteDTO.class)))
                .thenThrow(new ResourceNotFoundException("Paciente no encontrado con ID: 99"));

        // When & Then
        mockMvc.perform(put("/api/pacientes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pacienteDTO)))
                .andExpect(status().isNotFound());
    }

    // ─── DELETE /api/pacientes/{id} ─────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/pacientes/{id} - Debe retornar 204 NO CONTENT")
    void deleteById_deberiaRetornar204() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/pacientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/pacientes/{id} - Debe retornar 404 cuando el paciente no existe")
    void deleteById_cuandoNoExiste_deberiaRetornar404() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("Paciente no encontrado con ID: 99"))
                .when(service).deleteById(99L);

        // When & Then
        mockMvc.perform(delete("/api/pacientes/99"))
                .andExpect(status().isNotFound());
    }

    // ─── GET /api/pacientes/rut/{rut} ───────────────────────────────────────

    @Test
    @DisplayName("GET /api/pacientes/rut/{rut} - Debe retornar 200 OK cuando el RUT existe")
    void findByRut_cuandoExiste_deberiaRetornar200() throws Exception {
        // Given
        when(service.findByRut("12345678-9")).thenReturn(pacienteDTO);

        // When & Then
        mockMvc.perform(get("/api/pacientes/rut/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    @DisplayName("GET /api/pacientes/rut/{rut} - Debe retornar 404 cuando el RUT no existe")
    void findByRut_cuandoNoExiste_deberiaRetornar404() throws Exception {
        // Given
        when(service.findByRut("00000000-0"))
                .thenThrow(new ResourceNotFoundException("Paciente no encontrado con RUT: 00000000-0"));

        // When & Then
        mockMvc.perform(get("/api/pacientes/rut/00000000-0"))
                .andExpect(status().isNotFound());
    }

    // ─── GET /api/pacientes/apellidos/{apellidos} ───────────────────────────

    @Test
    @DisplayName("GET /api/pacientes/apellidos/{apellidos} - Debe retornar 200 OK con lista")
    void findByApellidos_cuandoExisten_deberiaRetornar200() throws Exception {
        // Given
        when(service.findByApellidos("González")).thenReturn(List.of(pacienteDTO));

        // When & Then
        mockMvc.perform(get("/api/pacientes/apellidos/González"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].apellidos").value("González"))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    @DisplayName("GET /api/pacientes/apellidos/{apellidos} - Debe retornar 404 cuando no hay coincidencias")
    void findByApellidos_cuandoNoExisten_deberiaRetornar404() throws Exception {
        // Given
        when(service.findByApellidos("Desconocido"))
                .thenThrow(new ResourceNotFoundException("No se encontraron pacientes con apellido: Desconocido"));

        // When & Then
        mockMvc.perform(get("/api/pacientes/apellidos/Desconocido"))
                .andExpect(status().isNotFound());
    }

    // ─── GET /api/pacientes/email/{email} ───────────────────────────────────

    @Test
    @DisplayName("GET /api/pacientes/email/{email} - Debe retornar 200 OK cuando el email existe")
    void findByEmail_cuandoExiste_deberiaRetornar200() throws Exception {
        // Given
        when(service.findByEmail("juan.gonzalez@email.com")).thenReturn(pacienteDTO);

        // When & Then
        mockMvc.perform(get("/api/pacientes/email/juan.gonzalez@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan.gonzalez@email.com"))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    @DisplayName("GET /api/pacientes/email/{email} - Debe retornar 404 cuando el email no existe")
    void findByEmail_cuandoNoExiste_deberiaRetornar404() throws Exception {
        // Given
        when(service.findByEmail("noexiste@email.com"))
                .thenThrow(new ResourceNotFoundException("Paciente no encontrado con email: noexiste@email.com"));

        // When & Then
        mockMvc.perform(get("/api/pacientes/email/noexiste@email.com"))
                .andExpect(status().isNotFound());
    }
}