import React, { useEffect, useState } from "react";
import axios from "axios";

const ListarPacientesPorEstado = () => {
  const [pacientesPorEstado, setPacientesPorEstado] = useState([]);

  useEffect(() => {
    const fetchPacientes = async () => {
      try {
        const response = await axios.get("/api/pacientes/estados");
        setPacientesPorEstado(response.data);
      } catch (error) {
        console.error(error);
        alert("Erro ao buscar pacientes por estado");
      }
    };

    fetchPacientes();
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
