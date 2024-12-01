package com.clinica.controller;

import com.clinica.model.Paciente;
import com.clinica.service.PacienteService;
import com.clinica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "http://localhost:3000") // Permite acesso ao frontend local
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private UsuarioService usuarioService;

    // O campo jwtDecoder foi removido, pois não está sendo usado.
    
    @GetMapping
    public ResponseEntity<List<Paciente>> listarTodos() {
        // Valida se o usuário tem permissão para visualizar pacientes
        if (!usuarioService.verificarPermissaoVerPacientes()) {
            return ResponseEntity.status(403).build(); // Se o usuário não tiver permissão
        }

        List<Paciente> pacientes = pacienteService.listarTodos();
        return ResponseEntity.ok(pacientes);
    }

    @PostMapping
    public ResponseEntity<Paciente> criarPaciente(@RequestBody Paciente paciente) {
        // Valida se o usuário tem permissão para criar pacientes
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String cpfUsuario = jwt.getClaim("cpf"); // Supondo que o CPF seja armazenado no JWT

        // Verifique o tipo de usuário e a permissão associada
        if (!usuarioService.verificarPermissaoCriarPaciente(cpfUsuario)) {
            return ResponseEntity.status(403).build(); // Se não tiver permissão, retorna 403
        }

        try {
            Paciente pacienteCriado = pacienteService.criarPaciente(paciente);
            return ResponseEntity.status(201).body(pacienteCriado);
        } catch (IllegalArgumentException e) {
            // Retorna uma resposta de erro com o status apropriado
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        // Verifica se o usuário tem permissão para deletar pacientes
        if (!usuarioService.verificarPermissaoDeletarPaciente()) {
            return ResponseEntity.status(403).build(); // Se não tiver permissão
        }

        pacienteService.deletarPaciente(id);
        return ResponseEntity.noContent().build();
    }
}