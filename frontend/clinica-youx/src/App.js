// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import CadastroPaciente from './components/CadastroPaciente';
import MapaPacientes from './components/MapaPacientes';
import Home from './views/Home'; // Importando a página Home

const App = () => {
  return (
    <Router>
      <div>
        <h1>Aplicação de Gestão de Pacientes</h1>
        <Switch>
          <Route exact path="/" component={Home} /> {/* Página inicial */}
          <Route path="/cadastro" component={CadastroPaciente} />
          <Route path="/mapa" component={MapaPacientes} />
        </Switch>
      </div>
    </Router>
  );
};

export default App;
