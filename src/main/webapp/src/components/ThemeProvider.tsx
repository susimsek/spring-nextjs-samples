// components/ThemeProvider.tsx
import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { IRootState } from '@/config/store';
import { Spinner } from 'react-bootstrap';

const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const theme = useSelector((state: IRootState) => state.theme.theme);
  const [isClient, setIsClient] = useState(false);

  // Check if we are on the client side
  useEffect(() => {
    setIsClient(true);
  }, []);

  // Render only after confirming we are on the client side
  if (!isClient) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
        <Spinner animation="border" role="status" />
      </div>
    );
  }

  return <div className={theme === 'dark' ? 'dark-theme' : 'light-theme'}>{children}</div>;
};

export default ThemeProvider;
