import React from "react";
import type { OrderResponse } from "@/types/index.d.ts";

/**
 * Компонент отображает данные успешного заказа после POST /order.
 * Показывает номер заказа, статус, дату, сумму и список позиций.
 */
interface OrderConfirmationProps {
    order: OrderResponse;
}

export default function OrderConfirmation({
                                              order,
                                          }: OrderConfirmationProps) {
    return (
        <div className="max-w-2xl mx-auto my-8 bg-white rounded-lg shadow-md overflow-hidden">
            <div className="px-6 py-4 border-b border-gray-200">
                <h2 className="text-2xl font-semibold text-gray-800">
                    Заказ №{order.orderId} оформлен
                </h2>
                <p className="mt-2 text-gray-600">
                    Статус: <span className="font-medium">{order.status}</span>
                </p>
                <p className="mt-1 text-gray-600">
                    Дата:{" "}
                    <span className="font-medium">
            {new Date(order.createdAt).toLocaleString("ru-RU", {
                day: "2-digit",
                month: "2-digit",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit",
            })}
          </span>
                </p>
                <p className="mt-1 text-gray-600">
                    Общая сумма:{" "}
                    <span className="font-medium">₽{order.totalAmount.toFixed(2)}</span>
                </p>
            </div>

            <div className="px-6 py-4">
                <h3 className="text-xl font-semibold text-gray-800 mb-4">
                    Состав заказа:
                </h3>
                <ul className="space-y-3">
                    {order.items.map((item) => (
                        <li
                            key={item.productId}
                            className="flex justify-between items-center bg-gray-50 px-4 py-3 rounded-md"
                        >
                            <span className="text-gray-800 font-medium">{item.title}</span>
                            <span className="text-gray-600">
                × {item.quantity} &nbsp; (₽{item.price.toFixed(2)})
              </span>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
