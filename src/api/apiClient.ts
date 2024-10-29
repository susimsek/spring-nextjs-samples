// /api/apiClient.ts
import axios from 'axios';
import languageDetector from '../lib/languageDetector';

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

export default apiClient;
