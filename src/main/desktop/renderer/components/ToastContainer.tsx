import React from 'react';
import NotificationToast from '../components/NotificationToast';
import { useAppDispatch, useAppSelector } from '../config/store';
import { hideNotification } from '../reducers/notification';

const ToastContainer: React.FC = () => {
  const toast = useAppSelector(state => state.notification);
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
};

export default ToastContainer;
