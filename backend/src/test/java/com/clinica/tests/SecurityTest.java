package com.clinica.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAccessPacienteAsEnfermeiro() throws Exception {
        mockMvc.perform(get("/api/pacientes")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.claim("roles", "ROLE_ENFERMEIRO"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testAccessPacienteAsMedico() throws Exception {
        mockMvc.perform(get("/api/pacientes")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.claim("roles", "ROLE_MEDICO"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testAccessWithoutRole() throws Exception {
        mockMvc.perform(get("/api/pacientes")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.claim("roles", "")))) // Sem roles
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
