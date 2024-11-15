import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { isClient } from '../config/constants';

interface AuthenticationState {
  token: string | null;
  isAuthenticated: boolean;
}

const initialState: AuthenticationState = {
  token: null,
  isAuthenticated: null,
};

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
    login: (state, action: PayloadAction<string>) => {
      state.token = action.payload;
      state.isAuthenticated = true;

      // Save token to Electron Store
      if (isClient && window.ipc && typeof window.ipc.setToken === 'function') {
        try {
          window.ipc.setToken(action.payload);
        } catch (error) {
          console.error('Failed to set token:', error);
        }
      }
    },
    logout: state => {
      state.token = null;
      state.isAuthenticated = false;

      // Remove token from Electron Store
      if (isClient && window.ipc && typeof window.ipc.removeToken === 'function') {
        window.ipc.removeToken();
      }
    },
  },
  extraReducers: builder => {
    builder.addCase(fetchToken.fulfilled, (state, action) => {
      state.token = action.payload;
      state.isAuthenticated = !!action.payload;
    });
  },
});

export const { login, logout } = authenticationSlice.actions;
export default authenticationSlice.reducer;
