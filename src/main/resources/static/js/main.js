const { app, BrowserWindow } = require('eletron');

let mainWindow;


app.on('ready', () => {
    mainWindow = new BrowserWindow({
        width: 1200,
        height: 800,
        webPreferences: {
            nodeIntegration: true,
        },
    });

    // Load your web application
    mainWindow.loadURL('http://localhost:3000'); // Replace with your web app's URL
});

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});