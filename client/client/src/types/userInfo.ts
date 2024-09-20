export interface UserInfo {
  userId: number;
  username: string;
  email: string;
  accessToken: string | null;
  emailVerified: boolean;
  profileImage: string;
}
