export interface UserStoreType {
  accessToken: string;
  userId : string;
  userName : string;
  userProfile : string;
  setAccessToken : (newToken: string) => void;
  setUserId : (newId: string) => void;
  setUserName : (newName: string) => void;
  setUserProfile : (newUrl: string) => void;
}