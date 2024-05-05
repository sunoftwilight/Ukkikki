import React from "react";
import upload from "@/assets/Header/AlbumEditOptions/upload.png";
import edit from "@/assets/Header/AlbumEditOptions/edit.png";
import trash from "@/assets/Header/AlbumEditOptions/trash.png";
import { albumEditStore } from "../../stores/HeaderStateStore";

const AlbumEditOptions: React.FC = () => {
  const optionStyle = "flex rounded-[10px] w-full h-[30px] items-center px-3 gap-3 font-pre-R text-black text-sm bg-white/70"
  const { setIsEdit } = albumEditStore()

  return (
    <div className="flex flex-col px-2 py-[10px] gap-[5px] fixed top-14 right-4 w-40 h-[120px] bg-zinc bg-opacity-30 rounded-xl shadow-inner backdrop-blur-[50px]">
      <div className={`${optionStyle}`} onClick={() => setIsEdit()}>
        <img src={upload} className="w-4" />
        업로드
      </div>

      <div className={`${optionStyle}`} onClick={() => setIsEdit()}>
        <img src={edit} className="w-4" />
        현재 폴더명 수정
      </div>

      <div className={`${optionStyle}`} onClick={() => setIsEdit()}>
        <img src={trash} className="w-4" />
        현재 폴더 삭제
      </div>
    </div>
  )
};

export default AlbumEditOptions;