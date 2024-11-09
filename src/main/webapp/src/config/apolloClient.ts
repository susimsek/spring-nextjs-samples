// /api/apolloClient.ts
import { ApolloClient, InMemoryCache, HttpLink, ApolloLink, concat } from '@apollo/client';
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

  return forward(operation).map((response) => {
    if (response.errors) {
      response.errors.forEach((error) => {
        const { extensions } = error;
        // Check for UNAUTHORIZED classification or status 401
        if (extensions?.classification === 'UNAUTHORIZED' || extensions?.status === 401) {
          store.dispatch(logout()); // Trigger logout for 401 errors or UNAUTHORIZED classification
        }
      });
    }
    return response;
  });
});

// Create the Apollo Client instance
const client = new ApolloClient({
  link: concat(authLink, concat(notificationLink, concat(loggingLink, httpLink))), // Combine links
  cache: new InMemoryCache(),
});

export default client;
