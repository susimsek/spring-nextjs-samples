import { ipcMain } from 'electron';
import { userStore } from './user-store';

ipcMain.handle('setLocale', (_event, locale: string) => {
  userStore.set('locale', locale);
});

ipcMain.handle('getLocale', () => {
  return userStore.get('locale');
});

ipcMain.handle('setTheme', (_event, theme: 'light' | 'dark') => {
  userStore.set('theme', theme);
});

ipcMain.handle('getTheme', () => {
  return userStore.get('theme');
});

// Handle setting the authentication token
ipcMain.handle('setToken', (_event, token: string) => {
  userStore.set('token', token);
});

// Handle retrieving the authentication token
ipcMain.handle('getToken', () => {
  return userStore.get('token');
});

// Handle removing the authentication token
ipcMain.handle('removeToken', () => {
  userStore.delete('token');
});
