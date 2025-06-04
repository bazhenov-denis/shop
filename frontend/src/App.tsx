import React, { useState } from "react";
import ProductList from "@/components/ProductList";
import CartSidebar from "@/components/CartSidebar";
import OrderConfirmation from "@/components/OrderConfirmation";
import { CartProvider } from "@/context/CartContext";
import type { OrderResponse } from "@/types/index.d.ts";

export default function App() {
    // Состояние: открыта ли боковая панель корзины
    const [isCartOpen, setIsCartOpen] = useState<boolean>(false);
    // После оформления заказа сюда попадает ответ от сервера
    const [orderResult, setOrderResult] = useState<OrderResponse | null>(null);

    // Вызывается после успешного оформления заказа в CartSidebar
    const handleOrderSuccess = (order: OrderResponse) => {
        setOrderResult(order);
        // Закроем корзину (можно, но не обязательно, т.к. CartSidebar уже её закрыл)
        setIsCartOpen(false);
    };

    return (
        <CartProvider>
            <div className="min-h-screen flex flex-col bg-gray-100">
                {/* Хедер */}
                <header className="flex items-center justify-between bg-white shadow-md px-6 py-4">
                    <h1 className="text-2xl font-bold text-gray-800">Электрошоп</h1>
                    <button
                        type="button"
                        onClick={() => setIsCartOpen(true)}
                        className="relative inline-flex items-center px-4 py-2 bg-indigo-600 text-white text-sm font-medium rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    >
                        🛒 Корзина
                    </button>
                </header>

                {/* Основное содержание */}
                <main className="flex-1">
                    {/* Если заказ уже оформлен, показываем окно подтверждения */}
                    {orderResult ? (
                        <div className="container mx-auto px-4">
                            <OrderConfirmation order={orderResult} />
                        </div>
                    ) : (
                        <ProductList />
                    )}
                </main>

                {/* Футер (можно не выводить, т.к. учебный проект) */}
                <footer className="bg-white py-4 shadow-inner">
                    <p className="text-center text-sm text-gray-500">
                        © 2025 Учебный магазин. Все права защищены.
                    </p>
                </footer>

                {/* Боковая панель корзины */}
                <CartSidebar
                    isOpen={isCartOpen}
                    onClose={() => setIsCartOpen(false)}
                    onOrderSuccess={handleOrderSuccess}
                />
            </div>
        </CartProvider>
    );
}
