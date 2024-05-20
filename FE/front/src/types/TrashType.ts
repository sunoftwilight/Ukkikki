// Props Type


// API type

export interface TrashItemType {
  deadLine: string;
  name: string;
  pk: string;
  type: string;
  url: string;
}

export interface GetTrashResponseType {
  resultCode: string;
  data: TrashItemType[];
}

export interface trashInfoType {
  sseKey: string;
  trashIdList: string[];
}