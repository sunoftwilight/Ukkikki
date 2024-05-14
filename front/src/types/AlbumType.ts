// Props Type



// API type

export interface FileDownloadDto {
  fileId: number;
  prefix: string;
}

export interface MultiFileDownloadDto {
  fileIdList: number[];
  prefix: string;
}

export interface contentListData {
  type: string;
  pk: string;
  name: string;
  url: string;
  photoId: number;
  isDownload: boolean;
  isLikes: boolean;
}

export interface AlbumData {
  parentId: string;
  contentList: contentListData[]
}

export interface AlbumResponse {
  resultCode: string;
  data: AlbumData
}

export interface DirInfoType {
  parentDirId: string;
  dirName: string;
}

export interface EditDirInfoType {
  dirId: string;
  newName: string;
}

export interface getDetailImgDataType {
  isDownload : boolean;
  isLikes: false;
  url: string;
}

export interface getDetailImgType {
  data: getDetailImgDataType;
  resultCode: string;
}

export interface thumbNailItemType {
  photoId: number;
  pk: string;
  thumbUrl2: string;
}

export interface thumbNailResponseType {
  data: thumbNailItemType[];
  resultCode: string;
}

export interface delFilesDtoType {
  data : {
    sseKey: string;
    fileIdList: string[];
  }
}

export interface sseKeyDtoType {
  data: {
    sseKey: string;
  }
}