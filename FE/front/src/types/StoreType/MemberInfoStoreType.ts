import { UserGrantData } from "../GroupType";

export interface MemberInfoStoreType {
  data: UserGrantData | null;
  setData: (newData: UserGrantData|null) => void;
}