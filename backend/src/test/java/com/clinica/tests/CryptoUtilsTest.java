package com.clinica.tests;

import com.clinica.security.CryptoUtils;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONException;

import static org.junit.jupiter.api.Assertions.*;

class CryptoUtilsTest {

    private static final String CLIENT_ID = "<YOUR_CLIENT_ID>";
    private static final String CLIENT_SECRET = "<YOUR_CLIENT_SECRET>";
    private static final String API_IDENTIFIER = "<YOUR_API_IDENTIFIER>";
    private static final String AUTH0_DOMAIN = "https://dev-theuxl.us.auth0.com";  // Seu domínio Auth0

    // Teste de criptografia e descriptografia
    @Test
    void testEncryptAndDecrypt() throws Exception {
        String originalCpf = "12345678900";

        // Gerando chave e IV
        SecretKey secretKey = CryptoUtils.generateSecretKey();
        IvParameterSpec iv = CryptoUtils.generateIv();

        // Criptografando
        String encryptedCpf = CryptoUtils.encrypt(originalCpf, secretKey, iv);

        // Descriptografando
        String decryptedCpf = CryptoUtils.decrypt(encryptedCpf, secretKey, iv);

        // Verificando se os resultados são válidos
        assertNotNull(encryptedCpf, "Encrypted data should not be null");
        assertEquals(originalCpf, decryptedCpf, "Decrypted data should match the original CPF");
    }

    // Teste de descriptografia com chave errada
    @Test
    void testDecryptWithWrongKey() throws Exception {
        String originalCpf = "12345678900";

        // Gerando chave e IV
        SecretKey secretKey = CryptoUtils.generateSecretKey();
        IvParameterSpec iv = CryptoUtils.generateIv();

        // Criptografando
        String encryptedCpf = CryptoUtils.encrypt(originalCpf, secretKey, iv);

        // Gerando uma chave errada para testar falha na descriptografia
        SecretKey wrongSecretKey = CryptoUtils.generateSecretKey();

        // Testando a descriptografia com a chave errada
        assertThrows(Exception.class, () -> {
            CryptoUtils.decrypt(encryptedCpf, wrongSecretKey, iv);
        });
    }

    // Teste para realizar o login no Auth0 e gerar o token automaticamente
    @Test
    void testGenerateAuth0Token() throws IOException {
        String accessToken = getAuth0Token();

        // Verificando se o token gerado não é nulo
        assertNotNull(accessToken, "Access token should not be null");
    }

    // Método para obter o token de acesso do Auth0
    private String getAuth0Token() throws IOException {
        // Montando o corpo da requisição
        String jsonBody = "{ \"grant_type\": \"client_credentials\", " +
                "\"client_id\": \"" + CLIENT_ID + "\", " +
                "\"client_secret\": \"" + CLIENT_SECRET + "\", " +
                "\"audience\": \"" + API_IDENTIFIER + "\" }";

        // Criando a requisição HTTP POST para o Auth0
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(AUTH0_DOMAIN + "/oauth/token");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(jsonBody));

            // Executando a requisição e pegando a resposta
            HttpResponse response = client.execute(post);
            String responseString = EntityUtils.toString(response.getEntity());

            // Verificando o código de status
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Failed to get Auth0 token. Response: " + responseString);
            }

            // Extraindo o token de acesso da resposta JSON
            try {
                JSONObject jsonResponse = new JSONObject(responseString);
                return jsonResponse.getString("access_token");
            } catch (JSONException e) {
                throw new IOException("Failed to parse JSON response from Auth0", e);
            }
        }
    }
}
