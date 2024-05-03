import React from "react";
import CommentItem from "./CommentItem";
import { CommentItemType } from "../../types/ArticleType";

const dummyComment: CommentItemType[] = [
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
  {
    writer: '나는이해진같음',
    writerUrl: 'https://www.econovill.com/news/photo/201603/285365_95988_038.png',
    createdDate: '23.04.17',
    content: '우리는모두친구 마자~용!',
  },
  {
    writer: '나는이해진인지모르겟음',
    writerUrl: 'https://img1.daumcdn.net/thumb/R1280x0.fjpg/?fname=http://t1.daumcdn.net/brunch/service/user/cnoC/image/kQMuuagu-nSEu5MvmcSPrOI0nAk',
    createdDate: '23.04.17',
    content: '내가원하는걸너도원하고마주잡은두손에',
  },
  {
    writer: '나는이해진이다',
    writerUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSTMxhHU82HQ01LSIdTM04UAVdggtLGf0IcV8P0y1f87A&s',
    createdDate: '23.04.17',
    content: '힘을내봐 힘을내봐 그래 용기를내봐',
  },
]

const Comment: React.FC = () => {
  return (
    <div className="w-full h-full flex flex-col gap-2">
      <div className="font-pre-SB text-black text-lg w-full h-9 py-2 px-4">댓글</div>

      <div className="flex flex-col gap-4">
        { dummyComment.map((item, idx) => (
          <CommentItem key={idx} reply={item} />
        ))}
      </div>
    </div>
  )
};

export default Comment;