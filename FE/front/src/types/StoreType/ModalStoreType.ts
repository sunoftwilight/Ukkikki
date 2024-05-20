import { MemberData } from "../GroupType";

export interface InviteStoreType {
  inviteOpen : boolean;
  inviteCode : string;
  setInviteOpen: () => void;
  setInviteCode: (code:string) => void;
}

export interface MemberStoreType {
  memberOpen : boolean;
  members: MemberData[]
  setMemberOpen: () => void;
  setMembers: (data: MemberData[]) => void;
}

export interface FolderStoreType {
  folderOpen: boolean;
  folderMode: string;
  setFolderOpen: (mode: string) => void;
}