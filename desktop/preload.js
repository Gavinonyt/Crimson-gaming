const { contextBridge, ipcRenderer } = require("electron");

contextBridge.exposeInMainWorld("ultraPresence", {
  setActivity: (activity) => ipcRenderer.invoke("ultra-presence:set", activity),
  saveClientId: (clientId) => ipcRenderer.invoke("ultra-presence:save-client-id", clientId),
  getStatus: () => ipcRenderer.invoke("ultra-presence:status"),
  openSettings: () => ipcRenderer.invoke("ultra-presence:open-settings"),
  openExternal: (url) => ipcRenderer.invoke("ultra-presence:open-external", url)
});
