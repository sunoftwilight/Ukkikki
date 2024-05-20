import React, { useState } from "react";
import check from "@/assets/Album/check.png"
import { useStore } from "zustand";
import SecureImg from "../Album/SecureImg";
import { TrashItemType } from "../../types/TrashType";
import folder from "@/assets/Album/folder.png"
import { selectTrashStore } from "../../stores/TrashStore";

interface SelectModeImgProps {
  item : TrashItemType
}

const TrashSelectModeImg: React.FC<SelectModeImgProps> = ({ item }) => {
  const [isSelect, setIsSelect] = useState(false)
  const { setSelectTrash } = useStore(selectTrashStore)

  const clickHandler = (pk: string) => {
    setSelectTrash(pk, isSelect)
    setIsSelect(!isSelect)
  }

  return (
    <div onClick={() => clickHandler(item.pk)} className="flex justify-center items-center">
      <div className="w-[106px] h-[90px] relative">
        { isSelect ?
          <div className="w-[18px] h-[18px] rounded-full absolute top-1 right-1 bg-main-blue flex justify-center items-center">
            <img src={check} className="w-3" />
          </div>
          :
          <div className="w-[18px] h-[18px] rounded-full absolute top-1 right-1 bg-soft-gray border-[0.3px] border-disabled-gray " />
        }
        { item.type === 'DIRECTORY' ? 
          <div className="flex flex-col justify-center items-center gap-1">
            <img src={folder} className="w-[82px] h-[65px]" />
            <div className="font-pre-R text-center text-xs">{item.name}</div>
          </div>
        :
          <SecureImg url={item.url} />
        }
      </div>
    </div>
  )
};

export default TrashSelectModeImg;