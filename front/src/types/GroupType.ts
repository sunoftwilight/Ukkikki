// Props Type

export interface CreateData {
    
}


// API type

export interface PartyDetailResponse {
  data: {
    partyMembers : [],
    partyName : string,
    rootDirId : string,
    thumbnail : string
  };
  resultCode: string;
}