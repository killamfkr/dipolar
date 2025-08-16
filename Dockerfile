# dipolar: lightweight IPTV/M3U web player
FROM nginx:alpine
COPY index.html /usr/share/nginx/html/index.html
EXPOSE 80
