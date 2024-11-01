// /pages/_app.tsx
import { AppProps } from 'next/app';
import '../styles/global.scss';
import React from 'react';
import store, {IRootState} from '@/config/store';
import {Provider, useSelector} from 'react-redux';
import { appWithTranslation } from 'next-i18next';
import ToastContainer from "@/components/ToastContainer";

const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const theme = useSelector((state: IRootState) => state.theme.theme);

  return <div className={theme === 'dark' ? 'dark-theme' : 'light-theme'}>{children}</div>;
};

const MyApp = ({ Component, pageProps }: AppProps) => (
  <Provider store={store}>
    <ThemeProvider>
      <ToastContainer />
      <Component {...pageProps} />
    </ThemeProvider>
  </Provider>
);

export default appWithTranslation(MyApp);
