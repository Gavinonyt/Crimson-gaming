# Ultra Launcher Windows Companion

The companion opens the Ultra Launcher website in a desktop window and connects to the **Discord desktop client** using Discord Rich Presence.

## Setup

1. Install and open the Discord desktop application. Sign in normally inside Discord.
2. Start Ultra Launcher from the Windows companion.
3. On first launch, open the Discord Developer Portal: https://discord.com/developers/applications
4. Create a Discord application named **Ultra Launcher**.
5. In **General Information**, copy the **Application ID** (not a bot token or client secret).
6. Paste the Application ID into the companion setup window and choose **Save & Connect Discord**.
7. Keep both Discord and Ultra Launcher open. Your Discord profile can now show activity such as **Playing Ultra Launcher**, **Playing Ultra Sprint**, or **Playing Ultra Drift**.

The application ID is stored locally in the companion's app data. Never enter your Discord password, bot token, or client secret.

## Build the Windows installer

From this directory:

```sh
npm install
npm run dist
```

The installer is written to `dist/Ultra-Launcher-Setup-1.0.0.exe`.

## Important

- Rich Presence requires the Discord **desktop** app running on the same Windows PC. The Discord mobile app and a normal mobile browser cannot publish desktop Rich Presence.
- Ultra's built-in games report their current activity to the companion. Other PC games are not automatically detected by this initial version.
- The existing server invite and server widget in Ultra Social are separate from Rich Presence. They let players join/view the community server; they do not sign a user into Discord.
- The website and companion do not request Discord passwords, bot tokens, or private account credentials.
