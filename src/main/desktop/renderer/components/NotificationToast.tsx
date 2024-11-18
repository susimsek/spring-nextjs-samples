// components/NotificationToast.tsx
import React from 'react';
import { Toast, ToastContainer } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useTranslation } from 'next-i18next';
import { IconProp } from '@fortawesome/fontawesome-svg-core';

export type TostMessage = {
  message?: string;
  messageKey?: string;
  data?: Record<string, unknown>;
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
    danger: 'exclamation-triangle',
    info: 'info-circle',
    success: 'check-circle',
    warning: 'exclamation-circle',
    primary: 'info-circle',
    secondary: 'info-circle',
    dark: 'info-circle',
    light: 'info-circle',
  };

  const icon = variantStyles[variant] as IconProp;
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
        autohide
        className={`toast toast-${variant}`}
        show={show}
        onClose={onClose}
        delay={5000}
        style={{
          width,
          minHeight,
          padding: '16px',
          fontSize: '16px',
          borderRadius: '8px',
          boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        }}
      >
        <Toast.Header className={`toast-header toast-header-${variant}`} closeButton={false}>
          <FontAwesomeIcon icon={icon as IconProp} className={`me-2 text-${variant}`} style={{ fontSize: '20px' }} />
          <strong className="me-auto" style={{ fontSize: '18px' }}>
            {title}
          </strong>
          {dismissible && (
            <button type="button" className="close-icon" aria-label="Close" onClick={onClose}>
              <FontAwesomeIcon
                icon="times"
                style={{
                  fontSize: '16px',
                }}
              />
            </button>
          )}
        </Toast.Header>
        <Toast.Body style={{ fontSize: '16px' }}>{toastMessage ?? t('common:common.error.http.default')}</Toast.Body>
      </Toast>
    </ToastContainer>
  );
};

export default NotificationToast;
