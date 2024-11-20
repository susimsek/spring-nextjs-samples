import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { isBrowser } from '@/config/constants';
import { login as loginApi } from '@/api/authApi';
import { LoginRequestDTO } from '@/types/loginRequestDTO';
import { AxiosError } from 'axios';
import { ProblemDetail } from '@/types/problemDetail';

interface AuthenticationState {
  token: string | null;
  isAuthenticated: boolean;
  loginError: boolean;
  loading: boolean;
}

const initialState: AuthenticationState = {
  token: isBrowser ? localStorage.getItem('token') : null,
  isAuthenticated: isBrowser ? !!localStorage.getItem('token') : false,
  loginError: false,
  loading: false,
};

// Thunk for login
export const login = createAsyncThunk(
  'authentication/login',
  async (credentials: LoginRequestDTO, { rejectWithValue }) => {
    try {
      const response = await loginApi(credentials);
      localStorage.setItem('token', response.accessToken);
      return response.accessToken;
    } catch (error) {
      const axiosError = error as AxiosError<ProblemDetail>;
      return rejectWithValue(axiosError.response?.data?.detail);
    }
  },
);

const authenticationSlice = createSlice({
  name: 'authentication',
  initialState,
  reducers: {
    logout: state => {
      state.token = null;
      state.isAuthenticated = false;
      state.loginError = false;
      localStorage.removeItem('token');
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
      });
  },
});

export const { logout, authError } = authenticationSlice.actions;
export default authenticationSlice.reducer;
