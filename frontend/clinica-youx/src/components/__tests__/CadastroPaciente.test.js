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
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
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
  expect(screen.getByLabelText(/Peso/i)).toBeInTheDocument();
  expect(screen.getByLabelText(/Altura/i)).toBeInTheDocument();
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

/**
 * Teste para garantir que campos obrigatórios não podem ser enviados vazios.
 * Se algum campo obrigatório estiver vazio, deve ser exibida uma mensagem de erro.
 */
it('não deve permitir enviar o formulário com campos obrigatórios vazios', () => {
  render(<CadastroPaciente />);

  fireEvent.click(screen.getByText(/Cadastrar/i));

  // Verifica se a mensagem de erro é exibida quando os campos obrigatórios não estão preenchidos
  expect(screen.getByText(/Todos os campos obrigatórios devem ser preenchidos./i)).toBeInTheDocument();
});

/**
 * Teste para verificar se o CPF é único e gera erro se o CPF já estiver cadastrado.
 */
it('deve mostrar erro se o CPF já estiver cadastrado', async () => {
  // Simula uma resposta da API indicando que o CPF já existe
  api.post.mockRejectedValueOnce({ response: { data: { message: 'CPF já cadastrado' } } });

  render(<CadastroPaciente />);

  fireEvent.change(screen.getByLabelText(/Nome/i), { target: { value: 'Maria Souza' } });
  fireEvent.change(screen.getByLabelText(/CPF/i), { target: { value: '12345678901' } });
  fireEvent.change(screen.getByLabelText(/Data de Nascimento/i), { target: { value: '1985-03-12' } });
  fireEvent.change(screen.getByLabelText(/Estado/i), { target: { value: 'RJ' } });

  fireEvent.click(screen.getByText(/Cadastrar/i));

  // Espera até que a resposta da API seja processada e exibe a mensagem de erro
  await waitFor(() => {
    expect(screen.getByText(/CPF já cadastrado/i)).toBeInTheDocument();
  });
});
