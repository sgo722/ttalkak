"use client";

import { Suspense } from "react";
import ProjectContent from "@/app/projects/[id]/components/ProjectContent";
import SkeletonUI from "@/app/projects/[id]/components/SkeletonUI";

export default function ProjectPage({ params }: { params: { id: string } }) {
  return (
    <Suspense fallback={<SkeletonUI />}>
      <ProjectContent id={params.id} />
    </Suspense>
  );
}
