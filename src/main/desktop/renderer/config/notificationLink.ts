// /config/notificationLink.ts
import { onError } from '@apollo/client/link/error';
import store from '../config/store';
import { showNotification } from '../reducers/notification';
import { GraphQLFormattedError } from 'graphql/error';
import { NextLink, Operation, ServerError, ServerParseError } from '@apollo/client';

export const notificationLink = onError(({ graphQLErrors, networkError, operation, forward }) => {
  // Takes the first GraphQL error and processes it through the handleGraphQLError function
  const graphQLError = graphQLErrors && graphQLErrors.length > 0 ? graphQLErrors[0] : null;
  if (graphQLError) {
    const result = handleGraphQLError(graphQLError, operation, forward);
    if (result) {
      const { messageKey, errorMessage } = result;
      if (messageKey || errorMessage) {
        dispatchNotification(messageKey, errorMessage);
      }
    }
  }

  // Processes the network error through the handleNetworkError function
  if (networkError) {
    const result = handleNetworkError(networkError, operation, forward);
    if (result) {
      const { messageKey, errorMessage } = result;
      if (messageKey || errorMessage) {
        dispatchNotification(messageKey, errorMessage);
      }
    }
  }
});

// Function to handle GraphQL errors
function handleGraphQLError(
  graphQLError: GraphQLFormattedError,
  operation: Operation,
  forward: NextLink,
): { messageKey?: string; errorMessage?: string } | null {
  let messageKey: string | undefined = 'common:common.error.http.default';
  let errorMessage: string | undefined;

  const classification = graphQLError.extensions?.classification;
  switch (classification) {
    case 'UNAUTHORIZED':
      forward(operation); // Redirects for UNAUTHORIZED errors
      return null;
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
function handleNetworkError(
  networkError: Error | ServerParseError | ServerError,
  operation: Operation,
  forward: NextLink,
): { messageKey?: string; errorMessage?: string } | null {
  let messageKey: string | undefined = 'common:common.error.http.default';
  let errorMessage: string | undefined;

  const status = (networkError as ServerError).statusCode ?? undefined;

  switch (status) {
    case 401:
      forward(operation); // Redirects for 401 errors
      return null;
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
        forward(operation); // Redirects for 4401 errors
        return null;
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
    }),
  );
}
