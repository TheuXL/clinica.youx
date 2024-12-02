import React from "react";
import { useAuth } from "../auth/AuthProvider";

const LoginButton = () => {
  const { login } = useAuth();

  return <button onClick={login}>Login</button>;
};

export default LoginButton;
