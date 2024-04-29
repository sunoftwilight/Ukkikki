import React from "react";

const GroupMenu: React.FC = () => {
  const btnStyle = "w-full h-[50px] rounded-[15px] bg-soft-blue font-pre-SB text-white text-xl flex justify-center items-center"
  return (
    <div className="flex flex-col gap-[10px]">
      <div className={btnStyle}>앨범</div>
      <div className={btnStyle}>피드</div>
      <div className={btnStyle}>채팅</div>
      <div className={btnStyle}>초대</div>
    </div>
  )
};

export default GroupMenu;