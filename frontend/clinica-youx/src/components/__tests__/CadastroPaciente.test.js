// src/components/__tests__/CadastroPaciente.test.js

/**
 * Testa o componente CadastroPaciente.
 * 
 * Esse arquivo contém os testes para garantir que o formulário de cadastro de paciente
 * esteja funcionando corretamente. Verificamos se os campos obrigatórios estão presentes, 
 * se o formulário é enviado corretamente, e se a validação de campos obrigatórios é feita.
 * 
 * @module CadastroPacienteTest
 */

import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import CadastroPaciente from '../CadastroPaciente';
import api from '../../services/api';

// Mock da API para simular o comportamento da requisição
jest.mock('../../services/api');

/**
 * Teste para garantir que o formulário de cadastro seja renderizado corretamente.
 * Verifica se todos os campos obrigatórios estão presentes no formulário.
 */
it('deve renderizar o formulário de cadastro corretamente', () => {
  render(<CadastroPaciente />);

  expect(screen.getByLabelText(/Nome/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/CPF/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/Data de Nascimento/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/Estado/i)).toBeInTheDocument();
});

/**
 * Teste para verificar se o botão de envio está presente e acessível.
 */
it('deve ter o botão de cadastro', () => {
  render(<CadastroPaciente />);

  const button = screen.getByText(/Cadastrar/i);
  expect(button).toBeInTheDocument();
});

/**
 * Teste para simular o envio do formulário com dados válidos.
 * Verifica se a API foi chamada corretamente com os dados preenchidos no formulário.
 */
it('deve enviar o formulário com dados válidos', async () => {
  render(<CadastroPaciente />);

  fireEvent.change(screen.getByLabelText(/Nome/i), { target: { value: 'João Silva' } });
  fireEvent.change(screen.getByLabelText(/CPF/i), { target: { value: '12345678901' } });
  fireEvent.change(screen.getByLabelText(/Data de Nascimento/i), { target: { value: '1990-01-01' } });
  fireEvent.change(screen.getByLabelText(/Estado/i), { target: { value: 'SP' } });

  fireEvent.click(screen.getByText(/Cadastrar/i));

  expect(api.post).toHaveBeenCalledWith('/pacientes', expect.objectContaining({
    nome: 'João Silva',
    cpf: '12345678901',
    dataNascimento: '1990-01-01',
    estado: 'SP',
  }));
});

/**
 * Teste para garantir que campos obrigatórios não podem ser enviados vazios.
 * Se algum campo obrigatório estiver vazio, deve ser exibida uma mensagem de erro.
 */
it('não deve permitir enviar o formulário com campos obrigatórios vazios', () => {
  render(<CadastroPaciente />);

  fireEvent.click(screen.getByText(/Cadastrar/i));

  expect(screen.getByText(/Todos os campos obrigatórios devem ser preenchidos./i)).toBeInTheDocument();
});
