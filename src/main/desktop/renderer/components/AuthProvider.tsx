// src/components/AuthProvider.tsx
import React, { useEffect, useState } from 'react';
import { useAppDispatch } from '../config/store';
import { setToken } from '../reducers/authentication';
import { Spinner } from 'react-bootstrap';

interface AuthProviderProps {
  children: React.ReactNode;
}

const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initializeAuth = async () => {
      const storedToken = await window.ipc.getToken();
      if (storedToken) {
        dispatch(setToken(storedToken));
      }
      setLoading(false);
    };
    initializeAuth();
  }, [dispatch]);

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
        <Spinner animation="border" role="status" />
      </div>
    );
  }

  return <>{children}</>;
};

export default AuthProvider;
