// Props Type

export interface CommentItemType {
  writer: string;
  writerUrl: string;
  createdDate: string;
  content: string;
}

export interface CommentItemProps {
  reply: CommentItemType;
}

export interface ArticleProps {
  id : number;
  title: string;
  writer: string;
  writerUrl: string;
  content: string;
  createDate: string;
  modify: boolean;
  partyId: number;
  photoList: ArticlePhotoProps[];
}

export interface ArticlePhotoProps {
  id: number;
  photoId: number;
  fileId: string;
  photoUrl: string;
}

export interface ArticleCreateProps{
  title: string;
  content: string;
  password: string;
  photoIdList: string[];
}

// API type

export interface ArticleListRespones {
  articleDtoList: ArticleProps[]
  page: number;
  size: number;
  next: boolean;
}

export interface ArticleDetailRespones {
  resultCode: string;
  data: ArticleProps;
}