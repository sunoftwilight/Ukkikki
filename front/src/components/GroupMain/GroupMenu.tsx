import React from "react";
import { inviteStore } from "../../stores/ModalStore";
import { Link } from "react-router-dom";

const GroupMenu: React.FC = () => {
  const btnStyle = "w-full h-[50px] rounded-[15px] bg-soft-blue font-pre-SB text-white text-xl flex justify-center items-center hover:scale-95 hover:bg-main-blue active:scale-95 active:bg-main-blue transition-transform duration-75"
  const { setInviteOpen } = inviteStore()

  return (
    <div className="flex flex-col gap-[10px]">
      <Link to='/album' className={btnStyle}>앨범</Link>
      <Link to='/feed' className={btnStyle}>피드</Link>
      <Link to='/chat' className={btnStyle}>채팅</Link>
      <div className={btnStyle} onClick={() => setInviteOpen()}>초대</div>
    </div>
  )
};

export default GroupMenu;