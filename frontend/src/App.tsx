// src/App.tsx

import React, { useState } from "react";
import { Routes, Route } from "react-router-dom";
import Header from "@/components/Header";
import ProductsPage from "@/pages/ProductsPage";
import AccountPage from "@/pages/AccountPage";
import OrdersListPage from "@/pages/OrdersListPage";
import OrderDetailsPage from "@/pages/OrderDetailsPage";
import CartSidebar from "@/components/CartSidebar";
import { CartProvider } from "@/context/CartContext";

export default function App() {
    const [cartOpen, setCartOpen] = useState(false);

    return (
        <CartProvider>
            <div className="min-h-screen flex flex-col bg-gray-100">
                <Header openCart={() => setCartOpen(true)} />

                <main className="flex-1">
                    <Routes>
                        <Route path="/" element={<ProductsPage />} />
                        <Route path="/orders" element={<OrdersListPage />} />
                        <Route path="/orders/:orderId" element={<OrderDetailsPage />} />
                        <Route path="/account" element={<AccountPage />} />
                        <Route path="*" element={<ProductsPage />} />
                    </Routes>
                </main>

                <footer className="bg-white py-4 shadow-inner text-center text-sm text-gray-500">
                    © 2025 Учебный магазин.
                </footer>

                <CartSidebar
                    isOpen={cartOpen}
                    onClose={() => setCartOpen(false)}
                />
            </div>
        </CartProvider>
    );
}
