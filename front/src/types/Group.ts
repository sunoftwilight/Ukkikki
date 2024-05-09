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

// API type
