// src/services/api.js
import axios from 'axios';

// Criação do Axios com a URL base da API
const api = axios.create({
  baseURL: 'http://localhost:3000',  // Endereço do backend
  headers: {
    'Content-Type': 'application/json',
  },
});

// Adicionando token de autenticação OAuth 2
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');  // Token armazenado no localStorage
  if (token) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});

export default api;
