import type { OrderRequest, OrderResponse } from "@/types/index.d.ts";

const BASE_URL = "http://localhost:8080";

/**
 * Создание нового заказа.
 * POST http://localhost:8080/order
 *
 * @param userId – ID пользователя (пока «1»)
 * @param items – массив {productId, quantity}
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
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
    });
    if (!response.ok) {
        const text = await response.text();
        throw new Error(`Ошибка при создании заказа: ${text}`);
    }
    const data: OrderResponse = await response.json();
    return data;
}
