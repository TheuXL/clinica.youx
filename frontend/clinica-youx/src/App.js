import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import CadastroPaciente from './components/CadastroPaciente';
import MapaPacientes from './components/MapaPacientes';
import Home from './views/Home';
import { useAuth } from './auth/AuthProvider';
import './App.css';

const PrivateRoute = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuth();
  if (isLoading) return <p>Carregando...</p>;
  return isAuthenticated ? children : <Navigate to="/" />;
};

const App = () => {
  return (
    <Router>
      <div>
        <h1>Aplicação de Gestão de Pacientes</h1>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route
            path="/cadastro"
            element={
              <PrivateRoute>
                <CadastroPaciente />
              </PrivateRoute>
            }
          />
          <Route
            path="/mapa"
            element={
              <PrivateRoute>
                <MapaPacientes />
              </PrivateRoute>
            }
          />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
