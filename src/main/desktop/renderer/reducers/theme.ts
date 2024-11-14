import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { isClient } from '../config/constants';

interface ThemeState {
  theme: 'light' | 'dark';
}

// Initial state with default theme 'light'
const initialState: ThemeState = {
  theme: 'light',
};

// Async thunk to fetch the current theme from the main process
export const fetchTheme = createAsyncThunk('theme/fetchTheme', async () => {
  if (isClient && window.ipc && window.ipc.getTheme) {
    const theme = await window.ipc.getTheme();
    return theme as 'light' | 'dark';
  }
  return 'light';
});

const themeSlice = createSlice({
  name: 'theme',
  initialState,
  reducers: {
    toggleTheme: state => {
      // Toggle theme between 'light' and 'dark'
      state.theme = state.theme === 'light' ? 'dark' : 'light';

      // Send the updated theme to the main process
      if (isClient && window.ipc && window.ipc.setTheme) {
        window.ipc.setTheme(state.theme);
      }
    },
    setTheme: (state, action: PayloadAction<'light' | 'dark'>) => {
      // Set the theme to the specified value
      state.theme = action.payload;

      // Send the updated theme to the main process
      if (isClient && window.ipc && window.ipc.setTheme) {
        window.ipc.setTheme(state.theme);
      }
    },
  },
  extraReducers: builder => {
    builder.addCase(fetchTheme.fulfilled, (state, action) => {
      // Set the theme to the fetched value
      state.theme = action.payload;
    });
  },
});

export const { toggleTheme, setTheme } = themeSlice.actions;
export default themeSlice.reducer;
