import React from "react";
import editBtn from "@/assets/ArticleDetail/edit.png";
import deleteBtn from "@/assets/ArticleDetail/delete.png";
import { useNavigate } from "react-router-dom";

const dummy = {
  writer: '나는 이해진이다',
  writerImg: 'https://pds.joins.com/service/ssully/pd/2022/04/04/2022040415205694294.jpg',
  title: '미안한데 내 동년배들 요즘 다 우끼끼로 사진 공유한다',
  content: '내 동년배 중에 사진 공유 카톡으로 하는 놈들 아무도 없다 요즘 카톡으로 사진공유하면 같이 못논다 누가 짜치게 30장씩 끊어치기하냐',
  createdDate: '2018-06-17T22:46:17.348',
  isModify: true,
  isMine: true,
  imgList: [
    { pk: 1, url: 'https://i.namu.wiki/i/6nJq7Dza9kRKQbvw-EBUHqArvnLuKWGqeaWTT5odfIp1mJnrJNuLRe5hmxC3eXQtB0_1sQiknnDOpT0-kz1baA.webp' },
    { pk: 2, url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeIaJPlIwTx-zhhvIq-Ymptj3fstzg8tntZ6om8hmybj-Z4tKndKK2u3JgcAHKhY_cWFQ&usqp=CAU' },
    { pk: 3, url: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbs36JG0hg3CcHcfJUbbBZc23aqqRePcmf8c8XECCWdr2XkQdEo10Q3p_FwcsVU3IxGsQ&usqp=CAU' },
    { pk: 4, url: 'https://mblogthumb-phinf.pstatic.net/MjAyMTExMTlfMTkx/MDAxNjM3MjU1MjMwNTAw.VZU97O6P_cpm2FY7IeZQsaoapq11qYMQggY0EDjSeY0g.YT-b7uqVrMJjHSIyfOtM1OmEiZlTiB8ZCgbr-dVg4vIg.JPEG.letyourselfglow/IMG_3017.jpg?type=w800' },
  ],
}

interface navigateProps {
  pk: number;
  url: string
}

const Content: React.FC = () => {
  const navigate = useNavigate()

  const goImgDetailHandler = (imgInfo: navigateProps) => {
    navigate(`/feed/img/${imgInfo.pk}`, { state: { imgUrl: imgInfo.url } })
  }

  return (
    <div className="flex flex-col w-full bg-white gap-1">
      {/* 작성자 프로필 */}
      <div className="w-full h-16 px-5 py-2 flex gap-[14px]">
        <img src={dummy.writerImg} className="w-12 h-12 rounded-full" />

        <div className="w-full flex flex-col gap-1">
          <div className="w-full flex justify-between items-center">
            <div className="font-pre-SB text-black text-base">{dummy.writer}</div>
            <div className="flex gap-2">
              <img src={editBtn} className="w-[20px]" />
              <img src={deleteBtn} className="w-[16px]" />
            </div>
          </div>

          <div className="flex gap-2 font-pre-R text-point-gray text-sm">
            <div>{dummy.createdDate.split('T')[0]}</div>
            <div>(수정됨)</div>
          </div>
        </div>
      </div>

      {/* 본문 내용 */}
      <div className="flex flex-col p-3 bg-soft-gray gap-4 mx-4 rounded-xl">
        <div className="flex flex-col gap-3">
          <div className="font-pre-SB text-black text-lg">{dummy.title}</div>
          <div className="font-pre-R text-black text-sm">{dummy.content}</div>
        </div>

        <div className="flex gap-[6px] overflow-x-scroll scrollbar-hide">
          { dummy.imgList.map((item, idx) => (
            <img key={idx} src={item.url} onClick={() => goImgDetailHandler(item)} className="w-full h-60 rounded-xl object-cover" />
          ))}
        </div>
      </div>
    </div>
  )
};

export default Content;