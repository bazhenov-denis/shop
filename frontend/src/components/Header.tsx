import { Link, NavLink, useLocation } from "react-router-dom";

export default function Header({ openCart }: { openCart: () => void }) {
    const { pathname } = useLocation();

    const baseLinkClasses =
        "px-4 py-2 text-sm font-medium rounded-lg transition-colors";
    const activeClasses = "text-indigo-600";
    const inactiveClasses = "text-gray-600 hover:text-gray-800";

    return (
        <header className="bg-white shadow-md px-6 py-4 flex items-center justify-between">
            <Link to="/" className="text-2xl font-bold text-gray-800">
                ShopifyDemo
            </Link>

            <nav className="flex items-center space-x-6">
                <NavLink
                    to="/"
                    end
                    className={`${baseLinkClasses} ${
                        pathname === "/" ? activeClasses : inactiveClasses
                    }`}
                >
                    Товары
                </NavLink>

                <NavLink
                    to="/orders"
                    className={`${baseLinkClasses} ${
                        pathname.startsWith("/orders") ? activeClasses : inactiveClasses
                    }`}
                >
                    Заказы
                </NavLink>

                <NavLink
                    to="/account"
                    className={`${baseLinkClasses} ${
                        pathname === "/account" ? activeClasses : inactiveClasses
                    }`}
                >
                    Аккаунт
                </NavLink>

                <button
                    onClick={openCart}
                    className="relative inline-flex items-center px-4 py-2 bg-indigo-600 text-white text-sm font-medium rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                    🛒
                </button>
            </nav>
        </header>
    );
}
