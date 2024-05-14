// Props Type

export interface DetailImgProps {
  id: number;
}

// API Type
export interface DownloadDataType {
  size: number;
  type: string;
}

// memo List
export type MemoListType = MemoListInterface[];

export interface MemoListInterface {
  memoId : number;
  content : string;
  userId : string;
  username : string;
  date : string;
}

export interface MemoResponseData {
  data: MemoListType;
  status: number;
  statusText: string;
  headers: Record<string, string>;
  config: string;
}