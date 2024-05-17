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
  msgList: ChatItemType[]
}

export interface pageableType {
  page: number;
  size: number;
}

export interface getMsgResponseType {
  size: number;
  pageable: number;
  next: boolean;
  simpleChatDtos: ChatItemType[]
}