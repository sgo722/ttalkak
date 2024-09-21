"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

type NavLinkProps = {
  href: string;
  children: React.ReactNode;
  className?: string;
};

export default function NavLink({
  href,
  children,
  className = "",
}: NavLinkProps) {
  const pathname = usePathname();
  const isActive = pathname === href;

  return (
    <Link
      href={href}
      className={`
      px-3 py-2 text-sm font-semibold
      ${
        isActive
          ? "text-gray-900 font-semibold bg-gray-200 lg:bg-transparent lg:border-b-2 lg:border-black"
          : "text-gray-400 hover:text-gray-900 hover:bg-gray-200 lg:rounded-full"
      }
      ${className}
    `}
    >
      {children}
    </Link>
  );
}
