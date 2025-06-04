import React from "react";
import type { Product } from "@/types/index.d.ts";
import placeholder from "@/assets/placeholder.jpg";

interface ProductCardProps {
    product: Product;
    onAdd: (product: Product) => void;
}

/**
 * Карточка одного товара:
 * - Показывает заглушку (placeholder.png)
 * - Показывает название и цену
 * - Кнопка «Добавить в корзину» сообщает наружу о продукте
 */
export default function ProductCard({ product, onAdd }: ProductCardProps) {
    return (
        <div className="flex flex-col bg-white rounded-lg shadow-lg overflow-hidden w-full max-w-xs m-4">
            {/* Блок с изображением */}
            <div className="w-full h-48 flex justify-center items-center bg-gray-100">
                <img
                    src={placeholder}
                    alt={product.title}
                    className="object-cover h-full w-full"
                />
            </div>

            {/* Контент карточки */}
            <div className="p-4 flex flex-col flex-1 justify-between">
                <div>
                    <h3 className="text-lg font-semibold text-gray-800 mb-2">
                        {product.title}
                    </h3>
                    <p className="text-sm text-gray-600 mb-4 line-clamp-2">
                        {product.description}
                    </p>
                </div>

                <div className="flex items-center justify-between mt-auto">
          <span className="text-xl font-bold text-gray-900">
            ₽{product.price.toFixed(2)}
          </span>
                    <button
                        type="button"
                        onClick={() => onAdd(product)}
                        className="px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                    >
                        Добавить в корзину
                    </button>
                </div>
            </div>
        </div>
    );
}
