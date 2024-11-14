// store.ts
import { configureStore } from '@reduxjs/toolkit';
import themeReducer, { fetchTheme } from '../reducers/theme'; // Import the theme reducer
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';

const store = configureStore({
  reducer: {
    theme: themeReducer,
  },
});

store.dispatch(fetchTheme());

export type IRootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<IRootState> = useSelector;

export default store;
