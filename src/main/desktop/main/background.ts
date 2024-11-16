import path from 'path';
import { app } from 'electron';
import serve from 'electron-serve';
import i18next from '../next-i18next.config.js';
import { createWindow } from './helpers';
import { userStore } from './helpers/user-store';

const isProd = process.env.NODE_ENV === 'production';

if (isProd) {
  serve({ directory: 'app' });
} else {
  app.setPath('userData', `${app.getPath('userData')} (development)`);
}

(async () => {
  await app.whenReady();

  const mainWindow = createWindow('main', {
    width: 1000,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      nodeIntegration: false,
      contextIsolation: true,
    },
  });

  const locale = userStore.get('locale', i18next.i18n.defaultLocale);

  if (isProd) {
    await mainWindow.loadURL(`app://./${locale}/home`);
  } else {
    const port = process.argv[2];
    await mainWindow.loadURL(`http://localhost:${port}/${locale}/home`);
    mainWindow.webContents.openDevTools();
  }
})();

app.on('window-all-closed', () => {
  app.quit();
});
