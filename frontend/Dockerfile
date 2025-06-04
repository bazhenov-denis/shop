FROM node:18-alpine AS builder
WORKDIR /app

# Устанавливаем зависимости
COPY package*.json ./
RUN npm ci

# Копируем приложение
COPY . .

# Открываем порт Vite
EXPOSE 5173

# Запуск dev-сервера Vite на 0.0.0.0, чтобы быть доступным в контейнере
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
