// src/components/MapaPacientes.js
import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import api from '../services/api';

const MapaPacientes = () => {
  const [pacientesPorEstado, setPacientesPorEstado] = useState([]);

  useEffect(() => {
    const fetchDados = async () => {
      try {
        const response = await api.get('/pacientes/estados');
        setPacientesPorEstado(response.data);
      } catch (error) {
        console.error('Erro ao buscar dados:', error);
      }
    };

    fetchDados();
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