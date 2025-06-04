import React, {
    createContext,
    ReactNode,
    useContext,
    useReducer,
    Dispatch,
} from "react";
import type { CartItem } from "@/types/index.d.ts";

interface CartState {
    items: CartItem[];
    totalAmount: number;
}

type CartAction =
    | { type: "ADD_ITEM"; payload: { product: { id: number; title: string; price: number }; quantity: number } }
    | { type: "REMOVE_ITEM"; payload: { productId: number } }
    | { type: "CLEAR_CART" };

const CartStateContext = createContext<CartState | undefined>(undefined);
const CartDispatchContext = createContext<Dispatch<CartAction> | undefined>(
    undefined
);

const initialState: CartState = {
    items: [],
    totalAmount: 0,
};

/**
 * Reducer для корзины:
 * - ADD_ITEM: добавляет товар или увеличивает его количество
 * - REMOVE_ITEM: уменьшает количество на 1 или удаляет, если quantity === 1
 * - CLEAR_CART: сбрасывает корзину в начальное состояние
 */
function cartReducer(state: CartState, action: CartAction): CartState {
    switch (action.type) {
        case "ADD_ITEM": {
            const { product, quantity } = action.payload;
            const existingIndex = state.items.findIndex(
                (item) => item.productId === product.id
            );
            let updatedItems: CartItem[] = [];

            if (existingIndex >= 0) {
                // Товар уже есть – увеличиваем количество
                updatedItems = state.items.map((item, idx) =>
                    idx === existingIndex
                        ? { ...item, quantity: item.quantity + quantity }
                        : item
                );
            } else {
                // Добавляем новый товар
                const newCartItem: CartItem = {
                    productId: product.id,
                    title: product.title,
                    price: product.price,
                    quantity: quantity,
                };
                updatedItems = [...state.items, newCartItem];
            }

            // Пересчитываем общую сумму
            const newTotal = updatedItems.reduce(
                (sum, it) => sum + it.price * it.quantity,
                0
            );

            return {
                items: updatedItems,
                totalAmount: newTotal,
            };
        }

        case "REMOVE_ITEM": {
            const productId = action.payload.productId;
            const existingIndex = state.items.findIndex(
                (item) => item.productId === productId
            );
            if (existingIndex < 0) {
                return state;
            }
            const existingItem = state.items[existingIndex];
            let updatedItems: CartItem[] = [];

            if (existingItem.quantity > 1) {
                // Уменьшаем количество на 1
                updatedItems = state.items.map((item, idx) =>
                    idx === existingIndex
                        ? { ...item, quantity: item.quantity - 1 }
                        : item
                );
            } else {
                // Удаляем товар полностью
                updatedItems = state.items.filter(
                    (item) => item.productId !== productId
                );
            }

            const newTotal = updatedItems.reduce(
                (sum, it) => sum + it.price * it.quantity,
                0
            );
            return {
                items: updatedItems,
                totalAmount: newTotal,
            };
        }

        case "CLEAR_CART": {
            return initialState;
        }

        default: {
            return state;
        }
    }
}

/**
 * Провайдер, оборачивает всё приложение, чтобы предоставить
 * доступ к состоянию корзины и функции dispatch.
 */
export function CartProvider({ children }: { children: ReactNode }) {
    const [state, dispatch] = useReducer(cartReducer, initialState);
    return (
        <CartDispatchContext.Provider value={dispatch}>
            <CartStateContext.Provider value={state}>
                {children}
            </CartStateContext.Provider>
        </CartDispatchContext.Provider>
    );
}

/** Хук для получения состояния корзины */
export function useCartState(): CartState {
    const context = useContext(CartStateContext);
    if (context === undefined) {
        throw new Error("useCartState должен вызываться внутри CartProvider");
    }
    return context;
}

/** Хук для отправки действий к корзине */
export function useCartDispatch(): Dispatch<CartAction> {
    const context = useContext(CartDispatchContext);
    if (context === undefined) {
        throw new Error("useCartDispatch должен вызываться внутри CartProvider");
    }
    return context;
}
