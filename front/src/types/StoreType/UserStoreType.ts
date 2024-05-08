export interface UserStoreType {
  isLogin: boolean;
  accessToken: string;
  userId : string;
  userName : string;
  userProfile : string;
  setIsLogin: (newBool: boolean) => void;
  setAccessToken : (newToken: string) => void;
  setUserId : (newId: string) => void;
  setUserName : (newName: string) => void;
  setUserProfile : (newUrl: string) => void;
}