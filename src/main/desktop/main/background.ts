import path from 'path';
import { app, ipcMain } from 'electron';
import serve from 'electron-serve';
import { createWindow } from './helpers';
import { userStore } from './helpers/user-store';
import { autoUpdater } from 'electron-updater'; // Electron-Updater'ı ekliyoruz
import i18next from '../next-i18next.config.js';

const isProd: boolean = process.env.NODE_ENV === 'production';
export const protocol = 'spring-nextron';

if (isProd) {
  serve({ directory: 'app' });
} else {
  app.setPath('userData', `${app.getPath('userData')} (development)`);
}

let windowIsReady = false;
let mainWindow: Electron.BrowserWindow | null = null;

const getMainWindowWhenReady = async () => {
  if (!windowIsReady) {
    await new Promise(resolve => ipcMain.once('window-is-ready', resolve));
  }
  return mainWindow;
};

function checkLauncherUrl(getMainWindow: () => Promise<Electron.BrowserWindow | null>) {
  if (process.platform === 'darwin') {
    app.on('open-url', async (_event, url) => {
      const mainWindow = await getMainWindow();
      if (mainWindow) {
        mainWindow.webContents.send('launcher-url', url);
        if (mainWindow.isMinimized()) {
          mainWindow.restore();
        }
      }
    });
  }

  if (process.platform === 'win32') {
    const gotTheLock = app.requestSingleInstanceLock();
    if (!gotTheLock) {
      app.quit();
      return false;
    }

    app.setAsDefaultProtocolClient(protocol);

    app.on('second-instance', async (_event, args) => {
      const mainWindow = await getMainWindow();
      const url = args.find(arg => arg.startsWith(`${protocol}://`));
      if (url && mainWindow) {
        mainWindow.webContents.send('launcher-url', url);
        if (mainWindow.isMinimized()) {
          mainWindow.restore();
        }
        mainWindow.focus();
      }
    });

    const url = process.argv.find(arg => arg.startsWith(`${protocol}://`));
    if (url) {
      getMainWindow().then(mainWindow => {
        if (mainWindow) {
          mainWindow.webContents.send('launcher-url', url);
        }
      });
    }
  }

  return true;
}

function checkForUpdates() {
  autoUpdater.checkForUpdatesAndNotify();

  autoUpdater.on('update-available', () => {
    if (mainWindow) {
      mainWindow.webContents.send('update_available');
    }
  });

  autoUpdater.on('update-downloaded', () => {
    if (mainWindow) {
      mainWindow.webContents.send('update_downloaded');
    }
  });

  autoUpdater.on('error', error => {
    console.error('Error during update:', error);
  });
}

(async () => {
  const shouldContinue = checkLauncherUrl(getMainWindowWhenReady);
  if (!shouldContinue) return;

  await app.whenReady();

  ipcMain.once('window-is-ready', () => {
    windowIsReady = true;
  });

  mainWindow = createWindow('main', {
    width: 1000,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      nodeIntegration: false,
      contextIsolation: true,
    },
  });

  mainWindow.once('ready-to-show', () => {
    checkForUpdates();
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
