import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Button, Modal } from 'react-bootstrap';
import { useAppSelector } from '../config/store';

const UpdateNotifier = () => {
  const { t } = useTranslation('common'); // Use the 'common' namespace for translations
  const { theme } = useAppSelector(state => state.theme);
  const [showUpdateModal, setShowUpdateModal] = useState(false);

  useEffect(() => {
    window.ipc?.onUpdateDownloaded?.(() => {
      setShowUpdateModal(true);
    });
  }, [t]);

  const handleRestart = () => {
    window.ipc?.restartApp();
    setShowUpdateModal(false);
  };

  // Handle closing the modal
  const handleClose = () => {
    setShowUpdateModal(false);
  };

  return (
    <Modal
      show={showUpdateModal}
      onHide={handleClose}
      className={`modal ${theme === 'dark' ? 'dark-theme' : 'light-theme'}`}
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title>{t('common:common.update.title')}</Modal.Title>
      </Modal.Header>
      <Modal.Body>{t('common:common.update.downloaded')}</Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={handleRestart}>
          {t('common:common.update.confirm')}
        </Button>
        <Button variant="secondary" onClick={handleClose}>
          {t('common:common.update.remindLater')}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default UpdateNotifier;
