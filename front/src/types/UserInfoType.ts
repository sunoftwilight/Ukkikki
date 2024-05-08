
export interface UserResponeseData {
    resultCode: string;
    data: UserInfo;
  }
  
export interface UserInfo {
    userId: string;
    userName: string;
    profileUrl: string;
}