### **README - Clinica API**

---

## **Descrição do Projeto**

A **Clinica API** é um sistema backend desenvolvido em **Spring Boot** para gerenciar operações de uma clínica, incluindo gerenciamento de pacientes, usuários (médicos e enfermeiros), autenticação e autorização via OAuth2 (integrado com Auth0), e armazenamento seguro de dados sensíveis como CPF.

---

## **Estrutura do Projeto**

### **Diretórios Principais**

- **`src/main/java/com/clinica`**: Código-fonte principal, incluindo controladores, serviços, modelos, e repositórios.
- **`src/main/resources`**: Arquivos de configuração, como `application.properties`.
- **`src/test/java/com/clinica/tests`**: Testes unitários e de integração.
- **`banco/db.sql`**: Script para configuração inicial do banco de dados.

### **Principais Componentes**

#### **1. Modelos**
Os modelos representam as entidades do sistema:
- **`Usuario`**: Representa um usuário (enfermeiro ou médico). Cada usuário possui CPF, senha e associações a roles.
- **`Paciente`**: Representa pacientes com informações como nome, CPF (encriptado), peso, altura e estado.

#### **2. Repositórios**
- **`UsuarioRepository`** e **`PacienteRepository`**: Interfaces que gerenciam a interação com o banco de dados. Por exemplo:
  ```java
  boolean existsByCpf(String cpf);
  ```

#### **3. Serviços**
- **`PacienteService`**: Contém a lógica de negócios para operações com pacientes, como criar novos registros e validar CPF duplicado.

#### **4. Segurança**
- Configurado para usar **OAuth2** via Auth0 como provedor de autenticação. Tokens JWT são usados para autenticar e autorizar usuários.

---

## **Dependências**

O projeto usa **Maven** para gerenciar dependências. Aqui estão as principais:
- **Spring Boot**: Framework principal para criar aplicações Java.
- **Spring Security OAuth2**: Para autenticação e autorização.
- **MySQL**: Banco de dados relacional.
- **Lombok**: Redução de código boilerplate.
- **Jasypt**: Criptografia de dados sensíveis.

Veja a configuração completa no arquivo `pom.xml`.

---

## **Configuração do Ambiente**

### **Pré-requisitos**
1. **Java 17** ou superior.
2. **Maven** instalado.
3. **MySQL** configurado e em execução.
4. Uma conta configurada no **Auth0** com as seguintes informações:
   - **Client ID**
   - **Client Secret**
   - **Audience**
   - **Issuer URI**

---

### **Passos para Configuração**

#### **1. Banco de Dados**
1. Crie um banco de dados MySQL com o nome `clinica`:
   ```sql
   CREATE DATABASE clinica;
   ```
2. Execute o script SQL localizado em `banco/db.sql` para criar as tabelas e os dados iniciais:
   ```bash
   mysql -u root -p clinica < banco/db.sql
   ```

#### **2. Configuração do `application.properties`**
Edite o arquivo `src/main/resources/application.properties` para incluir os dados do seu banco de dados e do Auth0:

```properties
# Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/clinica
spring.datasource.username=<SEU_USUARIO_MYSQL>
spring.datasource.password=<SUA_SENHA_MYSQL>

# Auth0 Configuração
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://<SEU_DOMINIO_AUTH0>/
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://<SEU_DOMINIO_AUTH0>/.well-known/jwks.json
```

> **Nota:** Substitua `<SEU_DOMINIO_AUTH0>` pelo domínio configurado no Auth0.

#### **3. Configuração de Variáveis Sensíveis**
Use `jasypt` para encriptar senhas sensíveis, como credenciais do banco. Adicione o seguinte ao seu arquivo de configuração:

```properties
jasypt.encryptor.password=<CHAVE_SECRET>
```

---

## **Rodando a Aplicação**

### **1. Construção do Projeto**
Na raiz do projeto, execute:
```bash
mvn clean install
```

### **2. Inicializando a Aplicação**
Inicie a aplicação:
```bash
mvn spring-boot:run
```

### **3. Testando a Aplicação**
- Acesse a API usando ferramentas como **Postman** ou **cURL**.
- Endpoints principais:
  - **GET** `/api/pacientes`: Retorna a lista de pacientes (requere autenticação).
  - **POST** `/api/pacientes`: Cria um novo paciente (requere role `ROLE_ENFERMEIRO` ou `ROLE_MEDICO`).

---

## **Testes**

### **Testes Unitários**
O projeto inclui testes para validar a lógica de criptografia e os serviços. Exemplos:
1. **Teste de criptografia:**
   ```java
   @Test
   void testEncryptAndDecrypt() throws Exception {
       String originalCpf = "12345678900";
       SecretKey secretKey = CryptoUtils.generateSecretKey();
       IvParameterSpec iv = CryptoUtils.generateIv();
       String encryptedCpf = CryptoUtils.encrypt(originalCpf, secretKey, iv);
       String decryptedCpf = CryptoUtils.decrypt(encryptedCpf, secretKey, iv);
       assertEquals(originalCpf, decryptedCpf);
   }
   ```

2. **Teste de criação de pacientes:**
   ```java
   @Test
   void criarPacienteComSucesso() {
       Paciente paciente = new Paciente();
       paciente.setNome("João Silva");
       when(pacienteRepository.existsByCpf("12345678900")).thenReturn(false);
       Paciente resultado = pacienteService.criarPaciente(paciente);
       assertEquals("João Silva", resultado.getNome());
   }
   ```

### **Executando Testes**
Para rodar os testes:
```bash
mvn test
```

---

## **Segurança**

### **JWT e OAuth2**
A aplicação utiliza tokens JWT fornecidos pelo Auth0 para autenticação e autorização. A role do usuário define o acesso aos endpoints:
- **ROLE_ENFERMEIRO**: Acesso à criação e consulta de pacientes.
- **ROLE_MEDICO**: Acesso às operações de leitura.

### **Criptografia**
Os CPFs dos pacientes são criptografados antes de serem salvos no banco, garantindo a segurança de dados sensíveis.

---

## **Contribuição**

1. Faça um **fork** deste repositório.
2. Crie uma **branch** para sua feature:
   ```bash
   git checkout -b minha-feature
   ```
3. Faça as alterações e adicione os commits:
   ```bash
   git commit -m "Minha nova feature"
   ```
4. Envie para sua branch:
   ```bash
   git push origin minha-feature
   ```
5. Abra um **pull request**.

