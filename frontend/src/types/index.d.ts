// Здесь храним все интерфейсы и типы, которые понадобятся в проекте.

/** Информация, возвращаемая сервером при GET /product */
export interface Product {
    id: number;
    title: string;
    description: string;
    price: number;
    // Когда появится, можно будет добавить поле:
    // imageUrl?: string;
}

/** Элемент в корзине (локальная структура) */
export interface CartItem {
    productId: number;
    title: string;
    price: number;
    quantity: number;
}

/** Запрос для POST /order */
export interface OrderRequestItem {
    productId: number;
    quantity: number;
}

export interface OrderRequest {
    userId: string;
    items: OrderRequestItem[];
}

/** Позиция заказа в ответе от сервера */
export interface OrderResponseItem {
    productId: number;
    title: string;
    quantity: number;
    price: number;
}

/** Ответ сервера после создания заказа */
export interface OrderResponse {
    orderId: number;
    totalAmount: number;
    status: string;
    createdAt: string; // ISO-строка
    items: OrderResponseItem[];
}
