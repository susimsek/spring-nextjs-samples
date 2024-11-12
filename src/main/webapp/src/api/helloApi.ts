// /api/helloApi.ts
import apiClient from './apiClient';
import { HelloDTO } from '@/types/helloDTO';

export const fetchHelloMessage = async (): Promise<HelloDTO> => {
  const response = await apiClient.get<HelloDTO>('/hello');
  return response.data;
};
