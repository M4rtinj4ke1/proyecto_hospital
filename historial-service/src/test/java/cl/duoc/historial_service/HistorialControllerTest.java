package cl.duoc.historial_service;

import cl.duoc.historial_service.controller.HistorialController;
import cl.duoc.historial_service.dto.HistorialDTO;
import cl.duoc.historial_service.dto.MedicoDTO;
import cl.duoc.historial_service.dto.PacienteDTO;
import cl.duoc.historial_service.exception.ResourceNotFoundException;
import cl.duoc.historial_service.service.HistorialService;

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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistorialController.class)
@DisplayName("Pruebas en la capa Controller de historial")
public class HistorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistorialService service;

    @Autowired
    private ObjectMapper objectMapper;

    private HistorialDTO historialDTO;
    private HistorialDTO historialDTOSinId;

    @BeforeEach
    void setUp() {
        PacienteDTO paciente = new PacienteDTO(1L, "12345678-9", "Juan", "González", LocalDate.of(1990, 5, 15), "juan.gonzalez@email.com");
        MedicoDTO medico = new MedicoDTO(1L, "98765432-1", "Carla", "Pérez", "carla.perez@email.com", null);

        historialDTO = new HistorialDTO(1L, LocalDate.of(2026, 5, 10), "Gripe", "Reposo", paciente, medico);
        historialDTOSinId = new HistorialDTO(null, LocalDate.of(2026, 5, 10), "Gripe", "Reposo", paciente, medico);
    }

    @Test
    @DisplayName("GET /api/historiales - Debe retornar 200 OK con lista")
    void findAll_deberiaRetornar200ConLista() throws Exception {
        when(service.findAll()).thenReturn(List.of(historialDTO));

        mockMvc.perform(get("/api/historiales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].diagnostico").value("Gripe"));
    }

    @Test
    @DisplayName("GET /api/historiales/{id} - Debe retornar 200 OK cuando existe")
    void findById_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.findById(1L)).thenReturn(historialDTO);

        mockMvc.perform(get("/api/historiales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tratamiento").value("Reposo"));
    }

    @Test
    @DisplayName("GET /api/historiales/{id} - Debe retornar 404 cuando no existe")
    void findById_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.findById(99L)).thenThrow(new ResourceNotFoundException("Historial no encontrado con ID: 99"));

        mockMvc.perform(get("/api/historiales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/historiales - Debe retornar 201 CREATED")
    void save_deberiaRetornar201ConHistorialCreado() throws Exception {
        when(service.save(any(HistorialDTO.class))).thenReturn(historialDTO);

        mockMvc.perform(post("/api/historiales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(historialDTOSinId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.diagnostico").value("Gripe"));
    }

    @Test
    @DisplayName("POST /api/historiales - Debe retornar 400 cuando los datos son inválidos")
    void save_conDatosInvalidos_deberiaRetornar400() throws Exception {
        HistorialDTO dtoInvalido = new HistorialDTO(null, null, "", null, null, null);

        mockMvc.perform(post("/api/historiales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/historiales/{id} - Debe retornar 200 OK cuando existe")
    void update_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.update(eq(1L), any(HistorialDTO.class))).thenReturn(historialDTO);

        mockMvc.perform(put("/api/historiales/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(historialDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("PUT /api/historiales/{id} - Debe retornar 404 cuando no existe")
    void update_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.update(eq(99L), any(HistorialDTO.class)))
                .thenThrow(new ResourceNotFoundException("Historial no encontrado con ID: 99"));

        mockMvc.perform(put("/api/historiales/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(historialDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/historiales/{id} - Debe retornar 204 NO CONTENT")
    void deleteById_deberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/api/historiales/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/historiales/{id} - Debe retornar 404 cuando no existe")
    void deleteById_cuandoNoExiste_deberiaRetornar404() throws Exception {
        doThrow(new ResourceNotFoundException("Historial no encontrado con ID: 99"))
                .when(service).deleteById(99L);

        mockMvc.perform(delete("/api/historiales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/historiales/paciente/{pacienteId} - Debe retornar 200 OK con lista")
    void findByPaciente_cuandoExisten_deberiaRetornar200() throws Exception {
        when(service.findByPaciente(1L)).thenReturn(List.of(historialDTO));

        mockMvc.perform(get("/api/historiales/paciente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].diagnostico").value("Gripe"));
    }

    @Test
    @DisplayName("GET /api/historiales/paciente/{pacienteId} - Debe retornar 404 cuando no hay coincidencias")
    void findByPaciente_cuandoNoExisten_deberiaRetornar404() throws Exception {
        when(service.findByPaciente(99L))
                .thenThrow(new ResourceNotFoundException("No se encontraron historiales para el paciente con ID: 99"));

        mockMvc.perform(get("/api/historiales/paciente/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/historiales/medico/{medicoId} - Debe retornar 200 OK con lista")
    void findByMedico_cuandoExisten_deberiaRetornar200() throws Exception {
        when(service.findByMedico(1L)).thenReturn(List.of(historialDTO));

        mockMvc.perform(get("/api/historiales/medico/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].diagnostico").value("Gripe"));
    }

    @Test
    @DisplayName("GET /api/historiales/medico/{medicoId} - Debe retornar 404 cuando no hay coincidencias")
    void findByMedico_cuandoNoExisten_deberiaRetornar404() throws Exception {
        when(service.findByMedico(99L))
                .thenThrow(new ResourceNotFoundException("No se encontraron historiales para el médico con ID: 99"));

        mockMvc.perform(get("/api/historiales/medico/99"))
                .andExpect(status().isNotFound());
    }
}