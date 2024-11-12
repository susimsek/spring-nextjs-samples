// /config/loggingLink.ts
import {onError} from '@apollo/client/link/error';

const DEVELOPMENT = process.env.NODE_ENV === 'development';

const loggingLink = onError(({graphQLErrors, networkError}) => {
  if (DEVELOPMENT) {
    if (graphQLErrors) {
      graphQLErrors.forEach(({message, extensions}) => {
        const classification = extensions?.classification || 'Unknown Classification';
        console.error(`[GraphQL Error] - Classification: ${classification}, Message: ${message}`);
      });
    }

    if (networkError) {
      console.error(`[Network Error]: ${networkError.message}`);
    }
  }
});

export default loggingLink;
