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
    danger: {
      bg: 'var(--danger-bg-color)',
      color: 'var(--danger-text-color)',
      border: 'var(--danger-border-color)',
      icon: 'exclamation-triangle',
    },
    info: {
      bg: 'var(--info-bg-color)',
      color: 'var(--info-text-color)',
      border: 'var(--info-border-color)',
      icon: 'info-circle',
    },
    success: {
      bg: 'var(--success-bg-color)',
      color: 'var(--success-text-color)',
      border: 'var(--success-border-color)',
      icon: 'check-circle',
    },
    warning: {
      bg: 'var(--warning-bg-color)',
      color: 'var(--warning-text-color)',
      border: 'var(--warning-border-color)',
      icon: 'exclamation-circle',
    },
    primary: {
      bg: 'var(--primary-bg-color)',
      color: 'var(--primary-text-color)',
      border: 'var(--primary-border-color)',
      icon: 'info-circle',
    },
    secondary: {
      bg: 'var(--secondary-bg-color)',
      color: 'var(--secondary-text-color)',
      border: 'var(--secondary-border-color)',
      icon: 'info-circle',
    },
    dark: {
      bg: 'var(--dark-bg-color)',
      color: 'var(--dark-text-color)',
      border: 'var(--dark-border-color)',
      icon: 'info-circle',
    },
    light: {
      bg: 'var(--light-bg-color)',
      color: 'var(--light-text-color)',
      border: 'var(--light-border-color)',
      icon: 'info-circle',
    },
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
        autohide
        show={show}
        onClose={onClose}
        delay={5000}
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
          closeButton={false}
          style={{
            backgroundColor: bg,
            color,
            borderBottom: `1px solid ${border}`,
          }}
        >
          <FontAwesomeIcon icon={icon as IconProp} className="me-2" style={{ color, fontSize: '20px' }} />
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
