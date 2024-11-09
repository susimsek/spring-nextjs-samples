// /api/apolloClient.ts
import { ApolloClient, InMemoryCache, HttpLink, ApolloLink, concat } from '@apollo/client';
import { onError } from '@apollo/client/link/error';
import { logout } from '@/reducers/authentication';
import store from '@/config/store';

// Create an HTTP link for the Apollo Client
const httpLink = new HttpLink({
  uri: '/graphql', // API endpoint for GraphQL
});

// Create an error link to handle GraphQL errors
const errorLink = onError(({ graphQLErrors, networkError }) => {
  if (graphQLErrors) {
    graphQLErrors.forEach(({ message, locations, path }) => {
      console.error(
        `[GraphQL error]: Message: ${message}, Location: ${locations}, Path: ${path}`
      );
    });
  }
  if (networkError) {
    console.error(`[Network error]: ${networkError}`);
    // Handle unauthorized errors (401)
    // Check if networkError is an instance of a known error type that includes status
    if ('status' in networkError && networkError.status === 401) {
      store.dispatch(logout()); // Dispatch logout action
    }
  }
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

  return forward(operation);
});

// Create the Apollo Client instance
const client = new ApolloClient({
  link: concat(authLink, concat(errorLink, httpLink)), // Combine links
  cache: new InMemoryCache(),
});

export default client;
