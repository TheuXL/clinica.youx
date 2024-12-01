package com.clinica.repository;

import com.clinica.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Busca um usuário pelo Auth0 ID (sub)
    Optional<Usuario> findByAuth0Id(String auth0Id);
    
    // Verifica se o Auth0 ID já está cadastrado
    boolean existsByAuth0Id(String auth0Id);

    // Verifica se o CPF já está cadastrado
    boolean existsByCpf(String cpf);  // Adiciona o método para verificar se o CPF já está registrado
}