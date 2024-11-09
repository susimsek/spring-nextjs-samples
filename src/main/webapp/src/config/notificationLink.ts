// /config/notificationLink.ts
import { onError } from '@apollo/client/link/error';
import store from '@/config/store';
import { showNotification } from '@/reducers/notification';

export const notificationLink = onError(({ graphQLErrors, networkError, operation, forward }) => {
  let messageKey: string | undefined = 'common:common.error.http.default';
  let errorMessage: string | undefined = undefined;

  // Network Errors handling
  if (networkError) {
    const status = 'statusCode' in networkError ? networkError.statusCode : undefined;

    switch (status) {
      case 401:
        // Ignore, page will be redirected to login.
        return forward(operation);
      case 0:
      case 403:
      case 405:
      case 500:
      case 503:
        messageKey = `common:common.error.http.${status}`;
        break;
      default:
        errorMessage = networkError.message;
        break;
    }
  }

  // Handle only the first GraphQL error
  const graphQLError = graphQLErrors && graphQLErrors.length > 0 ? graphQLErrors[0] : null;
  if (graphQLError) {
    const classification = graphQLError.extensions?.classification;

    switch (classification) {
      case 'UNAUTHORIZED':
        // Ignore UNAUTHORIZED errors
        return forward(operation);
      case 'FORBIDDEN':
      case 'INTERNAL_ERROR':
      case 'THROTTLED':
      case 'CONFLICT':
        messageKey = `common:common.error.graphql.${classification}`;
        break;
      default:
        errorMessage = graphQLError.message;
        messageKey = undefined;
        break;
    }
  }

  // Dispatch the notification if a messageKey or errorMessage is set
  store.dispatch(
    showNotification({
      messageKey,
      message: errorMessage,
      variant: 'danger',
    })
  );
});
