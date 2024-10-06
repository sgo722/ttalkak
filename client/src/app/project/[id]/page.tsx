import ProjectContent from "@/app/project/[id]/components/ProjectContent";

export default function ProjectPage({ params }: { params: { id: string } }) {
  return <ProjectContent id={params.id} />;
}
