package com.clinica.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {

    @Bean
    public StandardPBEStringEncryptor encryptor() {
        // Configuração baseada no ambiente para maior segurança
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();

        // Defina a senha de criptografia via variável de ambiente ou arquivo de configuração
        config.setPassword(System.getenv("JASYPT_ENCRYPTOR_PASSWORD")); 
        if (config.getPassword() == null || config.getPassword().isEmpty()) {
            throw new IllegalArgumentException("A senha de criptografia não foi configurada.");
        }

        // Configurações seguras
        config.setAlgorithm("PBEWithHMACSHA512AndAES_256"); // Algoritmo seguro
        config.setKeyObtentionIterations(1000); // Iterações de obtenção da chave
        config.setPoolSize(1); // Tamanho do pool (opcional, para threads concorrentes)
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // Gerador de sal aleatório
        config.setStringOutputType("base64"); // Saída em base64

        // Criação do encryptor
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);
        return encryptor;
    }
}