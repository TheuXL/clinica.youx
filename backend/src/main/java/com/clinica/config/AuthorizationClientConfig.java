package com.clinica.config;

import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public class AuthorizationClientConfig {

    // Não é necessário redefinir, apenas use diretamente da classe ClientAuthenticationMethod
    public static final ClientAuthenticationMethod CLIENT_SECRET_BASIC = ClientAuthenticationMethod.CLIENT_SECRET_BASIC;

}