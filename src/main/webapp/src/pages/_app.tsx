// /pages/_app.tsx
import { AppProps } from 'next/app';
import '../styles/global.scss';
import React from 'react';
import store from '@/config/store';
import { Provider } from 'react-redux';
import { appWithTranslation } from 'next-i18next';
import ToastContainer from '@/components/ToastContainer';
import ThemeProvider from '@/components/ThemeProvider';
import { ApolloProvider } from '@apollo/client';
import client from '@/config/apolloClient';
import { loadIcons } from '@/config/iconLoader';

loadIcons();

const MyApp = ({ Component, pageProps }: AppProps) => (
  <Provider store={store}>
    <ApolloProvider client={client}>
      <ThemeProvider>
        <ToastContainer />
        <Component {...pageProps} />
      </ThemeProvider>
    </ApolloProvider>
  </Provider>
);

export default appWithTranslation(MyApp);
