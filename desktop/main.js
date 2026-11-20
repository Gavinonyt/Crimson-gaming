const { app, BrowserWindow, ipcMain, Menu, Tray, nativeImage, shell } = require("electron");
const path = require("path");
const fs = require("fs");
const RPC = require("discord-rpc");

const SITE_URL = "https://gavinonyt.github.io/Crimson-gaming/";
const CONFIG_PATH = () => path.join(app.getPath("userData"), "config.json");
let mainWindow = null;
let settingsWindow = null;
let tray = null;
let rpc = null;
let rpcClientId = "";
let rpcReady = false;
let lastActivity = null;
let connectionMessage = "Add your Discord Application ID to connect.";

function readConfig() {
  try { return JSON.parse(fs.readFileSync(CONFIG_PATH(), "utf8")); }
  catch { return {}; }
}
function writeConfig(config) {
  fs.mkdirSync(path.dirname(CONFIG_PATH()), { recursive: true });
  fs.writeFileSync(CONFIG_PATH(), JSON.stringify(config, null, 2), "utf8");
}
function disconnectRPC() {
  rpcReady = false;
  if (rpc) {
    try { rpc.clearActivity(); } catch {}
    try { rpc.destroy(); } catch {}
    rpc = null;
  }
}
async function connectRPC(clientId) {
  if (!/^\d{17,22}$/.test(String(clientId || ""))) {
    connectionMessage = "Enter a valid 17–22 digit Discord Application ID.";
    return { ok: false, message: connectionMessage };
  }
  if (rpcReady && rpcClientId === clientId) return { ok: true, message: "Discord is connected." };
  disconnectRPC();
  rpcClientId = clientId;
  try {
    RPC.register(clientId);
    const client = new RPC.Client({ transport: "ipc" });
    rpc = client;
    client.once("ready", () => {
      if (rpc !== client) return;
      rpcReady = true;
      connectionMessage = "Connected to Discord. Rich Presence is active.";
      if (lastActivity) client.setActivity(lastActivity).catch(() => {});
      sendStatus();
    });
    await client.login({ clientId });
    await new Promise((resolve, reject) => {
      if (rpcReady) return resolve();
      const timer = setTimeout(() => reject(new Error("Discord did not respond. Open the Discord desktop app and try again.")), 12000);
      client.once("ready", () => { clearTimeout(timer); resolve(); });
      client.once("disconnected", () => { clearTimeout(timer); reject(new Error("Discord disconnected.")); });
    });
    return { ok: true, message: connectionMessage };
  } catch (error) {
    disconnectRPC();
    connectionMessage = "Could not connect. Make sure the Discord desktop app is open.";
    return { ok: false, message: connectionMessage };
  }
}
function sendStatus() {
  const status = { connected: rpcReady, message: connectionMessage, clientIdConfigured: /^\d{17,22}$/.test(rpcClientId) };
  for (const win of [mainWindow, settingsWindow]) {
    if (win && !win.isDestroyed()) win.webContents.send("ultra-presence:status-changed", status);
  }
}
async function setActivity(activity) {
  const safe = {
    details: String(activity?.details || "Playing Ultra Launcher").slice(0, 128),
    state: String(activity?.state || "Gaming").slice(0, 128),
    instance: false
  };
  lastActivity = safe;
  if (!rpcReady) {
    const cfg = readConfig();
    const connected = await connectRPC(cfg.discordApplicationId || "");
    if (!connected.ok) { sendStatus(); return connected; }
  }
  try {
    await rpc.setActivity(safe);
    connectionMessage = "Connected to Discord. Rich Presence is active.";
    sendStatus();
    return { ok: true, message: connectionMessage };
  } catch {
    connectionMessage = "Discord activity could not be updated. Reconnect Discord and try again.";
    sendStatus();
    return { ok: false, message: connectionMessage };
  }
}
function createMainWindow() {
  mainWindow = new BrowserWindow({
    width: 1440, height: 900, minWidth: 900, minHeight: 620,
    title: "Ultra Launcher",
    backgroundColor: "#08070d",
    webPreferences: {
      preload: path.join(__dirname, "preload.js"),
      contextIsolation: true,
      nodeIntegration: false,
      sandbox: true
    }
  });
  mainWindow.loadURL(SITE_URL);
  mainWindow.on("closed", () => { mainWindow = null; });
}
function createSettingsWindow() {
  if (settingsWindow && !settingsWindow.isDestroyed()) {
    settingsWindow.focus();
    return;
  }
  settingsWindow = new BrowserWindow({
    width: 520, height: 650, resizable: false, title: "Ultra Launcher — Discord Setup",
    backgroundColor: "#090812",
    webPreferences: {
      preload: path.join(__dirname, "preload.js"),
      contextIsolation: true, nodeIntegration: false, sandbox: true
    }
  });
  settingsWindow.loadFile(path.join(__dirname, "settings.html"));
  settingsWindow.on("closed", () => { settingsWindow = null; });
}
function createTray() {
  try {
    tray = new Tray(nativeImage.createEmpty());
    tray.setToolTip("Ultra Launcher");
    tray.setContextMenu(Menu.buildFromTemplate([
      { label: "Open Ultra Launcher", click: () => mainWindow ? mainWindow.show() : createMainWindow() },
      { label: "Discord Rich Presence Settings", click: createSettingsWindow },
      { type: "separator" },
      { label: "Quit Ultra Launcher", click: () => app.quit() }
    ]));
    tray.on("double-click", () => mainWindow ? mainWindow.show() : createMainWindow());
  } catch {}
}
ipcMain.handle("ultra-presence:set", (_event, activity) => setActivity(activity));
ipcMain.handle("ultra-presence:status", () => ({
  connected: rpcReady, message: connectionMessage,
  clientIdConfigured: /^\d{17,22}$/.test(rpcClientId)
}));
ipcMain.handle("ultra-presence:save-client-id", async (_event, clientId) => {
  const value = String(clientId || "").trim();
  if (!/^\d{17,22}$/.test(value)) return { ok: false, message: "Application ID must be 17–22 digits." };
  writeConfig({ ...readConfig(), discordApplicationId: value });
  const result = await connectRPC(value);
  sendStatus();
  if (result.ok && lastActivity) await setActivity(lastActivity);
  return result;
});
ipcMain.handle("ultra-presence:open-settings", () => { createSettingsWindow(); return { ok: true }; });
ipcMain.handle("ultra-presence:open-external", async (_event, url) => {
  if (url === "https://discord.com/developers/applications") await shell.openExternal(url);
  return { ok: true };
});
app.whenReady().then(() => {
  const cfg = readConfig();
  rpcClientId = cfg.discordApplicationId || "";
  createMainWindow();
  createTray();
  if (!rpcClientId) createSettingsWindow();
  else connectRPC(rpcClientId).then(sendStatus);
  app.on("activate", () => { if (BrowserWindow.getAllWindows().length === 0) createMainWindow(); });
});
app.on("before-quit", disconnectRPC);
app.on("window-all-closed", (event) => { /* Keep the companion alive in the tray on Windows. */ });
