"use client";

import { useState, useEffect, useRef } from "react";
import Link from "next/link";
import { routes } from "../../../constants/routeURL";
import {
  RiArrowDropDownLine,
  RiArrowDropUpLine,
  RiMenuLine,
} from "react-icons/ri";

interface SidebarItem {
  href?: string;
  label: string;
  subItems?: { href: string; label: string }[];
}

const sidebarItems: SidebarItem[] = [
  { href: routes.guide.root, label: "딸깍 가이드" },
  {
    label: "웹 가이드",
    subItems: [
      { href: routes.guide.frontinfo, label: "Frontend" },
      { href: routes.guide.backinfo, label: "Backend" },
      { href: routes.guide.dbinfo, label: "Database" },
    ],
  },
  {
    label: "앱 가이드",
    subItems: [{ href: routes.guide.desktopinfo, label: "Desktop" }],
  },
  { href: routes.guide.payment, label: "결제 가이드" },
  { href: routes.guide.license, label: "라이선스" },
];

export default function Sidebar() {
  const [openItems, setOpenItems] = useState<{ [key: string]: boolean }>({});
  const [isSmallScreen, setIsSmallScreen] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const sidebarRef = useRef<HTMLDivElement>(null);
  const buttonRef = useRef<HTMLButtonElement>(null);

  useEffect(() => {
    const handleResize = () => {
      setIsSmallScreen(window.innerWidth < 768);
    };

    const handleClickOutside = (event: MouseEvent) => {
      if (
        isSidebarOpen &&
        sidebarRef.current &&
        !sidebarRef.current.contains(event.target as Node) &&
        buttonRef.current &&
        !buttonRef.current.contains(event.target as Node)
      ) {
        setIsSidebarOpen(false);
      }
    };

    handleResize();
    window.addEventListener("resize", handleResize);
    document.addEventListener("mousedown", handleClickOutside);

    return () => {
      window.removeEventListener("resize", handleResize);
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isSidebarOpen]);

  const toggleItem = (label: string) => {
    setOpenItems((prev) => ({ ...prev, [label]: !prev[label] }));
  };

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  const renderSidebarContent = () => (
    <ul className="pb-6 space-y-4">
      {sidebarItems.map((item, index) => (
        <li key={index} className="py-2">
          {item.subItems ? (
            <div>
              <button
                onClick={() => toggleItem(item.label)}
                className="hover:text-blue-700 w-full text-left flex justify-between items-center transition-colors duration-300 text-lg font-semibold"
              >
                {item.label}
                <span>
                  {openItems[item.label] ? (
                    <RiArrowDropUpLine />
                  ) : (
                    <RiArrowDropDownLine />
                  )}
                </span>
              </button>
              {openItems[item.label] && (
                <ul className="pl-4 mt-2 space-y-2">
                  {item.subItems.map((subItem, subIndex) => (
                    <li key={subIndex}>
                      <Link
                        href={subItem.href}
                        className="hover:text-blue-700 block py-1 text-base font-normal"
                      >
                        {subItem.label}
                      </Link>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          ) : (
            <Link
              href={item.href!}
              className="hover:text-blue-700 block text-lg font-semibold"
            >
              {item.label}
            </Link>
          )}
        </li>
      ))}
    </ul>
  );
  return (
    <>
      {isSmallScreen && (
        <button
          ref={buttonRef}
          onClick={toggleSidebar}
          className="fixed top-16 left-10 z-50 bg-white p-2 rounded-md shadow-md"
        >
          <RiMenuLine size={24} />
        </button>
      )}
      <nav
        ref={sidebarRef}
        className={`${
          isSmallScreen
            ? "fixed top-24 left-4 w-72 transform transition-all shadow-md border-r duration-300 ease-in-out z-40"
            : "min-w-60 h-screen"
        } bg-white p-4 border-r ${
          isSmallScreen && !isSidebarOpen
            ? "opacity-0 invisible"
            : "opacity-100 visible"
        }`}
      >
        {renderSidebarContent()}
      </nav>
    </>
  );
}
