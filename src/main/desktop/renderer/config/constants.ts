// src/constants.ts

export const ALPHANUMERIC_PATTERN = /^[a-zA-Z0-9]*$/;

export const isClient = typeof window !== 'undefined';

export const isDev = process.env.NODE_ENV === 'development';

export const isProd = process.env.NODE_ENV === 'production';

export const apiBaseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? '';
