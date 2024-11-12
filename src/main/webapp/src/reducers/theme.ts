// reducers/theme.ts
import {createSlice} from '@reduxjs/toolkit';
import {isBrowser} from "@/config/constants";

interface ThemeState {
  theme: 'light' | 'dark';
}

// Retrieve the theme from localStorage or default to 'light' if not set
const initialState: ThemeState = {
  theme: (isBrowser && localStorage.getItem('theme')) as 'light' | 'dark' || 'light',
};

const themeSlice = createSlice({
  name: 'theme',
  initialState,
  reducers: {
    toggleTheme: (state) => {
      state.theme = state.theme === 'light' ? 'dark' : 'light';
      localStorage.setItem('theme', state.theme); // Save the theme to localStorage
    },
    setTheme: (state, action) => {
      state.theme = action.payload;
      localStorage.setItem('theme', state.theme); // Save the theme to localStorage
    }
  },
});

export const {toggleTheme, setTheme} = themeSlice.actions;
export default themeSlice.reducer;
