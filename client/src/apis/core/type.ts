export interface ApiResponse<T> {
  success: boolean;
  message: string | null;
  status: number;
  data: T;
}
