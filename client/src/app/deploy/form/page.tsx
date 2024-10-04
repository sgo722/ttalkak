import { Suspense } from "react";
import { notFound } from "next/navigation";
import DeploymentForm from "@/app/deploy/form/components/DeploymentForm";
import SkeletonUI from "@/app/deploy/form/components/SkeletonUI";

export default function deployFormPage({
  searchParams,
}: {
  searchParams: { [key: string]: string | string[] | undefined };
}) {
  const type = searchParams.type as string | undefined;

  if (!type || (type !== "BACKEND" && type !== "FRONTEND")) {
    notFound();
  }

  return (
    <div>
      <Suspense fallback={<SkeletonUI />}>
        <DeploymentForm />
      </Suspense>
    </div>
  );
}
