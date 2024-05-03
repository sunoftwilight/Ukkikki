import React from "react";
import { CommentItemProps, CommentItemType } from "../../types/ArticleType";
import ReplyItem from "./ReplyItem";
import editBtn from "@/assets/ArticleDetail/edit.png";
import deleteBtn from "@/assets/ArticleDetail/delete.png";

const dummyReply: CommentItemType[] = [
  {
    writer: '나는이해진일수도있음',
    writerUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwgm8I4SdpSyov1KHjMWyRR3O5NknbVYg5ENoffjW5_wEL3W9tegVjGe3maDQgL4QI4YA&usqp=CAU',
    createdDate: '23.04.17',
    content: '피카츄라이츄파이리꼬부기버터풀야도란피존트또가스피카츄라이츄파이리꼬부기버터풀야도란피존트또가스피카츄라이츄파이리꼬부기버터풀야도란피존트또가스',
  },
  {
    writer: '나는이해진아닐수도있음',
    writerUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTzW-ft0iIYh-ZytBnYyknKlUANQ0NFTwo1XltVXyhKw&s',
    createdDate: '23.04.17',
    content: '서로생긴모습은달라도서로생긴모습은달라도서로생긴모습은달라도서로생긴모습은달라도서로생긴모습은달라도',
  },
]

const CommentItem: React.FC<CommentItemProps> = ({ reply }) => {
  return (
    <div className="flex flex-col">
      <div className="w-full py-2 px-5 flex gap-3 bg-white">
        <img src={reply.writerUrl} className="w-9 h-9 rounded-full" />
        <div className="flex flex-col gap-2 w-full">
          
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

      <div>
        { dummyReply.map((item, idx) => (
          <ReplyItem key={idx} reply={item} />
        ))}
      </div>
    </div>
  )
};

export default CommentItem;