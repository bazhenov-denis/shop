// src/api/payments.ts

// Базовый URL вашего бэкенда
const BASE_URL = "https://fleshly-economic-yellowhammer.cloudpub.ru";

export interface AccountInfo {
    userId: number;
    balance: number;
}

export interface Transaction {
    transactionId: number;
    type: string;
    amount: number;
    date: string;
}

/**
 * Получить информацию о счёте (текущий баланс).
 * GET /payment/{userId}
 */
export async function getAccountInfo(userId: number): Promise<AccountInfo> {
    const response = await fetch(`${BASE_URL}/payment/${userId}`);
    if (!response.ok) {
        throw new Error(`Не удалось получить информацию о счёте (status: ${response.status})`);
    }
    const data: AccountInfo = await response.json();
    return data;
}

/**
 * Создать (инициализировать) счёт пользователя.
 * POST /payment/create/{userId}
 */
export async function createAccount(userId: number): Promise<AccountInfo> {
    const response = await fetch(`${BASE_URL}/payment/create/${userId}`, {
        method: "POST",
    });
    if (!response.ok) {
        throw new Error(`Не удалось создать счёт (status: ${response.status})`);
    }
    const data: AccountInfo = await response.json();
    return data;
}

/**
 * Пополнить баланс пользователя на указанную сумму.
 * POST /payment/topup/{userId}/{amount}
 */
export async function topUpAccount(
    userId: number,
    amount: number
): Promise<AccountInfo> {
    const response = await fetch(`${BASE_URL}/payment/topup/${userId}/${amount}`, {
        method: "PUT",
    });
    if (!response.ok) {
        const text = await response.text();
        throw new Error(`Не удалось пополнить счёт: ${text}`);
    }
    const data: AccountInfo = await response.json();
    return data;
}

/**
 * Получить историю операций пользователя.
 * GET /payment/transaction/{userId}
 */
export async function getTransactions(userId: number): Promise<Transaction[]> {
    const response = await fetch(`${BASE_URL}/payment/transaction/${userId}`);
    if (!response.ok) {
        throw new Error(`Не удалось получить историю транзакций (status: ${response.status})`);
    }
    const data: Transaction[] = await response.json();
    return data;
}
