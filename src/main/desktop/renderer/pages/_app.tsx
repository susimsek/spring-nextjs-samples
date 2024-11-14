import { AppProps } from 'next/app';
import '../styles/global.scss';
import { appWithTranslation } from 'next-i18next';
import { loadIcons } from '../config/iconLoader';

loadIcons();

function App({ Component, pageProps }: AppProps) {
  return (
    <>
      <Component {...pageProps} />
    </>
  );
}

export default appWithTranslation(App);
