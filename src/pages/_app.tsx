// pages/_app.tsx
import { AppProps } from 'next/app';
import '../styles/global.scss';
import React from 'react';
import store from '@/config/store';
import { Provider } from 'react-redux';
import { appWithTranslation } from 'next-i18next';

const MyApp = ({ Component, pageProps }: AppProps) => (
    <Provider store={store}>
      <Component {...pageProps} />
    </Provider>
)

export default appWithTranslation(MyApp)
