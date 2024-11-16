// src/components/AuthProvider.tsx
import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../config/store';
import { fetchToken } from '../reducers/authentication';
import { Spinner } from 'react-bootstrap';

interface AuthProviderProps {
  children: React.ReactNode;
}

const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const { isLoading } = useAppSelector(state => state.authentication);
  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    const initializeAuth = async () => {
      await dispatch(fetchToken());
      setIsInitialized(true);
    };

    initializeAuth();
  }, [dispatch]);

  if (!isInitialized || isLoading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
        <Spinner animation="border" role="status" />
      </div>
    );
  }

  return <>{children}</>;
};

export default AuthProvider;
