import React from "react";
import download from "@/assets/Header/AlbumSelectOptions/download.png";
import move from "@/assets/Header/AlbumSelectOptions/move.png";
import copy from "@/assets/Header/AlbumSelectOptions/copy.png";

import trash from "@/assets/Header/AlbumEditOptions/trash.png";
import edit from "@/assets/Header/AlbumEditOptions/edit.png";

const AlbumSelectOptions: React.FC = () => {
  const optionStyle = "flex rounded-[10px] w-full h-[30px] items-center px-3 gap-3 font-pre-R text-black text-sm bg-white/70"
  return (
    <div className="flex flex-col px-2 py-[10px] gap-[5px] fixed top-14 right-4 w-40 h-[186px] bg-zinc bg-opacity-30 rounded-xl shadow-inner backdrop-blur-[50px]">
      <div className={`${optionStyle}`}>
        <img src={download} className="w-4" />
        업로드
      </div>

      <div className={`${optionStyle}`}>
        <img src={move} className="w-4" />
        이동
      </div>

      <div className={`${optionStyle}`}>
        <img src={copy} className="w-4" />
        복제
      </div>

      <div className={`${optionStyle}`}>
        <img src={trash} className="w-4" />
        삭제
      </div>

      <div className={`${optionStyle}`}>
        <img src={edit} className="w-4" />
        게시글 작성
      </div>
    </div>
  )
};

export default AlbumSelectOptions;