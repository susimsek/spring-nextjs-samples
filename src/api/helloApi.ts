// /api/helloApi.ts
import apiClient from './apiClient';

// Define the HelloDTO interface to match the backend response
export interface HelloDTO {
  message: string;
}

export const fetchHelloMessage = async (): Promise<HelloDTO> => {
  const response = await apiClient.get<HelloDTO>('/hello');
  return response.data;
};
