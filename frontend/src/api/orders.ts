// src/api/orders.ts

const BASE_URL = "http://localhost:8080";

export interface OrderRequestItem {
    productId: number;
    quantity: number;
}

export interface OrderRequest {
    userId: string;
    items: OrderRequestItem[];
}

export interface OrderResponseItem {
    productId: number;
    title: string;
    quantity: number;
    price: number;
}

export interface OrderResponse {
    orderId: number;
    totalAmount: number;
    status: string;
    createdAt: string; // ISO-строка
    items: OrderResponseItem[];
}

/**
 * Создать новый заказ.
 * POST http://localhost:8080/order
 */
export async function createOrder(
    userId: string,
    items: Array<{ productId: number; quantity: number }>
): Promise<OrderResponse> {
    const body: OrderRequest = {
        userId,
        items: items.map((it) => ({
            productId: it.productId,
            quantity: it.quantity,
        })),
    };

    const response = await fetch(`${BASE_URL}/order`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
    });
    if (!response.ok) {
        const txt = await response.text();
        throw new Error(`Ошибка при создании заказа: ${txt}`);
    }
    const data: OrderResponse = await response.json();
    return data;
}

/**
 * Получить список всех заказов пользователя.
 * GET http://localhost:8080/order/{userId}
 */
export async function fetchAllOrders(userId: string): Promise<OrderResponse[]> {
    const response = await fetch(`${BASE_URL}/order/${userId}`);
    if (!response.ok) {
        throw new Error(
            `Не удалось получить список заказов (status: ${response.status})`
        );
    }
    const data: OrderResponse[] = await response.json();
    return data;
}

/**
 * Получить один заказ по ID пользователя и ID заказа.
 * GET http://localhost:8080/order/{userId}/{orderId}
 */
export async function fetchOrderById(
    userId: string,
    orderId: number
): Promise<OrderResponse> {
    const response = await fetch(`${BASE_URL}/order/${userId}/${orderId}`);
    if (!response.ok) {
        throw new Error(
            `Не удалось получить заказ с ID=${orderId} (status: ${response.status})`
        );
    }
    const data: OrderResponse = await response.json();
    return data;
}
