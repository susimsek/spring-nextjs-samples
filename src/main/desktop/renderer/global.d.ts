import { IpcRenderer } from 'electron';

declare global {
  interface Window {
    ipc: IpcRenderer & {
      setLocale?: (locale: string) => void;
      getLocale?: () => Promise<string | null>;
      setTheme?: (theme: 'light' | 'dark') => void;
      getTheme?: () => Promise<'light' | 'dark'>;
      setToken?: (token: string) => Promise<void>;
      getToken?: () => Promise<string | null>;
      removeToken?: () => Promise<void>;
    };
  }
}
