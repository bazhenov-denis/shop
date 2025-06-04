// src/pages/OrderDetailsPage.tsx

import React, { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { fetchOrderById, OrderResponse } from "@/api/orders";

export default function OrderDetailsPage() {
    const { orderId } = useParams<{ orderId: string }>();
    const navigate = useNavigate();
    const USER_ID = "1";

    const [order, setOrder] = useState<OrderResponse | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!orderId) {
            setError("ID заказа не определён");
            setLoading(false);
            return;
        }

        (async () => {
            setLoading(true);
            setError(null);
            try {
                const data = await fetchOrderById(USER_ID, Number(orderId));
                setOrder(data);
            } catch (err) {
                console.error(err);
                setError("Не удалось загрузить данные заказа");
            } finally {
                setLoading(false);
            }
        })();
    }, [orderId]);

    if (loading) {
        return (
            <div className="container mx-auto px-4 py-8">
                <p className="text-gray-600">Загрузка…</p>
            </div>
        );
    }

    if (error || !order) {
        return (
            <div className="container mx-auto px-4 py-8">
                <p className="text-red-600">{error ?? "Заказ не найден"}</p>
                <button
                    onClick={() => navigate(-1)}
                    className="mt-4 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                    Вернуться назад
                </button>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-4 py-8">
            <div className="flex items-center justify-between mb-6">
                <h1 className="text-2xl font-semibold text-gray-800">
                    Заказ №{order.orderId}
                </h1>
                <Link to="/orders" className="text-sm text-indigo-600 hover:underline">
                    &larr; К списку заказов
                </Link>
            </div>

            <div className="bg-white rounded-lg shadow-md p-6 mb-8">
                <div className="mb-4">
                    <span className="text-gray-700 font-medium">Статус:</span>{" "}
                    <span className="text-gray-800">{order.status}</span>
                </div>
                <div className="mb-4">
                    <span className="text-gray-700 font-medium">Дата создания:</span>{" "}
                    <span className="text-gray-800">
            {new Date(order.createdAt).toLocaleString("ru-RU", {
                day: "2-digit",
                month: "2-digit",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit",
            })}
          </span>
                </div>
                <div className="mb-4">
                    <span className="text-gray-700 font-medium">Итого:</span>{" "}
                    <span className="text-gray-800">
            ₽{order.totalAmount.toFixed(2)}
          </span>
                </div>
            </div>

            <h2 className="text-xl font-semibold text-gray-800 mb-4">
                Состав заказа:
            </h2>
            <ul className="space-y-3">
                {order.items.map((it) => (
                    <li
                        key={it.productId}
                        className="flex justify-between items-center bg-gray-50 px-4 py-3 rounded-md"
                    >
                        <span className="text-gray-800 font-medium">{it.title}</span>
                        <span className="text-gray-600">
              × {it.quantity} &nbsp; (₽{it.price.toFixed(2)})
            </span>
                    </li>
                ))}
            </ul>
        </div>
    );
}
