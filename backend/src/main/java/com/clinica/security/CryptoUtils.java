package com.clinica.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;
import java.security.SecureRandom;

public class CryptoUtils {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"; // Usando CBC mode
    private static final int KEY_SIZE = 128; // Tamanho da chave (pode ser 128, 192 ou 256 bits)
    private static final int IV_SIZE = 16; // Tamanho do IV para AES (16 bytes)

    /**
     * Gera uma chave AES segura aleatória.
     * @return A chave gerada.
     * @throws Exception
     */
    public static SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE); // 128 bits por padrão
        return keyGenerator.generateKey();
    }

    /**
     * Gera um vetor de inicialização (IV) aleatório.
     * @return O vetor de inicialização (IV) gerado.
     * @throws Exception
     */
    public static IvParameterSpec generateIv() throws Exception {
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Criptografa os dados com AES utilizando CBC.
     * @param data Os dados a serem criptografados.
     * @param secretKey A chave AES.
     * @param iv O vetor de inicialização (IV).
     * @return Os dados criptografados, codificados em Base64.
     * @throws Exception
     */
    public static String encrypt(String data, SecretKey secretKey, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Descriptografa os dados com AES utilizando CBC.
     * @param encryptedData Os dados criptografados codificados em Base64.
     * @param secretKey A chave AES.
     * @param iv O vetor de inicialização (IV).
     * @return Os dados descriptografados.
     * @throws Exception
     */
    public static String decrypt(String encryptedData, SecretKey secretKey, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    /**
     * Criptografa o CPF do paciente antes de armazená-lo no banco de dados.
     * @param cpf O CPF a ser criptografado.
     * @param secretKey A chave AES usada para criptografia.
     * @param iv O vetor de inicialização (IV) usado para a criptografia.
     * @return O CPF criptografado.
     * @throws Exception
     */
    public static String encryptCpf(String cpf, SecretKey secretKey, IvParameterSpec iv) throws Exception {
        return encrypt(cpf, secretKey, iv); // Criptografa o CPF utilizando o mesmo método
    }

    /**
     * Descriptografa o CPF do paciente ao recuperá-lo do banco de dados.
     * @param encryptedCpf O CPF criptografado.
     * @param secretKey A chave AES usada para descriptografar.
     * @param iv O vetor de inicialização (IV) usado para a descriptografia.
     * @return O CPF original.
     * @throws Exception
     */
    public static String decryptCpf(String encryptedCpf, SecretKey secretKey, IvParameterSpec iv) throws Exception {
        return decrypt(encryptedCpf, secretKey, iv); // Descriptografa o CPF utilizando o mesmo método
    }

    // Exemplo de uso
    public static void main(String[] args) throws Exception {
        // Gerando chave e IV
        SecretKey secretKey = generateSecretKey();
        IvParameterSpec iv = generateIv();

        // Exemplo de CPF
        String cpfOriginal = "12345678909"; // CPF fictício

        // Criptografando CPF
        String encryptedCpf = encryptCpf(cpfOriginal, secretKey, iv);
        System.out.println("Encrypted CPF: " + encryptedCpf);

        // Descriptografando CPF
        String decryptedCpf = decryptCpf(encryptedCpf, secretKey, iv);
        System.out.println("Decrypted CPF: " + decryptedCpf);
    }
}
