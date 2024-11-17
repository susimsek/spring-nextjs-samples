import { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useAppDispatch } from '../config/store';
import { showNotification } from '../reducers/notification';

const UpdateNotifier = () => {
  // Use 'common' namespace for translations
  const { t } = useTranslation('common');
  const dispatch = useAppDispatch();

  useEffect(() => {
    /**
     * Dispatch a notification to the Redux store.
     * @param messageKey - The translation key for the notification message.
     * @param variant - The type of notification (e.g., 'info', 'success', 'danger').
     */
    const dispatchNotification = (messageKey: string, variant: 'info' | 'success' | 'danger') => {
      dispatch(
        showNotification({
          messageKey,
          message: undefined, // Use the translation value from i18n
          variant,
        }),
      );
    };

    // Listen for the 'update_available' event from Electron and show an info notification
    window.ipc?.onUpdateAvailable?.(() => {
      dispatchNotification('common:common.update.available', 'info'); // Scoped to 'common'
    });

    // Listen for the 'update_downloaded' event from Electron and show a success notification
    window.ipc?.onUpdateDownloaded?.(() => {
      dispatchNotification('common:common.update.downloaded', 'success'); // Scoped to 'common'
    });

    // Cleanup: Remove event listeners when the component is unmounted
    return () => {
      window.ipc?.onUpdateAvailable?.(() => {});
      window.ipc?.onUpdateDownloaded?.(() => {});
    };
  }, [dispatch, t]);

  // This component does not render any UI; it only listens for update events
  return null;
};

export default UpdateNotifier;
