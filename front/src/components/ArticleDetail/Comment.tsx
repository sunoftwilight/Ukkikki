import React, { useEffect } from "react";
import CommentItem from "./CommentItem"
import { CommentListType } from "../../types/Comment/CommentType";


interface CommentProps {
  getCommentList : () => void;

  commentList? : CommentListType
}

const Comment: React.FC<CommentProps> = ({getCommentList, commentList}) => {

  useEffect(() => {
    if(commentList === undefined){
      getCommentList();
    }
  },[])

  return (
    <div className="w-full h-full flex flex-col gap-2">
      <div className="font-pre-SB text-black text-lg w-full h-9 py-2 px-4">댓글</div>
      
      <div className="flex flex-col gap-4">
        { commentList?.comment.map((item, idx) => (
          <CommentItem idx={idx} comment={item} getCommentList={getCommentList} />
        ))}
      </div>
    </div>
  )
};

export default Comment;