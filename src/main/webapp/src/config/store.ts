// store.ts
import { configureStore } from '@reduxjs/toolkit';
import authenticationReducer from '@/reducers/authentication';
import notificationReducer from '@/reducers/notification';
import themeReducer from '@/reducers/theme'; // Import the theme reducer
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';

const store = configureStore({
  reducer: {
    authentication: authenticationReducer,
    notification: notificationReducer,
    theme: themeReducer,
  },
});

export type IRootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<IRootState> = useSelector;

export default store;
