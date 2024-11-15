// src/components/AuthProvider.tsx
import React, { useEffect } from 'react';
import { useAppDispatch } from '../config/store';
import { fetchToken } from '../reducers/authentication';

interface AuthProviderProps {
  children: React.ReactNode;
}

const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    // Fetch token from Electron Store on app startup
    dispatch(fetchToken());
  }, [dispatch]);

  return <>{children}</>;
};

export default AuthProvider;
