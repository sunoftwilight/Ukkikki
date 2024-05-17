export interface ChatItemType {
  memberName: string;
  content: string;
  createDate: string;
  profileType: string;
  profileUrl: string;
  readNum: number;
  chatType: string
}

export interface ChattingRoomProps {
  list: ChatItemType[]
}

export interface pageableType {
  page: number;
  size: number;
}