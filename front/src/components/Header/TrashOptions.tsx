import React from "react";
import trash from "@/assets/Trash/trash.png"
import restore from "@/assets/Trash/restore.png"

const TrashOptions: React.FC = () => {
  const optionStyle = "flex rounded-[10px] w-full h-[30px] items-center px-3 gap-3 font-pre-R text-black text-sm bg-white/70"

  return (
    <div className="flex flex-col px-2 py-[10px] gap-[5px] fixed top-14 right-4 w-40 bg-zinc bg-opacity-30 rounded-xl shadow-inner backdrop-blur-[50px]">
      <div className={`${optionStyle}`} >
        <img src={trash} className="w-4" />
        완전 삭제
      </div>

      <div className={`${optionStyle}`} >
        <img src={restore} className="w-4" />
        복구
      </div>
    </div>
  )
};

export default TrashOptions;