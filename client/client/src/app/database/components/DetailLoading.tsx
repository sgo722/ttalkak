import Skeleton from "react-loading-skeleton";
import "react-loading-skeleton/dist/skeleton.css";
import { IoClose } from "react-icons/io5";

export default function DetailLoading() {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-16 rounded-lg shadow-xl relative">
        <IoClose className="w-6 h-6 absolute top-6 right-6 cursor-pointer" />
        <div className="text-center mb-6">
          <Skeleton width={361} height={40} />
        </div>
        <Skeleton width={78} height={24} />
        <div className="mb-4">
          <table className="w-full">
            <tbody>
              {[...Array(4)].map((_, index) => (
                <tr key={index} className="border-b last:border-b-0">
                  <td className="py-3 w-1/3">
                    <Skeleton width={120} height={24} />
                  </td>
                  <td className="py-3">
                    <Skeleton width="100%" height={24} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="flex justify-center p-4 gap-4">
          <div className="text-center mt-8">
            <Skeleton width={155} height={48} />
          </div>
          <div className="text-center mt-8">
            <Skeleton width={155} height={48} />
          </div>
        </div>
      </div>
    </div>
  );
}
