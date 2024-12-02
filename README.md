### **README - Clinica API**

---

## **Descrição do Projeto**

A **Clinica API** é um sistema backend desenvolvido em **Spring Boot** para gerenciar operações de uma clínica, incluindo gerenciamento de pacientes, usuários (médicos e enfermeiros), autenticação e autorização via OAuth2 (integrado com Auth0), e armazenamento seguro de dados sensíveis como CPF.

( "Para quem for analisar o teste, leve em consideração que, infelizmente, por conta de problemas pessoais, eu não consegui desenvolver o projeto 100%, mas dei o meu melhor para mostrar minhas habilidades e, ao mesmo tempo, apresentar minha forma de pensar e como foi minha ideia para desenvolver este projeto. Deixei tudo bem detalhado para facilitar a compreensão, Agradeço a compreenção" )

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



---

### **Frontend - Clinica YOUX**

---

## **Descrição do Projeto**

O **Frontend da Clinica YOUX** é uma aplicação desenvolvida com **React** que se comunica com o backend da clínica para realizar o gerenciamento de pacientes, incluindo o cadastro de novos pacientes, visualização de pacientes por estado, e exibição de um mapa com a distribuição dos pacientes.

A autenticação é realizada via **Auth0** com OAuth2, e a aplicação oferece interações para **médicos** e **enfermeiros** gerenciarem as informações dos pacientes de maneira fácil e intuitiva.

---

## **Estrutura do Projeto**

### **Diretórios Principais**

- **`src`**: Diretório contendo todos os arquivos-fonte da aplicação.
  - **`auth`**: Diretório com arquivos relacionados à autenticação.
  - **`components`**: Componentes reutilizáveis para a interface do usuário.
  - **`services`**: Arquivos relacionados à comunicação com o backend (API).
  - **`views`**: Arquivos para as diferentes páginas da aplicação.
  - **`__tests__`**: Contém os arquivos de teste para os componentes.

---

## **Principais Componentes**

### **1. AuthProvider.js**
O `AuthProvider` é um componente que gerencia o estado de autenticação na aplicação, usando o **Auth0** para login e logout. Ele utiliza o contexto do React para compartilhar o estado de autenticação entre os componentes.

#### **Explicação do Código**:

```javascript
import { createContext, useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const { loginWithRedirect, logout, user, isAuthenticated, isLoading } = useAuth0();
  const navigate = useNavigate();

  const login = () => loginWithRedirect();  // Método para redirecionar o usuário ao login
  const logoutUser = () => logout({ returnTo: window.location.origin });  // Método para logout

  return (
    <AuthContext.Provider value={{ login, logoutUser, user, isAuthenticated, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
```

- **`AuthProvider`**: Envolve a aplicação e fornece informações de autenticação para todos os componentes.
- **`useAuth`**: Hook customizado para acessar o contexto de autenticação em outros componentes.

---

### **2. CadastroPaciente.js**
O componente `CadastroPaciente` permite cadastrar um paciente no sistema. Ele usa o hook `useState` para armazenar os dados do paciente e `useEffect` para buscar os estados do Brasil.

#### **Explicação do Código**:

```javascript
import React, { useState, useEffect } from 'react';
import api from '../services/api';

const CadastroPaciente = () => {
  const [nome, setNome] = useState('');
  const [cpf, setCpf] = useState('');
  const [dataNascimento, setDataNascimento] = useState('');
  const [peso, setPeso] = useState('');
  const [altura, setAltura] = useState('');
  const [estado, setEstado] = useState('');
  const [estados, setEstados] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchEstados = async () => {
      try {
        const response = await api.get('/estados');
        setEstados(response.data);  // Armazena os estados na variável de estado
      } catch (error) {
        console.error('Erro ao carregar estados:', error);
      }
    };

    fetchEstados();  // Carrega os estados ao montar o componente
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!nome || !cpf || !dataNascimento || !estado) {
      setError('Todos os campos obrigatórios devem ser preenchidos.');
      return;
    }

    try {
      await api.post('/pacientes', { nome, cpf, dataNascimento, peso, altura, estado });  // Envia os dados para o backend
      alert('Paciente cadastrado com sucesso!');
    } catch (error) {
      setError('Erro ao cadastrar paciente');
    }
  };

  return (
    <div>
      <h2>Cadastro de Paciente</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        {/* Campos de formulário para dados do paciente */}
        <button type="submit">Cadastrar</button>
      </form>
    </div>
  );
};

export default CadastroPaciente;
```

