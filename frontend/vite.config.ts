// vite.config.ts
import { defineConfig } from "vite"
import react from "@vitejs/plugin-react"
import path from "path"

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src"),
    }
  },
  server: {
    host: "0.0.0.0",

    // Разрешаем доступ с вашего Cloudpub‐хоста:
    allowedHosts: [
      "densely-bewitching-sloth.cloudpub.ru"
    ]

    // Если вы хотите разрешить вообще все хосты (не только этот),
    // можно указать строку "all":
    // allowedHosts: "all"
  }
})
