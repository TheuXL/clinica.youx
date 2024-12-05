// src/auth/AuthProvider.js
import React, { createContext, useContext } from "react";
import { useAuth0 } from "@auth0/auth0-react";

// Criação do contexto de autenticação
const AuthContext = createContext();

// Provedor de autenticação
export const AuthProvider = ({ children }) => {
  const {
    loginWithRedirect,
    logout,
    user,
    isAuthenticated,
    isLoading,
    getIdTokenClaims,
  } = useAuth0();

  const login = () => loginWithRedirect();
  const logoutUser = () => logout({ returnTo: window.location.origin });
  const getToken = async () => {
    const claims = await getIdTokenClaims();
    return claims?.__raw;
  };

  return (
    <AuthContext.Provider
      value={{
        login,
        logoutUser,
        user,
        isAuthenticated,
        isLoading,
        getToken,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

// Hook para usar o contexto de autenticação
export const useAuth = () => useContext(AuthContext);
