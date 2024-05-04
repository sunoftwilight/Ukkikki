import React from "react";
import back from "@/assets/Header/back.png"
import { useNavigate } from "react-router-dom";

const BackHeader: React.FC = () => {
  const navigate = useNavigate()

  const goBackHandler = () => {
    navigate(-1)
  }

  const writeHandler = () => {
    navigate('/feed')
  }

  return (
    <div className="flex justify-between items-center px-4 w-full h-14 bg-white">
      <img src={back} onClick={() => goBackHandler()} />
      <button onClick={() => writeHandler()} className="rounded-[10px] font-pre-SB text-white text-lg justify-center flex items-center bg-main-blue w-14 h-8">작성</button>
    </div>
  )
};

export default BackHeader;