import React, { useState } from "react";
import check from "@/assets/Album/check.png"
import { useStore } from "zustand";
import { selectStore } from "../../stores/AlbumStore";
import { contentListData } from "../../types/AlbumType";
import SecureImg from "./SecureImg";
import heart from "@/assets/Album/heart.png"
import downloaded from "@/assets/Album/downloaded.png"

interface SelectModeImgProps {
  item : contentListData
}

const SelectModeImg: React.FC<SelectModeImgProps> = ({ item }) => {
  const [isSelect, setIsSelect] = useState(false)
  const { setSelectList, setSelectListForPk } = useStore(selectStore)

  const clickHandler = (id: number, pk: string) => {
    setSelectList(id, isSelect)
    setSelectListForPk(pk, isSelect)
    setIsSelect(!isSelect)
  }

  return (
    <div onClick={() => clickHandler(item.photoId, item.pk)} className="flex justify-center items-center">
      <div className="w-[106px] h-[90px] relative">
        { isSelect ?
          <div className="w-[18px] h-[18px] rounded-full absolute top-1 right-1 bg-main-blue flex justify-center items-center">
            <img src={check} className="w-3" />
          </div>
          :
          <div className="w-[18px] h-[18px] rounded-full absolute top-1 right-1 bg-soft-gray border-[0.3px] border-disabled-gray " />
        }
        {item.isDownload ? 
          <img src={downloaded} className="absolute bottom-0 left-0 w-6" />
          : <></>
        }
        {item.isLikes ? 
          <div className="absolute bottom-1 right-1">
            <img className="w-4" src={heart} />
          </div>
          : <></>
        }
        <SecureImg url={item.url} />
      </div>
    </div>
  )
};

export default SelectModeImg;