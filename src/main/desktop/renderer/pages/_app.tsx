import { AppProps } from 'next/app';
import '../styles/global.scss';
import { appWithTranslation } from 'next-i18next';
import { loadIcons } from '../config/iconLoader';
import store from '../config/store';
import { Provider } from 'react-redux';
import ThemeProvider from '../components/ThemeProvider';

loadIcons();

const MyApp = ({ Component, pageProps }: AppProps) => (
  <Provider store={store}>
    <ThemeProvider>
      <Component {...pageProps} />
    </ThemeProvider>
  </Provider>
);

export default appWithTranslation(MyApp);
