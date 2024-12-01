package com.clinica.model;

import jakarta.persistence.Entity;

@Entity
public class Enfermeiro extends Usuario {

    public Enfermeiro() {
        super();
        this.setPermissao("SCOPE_create_paciente");  // Permissão de criar pacientes
    }
}
