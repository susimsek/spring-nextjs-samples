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

  // Set the locale in the main process
  async setLocale(locale: string): Promise<void> {
    await ipcRenderer.invoke('setLocale', locale);
  },

  // Set the theme in the main process
  async setTheme(theme: 'light' | 'dark'): Promise<void> {
    await ipcRenderer.invoke('setTheme', theme);
  },

  // Get the current theme from the main process
  async getTheme(): Promise<'light' | 'dark'> {
    return ipcRenderer.invoke('getTheme');
  },
};

contextBridge.exposeInMainWorld('ipc', handler);

export type IpcHandler = typeof handler;
