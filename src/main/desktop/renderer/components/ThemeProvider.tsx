import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../config/store'; // Import new hooks
import { fetchTheme } from '../reducers/theme';

const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const theme = useAppSelector(state => state.theme.theme);
  const [isClient, setIsClient] = useState(false);
  const dispatch = useAppDispatch();

  useEffect(() => {
    const initializeTheme = async () => {
      setIsClient(true);
      await dispatch(fetchTheme());
    };

    initializeTheme();
  }, [dispatch]);

  if (!isClient) {
    return null;
  }

  return <div className={theme === 'dark' ? 'dark-theme' : 'light-theme'}>{children}</div>;
};

export default ThemeProvider;
