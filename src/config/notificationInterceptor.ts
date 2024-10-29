// /config/notificationInterceptor.ts
import { AxiosError } from 'axios';
import store from '@/config/store';
import { showNotification } from '@/reducers/notification';
import { ProblemDetail } from '@/types/problemDetail';

export const notificationInterceptor = (error: AxiosError): Promise<AxiosError> => {
  let messageKey: string | undefined = 'common:common.error.http.default';
  let errorMessage: string | undefined = undefined;

  if (error.response) {
    const status = error.response.status;
    const problemDetail: ProblemDetail = error.response.data as ProblemDetail;

    switch (status) {
      case 401:
        // Ignore, page will be redirected to login.
        return Promise.reject(error);
      case 0:
      case 403:
      case 405:
      case 500:
      case 503:
        messageKey = `common:common.error.http.${status}`;
        break;
      default:
        if (problemDetail.detail) {
          errorMessage = problemDetail.detail;
          messageKey = undefined;
        }
        break;
    }

    if (problemDetail.violations && problemDetail.violations.length > 0) {
      errorMessage += problemDetail.violations
        .map(violation => `field: ${violation.field}, message: ${violation.message}`)
        .join('\n');
    }
  }
  store.dispatch(
    showNotification({
      messageKey,
      message: errorMessage,
      variant: 'danger',
    })
  );

  return Promise.reject(error);
};
