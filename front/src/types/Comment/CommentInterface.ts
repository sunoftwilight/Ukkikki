import { CommentListType, CommentType, ReplyType } from "./CommentType";

export interface TagInterface{
    userId : number;
    userName : string;
  }

export interface CommentItemInterface {
    idx : number;
    comment : CommentInterface;
    getCommentList : () => void;
  }

// id
export interface CommentListInterface {
    id : number;
    comment : CommentType;
  }
  
  // 댓글
  export interface CommentInterface{
    userId : number;
    content : string;
    userName : string;
    profileUrl : string;
    createdDate : string;
    isDelete : boolean;
    reply : ReplyType;
    tag : TagInterface[]
  }
  
  // 대댓글
  export interface ReplyInterface{
    userId : number;
    content : string;
    userName : string;
    profileUrl : string;
    createdDate : string;
    isDelete : boolean;
    tag : TagInterface[]
  }
  
  // api
  export interface CommentResponseData {
    data: CommentListType;
    status: number;
    statusText: string;
    headers: Record<string, string>;
    config: string;
  }

  // CommentTop
  export interface CommentTopInterface {
    comment : CommentInterface;
    isModify : boolean;
    commentDelete : () => void;
    switchIsModify : () => void;
    createReply : () => void;
  }
  
  // CommentBottom
  export interface CommentBottomInterface {
    idx : number;
    comment : CommentInterface;
    isModify : boolean;
    commentModify : (inputValue : string, tagList : TagInterface[]) => void;
    switchIsModify : () => void;
  }
  
  // CommentInput
  export interface CommentInputInterface {
    userId : number;
    content : string;
    tag : TagInterface[];
    commentModify : (inputValue : string, tagList : TagInterface[]) => void;
    switchIsModify : () => void;
  }

  // replyItem
  export interface ReplyItemInterface {
    commentIdx : number;
    replyIdx : number;
    reply : ReplyInterface;
    createReplyCancel : () => void;
    getCommentList : () => void;
  }

  // ReplyTop
  export interface ReplyTopInterface {
    reply : ReplyInterface;
    isModify : boolean;
    commentDelete : () => void;
    switchIsModify : () => void;
  }

  // ReplyBottom
  export interface ReplyBottomInterface {
    idx : number;
    reply : ReplyInterface;
    isModify : boolean;
    replyModify : (inputValue : string, tagList : TagInterface[]) => void;
    switchIsModify : () => void;
  }