package com.clinica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

    // Criação do repositório de clientes registrados com Auth0
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("your-auth0-client-id") // Substitua pelo client_id do Auth0
                .clientSecret(authorizationServerPasswordEncoder().encode("your-auth0-client-secret")) // Substitua pela chave secreta do Auth0
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("read") // Scopes definidos conforme seu uso
                .scope("write")
                .redirectUri("http://localhost:3000/login/oauth2/code/youx-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // Método de autenticação
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient); // Repositório em memória
    }

    // Encoder para senha do cliente
    @Bean
    public PasswordEncoder authorizationServerPasswordEncoder() {
        return new BCryptPasswordEncoder(); // Usando bcrypt para codificar a senha do cliente
    }

    // Definindo as configurações do servidor de autorização
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("https://YOUR_AUTH0_DOMAIN") // URL do seu domínio Auth0
                .build();
    }

    // Configuração do cliente Auth0 usando Spring OAuth2 Client
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("auth0")
                .clientId("your-auth0-client-id") // Substitua pelo client_id do Auth0
                .clientSecret("your-auth0-client-secret") // Substitua pela chave secreta do Auth0
                .scope("openid", "profile", "email", "read", "write") // Defina os escopos conforme necessário
                .authorizationUri("https://YOUR_AUTH0_DOMAIN/authorize") // URL de autorização do Auth0
                .tokenUri("https://YOUR_AUTH0_DOMAIN/oauth/token") // URL de token do Auth0
                .userInfoUri("https://YOUR_AUTH0_DOMAIN/userinfo") // URL de informações do usuário do Auth0
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // Método de autenticação
                .build();

        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}
