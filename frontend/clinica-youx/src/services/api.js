import axios from "axios";

// Configuração do Axios com URL base da API
const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL, // Lê a URL da API do .env
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor para adicionar token de autenticação
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token"); // Lê o token armazenado
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

export default api;
