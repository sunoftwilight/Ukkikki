// Props Type



// API type

export interface FileDownloadDto {
  fileId: number;
  prefix: string;
}

export interface MultiFileDownloadDto {
  fileIdList: string[];
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