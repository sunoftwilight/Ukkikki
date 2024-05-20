import React from "react";
import back from "@/assets/Header/back.png"
import { useNavigate } from "react-router-dom";

const SaveHeader: React.FC = () => {
  const navigate = useNavigate()

  const goBackHandler = () => {
    navigate(-1)
  }

  const saveHandler = () => {}

  return (
    <div className="flex justify-between items-center px-4 w-full h-14 bg-white">
      <img src={back} onClick={() => goBackHandler()} />
      <button onClick={() => saveHandler()} className="rounded-[10px] font-pre-SB text-white text-lg justify-center flex items-center bg-main-blue w-14 h-8">저장</button>
    </div>
  )
};

export default SaveHeader;