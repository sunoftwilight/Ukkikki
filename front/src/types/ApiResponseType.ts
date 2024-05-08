export interface ResponseData {
  data: string;
  status: number;
  statusText: string;
  headers: Record<string, string>;
  config: string;
}

export interface UserInfoData {
  userId:string;
  userName:string;
  userProfile:string;
}