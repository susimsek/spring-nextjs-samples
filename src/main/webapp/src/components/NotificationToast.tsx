// components/NotificationToast.tsx
import React from 'react';
import { Toast, ToastContainer } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useTranslation } from 'next-i18next';

export type TostMessage = {
  message?: string;
  messageKey?: string;
  data?: any;
  variant?: 'danger' | 'info' | 'success' | 'warning' | 'primary' | 'secondary' | 'dark' | 'light';
};

interface NotificationToastProps extends TostMessage {
  show: boolean;
  onClose?: () => void;
  dismissible?: boolean;
  size?: 'sm' | 'lg' | 'xl';
}

const NotificationToast: React.FC<NotificationToastProps> = ({
                                                               message,
                                                               messageKey,
                                                               data,
                                                               variant = 'info',
                                                               show,
                                                               onClose = () => {},
                                                               dismissible = true,
                                                               size = 'lg',
                                                             }) => {
  const { t } = useTranslation('common');
  const toastMessage = messageKey ? String(t(messageKey, data)) : message;

  const variantStyles = {
    danger: { bg: '#fdecea', color: '#a71d2a', border: '#f5c2c7', icon: 'exclamation-triangle' },
    info: { bg: '#e7f5ff', color: '#1c7ed6', border: '#b6d4fe', icon: 'info-circle' },
    success: { bg: '#d3f9d8', color: '#2f9e44', border: '#b2f2bb', icon: 'check-circle' },
    warning: { bg: '#fff3cd', color: '#856404', border: '#ffeeba', icon: 'exclamation-circle' },
    primary: { bg: '#cfe2ff', color: '#084298', border: '#b6d4fe', icon: 'info-circle' },
    secondary: { bg: '#e2e3e5', color: '#41464b', border: '#d3d6d8', icon: 'info-circle' },
    dark: { bg: '#d3d3d4', color: '#3a3b3c', border: '#bcbebf', icon: 'info-circle' },
    light: { bg: '#fefefe', color: '#636464', border: '#fdfdfe', icon: 'info-circle' },
  };

  const { bg, color, border, icon } = variantStyles[variant] || variantStyles.info;
  const title = t(`common:common:variant.${variant}`, { defaultValue: 'Info' });

  const dimensions = {
    sm: { width: '300px', minHeight: '100px' },
    lg: { width: '450px', minHeight: '150px' },
    xl: { width: '700px', minHeight: '200px' },
  };

  const { width, minHeight } = dimensions[size || 'lg'];

  return (
    <ToastContainer
      className="d-flex justify-content-center"
      style={{
        position: 'fixed',
        top: '80px',
        left: '50%',
        transform: 'translateX(-50%)',
        zIndex: 9999,
      }}
    >
      <Toast
        show={show}
        onClose={onClose}
        delay={5000}
        autohide
        style={{
          width,
          minHeight,
          padding: '16px',
          fontSize: '16px',
          backgroundColor: bg,
          color,
          border: `1px solid ${border}`,
          borderRadius: '8px',
          boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        }}
      >
        <Toast.Header
          closeButton={dismissible}
          style={{
            backgroundColor: bg,
            color,
            borderBottom: `1px solid ${border}`,
          }}
        >
          <FontAwesomeIcon icon={icon as any} className="me-2" style={{ color, fontSize: '20px' }} />
          <strong className="me-auto" style={{ fontSize: '18px' }}>{title}</strong>
        </Toast.Header>
        <Toast.Body style={{ fontSize: '16px' }}>
          {toastMessage ?? t('common:common.error.http.default')}
        </Toast.Body>
      </Toast>
    </ToastContainer>
  );
};

export default NotificationToast;
