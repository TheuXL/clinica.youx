package com.clinica.tests;

import com.clinica.model.Paciente;
import com.clinica.repository.PacienteRepository;
import com.clinica.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)  // Garante que o Mockito seja inicializado corretamente
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    void criarPacienteComSucesso() {
        Paciente paciente = new Paciente();
        paciente.setNome("João Silva");
        paciente.setCpf("12345678900");
        paciente.setDataNascimento(LocalDate.of(1990, 1, 1));
        paciente.setUf("SP");

        // Configuração do comportamento do mock
        when(pacienteRepository.existsByCpf("12345678900")).thenReturn(false);
        when(pacienteRepository.save(paciente)).thenReturn(paciente);

        // Chamada ao método
        Paciente resultado = pacienteService.criarPaciente(paciente);

        // Verificação do resultado
        assertEquals("João Silva", resultado.getNome());
    }

    @Test
    void criarPacienteComCpfExistente() {
        Paciente paciente = new Paciente();
        paciente.setNome("João Silva");
        paciente.setCpf("12345678900");

        when(pacienteRepository.existsByCpf("12345678900")).thenReturn(true);

        // Verificando se uma exceção será lançada quando o CPF já existir
        assertThrows(IllegalArgumentException.class, () -> pacienteService.criarPaciente(paciente));
    }
}
