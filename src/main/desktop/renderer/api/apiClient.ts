// /api/apiClient.ts
import axios from 'axios';
import { loggingInterceptor } from '../config/loggingInterceptor';
import { notificationInterceptor } from '../config/notificationInterceptor';
import store from '../config/store';
import { logout } from '../reducers/authentication';
import { apiBaseUrl, isClient } from '../config/constants';

const TIMEOUT = 60 * 1000;

// Create an Axios instance with default settings
const apiClient = axios.create({
  baseURL: `${apiBaseUrl ?? ''}/api/v1`,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: TIMEOUT,
});

// Add a request interceptor to set the Authorization token
apiClient.interceptors.request.use(
  async config => {
    // Token and locale retrieval using Electron's IPC if available
    const token = isClient && window.ipc ? await window.ipc.getToken() : null;
    const locale = isClient && window.ipc ? await window.ipc.getLocale() : 'en';

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    config.headers['Accept-Language'] = locale;

    return config;
  },
  error => Promise.reject(error),
);

// Add additional response interceptors for logging and notifications
apiClient.interceptors.response.use(response => response, loggingInterceptor);

apiClient.interceptors.response.use(response => response, notificationInterceptor);

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
  },
);

export default apiClient;
