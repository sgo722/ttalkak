export interface UserInfo {
  userId: number;
  username: string;
  email: string;
  accessToken: string | null;
  emailVerified: boolean;
  profileImage: string;
}

export interface UserEmailForm {
  userId: number;
  email: string;
  code: string;
}

export interface Validation {
  hasKey: boolean;
  hasContract: boolean;
  hasAdmin: boolean;
}
