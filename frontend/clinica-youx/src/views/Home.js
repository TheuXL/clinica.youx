// src/views/Home.js
import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div className="home-container">
      <h1>Bem-vindo à Clínica YOUX</h1>
      <p>Escolha uma das opções abaixo para continuar:</p>
      
      <div className="home-buttons">
        <Link to="/cadastro" className="home-button">
          Cadastrar Paciente
        </Link>
        <Link to="/mapa" className="home-button">
          Ver Mapa de Pacientes
        </Link>
      </div>
    </div>
  );
};

export default Home;
