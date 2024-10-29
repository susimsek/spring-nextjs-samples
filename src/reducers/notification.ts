// /redux/slices/notificationSlice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface NotificationState {
  messageKey: string | undefined;
  message: string | undefined;
  variant: 'success' | 'danger' | 'info' | 'warning' | undefined;
  show: boolean;
}

const initialState: NotificationState = {
  messageKey: undefined,
  message: undefined,
  variant: "info",
  show: false,
};

const notificationSlice = createSlice({
  name: 'notification',
  initialState,
  reducers: {
    showNotification: (state, action: PayloadAction<Omit<NotificationState, 'show'>>) => {
      state.messageKey = action.payload.messageKey;
      state.message = action.payload.message;
      state.variant = action.payload.variant;
      state.show = true;
    },
    hideNotification: (state) => {
      state.show = false;
      state.messageKey = undefined;
      state.message = undefined;
      state.variant = undefined;
    },
  },
});

export const { showNotification, hideNotification } = notificationSlice.actions;
export default notificationSlice.reducer;
