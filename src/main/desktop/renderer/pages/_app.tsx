// src/pages/_app.tsx
import { AppProps } from 'next/app';
import '../styles/global.scss';
import { appWithTranslation } from 'next-i18next';
import { loadIcons } from '../config/iconLoader';
import store from '../config/store';
import { Provider } from 'react-redux';
import ThemeProvider from '../components/ThemeProvider';
import AuthProvider from '../components/AuthProvider';
import client from '../config/apolloClient';
import ToastContainer from '../components/ToastContainer';
import { ApolloProvider } from '@apollo/client';

loadIcons();

const MyApp = ({ Component, pageProps }: AppProps) => (
  <Provider store={store}>
    <AuthProvider>
      <ApolloProvider client={client}>
        <ThemeProvider>
          <ToastContainer />
          <Component {...pageProps} />
        </ThemeProvider>
      </ApolloProvider>
    </AuthProvider>
  </Provider>
);

export default appWithTranslation(MyApp);
