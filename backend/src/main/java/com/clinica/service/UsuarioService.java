package com.clinica.service;

import com.clinica.model.Usuario;
import com.clinica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorAuth0Id(String auth0Id) {
        return usuarioRepository.findByAuth0Id(auth0Id);
    }

    public boolean verificarPermissaoVerPacientes() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String auth0Id = user.getUsername();
        Optional<Usuario> usuario = buscarPorAuth0Id(auth0Id);

        return usuario.isPresent() && usuario.get().getPermissao().equals("SCOPE_read:pacientes");
    }

    public boolean verificarPermissaoCriarPaciente(String auth0Id) {
        Optional<Usuario> usuario = buscarPorAuth0Id(auth0Id);
        return usuario.isPresent() && usuario.get().getPermissao().equals("SCOPE_create_paciente");
    }

    public boolean verificarPermissaoDeletarPaciente() {
        return true;  // Ajuste conforme as permissões associadas ao token
    }
}
