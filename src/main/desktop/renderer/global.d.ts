import { IpcRenderer } from 'electron';

declare global {
  interface Window {
    ipc: IpcRenderer & {
      setLocale?: (locale: string) => void;
      setTheme?: (theme: 'light' | 'dark') => void;
      getTheme?: () => Promise<'light' | 'dark'>;
    };
  }
}
