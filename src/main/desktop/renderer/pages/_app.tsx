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
import UpdateNotifier from '../components/UpdateNotifier';

loadIcons();

const MyApp = ({ Component, pageProps }: AppProps) => (
  <Provider store={store}>
    <ThemeProvider>
      <AuthProvider>
        <ApolloProvider client={client}>
          <ToastContainer />
          <UpdateNotifier />
          <Component {...pageProps} />
        </ApolloProvider>
      </AuthProvider>
    </ThemeProvider>
  </Provider>
);

export default appWithTranslation(MyApp);
