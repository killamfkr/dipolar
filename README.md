# dipolar (IPTV/M3U web player for CasaOS)

A tiny self‑hosted IPTV player that runs as a static web app in a container.
Paste a HLS/M3U8 stream or import an M3U playlist. Click a channel to play.

## Quick start (Docker Compose / CasaOS Custom Install)

1. Upload this folder to your CasaOS box (e.g., in `/DATA/AppData/dipolar`).
2. In CasaOS → *App Store* → *Custom Install* → *Compose*, select `docker-compose.yml` from this folder.
3. Install. CasaOS will build the image and run the `dipolar` container.
4. Open: `http://<your-box-ip>:8099`

### Notes
- HLS streams (`.m3u8`) use hls.js in the browser. Direct MP4/WEBM URLs play natively.
- If your playlist requires auth headers or tokens, host a proxy or supply pre‑signed URLs.
- To change the external port, edit `docker-compose.yml` (`8099:80`).

## Local Docker (optional)
```bash
docker build -t dipolar:latest .
docker run --name dipolar -p 8099:80 --restart unless-stopped dipolar:latest
```

## Files
- `index.html` – Single-file web app (HTML/CSS/JS).
- `Dockerfile` – Nginx serving the app.
- `docker-compose.yml` – Container definition for CasaOS.
