export interface UserStoreType {
  accessToken: string;
  userId : string;
  userName : string;
  userProfile : string;
  setAccessToken : (newData: string) => void;
  setUserId : (newData: string) => void;
  setUserName : (newData: string) => void;
  setUserProfile : (newData: string) => void;
}