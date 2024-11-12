// /api/authApi.ts
import apiClient from './apiClient';
import {TokenDTO} from '@/types/tokenDTO';
import {LoginRequestDTO} from '@/types/loginRequestDTO';

export const login = async (loginData: LoginRequestDTO): Promise<TokenDTO> => {
  const response = await apiClient.post<TokenDTO>('/auth/token', loginData);
  return response.data;
};
