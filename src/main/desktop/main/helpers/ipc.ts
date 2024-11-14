import { ipcMain } from 'electron';
import { userStore } from './user-store';

ipcMain.handle('setLocale', (_event, locale: string) => {
  userStore.set('locale', locale);
});

ipcMain.handle('setTheme', (_event, theme: 'light' | 'dark') => {
  userStore.set('theme', theme);
});

ipcMain.handle('getTheme', () => {
  return userStore.get('theme') || 'light';
});
