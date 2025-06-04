import type { Product } from "@/types/index.d.ts";

const BASE_URL = "http://localhost:8080";

/**
 * Получение списка всех товаров.
 * GET http://localhost:8080/product
 */
export async function fetchAllProducts(): Promise<Product[]> {
    const response = await fetch(`${BASE_URL}/product`);
    if (!response.ok) {
        throw new Error("Не удалось получить список товаров");
    }
    const data: Product[] = await response.json();
    return data;
}
