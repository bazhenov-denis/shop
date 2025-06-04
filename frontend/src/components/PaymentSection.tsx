// src/components/PaymentSection.tsx
import React, { useEffect, useState } from "react";
import {
    getAccountInfo,
    createAccount,
    topUpAccount,
    getTransactions,
    AccountInfo,
    Transaction,
} from "@/api/payments";

const USER_ID = 1;

export default function PaymentSection() {
    // Информация о счёте
    const [accountInfo, setAccountInfo] = useState<AccountInfo | null>(null);
    const [loadingInfo, setLoadingInfo] = useState<boolean>(true);
    const [errorInfo, setErrorInfo] = useState<string | null>(null);

    // Состояние пополнения
    const [isToppingUp, setIsToppingUp] = useState<boolean>(false);
    const [topUpAmount, setTopUpAmount] = useState<string>("");
    const [loadingTopUp, setLoadingTopUp] = useState<boolean>(false);
    const [errorTopUp, setErrorTopUp] = useState<string | null>(null);

    // Состояние истории транзакций
    const [showTransactions, setShowTransactions] = useState<boolean>(false);
    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [loadingTransactions, setLoadingTransactions] = useState<boolean>(false);
    const [errorTransactions, setErrorTransactions] = useState<string | null>(null);

    // 1. При монтировании пытаемся получить информацию о счёте (без автосоздания)
    useEffect(() => {
        (async () => {
            setLoadingInfo(true);
            try {
                const info = await getAccountInfo(USER_ID);
                setAccountInfo(info);
            } catch {
                // Счёта нет – accountInfo остаётся null
            } finally {
                setLoadingInfo(false);
            }
        })();
    }, []);

    // 2. Обработчик «Создать счёт»
    const handleCreateAccount = async () => {
        setLoadingInfo(true);
        setErrorInfo(null);
        try {
            const info = await createAccount(USER_ID);
            setAccountInfo(info);
        } catch (err) {
            console.error(err);
            setErrorInfo("Не удалось создать счёт");
        } finally {
            setLoadingInfo(false);
        }
    };

    // 3. Обработчик формы пополнения
    const handleTopUpSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setErrorTopUp(null);

        const amountNum = Number(topUpAmount);
        if (isNaN(amountNum) || amountNum <= 0) {
            setErrorTopUp("Введите, пожалуйста, корректное положительное число");
            return;
        }

        setLoadingTopUp(true);
        try {
            const updatedInfo = await topUpAccount(USER_ID, amountNum);
            setAccountInfo(updatedInfo);
            setTopUpAmount("");
            setIsToppingUp(false);
            // После пополнения автоподтягиваем свежие транзакции
            if (showTransactions) {
                await loadTransactions();
            }
        } catch (err) {
            console.error(err);
            setErrorTopUp("Ошибка при попытке пополнить счёт");
        } finally {
            setLoadingTopUp(false);
        }
    };

    // 4. Загрузка транзакций (каждый раз запрашиваем)
    const loadTransactions = async () => {
        setErrorTransactions(null);
        setLoadingTransactions(true);
        try {
            const txns = await getTransactions(USER_ID);
            setTransactions(txns);
        } catch (err) {
            console.error(err);
            setErrorTransactions("Не удалось загрузить историю транзакций");
            setTransactions([]); // на случай ошибки очищаем список
        } finally {
            setLoadingTransactions(false);
        }
    };

    // 5. Переключатель показа истории – при каждом открытии загружаем снова
    const toggleTransactions = async () => {
        if (showTransactions) {
            setShowTransactions(false);
            return;
        }
        setShowTransactions(true);
        await loadTransactions();
    };

    return (
        <div className="max-w-xl mx-auto mt-10 bg-white rounded-lg shadow-md overflow-hidden">
            <div className="px-6 py-8 text-center">
                {loadingInfo ? (
                    <p className="text-lg text-gray-700">Загрузка…</p>
                ) : accountInfo ? (
                    /* ====== Счёт найден: показываем баланс + пополнение + историю ===== */
                    <>
                        <h2 className="text-lg text-gray-700 mb-2">Текущий баланс</h2>
                        <p className="text-3xl font-bold text-gray-900 mb-4">
                            ₽{accountInfo.balance.toFixed(2)}
                        </p>

                        {/* Кнопки «Пополнить» и «История транзакций» */}
                        <div className="flex justify-center space-x-4 mb-4">
                            <button
                                type="button"
                                onClick={() => {
                                    setIsToppingUp((prev) => !prev);
                                    setErrorTopUp(null);
                                }}
                                className={`px-6 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                                    loadingInfo ? "opacity-50 cursor-not-allowed" : ""
                                }`}
                            >
                                Пополнить
                            </button>

                            <button
                                type="button"
                                onClick={toggleTransactions}
                                className={`px-6 py-2 border border-blue-600 text-blue-600 text-sm font-medium rounded-lg hover:bg-blue-50 focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                                    loadingInfo ? "opacity-50 cursor-not-allowed" : ""
                                }`}
                            >
                                История транзакций
                            </button>
                        </div>

                        {/* Форма пополнения (если isToppingUp=true) */}
                        {isToppingUp && (
                            <div className="border-t border-gray-200 px-6 py-4 bg-gray-50">
                                <form onSubmit={handleTopUpSubmit} className="max-w-sm mx-auto">
                                    <label
                                        htmlFor="amount"
                                        className="block text-sm font-medium text-gray-700 mb-1"
                                    >
                                        Сумма пополнения, ₽
                                    </label>
                                    <input
                                        id="amount"
                                        type="number"
                                        step="0.01"
                                        min="1"
                                        value={topUpAmount}
                                        onChange={(e) => setTopUpAmount(e.target.value)}
                                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                                        placeholder="Введите сумму"
                                    />
                                    {errorTopUp && (
                                        <p className="mt-1 text-sm text-red-600">{errorTopUp}</p>
                                    )}
                                    <div className="mt-4 flex justify-end">
                                        <button
                                            type="submit"
                                            disabled={loadingTopUp}
                                            className={`px-4 py-2 bg-green-600 text-white text-sm font-medium rounded-lg hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 ${
                                                loadingTopUp ? "opacity-50 cursor-not-allowed" : ""
                                            }`}
                                        >
                                            {loadingTopUp ? "Пополнение…" : "Пополнить"}
                                        </button>
                                    </div>
                                </form>
                            </div>
                        )}

                        {/* История транзакций (если showTransactions=true) */}
                        {showTransactions && (
                            <div className="border-t border-gray-200 px-6 py-4 bg-gray-50">
                                <h3 className="text-lg font-medium text-gray-800 mb-3">
                                    История транзакций
                                </h3>

                                {loadingTransactions ? (
                                    <p className="text-gray-600">Загрузка…</p>
                                ) : errorTransactions ? (
                                    <p className="text-red-600">{errorTransactions}</p>
                                ) : transactions.length === 0 ? (
                                    <p className="text-gray-600">Нет операций за всё время.</p>
                                ) : (
                                    <div className="overflow-x-auto">
                                        <table className="min-w-full divide-y divide-gray-200 text-sm">
                                            <thead className="bg-gray-100">
                                            <tr>
                                                <th className="px-4 py-2 text-left font-medium text-gray-700">
                                                    ID
                                                </th>
                                                <th className="px-4 py-2 text-left font-medium text-gray-700">
                                                    Тип
                                                </th>
                                                <th className="px-4 py-2 text-right font-medium text-gray-700">
                                                    Сумма, ₽
                                                </th>
                                                <th className="px-4 py-2 text-left font-medium text-gray-700">
                                                    Дата и время
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody className="bg-white divide-y divide-gray-200">
                                            {transactions.map((txn) => (
                                                <tr key={txn.transactionId}>
                                                    <td className="px-4 py-2">{txn.transactionId}</td>
                                                    <td className="px-4 py-2">{txn.type}</td>
                                                    <td className="px-4 py-2 text-right">
                                                        {txn.amount.toFixed(2)}
                                                    </td>
                                                    <td className="px-4 py-2">
                                                        {/* Используем правильное поле `txn.date` */}
                                                        {new Date(txn.date).toLocaleString("ru-RU", {
                                                            day: "2-digit",
                                                            month: "2-digit",
                                                            year: "numeric",
                                                            hour: "2-digit",
                                                            minute: "2-digit",
                                                            second: "2-digit",
                                                        })}
                                                    </td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </table>
                                    </div>
                                )}
                            </div>
                        )}
                    </>
                ) : (
                    /* ====== Счёта нет: показываем заглушку + кнопку «Создать счёт» ===== */
                    <>
                        <h2 className="text-lg text-gray-700 mb-4">Счёт пока не создан</h2>
                        {errorInfo && <p className="text-red-600 mb-2">{errorInfo}</p>}
                        <button
                            onClick={handleCreateAccount}
                            className="px-6 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            Создать счёт
                        </button>
                    </>
                )}
            </div>
        </div>
    );
}
