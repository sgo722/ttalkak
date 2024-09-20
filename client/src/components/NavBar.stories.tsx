import type { Meta, StoryObj } from "@storybook/react";
import NavBar from "./NavBar";

const meta: Meta<typeof NavBar> = {
  title: "Components/NavBar",
  component: NavBar,
  parameters: {
    layout: "fullscreen",
  },
};

export default meta;
type Story = StoryObj<typeof NavBar>;

export const Default: Story = {};

export const Mobile: Story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile1",
    },
  },
};
