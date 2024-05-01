import React from "react";
import back from "@/assets/Header/back.png"
import timeOut from "@/assets/Header/timeOut.png"
import albumOption from "@/assets/Header/albumOption.png"
import { useNavigate } from "react-router-dom";

const AlbumHeader: React.FC = () => {
  const navigate = useNavigate()

  const goBackHandler = () => {
    navigate(-1)
  }

  const isGuest = false

  return (
    <div className="fixed flex justify-between items-center px-4 w-full h-14 bg-white">
      <img src={back} onClick={() => goBackHandler()} />

      <div className="flex gap-6 items-center">
        { isGuest &&
          <div className="flex items-center gap-1">
            <img src={timeOut} />
            <div className="font-pre-R text-black">13 : 28</div>
          </div>
        }
        
        <img src={albumOption} className="w-6 h-6" />
        <button className="rounded-[10px] font-pre-SB text-white text-lg justify-center flex items-center bg-main-blue w-14 h-8">선택</button>
      </div>
    </div>
  )
};

export default AlbumHeader;