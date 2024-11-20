import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { isClient } from '../config/constants';
import { login as loginApi } from '../api/authApi';
import { LoginRequestDTO } from '../types/loginRequestDTO';
import { AxiosError } from 'axios';
import { ProblemDetail } from '../types/problemDetail';

interface AuthenticationState {
  token: string | null;
  isAuthenticated: boolean;
  loginError: boolean;
  loading: boolean;
}

const initialState: AuthenticationState = {
  token: null,
  isAuthenticated: false,
  loginError: false,
  loading: false,
};

// Thunk for login
export const login = createAsyncThunk(
  'authentication/login',
  async (credentials: LoginRequestDTO, { rejectWithValue }) => {
    try {
      const response = await loginApi(credentials);
      if (isClient && window.ipc && typeof window.ipc.setToken === 'function') {
        try {
          await window.ipc.setToken(response.accessToken);
        } catch (error) {
          console.error('Failed to set token:', error);
        }
      }
      return response.accessToken;
    } catch (error) {
      const axiosError = error as AxiosError<ProblemDetail>;
      return rejectWithValue(axiosError.response?.data?.detail);
    }
  },
);

// Async thunk to fetch the current token from the main process
export const fetchToken = createAsyncThunk('authentication/fetchToken', async () => {
  if (isClient && window.ipc && typeof window.ipc.getToken === 'function') {
    try {
      const token = await window.ipc.getToken();
      return token as string | null;
    } catch (error) {
      console.error('Failed to fetch token:', error);
      return null;
    }
  }
  return null;
});

const authenticationSlice = createSlice({
  name: 'authentication',
  initialState,
  reducers: {
    logout: state => {
      state.token = null;
      state.isAuthenticated = false;

      // Remove token from Electron Store
      if (isClient && window.ipc && typeof window.ipc.removeToken === 'function') {
        window.ipc.removeToken();
      }
    },
    authError: state => {
      state.token = null;
      state.isAuthenticated = false;
      state.loginError = false;
    },
  },
  extraReducers: builder => {
    builder
      .addCase(login.pending, state => {
        state.loading = true;
        state.loginError = false;
      })
      .addCase(login.fulfilled, (state, action: PayloadAction<string>) => {
        state.token = action.payload;
        state.isAuthenticated = true;
        state.loginError = false;
        state.loading = false;
      })
      .addCase(login.rejected, state => {
        state.loginError = true;
        state.loading = false;
      })
      .addCase(fetchToken.pending, state => {
        state.loading = true;
      })
      .addCase(fetchToken.fulfilled, (state, action) => {
        state.token = action.payload;
        state.isAuthenticated = !!action.payload;
        state.loading = false;
      })
      .addCase(fetchToken.rejected, state => {
        state.loading = false;
      });
  },
});

export const { logout, authError } = authenticationSlice.actions;
export default authenticationSlice.reducer;
