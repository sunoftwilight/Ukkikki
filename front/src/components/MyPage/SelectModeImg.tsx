import React, { useState } from "react";
import check from "@/assets/Album/check.png"

interface SelectModeImgProps {
  item : {
    pk: number
    url: string
  }
}

const SelectModeImg: React.FC<SelectModeImgProps> = ({ item }) => {
  const [isSelect, setIsSelect] = useState(false)

  return (
    <div onClick={() => setIsSelect(!isSelect)} className="flex justify-center items-center">
      <div className="w-[106px] h-[90px] relative">
        { isSelect ?
          <div className="w-[18px] h-[18px] rounded-full absolute top-1 right-1 bg-main-blue flex justify-center items-center">
            <img src={check} className="w-3" />
          </div>
          :
          <div className="w-[18px] h-[18px] rounded-full absolute top-1 right-1 bg-soft-gray border-[0.3px] border-disabled-gray " />
        }
        <img src={item.url} className="w-[106px] h-[90px] object-cover rounded-lg" />
      </div>
    </div>
  )
};

export default SelectModeImg;