import React from "react";
import { useNavigate } from "react-router-dom";

const GroupEnvConfig: React.FC = () => {
  const navi = useNavigate()
  

  return (
		<div className="p-4 w-full h-[calc(100%-48px)] flex flex-col gap-2 relative font-pre-R text-xl">
      <div className="px-4 w-full h-[60px] bg-soft-gray rounded-xl flex items-center" onClick={() => {navi('/groupuser')}}>
        <img src={'.'} className="w-6 h-6 me-4"/>
        <p>그룹 유저 관리</p>
      </div>
      <div className="px-4 w-full h-[60px] bg-soft-gray rounded-xl flex items-center" onClick={() => {navi('/groupban')}}>
        <img src={'.'} className="w-6 h-6 me-4"/>
        <p>차단 유저 관리</p>
      </div>
      <div className="px-4 w-full h-[60px] bg-soft-gray rounded-xl flex items-center" onClick={() => {navi('/groupinfo')}}>
        <img src={'.'} className="w-6 h-6 me-4"/>
        <p>그룹 대표정보 수정</p>
      </div>
      <div className="px-4 w-full h-[60px] bg-soft-gray rounded-xl flex items-center" onClick={() => {navi('/grouppass')}}>
        <img src={'.'} className="w-6 h-6 me-4"/>
        <p>그룹 비밀번호 변경</p>
      </div>
    </div>
  )
};

export default GroupEnvConfig;