- **useState**: Controla os dados do paciente no estado.
- **useEffect**: Carrega os estados do Brasil quando o componente é montado.
- **handleSubmit**: Função que envia os dados para o backend, validando os campos obrigatórios.

---

### **3. ListarPacientesPorEstado.js**
Esse componente exibe uma lista de pacientes por estado, mostrando a quantidade de pacientes e sua localização geográfica.

#### **Explicação do Código**:

```javascript
import React, { useEffect, useState } from "react";
import axios from "axios";

const ListarPacientesPorEstado = () => {
  const [pacientesPorEstado, setPacientesPorEstado] = useState([]);

  useEffect(() => {
    const fetchPacientes = async () => {
      try {
        const response = await axios.get("/api/pacientes/estados");
        setPacientesPorEstado(response.data);  // Armazena os dados dos pacientes por estado
      } catch (error) {
        console.error(error);
        alert("Erro ao buscar pacientes por estado");
      }
    };

    fetchPacientes();  // Executa a busca assim que o componente é montado
  }, []);

  return (
    <div>
      <h2>Pacientes por Estado</h2>
      <table>
        <thead>
          <tr>
            <th>Estado</th>
            <th>Quantidade</th>
            <th>Latitude</th>
            <th>Longitude</th>
          </tr>
        </thead>
        <tbody>
          {pacientesPorEstado.map((estado) => (
            <tr key={estado.estado}>
              <td>{estado.estado}</td>
              <td>{estado.quantidade}</td>
              <td>{estado.latitude}</td>
              <td>{estado.longitude}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ListarPacientesPorEstado;
```

- **useEffect**: Carrega os dados de pacientes por estado quando o componente é montado.
- **Exibição dos Dados**: Exibe os pacientes por estado em uma tabela.

---

### **4. MapaPacientes.js**
O componente `MapaPacientes` utiliza o **Leaflet** para exibir um mapa com marcadores dos pacientes distribuídos por estado.

#### **Explicação do Código**:

```javascript
import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import api from '../services/api';

const MapaPacientes = () => {
  const [pacientesPorEstado, setPacientesPorEstado] = useState([]);

  useEffect(() => {
    const fetchDados = async () => {
      try {
        const response = await api.get('/pacientes/estados');
        setPacientesPorEstado(response.data);  // Armazena os dados para renderizar os marcadores no mapa
      } catch (error) {
        console.error('Erro ao buscar dados:', error);
      }
    };

    fetchDados();  // Carrega os dados assim que o componente é montado
  }, []);

  return (
    <div>
      <h2>Mapa de Pacientes por Estado</h2>
      <MapContainer center={[51.505, -0.09]} zoom={6} style={{ height: '400px' }}>
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        {pacientesPorEstado.map((estado, index) => (
          <Marker key={index} position={[estado.latitude, estado.longitude]}>
            <Popup>
              {estado.nome}: {estado.quantidade} pacientes
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
};

export default MapaPacientes;
```

- **MapContainer**: Cria o container para o mapa com o Leaflet.
- **Marker**: Adiciona marcadores no mapa com base nos dados dos pacientes.

---

## **Testes**

O projeto inclui testes para garantir a funcionalidade correta do formulário de cadastro de pacientes. Estes testes foram escritos usando o **Jest** e o **React Testing Library**.

### **1. CadastroPaciente.test.js**

Este arquivo contém testes unitários para o componente `CadastroPaciente`. Ele garante que o formulário de cadastro esteja funcionando corretamente, verificando a presença dos campos obrigatórios, o envio dos dados para a API e a validação de erros.

