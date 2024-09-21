import { Meta, StoryObj } from "@storybook/react";
import NavLink from "./NavLink";
import { usePathname } from "next/navigation";

const meta: Meta<typeof NavLink> = {
  title: "Components/NavLink",
  component: NavLink,
  argTypes: {
    href: { control: "text" },
    children: { control: "text" },
    className: { control: "text" },
  },
  decorators: [
    (Story) => (
      <div style={{ padding: "1rem", background: "#f0f0f0" }}>
        <Story />
      </div>
    ),
  ],
};

export default meta;

type Story = StoryObj<typeof NavLink>;

const NavLinkWrapper = (
  args: React.ComponentProps<typeof NavLink> & { mockPathname: string }
) => {
  const { mockPathname, ...props } = args;
  // @ts-ignore
  usePathname.mockReturnValue(mockPathname);
  return <NavLink {...props} />;
};

export const Active: Story = {
  render: (args) => <NavLinkWrapper {...args} mockPathname="/home" />,
  args: {
    href: "/home",
    children: "Home",
  },
};

export const Inactive: Story = {
  render: (args) => <NavLinkWrapper {...args} mockPathname="/about" />,
  args: {
    href: "/home",
    children: "Home",
  },
};

export const CustomClass: Story = {
  render: (args) => <NavLinkWrapper {...args} mockPathname="/about" />,
  args: {
    href: "/contact",
    children: "Contact",
    className: "custom-class",
  },
};
