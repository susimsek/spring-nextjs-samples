// /api/apiClient.ts
import axios from 'axios';
import languageDetector from '../lib/languageDetector';
import {errorInterceptor} from "@/config/error-interceptor";

const TIMEOUT = 60 * 1000;
const detectedLng = languageDetector.detect() as string;

const apiClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
    'Accept-Language': detectedLng,
  },
  timeout: TIMEOUT,
});

apiClient.interceptors.response.use(
  response => response,
  errorInterceptor
);

export default apiClient;
