// /api/apolloClient.ts
import { ApolloClient, InMemoryCache, HttpLink, ApolloLink } from '@apollo/client';
import { onError } from '@apollo/client/link/error';
import { notificationLink } from "@/config/notificationLink";
import loggingLink from "@/config/loggingLink";
import { logout } from '@/reducers/authentication';
import store from '@/config/store';

// Create an HTTP link for the Apollo Client
const httpLink = new HttpLink({
  uri: '/graphql', // API endpoint for GraphQL
});

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
  // Handle GraphQL errors
  const graphQLError = graphQLErrors && graphQLErrors.length > 0 ? graphQLErrors[0] : null;
  if (graphQLError) {
    handleGraphQLErrorForLogout(graphQLError);
  }

  // Handle network errors
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
  link: ApolloLink.from([authLink, logoutLink, notificationLink, loggingLink, httpLink]), // Combine auth, logout, notification, logging, and HTTP links
  cache: new InMemoryCache(),
});

export default client;
