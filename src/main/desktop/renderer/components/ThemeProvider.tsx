import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../config/store'; // Import new hooks
import { fetchTheme } from '../reducers/theme';
import { Spinner } from 'react-bootstrap';

const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { theme, isLoading } = useAppSelector(state => state.theme);
  const [isClient, setIsClient] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    const initializeTheme = async () => {
      setIsClient(true);
      await dispatch(fetchTheme());
    };

    initializeTheme();
  }, [dispatch]);

  if (!isClient || isLoading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
        <Spinner animation="border" role="status" />
      </div>
    );
  }

  return <div className={theme === 'dark' ? 'dark-theme' : 'light-theme'}>{children}</div>;
};

export default ThemeProvider;
