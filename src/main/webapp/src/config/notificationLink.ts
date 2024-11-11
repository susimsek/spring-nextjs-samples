// /config/notificationLink.ts
import { onError } from '@apollo/client/link/error';
import store from '@/config/store';
import { showNotification } from '@/reducers/notification';

export const notificationLink = onError(({ graphQLErrors, networkError, operation, forward }) => {
  // Takes the first GraphQL error and processes it through the handleGraphQLError function
  const graphQLError = graphQLErrors && graphQLErrors.length > 0 ? graphQLErrors[0] : null;
  if (graphQLError) {
    const { messageKey, errorMessage } = handleGraphQLError(graphQLError, operation, forward);
    if (messageKey || errorMessage) {
      dispatchNotification(messageKey, errorMessage);
    }
  }

  // Processes the network error through the handleNetworkError function
  if (networkError) {
    const { messageKey, errorMessage } = handleNetworkError(networkError, operation, forward);
    if (messageKey || errorMessage) {
      dispatchNotification(messageKey, errorMessage);
    }
  }
});

// Function to handle GraphQL errors
function handleGraphQLError(graphQLError: any, operation: any, forward: any) {
  let messageKey: string | undefined = 'common:common.error.http.default';
  let errorMessage: string | undefined;

  const classification = graphQLError.extensions?.classification;
  switch (classification) {
    case 'UNAUTHORIZED':
      return forward(operation); // Redirects for UNAUTHORIZED errors
    case 'FORBIDDEN':
    case 'INTERNAL_ERROR':
    case 'THROTTLED':
    case 'CONFLICT':
      messageKey = `common:common.error.graphql.classification.${classification}`;
      break;
    default:
      errorMessage = graphQLError.message;
      messageKey = undefined;
      break;
  }

  return { messageKey, errorMessage };
}

// Function to handle network errors
function handleNetworkError(networkError: any, operation: any, forward: any) {
  let messageKey: string | undefined = 'common:common.error.http.default';
  let errorMessage: string | undefined;

  const status = 'statusCode' in networkError ? networkError.statusCode : undefined;

  switch (status) {
    case 401:
      return forward(operation); // Redirects for 401 errors
    case 0:
    case 403:
    case 405:
    case 500:
    case 503:
      messageKey = `common:common.error.http.${status}`;
      break;
    default:
      if (networkError.message.includes('4400')) {
        messageKey = 'common:error.graphql.subscription.4400';
      } else if (networkError.message.includes('4401')) {
        return forward(operation); // Redirects for 4401 errors
      } else if (networkError.message.includes('4408')) {
        messageKey = 'common:error.graphql.subscription.4408';
      } else if (networkError.message.includes('4429')) {
        messageKey = 'common:error.graphql.subscription.4429';
      } else {
        errorMessage = networkError.message;
      }
      break;
  }

  return { messageKey, errorMessage };
}

// Function to manage the notification dispatching
function dispatchNotification(messageKey: string | undefined, errorMessage: string | undefined) {
  store.dispatch(
    showNotification({
      messageKey,
      message: errorMessage,
      variant: 'danger',
    })
  );
}
