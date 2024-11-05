// reducers/authentication.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

const isBrowser = typeof window !== 'undefined';

interface AuthenticationState {
  token: string | null;
  isAuthenticated: boolean;
}

const initialState: AuthenticationState = {
  token: isBrowser ? localStorage.getItem('token') : null,
  isAuthenticated: isBrowser ? !!localStorage.getItem('token') : false,
};

const authenticationSlice = createSlice({
  name: 'authentication',
  initialState,
  reducers: {
    login: (state, action: PayloadAction<string>) => {
      state.token = action.payload;
      state.isAuthenticated = true;
      localStorage.setItem('token', action.payload);
    },
    logout: (state) => {
      state.token = null;
      state.isAuthenticated = false;
      localStorage.removeItem('token');
    },
  },
});

export const { login, logout } = authenticationSlice.actions;
export default authenticationSlice.reducer;
