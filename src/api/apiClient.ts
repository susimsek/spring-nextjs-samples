// /api/apiClient.ts
import axios from 'axios';
import languageDetector from '../lib/languageDetector';
import {loggingInterceptor} from "@/config/loggingInterceptor";
import {notificationInterceptor} from "@/config/notificationInterceptor";

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
  loggingInterceptor
);

apiClient.interceptors.response.use(
  response => response,
  notificationInterceptor
);



export default apiClient;
