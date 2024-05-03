import React from "react";
import { CommentItemProps } from "../../types/ArticleType";
import editBtn from "@/assets/ArticleDetail/edit.png";
import deleteBtn from "@/assets/ArticleDetail/delete.png";

const ReplyItem: React.FC<CommentItemProps> = ({ reply }) => {
  return (
    <div className="w-full py-2 px-5 flex gap-3 bg-white pl-16">
      <img src={reply.writerUrl} className="w-9 h-9 rounded-full" />
      <div className="flex flex-col gap-2">

        <div className="flex justify-between">
          <div className="flex gap-2">
            <div className="font-pre-SB text-black text-xs">{reply.writer}</div>
            <div className="font-pre-L text-point-gray text-[10px]">{reply.createdDate}</div>
          </div>

          <div className="flex gap-2">
            <img src={editBtn} className="w-4" />
            <img src={deleteBtn} className="w-3" />
          </div>
        </div>
        <div className="font-pre-L text-black text-sm">{reply.content}</div>
      </div>
    </div>
  )
};

export default ReplyItem;