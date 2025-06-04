import React, { useEffect, useState } from "react";
import { fetchAllProducts } from "@/api/products";
import type { Product } from "@/types/index.d.ts";
import ProductCard from "./ProductCard";
import { useCartDispatch } from "@/context/CartContext";

export default function ProductList() {
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const dispatch = useCartDispatch();

    // После первого рендера загружаем товары
    useEffect(() => {
        fetchAllProducts()
            .then((data) => {
                setProducts(data);
            })
            .catch((err) => {
                console.error(err);
                setError("Не удалось загрузить товары");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    // Обработчик добавления в корзину
    const handleAddToCart = (product: Product) => {
        dispatch({
            type: "ADD_ITEM",
            payload: {
                product: { id: product.id, title: product.title, price: product.price },
                quantity: 1,
            },
        });
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center h-64">
                <span className="text-lg font-medium text-gray-700">Загрузка товаров…</span>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex justify-center items-center h-64">
                <span className="text-lg font-medium text-red-600">{error}</span>
            </div>
        );
    }

    return (
        <div className="flex flex-wrap justify-center bg-gray-100 py-8">
            {products.map((product) => (
                <ProductCard
                    key={product.id}
                    product={product}
                    onAdd={handleAddToCart}
                />
            ))}
        </div>
    );
}
