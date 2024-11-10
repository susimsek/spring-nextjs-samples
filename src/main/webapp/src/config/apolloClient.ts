import { ApolloClient, InMemoryCache, HttpLink, ApolloLink, split } from '@apollo/client';
import { onError } from '@apollo/client/link/error';
import { notificationLink } from "@/config/notificationLink";
import loggingLink from "@/config/loggingLink";
import { logout } from '@/reducers/authentication';
import store from '@/config/store';
import { GraphQLWsLink } from '@apollo/client/link/subscriptions';
import { createClient } from 'graphql-ws';
import { getMainDefinition } from '@apollo/client/utilities';

// Create an HTTP link for the Apollo Client
const httpLink = new HttpLink({
  uri: '/graphql', // API endpoint for GraphQL
});

// Create a WebSocket link using graphql-ws for subscriptions
const wsLink = new GraphQLWsLink(createClient({
  url: '/subscriptions',
  connectionParams: () => {
    const token = localStorage.getItem('token');
    return {
      ...(token && { Authorization: `Bearer ${token}` }), // Send token as Authorization in the payload
    };
  },
}));

// Split link for using WebSocket link for subscriptions and HTTP link for queries and mutations
const splitLink = split(
  ({ query }) => {
    const definition = getMainDefinition(query);
    return (
      definition.kind === 'OperationDefinition' &&
      definition.operation === 'subscription'
    );
  },
  wsLink,
  httpLink,
);

// Create an authentication link to set the Authorization token and language header
const authLink = new ApolloLink((operation, forward) => {
  const token = localStorage.getItem('token');
  const language = localStorage.getItem('i18nextLng'); // Always get the language setting

  // Set headers
  operation.setContext({
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }), // Set Authorization only if token exists
      'Accept-Language': language, // Always set Accept-Language
    },
  });

  return forward(operation); // Continue the request
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
function handleGraphQLErrorForLogout(graphQLError: any) {
  const classification = graphQLError.extensions?.classification;
  if (classification === 'UNAUTHORIZED') {
    store.dispatch(logout()); // Trigger logout if the error is UNAUTHORIZED
  }
}

// Function to handle network errors for logout
function handleNetworkErrorForLogout(networkError: any) {
  const status = 'statusCode' in networkError ? networkError.statusCode : undefined;
  if (status === 401) {
    store.dispatch(logout()); // Trigger logout if the status code is 401
  }
}

// Combine all links
const client = new ApolloClient({
  link: ApolloLink.from([authLink, logoutLink, notificationLink, loggingLink, splitLink]), // Use splitLink for subscriptions
  cache: new InMemoryCache(),
});

export default client;
