import Skeleton from "react-loading-skeleton";
import "react-loading-skeleton/dist/skeleton.css";

export default function SkeletonUI() {
  return (
    <>
      <div className="w-full space-y-6 p-6 bg-white rounded-md border mx-auto">
        {/* 프론트엔드 프레임워크 선택 */}
        <div>
          <Skeleton style={{ width: "160px" }} className="h-6" />
          <Skeleton className="h-10 w-full" />
        </div>

        {/* 포트번호 */}
        <div>
          <Skeleton style={{ width: "160px" }} className="h-6" />

          <Skeleton className="h-10 w-full" />
        </div>

        {/* 루트 디렉토리 */}
        <div>
          <Skeleton style={{ width: "160px" }} className="h-6" />
          <Skeleton className="h-10 w-full" />
        </div>
        {/* 환경 변수 */}
        <div>
          <Skeleton style={{ width: "160px" }} className="h-6" />
          <Skeleton style={{ width: "160px" }} className="h-6" />
        </div>
      </div>
      {/* 다음 버튼 */}
      <div className="flex justify-end mt-6">
        <Skeleton style={{ width: "64px" }} className="h-9" />
      </div>
    </>
  );
}
