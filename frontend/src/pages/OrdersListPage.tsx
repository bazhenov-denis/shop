// src/pages/OrdersListPage.tsx

import React, { useEffect, useState } from "react";
import { fetchAllOrders, OrderResponse } from "@/api/orders";
import { Link } from "react-router-dom";

export default function OrdersListPage() {
    // Пока userId жёстко = "1"
    const USER_ID = "1";

    const [orders, setOrders] = useState<OrderResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        (async () => {
            setLoading(true);
            setError(null);
            try {
                const data = await fetchAllOrders(USER_ID);
                setOrders(data);
            } catch (err) {
                console.error(err);
                setError("Не удалось загрузить список заказов");
            } finally {
                setLoading(false);
            }
        })();
    }, []);

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-2xl font-semibold text-gray-800 mb-6">Все заказы</h1>

            {loading ? (
                <p className="text-gray-600">Загрузка…</p>
            ) : error ? (
                <p className="text-red-600">{error}</p>
            ) : orders.length === 0 ? (
                <p className="text-gray-600">Нет ни одного заказа.</p>
            ) : (
                <ul className="space-y-4">
                    {orders.map((order) => (
                        <li
                            key={order.orderId}
                            className="bg-white rounded-lg shadow-md p-4 flex justify-between items-center"
                        >
                            <Link
                                to={`/orders/${order.orderId}`}
                                className="text-lg text-indigo-600 hover:underline"
                            >
                                Заказ №{order.orderId}
                            </Link>
                            <div className="flex space-x-6">
                <span className="text-gray-700">
                  Сумма: ₽{order.totalAmount.toFixed(2)}
                </span>
                                <span className="text-gray-500">
                  {new Date(order.createdAt).toLocaleString("ru-RU", {
                      day: "2-digit",
                      month: "2-digit",
                      year: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                  })}
                </span>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}
