package com.clinica.model;

import jakarta.persistence.Entity;

@Entity
public class Medico extends Usuario {

    public Medico() {
        super();
        this.setPermissao("SCOPE_full_control");  // Controle total sobre pacientes e enfermeiros
    }
}