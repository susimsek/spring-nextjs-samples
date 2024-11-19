// src/constants.ts

export const ALPHANUMERIC_PATTERN = /^[a-zA-Z0-9]*$/;

export const isBrowser = typeof window !== 'undefined';

export const isDev = process.env.NODE_ENV === 'development';

export const isProd = process.env.NODE_ENV === 'production';