#### **Testes Incluídos

**:

- **Renderização do Formulário**: Verifica se todos os campos obrigatórios estão presentes.
- **Envio de Dados**: Verifica se a API é chamada corretamente com os dados do formulário.
- **Validação de Campos**: Garante que o formulário não seja enviado se os campos obrigatórios estiverem vazios.
- **Validação de CPF Único**: Simula um erro quando o CPF já está cadastrado e garante que a mensagem de erro seja exibida.

```javascript
it('deve enviar o formulário com dados válidos', async () => {
  render(<CadastroPaciente />);
  
  fireEvent.change(screen.getByLabelText(/Nome/i), { target: { value: 'João Silva' } });
  fireEvent.change(screen.getByLabelText(/CPF/i), { target: { value: '12345678901' } });
  fireEvent.change(screen.getByLabelText(/Data de Nascimento/i), { target: { value: '1990-01-01' } });
  fireEvent.change(screen.getByLabelText(/Estado/i), { target: { value: 'SP' } });
  fireEvent.change(screen.getByLabelText(/Peso/i), { target: { value: '70' } });
  fireEvent.change(screen.getByLabelText(/Altura/i), { target: { value: '1.75' } });
  
  fireEvent.click(screen.getByText(/Cadastrar/i));

  await waitFor(() => {
    expect(api.post).toHaveBeenCalledWith('/pacientes', expect.objectContaining({
      nome: 'João Silva',
      cpf: '12345678901',
      dataNascimento: '1990-01-01',
      estado: 'SP',
      peso: '70',
      altura: '1.75'
    }));
  });
});
```

---

Este **README** abrange os detalhes do frontend da **Clinica YOUX**, explicando cada parte do código, como a interação com o backend, a exibição de dados e a lógica dos testes.


### **Resumo Detalhado do Projeto**

**Backend (Clinica API)**  
A **Clinica API** é desenvolvida em **Spring Boot** para gerenciar pacientes e usuários (médicos e enfermeiros).  
- **Modelos**: Representam as entidades como **Usuario** e **Paciente**, com dados como CPF, nome, e informações de saúde.  
- **Repositórios**: Interagem com o banco de dados, incluindo validação de CPF único e persistência de dados.  
- **Serviços**: Contêm a lógica de negócios, como criar e validar pacientes.  
- **Segurança**: Implementa autenticação com **OAuth2** e **Auth0**, usando **JWT** para gerenciar permissões e acessos.  
- **Criptografia**: Utiliza **Jasypt** para garantir a segurança de dados sensíveis, como CPF.  
- **Banco de Dados**: Usa **MySQL** para armazenar dados. O script SQL configura as tabelas e insere dados iniciais.

**Frontend (Clinica YOUX)**  
O frontend é feito em **React** e se comunica com o backend via API.  
- **AuthProvider**: Gerencia o estado de autenticação usando **Auth0** e fornece login/logout para a aplicação.  
- **CadastroPaciente**: Permite registrar novos pacientes, validando os campos obrigatórios e enviando os dados para o backend.  
- **ListarPacientesPorEstado**: Exibe pacientes agrupados por estado, mostrando a quantidade e localização.  
- **MapaPacientes**: Usa **Leaflet** para mostrar a distribuição geográfica dos pacientes em um mapa interativo.  
- **Testes**: Utiliza **Jest** e **React Testing Library** para validar a funcionalidade dos componentes, como o envio do formulário e a validação de erros.

**Banco de Dados**  
O banco é configurado com **MySQL** e contém tabelas para pacientes e usuários.  
- **Tabelas**: Definem os campos para armazenar informações dos pacientes e usuários, incluindo dados como CPF, nome, e permissões.  
- **Script SQL**: O arquivo `banco/db.sql` cria as tabelas e insere dados iniciais.  
- **Segurança**: A criptografia do CPF é feita com **Jasypt**, garantindo que dados sensíveis sejam armazenados de forma segura.

