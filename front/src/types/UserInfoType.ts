import { GroupKey } from "./Group";

export interface UserResponeseData {
    resultCode: string;
    data: UserInfo;
  }
  
export interface UserInfo {
    userId: string;
    userName: string;
    profileUrl: string;
    uploadGroupId: number|null;
    insertPass: boolean;
}

export interface GroupKeyResponeseData{
  resultCode: string;
  data: GroupKey[]
}