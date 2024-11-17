import { contextBridge, ipcRenderer, IpcRendererEvent } from 'electron';

const handler = {
  send(channel: string, value: unknown) {
    ipcRenderer.send(channel, value);
  },

  on(channel: string, callback: (...args: unknown[]) => void) {
    const subscription = (_event: IpcRendererEvent, ...args: unknown[]) => callback(...args);
    ipcRenderer.on(channel, subscription);

    return () => {
      ipcRenderer.removeListener(channel, subscription);
    };
  },

  // Listen for update available event
  onUpdateAvailable(callback: () => void) {
    const subscription = (_event: IpcRendererEvent) => {
      void _event;
      callback();
    };
    ipcRenderer.on('update_available', subscription);

    return () => {
      ipcRenderer.removeListener('update_available', subscription);
    };
  },

  // Listen for update downloaded event
  onUpdateDownloaded(callback: () => void) {
    const subscription = (_event: IpcRendererEvent) => {
      void _event;
      callback();
    };
    ipcRenderer.on('update_downloaded', subscription);

    return () => {
      ipcRenderer.removeListener('update_downloaded', subscription);
    };
  },

  // Set the locale in the main process
  async setLocale(locale: string): Promise<void> {
    await ipcRenderer.invoke('setLocale', locale);
  },

  // Get the current locale from the main process
  async getLocale(): Promise<string | null> {
    return ipcRenderer.invoke('getLocale');
  },

  // Set the theme in the main process
  async setTheme(theme: 'light' | 'dark'): Promise<void> {
    await ipcRenderer.invoke('setTheme', theme);
  },

  // Get the current theme from the main process
  async getTheme(): Promise<'light' | 'dark'> {
    return ipcRenderer.invoke('getTheme');
  },

  // Set the authentication token in the main process
  async setToken(token: string): Promise<void> {
    await ipcRenderer.invoke('setToken', token);
  },

  // Get the current authentication token from the main process
  async getToken(): Promise<string | null> {
    return ipcRenderer.invoke('getToken');
  },

  // Remove the authentication token from the main process
  async removeToken(): Promise<void> {
    await ipcRenderer.invoke('removeToken');
  },
};

contextBridge.exposeInMainWorld('ipc', handler);

export type IpcHandler = typeof handler;
