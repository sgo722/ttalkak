// GitHub 레포지토리 정보를 위한 인터페이스
export interface Repository {
  id: number;
  name: string;
  full_name: string;
  html_url: string;
  updated_at: string;
  description: string;
  owner: {
    login: string;
    id: number;
    avatar_url: string;
  };
  default_branch: string;
}

// 파일 또는 디렉토리 정보를 위한 인터페이스
export interface FileContent {
  name: string;
  path: string;
  type: string;
  content?: string;
  download_url?: string;
  sha: string;
}

// 커밋 정보를 위한 인터페이스
export interface Commit {
  sha: string;
  commit: {
    message: string;
    author: {
      name: string;
      email: string;
      date: string;
    };
  };
  author: {
    login: string;
    avatar_url: string;
  } | null;
}

// 배포 유형을 위한 타입
export type DeployType = "FRONTEND" | "BACKEND" | null;
