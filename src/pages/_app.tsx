// /pages/_app.tsx
import { AppProps } from 'next/app';
import '../styles/global.scss';
import React from 'react';
import store from '@/config/store';
import { Provider } from 'react-redux';
import { appWithTranslation } from 'next-i18next';
import NotificationToast from '@/components/NotificationToast';
import { useAppDispatch, useAppSelector } from '@/config/store';
import { hideNotification } from '@/reducers/notification';

const MyApp = ({ Component, pageProps }: AppProps) => (
  <Provider store={store}>
    <GlobalNotification />
    <Component {...pageProps} />
  </Provider>
);

function GlobalNotification() {
  const toast = useAppSelector((state) => state.notification);
  const dispatch = useAppDispatch();

  const closeToast = () => dispatch(hideNotification());

  return (
    <NotificationToast
      message={toast.message}
      messageKey={toast.messageKey}
      show={toast.show}
      onClose={closeToast}
      variant={toast.variant}
      dismissible={true}
      size="lg"
    />
  );
}

export default appWithTranslation(MyApp);
