import Skeleton from "react-loading-skeleton";
import "react-loading-skeleton/dist/skeleton.css";

export default function ProjectListLoading() {
  return (
    <>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {[...Array(3)].map((_, index) => (
          <div key={index} className="border rounded-lg p-5">
            <div className="flex items-start mb-4">
              <Skeleton circle width={40} height={40} className="mr-3" />
              <div className="flex-grow">
                <Skeleton width="70%" height={20} className="mb-2" />
                <Skeleton width="50%" height={16} />
              </div>
            </div>
            <div className="flex justify-end items-center mt-4">
              <Skeleton width={80} height={16} />
            </div>
          </div>
        ))}
      </div>

      <div className="flex justify-center mt-8">
        <nav className="inline-flex rounded-md gap-2">
          <Skeleton width={60} height={38} className="rounded-md" />
          <Skeleton width={38} height={38} className="rounded-md" />
          <Skeleton width={60} height={38} className="rounded-md" />
        </nav>
      </div>
    </>
  );
}
