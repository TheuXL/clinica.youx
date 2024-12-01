// src/components/CadastroPaciente.js
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
    // Carregar os estados do Brasil usando o serviço do IBGE
    const fetchEstados = async () => {
      try {
        const response = await api.get('/estados');
        setEstados(response.data);
      } catch (error) {
        console.error('Erro ao carregar estados:', error);
      }
    };

    fetchEstados();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!nome || !cpf || !dataNascimento || !estado) {
      setError('Todos os campos obrigatórios devem ser preenchidos.');
      return;
    }

    try {
      // Enviar os dados para o backend
      await api.post('/pacientes', { nome, cpf, dataNascimento, peso, altura, estado });
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
        <div>
          <label>Nome</label>
          <input type="text" value={nome} onChange={(e) => setNome(e.target.value)} required />
        </div>
        <div>
          <label>CPF</label>
          <input type="text" value={cpf} onChange={(e) => setCpf(e.target.value)} required />
        </div>
        <div>
          <label>Data de Nascimento</label>
          <input type="date" value={dataNascimento} onChange={(e) => setDataNascimento(e.target.value)} required />
        </div>
        <div>
          <label>Peso</label>
          <input type="number" value={peso} onChange={(e) => setPeso(e.target.value)} />
        </div>
        <div>
          <label>Altura</label>
          <input type="number" value={altura} onChange={(e) => setAltura(e.target.value)} />
        </div>
        <div>
          <label>Estado</label>
          <select value={estado} onChange={(e) => setEstado(e.target.value)} required>
            <option value="">Selecione um estado</option>
            {estados.map((estado) => (
              <option key={estado.sigla} value={estado.sigla}>
                {estado.nome}
              </option>
            ))}
          </select>
        </div>
        <button type="submit">Cadastrar</button>
      </form>
    </div>
  );
};

export default CadastroPaciente;
