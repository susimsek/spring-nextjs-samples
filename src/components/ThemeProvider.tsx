// components/ThemeProvider.tsx
import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { IRootState } from '@/config/store';

const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const theme = useSelector((state: IRootState) => state.theme.theme);
  const [isClient, setIsClient] = useState(false);

  // Check if we are on the client side
  useEffect(() => {
    setIsClient(true);
  }, []);

  // Render only after confirming we are on the client side
  if (!isClient) {
    return null; // Render nothing on the server side initially
  }

  return <div className={theme === 'dark' ? 'dark-theme' : 'light-theme'}>{children}</div>;
};

export default ThemeProvider;
