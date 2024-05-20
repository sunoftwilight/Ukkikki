import React from "react";
import notice from "@/assets/Main/notice.png"

const Banner: React.FC = () => {
  return (
    <div className="flex items-center gap-4 bg-soft-gray w-full h-11 px-3 rounded-xl">
      <img src={notice} className="w-[30px] h-[30px]" />
      <div className="font-pre-R text-xs text-black">V 1.0.2 업데이트 안내</div>
    </div>
  )
};

export default Banner;