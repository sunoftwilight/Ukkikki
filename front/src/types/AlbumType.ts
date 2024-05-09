// Props Type

export interface DetailImgProps {
  url: string
}


// API type

export interface FileDownloadDto {
  fileId: number;
  prefix: string;
}

export interface MultiFileDownloadDto {
  fileIdList: number[];
  prefix: string;
}
