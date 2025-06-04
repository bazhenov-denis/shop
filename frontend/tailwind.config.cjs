/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./index.html",
        "./src/**/*.{js,ts,jsx,tsx}"
    ],
    theme: {
        extend: {
            // При желании можно расширять цвета, шрифты, промежутки
            colors: {
                primary: "#2563EB",    // пример своего основного цвета (Синий-600)
                secondary: "#10B981"   // пример своего вторичного цвета (Зелёный-500)
            }
        }
    },
    plugins: []
};
