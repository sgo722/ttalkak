import React from "react";
import Skeleton from "react-loading-skeleton";
import "react-loading-skeleton/dist/skeleton.css";

export default function GitHubReposSkeleton() {
  return (
    <div className="container mx-auto font-sans">
      <Skeleton className="h-10 w-full mb-4" />
      <div className="flex bg-white rounded-lg border">
        <div
          className="w-1/3 border-r p-4"
          style={{ height: "calc(100vh - 400px)" }}
        >
          {[...Array(5)].map((_, i) => (
            <Skeleton key={i} className="h-14 mb-3" />
          ))}
        </div>
        <div className="w-2/3 p-4" style={{ height: "calc(100vh - 400px)" }}>
          <Skeleton className="h-10 w-1/2 mb-2" />
          {[...Array(6)].map((_, i) => (
            <Skeleton key={i} className="h-10 mb-2" />
          ))}
        </div>
      </div>
      <div className="text-right mt-12">
        <Skeleton style={{ width: "100px" }} className="h-9" />
      </div>
    </div>
  );
}
