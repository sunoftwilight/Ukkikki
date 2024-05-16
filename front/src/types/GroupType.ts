// Props Type
export interface CreatePartyData {
  partyName: string;
  partyPass: string;
  simplePass: string;
  partyProfile: File;
}

export interface InsertInfoProps {
  onNextBtnClick: (type:string, data: CreatePartyData) => void;
  createData: CreatePartyData;
}

export interface InsertPasswordProps {
  onBackBtnClick: (type:string, data: CreatePartyData) => void;
  onNextBtnClick: (type:string, data: CreateDoneProps) => void;
  createData: CreatePartyData;
  doneData: CreateDoneProps;
}

export interface CreateDoneProps {
  partyPk: number | null;
  partyName: string;
  inviteCode: string;
}

export interface GroupKey {
  partyId: number;
  sseKey: string;
}

export interface PartyCreateData {
  party: number;
  partyLink: string;
  partyName: string;
  sseKey: string;
}

export interface PartyListData{
  id: number;
  partyName: string;
  partyProfile: string;
  expired: boolean;
}

export interface MemberData {
  type:string;
  nickname: string;
  profileUrl: string;
  memberId: number;
  partyId: number;
}

export interface PartyDetailData {
  partyMembers : MemberData[],
  partyName : string,
  rootDirId : string,
  thumbnail : string
}

export interface UserGrantData {
  partyId: number;
  userId: number;
  userName: string;
  profileUrl: string;
  memberRole: string;
}

// API
export interface CreateDoenResponesData{
  resultCode: string;
  data: PartyCreateData;
}

export interface PartyListResponesData {
  resultCode: string;
  data: PartyListData[]
}

export interface PartyDetailResponse {
  resultCode: string;
  data: PartyDetailData;
}

export interface PartyLinkResponse {
  resultCode: string;
  data: {
    partyLink : string,
  };
}

export interface PartyCheckPassRespones{
  resultCode: string;
  data: GroupKey;
}

export interface EnterGuestRespones {
  resultCode: string;
  data: {
    partyId: number;
    token: string;
  }
}

export interface PartyUserListRespones {
  resultCode: string;
  data: UserGrantData[]
}

export interface PartyPassChangeResponse {
  resultCode: string;
  data: GroupKey;
}
