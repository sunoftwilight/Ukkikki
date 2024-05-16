// Props Type

export interface AlarmRequestType {
  pageNo: number;
  pageSize: number;
}

export interface AlarmItemProps {
  alarmItem : AlarmItemType
}


// API type

export interface AlarmItemType {
  alarmType: string;
  content: string;
  contentsId: number;
  createDate?: string;
  identifier: string[];
  partyId: number;
  partyName: string;
  partyUrl: string;
  read: boolean;
  targetId: number;
  writerNick: string;
  alarmId: string;
}

export interface AlarmResponseType {
  data: {
    alarmList: AlarmItemType[],
    last: boolean;
    pageNo: number;
    pageSize: number;
  };
  resultCode: string;
}

export interface redirectDtoType {
  alarmId: string;
  redirectUrl: string;
}