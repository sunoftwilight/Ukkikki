import React from "react";
import edit from "@/assets/ArticleDetail/edit.png"
import deleteIcon from "@/assets/ArticleDetail/delete.png"

interface MemoItemProps {
  info : {
    profileUrl: string;
    name: string;
    date: string;
    content: string;
  }
}

const MemoItem: React.FC<MemoItemProps> = ({ info }) => {
  const isMine = true

  return (
    <div className="w-full p-2 flex bg-white/70 rounded-[15px] gap-2">
      <img src={info.profileUrl} className="w-[50px] h-[50px] rounded-full" />

      <div className="w-full flex flex-col gap-2">
        <div className="flex justify-between">
          <div className="flex gap-2">
            <div className="font-pre-SB text-black text-xs">{info.name}</div>
            <div className="font-pre-L text-point-gray text-[10px]">{info.date}</div>
          </div>

          {isMine ? 
            <div className="gap-1 flex">
              <img src={edit} className="w-[14px]" />
              <img src={deleteIcon} className="w-[12px]" />
            </div>
            : <></>
          }
        </div>

        <div className="font-pre-L text-black text-sm">{info.content}</div>
      </div>
    </div>
  )
}; 

export default MemoItem;