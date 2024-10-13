"use client";

interface ButtonProps {
  primary?: boolean;
  size?: "small" | "medium" | "large";
  label: string;
  onClick?: () => void;
  type?: "button" | "submit" | "reset";
  disabled?: boolean;
}

export default function Button({
  primary = false,
  size = "medium",
  label,
  onClick,
  type = "button",
  disabled,
}: ButtonProps) {
  const baseStyles = "font-bold rounded-md";
  const sizeStyled = {
    small: "px-3 py-1 text-sm",
    medium: "px-3 py-2",
    large: "px-16 py-3",
  };
  const colorStyles = primary
    ? "bg-black text-white"
    : "bg-white text-black border border-gray-400";

  const disabledStyles = disabled
    ? "opacity-50 cursor-not-allowed"
    : "hover:opacity-80";
    
  return (
    <button
      type={type}
      className={`${baseStyles} ${sizeStyled[size]} ${colorStyles} ${disabledStyles}`}
      onClick={onClick}
      disabled={disabled}
    >
      {label}
    </button>
  );
}
