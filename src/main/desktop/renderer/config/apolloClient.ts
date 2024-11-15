import { ApolloClient, ApolloLink, fromPromise, HttpLink, InMemoryCache, ServerError, ServerParseError, split } from '@apollo/client';
import { onError } from '@apollo/client/link/error';
import { notificationLink } from './notificationLink';
import loggingLink from './loggingLink';
import { logout } from '../reducers/authentication';
import store from './store';
import { GraphQLWsLink } from '@apollo/client/link/subscriptions';
import { createClient } from 'graphql-ws';
import { getMainDefinition } from '@apollo/client/utilities';
import { GraphQLFormattedError } from 'graphql/error';
import { apiBaseUrl, isClient } from './constants';

// Create an HTTP link for the Apollo Client
const httpLink = new HttpLink({
  uri: `${apiBaseUrl ?? ''}/graphql`,
});

// Create a WebSocket link using graphql-ws for subscriptions
const wsLink = new GraphQLWsLink(
  createClient({
    url: `${process.env.NEXT_PUBLIC_GRAPHQL_WS_BASE_URL ?? ''}/subscriptions`,
    connectionParams: async () => {
      const token = isClient && window.ipc ? await window.ipc.getToken() : null;
      return {
        ...(token && { Authorization: `Bearer ${token}` }),
      };
    },
  }),
);

// Split link for using WebSocket link for subscriptions and HTTP link for queries and mutations
const splitLink = split(
  ({ query }) => {
    const definition = getMainDefinition(query);
    return definition.kind === 'OperationDefinition' && definition.operation === 'subscription';
  },
  wsLink,
  httpLink,
);

// Create an authentication link to set the Authorization token and language header
const authLink = new ApolloLink((operation, forward) => {
  return fromPromise(
    (async () => {
      const token = isClient && window.ipc ? await window.ipc.getToken() : null;
      const language = isClient && window.ipc ? await window.ipc.getLocale() : 'en';

      // Set headers
      operation.setContext({
        headers: {
          ...(token && { Authorization: `Bearer ${token}` }),
          'Accept-Language': language,
        },
      });
    })(),
  ).flatMap(() => forward(operation));
});

// onError link for handling errors and triggering logout if necessary
const logoutLink = onError(({ graphQLErrors, networkError }) => {
  const graphQLError = graphQLErrors && graphQLErrors.length > 0 ? graphQLErrors[0] : null;
  if (graphQLError) {
    handleGraphQLErrorForLogout(graphQLError);
  }

  if (networkError) {
    handleNetworkErrorForLogout(networkError);
  }
});

// Function to handle GraphQL errors for logout
function handleGraphQLErrorForLogout(graphQLError: GraphQLFormattedError) {
  const classification = graphQLError.extensions?.classification;
  if (classification === 'UNAUTHORIZED') {
    store.dispatch(logout());
  }
}

// Function to handle network errors for logout
function handleNetworkErrorForLogout(networkError: Error | ServerParseError | ServerError) {
  const status = (networkError as ServerError).statusCode ?? undefined;

  if (networkError.message.includes('4401')) {
    store.dispatch(logout());
    return;
  }

  if (status === 401) {
    store.dispatch(logout());
  }
}

// Combine all links
const client = new ApolloClient({
  link: ApolloLink.from([authLink, logoutLink, notificationLink, loggingLink, splitLink]), // Use splitLink for subscriptions
  cache: new InMemoryCache(),
});

export default client;
