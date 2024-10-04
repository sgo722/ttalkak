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
  const insActiveStyle = "pt-5 pb-4 text-black border-b-2 border-black";

  return (
    <Link
      href={href}
      className={`
        text-sm font-medium
        ${isActive ? `${insActiveStyle}` : "py-5"}
        hover:text-[#7a7a7a] hover:border-b-2 hover:border-[#e0e0e0] hover:pt-5 hover:pb-4
        ${className}
      `}
    >
      {children}
    </Link>
  );
}
