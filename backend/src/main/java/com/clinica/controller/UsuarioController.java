package com.clinica.controller;

import com.clinica.model.Enfermeiro;
import com.clinica.model.Medico;
import com.clinica.model.Usuario;
import com.clinica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/enfermeiros")
    public ResponseEntity<Usuario> criarEnfermeiro(@RequestBody Enfermeiro enfermeiro) {
        // Adicionar verificação de permissões, caso necessário
        // Se o usuário tem permissão para criar enfermeiros, então prosseguir com a criação.
        Usuario usuarioCriado = usuarioService.salvarUsuario(enfermeiro);
        return ResponseEntity.status(201).body(usuarioCriado);
    }

    @PostMapping("/medicos")
    public ResponseEntity<Usuario> criarMedico(@RequestBody Medico medico) {
        // Adicionar verificação de permissões, caso necessário
        // Se o usuário tem permissão para criar médicos, então prosseguir com a criação.
        Usuario usuarioCriado = usuarioService.salvarUsuario(medico);
        return ResponseEntity.status(201).body(usuarioCriado);
    }
}