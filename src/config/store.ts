import { configureStore } from '@reduxjs/toolkit';
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';

import notificationReducer from '@/reducers/notification';

// Configure the store without any reducers
const store = configureStore({
  reducer: {
    notification: notificationReducer,
  },
});

// Types for Redux state and dispatch
export type IRootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

// Custom hooks for dispatch and selector
export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<IRootState> = useSelector;

export default store;
