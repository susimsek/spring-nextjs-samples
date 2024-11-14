import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { IRootState } from '../config/store';
import { setTheme } from '../reducers/theme';

const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const theme = useSelector((state: IRootState) => state.theme.theme);
  const [isClient, setIsClient] = useState(false);
  const dispatch = useDispatch();

  useEffect(() => {
    const initializeTheme = async () => {
      setIsClient(true);

      const storedTheme = await window.ipc.getTheme();
      if (storedTheme) {
        dispatch(setTheme(storedTheme));
      }
    };

    initializeTheme();
  }, [dispatch]);

  if (!isClient) {
    return null;
  }

  return <div className={theme === 'dark' ? 'dark-theme' : 'light-theme'}>{children}</div>;
};

export default ThemeProvider;
