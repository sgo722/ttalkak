import Sidebar from "./components/sidebar";
import NavSpacer from "./components/NavSpacer";

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div className="w-full fixed top-0 left-0 ">
      <NavSpacer />
      <div className="w-full flex items-center justify-center">
        <Sidebar />
        <main className="w-full overflow-y-auto custom-scrollbar bg-white">
          {children}
        </main>
      </div>
    </div>
  );
}
