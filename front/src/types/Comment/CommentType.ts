import { CommentInterface, CommentListInterface, ReplyInterface, TagInterface } from "./CommentInterface";

// comment type
export type CommentListType = CommentListInterface;
export type CommentType = CommentInterface[];
export type ReplyType = ReplyInterface[];

export type CommentInputType = {
  content : string;
  tagList: TagInterface[];
}