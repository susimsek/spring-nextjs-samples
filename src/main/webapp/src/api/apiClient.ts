// /api/apiClient.ts
import axios from 'axios';
import languageDetector from '../lib/languageDetector';
import { loggingInterceptor } from '@/config/loggingInterceptor';
import { notificationInterceptor } from '@/config/notificationInterceptor';
import store from '@/config/store';
import { logout } from '@/reducers/authentication';

const TIMEOUT = 60 * 1000;
const detectedLng = languageDetector.detect() as string;

// Create an Axios instance with default settings
const apiClient = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
    'Accept-Language': detectedLng,
  },
  timeout: TIMEOUT,
});

// Add a request interceptor to set the Authorization token
apiClient.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// Add additional response interceptors for logging and notifications
apiClient.interceptors.response.use(
  response => response,
  loggingInterceptor
);

apiClient.interceptors.response.use(
  response => response,
  notificationInterceptor
);

// Add a response interceptor for handling errors, including 401 Unauthorized
apiClient.interceptors.response.use(
  response => response,
  error => {
    const status = error.status || (error.response ? error.response.status : 0);

    // Check if the error is from the /auth/token endpoint
    if (status === 401 && !error.config?.url?.endsWith('/auth/token')) {
      store.dispatch(logout()); // Dispatch logout action for all endpoints except /auth/token
    }

    return Promise.reject(error);
  }
);

export default apiClient;