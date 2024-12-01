package com.clinica.service;

import com.clinica.model.Paciente;
import com.clinica.repository.PacienteRepository;
import com.clinica.security.CryptoUtils;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    private SecretKey generateSecretKey() throws Exception {
        return CryptoUtils.generateSecretKey();
    }

    private IvParameterSpec generateIv() throws Exception {
        return CryptoUtils.generateIv();
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Paciente criarPaciente(Paciente paciente) {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String auth0Id = user.getUsername();

            if (pacienteRepository.existsByCpf(paciente.getCpf())) {
                throw new IllegalArgumentException("CPF já cadastrado.");
            }

            SecretKey secretKey = generateSecretKey();
            IvParameterSpec iv = generateIv();

            String encryptedCpf = CryptoUtils.encrypt(paciente.getCpf(), secretKey, iv);
            paciente.setCpf(encryptedCpf);
            paciente.setUsuario(auth0Id);

            return pacienteRepository.save(paciente);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar o CPF", e);
        }
    }

    public String descriptografarCpf(Paciente paciente) {
        try {
            SecretKey secretKey = generateSecretKey(); 
            IvParameterSpec iv = generateIv();
            return CryptoUtils.decrypt(paciente.getCpf(), secretKey, iv);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar o CPF", e);
        }
    }

    public void deletarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }
}