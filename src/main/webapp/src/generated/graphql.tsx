import { gql } from '@apollo/client';
import * as Apollo from '@apollo/client';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
const defaultOptions = {} as const;
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string };
  String: { input: string; output: string };
  Boolean: { input: boolean; output: boolean };
  Int: { input: number; output: number };
  Float: { input: number; output: number };
  DateTime: { input: any; output: any };
};

/**  Data structure containing a welcome message and timestamp */
export type HelloDto = {
  __typename?: 'HelloDTO';
  /**
   *  The welcome message content
   *  Example: "Hello, welcome to our API!"
   */
  message: Scalars['String']['output'];
  /**
   *  The timestamp of the welcome message
   *  Example: "2023-11-10T10:00:00Z"
   */
  timestamp: Scalars['DateTime']['output'];
};

/**  Input structure for user login request */
export type LoginRequestDto = {
  /**
   *  Password of the user
   *  Example: "password"
   */
  password: Scalars['String']['input'];
  /**
   *  Username of the user
   *  Example: "admin"
   */
  username: Scalars['String']['input'];
};

/**  Mutations for user authentication */
export type Mutation = {
  __typename?: 'Mutation';
  /**  Authenticates the user with username and password, returns a JWT token. */
  authenticate?: Maybe<TokenDto>;
};

/**  Mutations for user authentication */
export type MutationAuthenticateArgs = {
  loginRequest: LoginRequestDto;
};

/**  Root queries for fetching data */
export type Query = {
  __typename?: 'Query';
  /**  Returns a simple welcome message */
  hello?: Maybe<HelloDto>;
};

/**  Subscriptions for real-time updates */
export type Subscription = {
  __typename?: 'Subscription';
  /**  Sends a welcome message periodically */
  helloSubscription?: Maybe<HelloDto>;
};

/**  JWT token response data structure */
export type TokenDto = {
  __typename?: 'TokenDTO';
  /**
   *  JWT access token
   *  Example: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   */
  accessToken: Scalars['String']['output'];
  /**
   *  Access token expiration time in seconds
   *  Example: 3600
   */
  accessTokenExpiresIn: Scalars['Int']['output'];
  /**
   *  Type of token (e.g., "Bearer")
   *  Example: "Bearer"
   */
  tokenType: Scalars['String']['output'];
};

export type AuthenticateMutationVariables = Exact<{
  loginRequest: LoginRequestDto;
}>;

export type AuthenticateMutation = {
  __typename?: 'Mutation';
  authenticate?: { __typename?: 'TokenDTO'; accessToken: string; tokenType: string; accessTokenExpiresIn: number } | null;
};

export type GetHelloMessageQueryVariables = Exact<{ [key: string]: never }>;

export type GetHelloMessageQuery = { __typename?: 'Query'; hello?: { __typename?: 'HelloDTO'; message: string; timestamp: any } | null };

export type HelloSubscriptionSubscriptionVariables = Exact<{ [key: string]: never }>;

export type HelloSubscriptionSubscription = {
  __typename?: 'Subscription';
  helloSubscription?: { __typename?: 'HelloDTO'; message: string; timestamp: any } | null;
};

export const AuthenticateDocument = gql`
  mutation Authenticate($loginRequest: LoginRequestDTO!) {
    authenticate(loginRequest: $loginRequest) {
      accessToken
      tokenType
      accessTokenExpiresIn
    }
  }
`;
export type AuthenticateMutationFn = Apollo.MutationFunction<AuthenticateMutation, AuthenticateMutationVariables>;

/**
 * __useAuthenticateMutation__
 *
 * To run a mutation, you first call `useAuthenticateMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useAuthenticateMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [authenticateMutation, { data, loading, error }] = useAuthenticateMutation({
 *   variables: {
 *      loginRequest: // value for 'loginRequest'
 *   },
 * });
 */
export function useAuthenticateMutation(baseOptions?: Apollo.MutationHookOptions<AuthenticateMutation, AuthenticateMutationVariables>) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useMutation<AuthenticateMutation, AuthenticateMutationVariables>(AuthenticateDocument, options);
}
export type AuthenticateMutationHookResult = ReturnType<typeof useAuthenticateMutation>;
export type AuthenticateMutationResult = Apollo.MutationResult<AuthenticateMutation>;
export type AuthenticateMutationOptions = Apollo.BaseMutationOptions<AuthenticateMutation, AuthenticateMutationVariables>;
export const GetHelloMessageDocument = gql`
  query GetHelloMessage {
    hello {
      message
      timestamp
    }
  }
`;

/**
 * __useGetHelloMessageQuery__
 *
 * To run a query within a React component, call `useGetHelloMessageQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetHelloMessageQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetHelloMessageQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetHelloMessageQuery(baseOptions?: Apollo.QueryHookOptions<GetHelloMessageQuery, GetHelloMessageQueryVariables>) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useQuery<GetHelloMessageQuery, GetHelloMessageQueryVariables>(GetHelloMessageDocument, options);
}
export function useGetHelloMessageLazyQuery(
  baseOptions?: Apollo.LazyQueryHookOptions<GetHelloMessageQuery, GetHelloMessageQueryVariables>,
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useLazyQuery<GetHelloMessageQuery, GetHelloMessageQueryVariables>(GetHelloMessageDocument, options);
}
export function useGetHelloMessageSuspenseQuery(
  baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<GetHelloMessageQuery, GetHelloMessageQueryVariables>,
) {
  const options = baseOptions === Apollo.skipToken ? baseOptions : { ...defaultOptions, ...baseOptions };
  return Apollo.useSuspenseQuery<GetHelloMessageQuery, GetHelloMessageQueryVariables>(GetHelloMessageDocument, options);
}
export type GetHelloMessageQueryHookResult = ReturnType<typeof useGetHelloMessageQuery>;
export type GetHelloMessageLazyQueryHookResult = ReturnType<typeof useGetHelloMessageLazyQuery>;
export type GetHelloMessageSuspenseQueryHookResult = ReturnType<typeof useGetHelloMessageSuspenseQuery>;
export type GetHelloMessageQueryResult = Apollo.QueryResult<GetHelloMessageQuery, GetHelloMessageQueryVariables>;
export const HelloSubscriptionDocument = gql`
  subscription HelloSubscription {
    helloSubscription {
      message
      timestamp
    }
  }
`;

/**
 * __useHelloSubscriptionSubscription__
 *
 * To run a query within a React component, call `useHelloSubscriptionSubscription` and pass it any options that fit your needs.
 * When your component renders, `useHelloSubscriptionSubscription` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the subscription, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useHelloSubscriptionSubscription({
 *   variables: {
 *   },
 * });
 */
export function useHelloSubscriptionSubscription(
  baseOptions?: Apollo.SubscriptionHookOptions<HelloSubscriptionSubscription, HelloSubscriptionSubscriptionVariables>,
) {
  const options = { ...defaultOptions, ...baseOptions };
  return Apollo.useSubscription<HelloSubscriptionSubscription, HelloSubscriptionSubscriptionVariables>(HelloSubscriptionDocument, options);
}
export type HelloSubscriptionSubscriptionHookResult = ReturnType<typeof useHelloSubscriptionSubscription>;
export type HelloSubscriptionSubscriptionResult = Apollo.SubscriptionResult<HelloSubscriptionSubscription>;
