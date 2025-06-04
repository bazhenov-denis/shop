import React from "react";
import { useCartState, useCartDispatch } from "@/context/CartContext";
import type { CartItem } from "@/types/index.d.ts";
import placeholder from "@/assets/placeholder.jpg";
import { createOrder } from "@/api/orders";
import type { OrderResponse } from "@/types/index.d.ts";

interface CartSidebarProps {
    isOpen: boolean;
    onClose: () => void;
    onOrderSuccess: (order: OrderResponse) => void;
}

/**
 * Боковая панель «Корзина»:
 * - Отображает все товары, которые есть в корзине
 * - Позволяет увеличить/уменьшить количество каждого
 * - Показывает итоговую сумму
 * - Кнопка «Оформить заказ» делает POST /order
 */
export default function CartSidebar({
                                        isOpen,
                                        onClose,
                                        onOrderSuccess,
                                    }: CartSidebarProps) {
    const { items, totalAmount } = useCartState();
    const dispatch = useCartDispatch();

    // Увеличение количества на 1
    const handleIncrement = (productId: number) => {
        const targetItem = items.find((it) => it.productId === productId);
        if (!targetItem) return;
        dispatch({
            type: "ADD_ITEM",
            payload: {
                product: { id: productId, title: targetItem.title, price: targetItem.price },
                quantity: 1,
            },
        });
    };

    // Уменьшение количества (или удаление, если 1)
    const handleDecrement = (productId: number) => {
        dispatch({
            type: "REMOVE_ITEM",
            payload: { productId },
        });
    };

    // Оформление заказа
    const handlePlaceOrder = async () => {
        if (items.length === 0) return;
        // Формируем массив для POST
        const payloadItems = items.map((it) => ({
            productId: it.productId,
            quantity: it.quantity,
        }));
        try {
            const orderResponse = await createOrder("1", payloadItems); // пока жёстко userId = "1"
            // После успешного ответа сбрасываем корзину
            dispatch({ type: "CLEAR_CART" });
            // Передаём объект заказа наверх
            onOrderSuccess(orderResponse);
            // Закрываем боковую панель
            onClose();
        } catch (err) {
            console.error(err);
            alert("Ошибка при оформлении заказа");
        }
    };

    // Если isOpen = false, просто не рендерим панель
    if (!isOpen) {
        return null;
    }

    return (
        <div className="fixed inset-0 z-50 flex">
            {/* Полупрозрачный задний фон */}
            <div
                className="fixed inset-0 bg-black bg-opacity-50"
                onClick={onClose}
            ></div>

            {/* Сама боковая панель */}
            <div className="relative ml-auto w-full sm:w-96 bg-white shadow-xl overflow-y-auto">
                {/* Заголовок и кнопка закрыть */}
                <div className="flex justify-between items-center px-6 py-4 border-b border-gray-200">
                    <h2 className="text-xl font-semibold text-gray-800">Корзина</h2>
                    <button
                        type="button"
                        onClick={onClose}
                        className="text-gray-500 hover:text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 rounded"
                    >
                        <span className="sr-only">Закрыть</span>
                        ×
                    </button>
                </div>

                {/* Контент с товарами */}
                <div className="px-6 py-4">
                    {items.length === 0 ? (
                        <p className="text-gray-600">В корзине нет товаров.</p>
                    ) : (
                        <ul className="space-y-4">
                            {items.map((item: CartItem) => (
                                <li
                                    key={item.productId}
                                    className="flex items-center space-x-4"
                                >
                                    {/* Изображение */}
                                    <div className="flex-shrink-0 w-16 h-16 bg-gray-100 rounded-md overflow-hidden">
                                        <img
                                            src={placeholder}
                                            alt={item.title}
                                            className="object-cover w-full h-full"
                                        />
                                    </div>

                                    {/* Информация о товаре */}
                                    <div className="flex-1">
                                        <p className="text-base font-medium text-gray-800">
                                            {item.title}
                                        </p>
                                        <p className="text-sm text-gray-600">
                                            ₽{item.price.toFixed(2)} × {item.quantity}
                                        </p>
                                    </div>

                                    {/* Контролы количества */}
                                    <div className="flex flex-col items-center space-y-1">
                                        <button
                                            type="button"
                                            onClick={() => handleIncrement(item.productId)}
                                            className="w-6 h-6 flex items-center justify-center bg-green-500 text-white rounded hover:bg-green-600 focus:outline-none focus:ring-2 focus:ring-green-400"
                                        >
                                            +
                                        </button>
                                        <span className="text-sm text-gray-700">
                      {item.quantity}
                    </span>
                                        <button
                                            type="button"
                                            onClick={() => handleDecrement(item.productId)}
                                            className="w-6 h-6 flex items-center justify-center bg-red-500 text-white rounded hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
                                        >
                                            −
                                        </button>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>

                {/* Футер: Итого и Кнопка оформить */}
                <div className="px-6 py-4 border-t border-gray-200">
                    <div className="flex items-center justify-between mb-4">
                        <span className="text-lg font-semibold text-gray-800">Итого:</span>
                        <span className="text-xl font-bold text-gray-900">
              ₽{totalAmount.toFixed(2)}
            </span>
                    </div>
                    <button
                        type="button"
                        onClick={handlePlaceOrder}
                        disabled={items.length === 0}
                        className="w-full px-4 py-3 bg-indigo-600 text-white font-medium rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-300 disabled:cursor-not-allowed"
                    >
                        Оформить заказ
                    </button>
                </div>
            </div>
        </div>
    );
}
