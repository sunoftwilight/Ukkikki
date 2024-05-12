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

export interface PartyData {
  party: number;
  partyLink: string;
  partyName: string;
  sseKey: string;
}

export interface PartyListData{
  id: number;
  partyName: string;
  partyProfile: string;
}

// API
export interface CreateDoenResponesData{
  resultCode: string;
  data: PartyData;
}

export interface PartyListResponesData {
  resultCode: string;
  data: PartyListData[]
}

export interface PartyDetailResponse {
  data: {
    partyMembers : [],
    partyName : string,
    rootDirId : string,
    thumbnail : string
  };
  resultCode: string;
}
export interface PartyLinkRespone {
  data: {
    partyLink : string,
  };
  resultCode: string;
}