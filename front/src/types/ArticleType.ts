export interface CommentItemType {
  writer: string;
  writerUrl: string;
  createdDate: string;
  content: string;
}

export interface CommentItemProps {
  reply: CommentItemType;
}