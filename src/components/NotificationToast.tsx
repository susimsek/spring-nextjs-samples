// components/NotificationToast.tsx
import React from 'react';
import { Toast, ToastContainer } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faExclamationTriangle, faInfoCircle, faCheckCircle, faExclamationCircle, faInfo } from '@fortawesome/free-solid-svg-icons';
import { useTranslation } from 'next-i18next';

export type TostMessage = {
  message?: string;
  key?: string;
  data?: any;
  variant?: 'danger' | 'info' | 'success' | 'warning' | 'primary' | 'secondary' | 'dark' | 'light';
};

interface NotificationToastProps extends TostMessage {
  show: boolean;
  onClose: () => void;
  dismissible?: boolean;
  size?: 'sm' | 'lg' | 'xl';
}

const NotificationToast: React.FC<NotificationToastProps> = ({
                                                               message,
                                                               key,
                                                               data,
                                                               variant = 'info',
                                                               show,
                                                               onClose,
                                                               dismissible = true,
                                                               size = 'lg',
                                                             }) => {
  const { t } = useTranslation('common');
  const toastMessage = key ? String(t(key, data)) : message;

  const getVariantStyles = () => {
    switch (variant) {
      case 'danger':
        return {
          backgroundColor: '#fdecea',
          color: '#a71d2a',
          borderColor: '#f5c2c7',
          icon: faExclamationTriangle,
          title: t('common:common.variant.danger'),
        };
      case 'info':
        return {
          backgroundColor: '#e7f5ff',
          color: '#1c7ed6',
          borderColor: '#b6d4fe',
          icon: faInfoCircle,
          title: t('common:common.variant.info'),
        };
      case 'success':
        return {
          backgroundColor: '#d3f9d8',
          color: '#2f9e44',
          borderColor: '#b2f2bb',
          icon: faCheckCircle,
          title: t('common:common.variant.success'),
        };
      case 'warning':
        return {
          backgroundColor: '#fff3cd',
          color: '#856404',
          borderColor: '#ffeeba',
          icon: faExclamationCircle,
          title: t('common:common.variant.warning'),
        };
      case 'primary':
        return {
          backgroundColor: '#cfe2ff',
          color: '#084298',
          borderColor: '#b6d4fe',
          icon: faInfoCircle,
          title: t('common:common.variant.info'),
        };
      case 'secondary':
        return {
          backgroundColor: '#e2e3e5',
          color: '#41464b',
          borderColor: '#d3d6d8',
          icon: faInfoCircle,
          title: t('common:common.variant.info'),
        };
      case 'dark':
        return {
          backgroundColor: '#d3d3d4',
          color: '#3a3b3c',
          borderColor: '#bcbebf',
          icon: faInfoCircle,
          title: t('common:common.variant.info'),
        };
      case 'light':
        return {
          backgroundColor: '#fefefe',
          color: '#636464',
          borderColor: '#fdfdfe',
          icon: faInfoCircle,
          title: t('common:common.variant.info'),
        };
      default:
        return {
          backgroundColor: '#e7f5ff',
          color: '#1c7ed6',
          borderColor: '#b6d4fe',
          icon: faInfoCircle,
          title: t('common:common.variant.info'),
        };
    }
  };

  const { backgroundColor, color, borderColor, icon, title } = getVariantStyles();

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
          backgroundColor,
          color,
          border: `1px solid ${borderColor}`,
          borderRadius: '8px',
          boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        }}
      >
        <Toast.Header
          closeButton={dismissible}
          style={{
            backgroundColor,
            color,
            borderBottom: `1px solid ${borderColor}`,
          }}
        >
          <FontAwesomeIcon icon={icon} className="me-2" style={{ color, fontSize: '20px' }} />
          <strong className="me-auto" style={{ fontSize: '18px' }}>{title}</strong>
        </Toast.Header>
        <Toast.Body style={{ fontSize: '16px' }}>
          {toastMessage ?? t('common:common.defaultErrorMessage')}
        </Toast.Body>
      </Toast>
    </ToastContainer>
  );
};

export default NotificationToast;
