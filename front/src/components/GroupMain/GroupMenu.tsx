import React from "react";
import { inviteStore } from "../../stores/ModalStore";
import { Link } from "react-router-dom";

const GroupMenu: React.FC = () => {
  const btnStyle = "w-full h-[50px] rounded-[15px] font-pre-SB text-white text-xl flex justify-center items-center hover:scale-95 hover:bg-main-blue active:scale-95 active:bg-main-blue transition-transform duration-75"
  const { setInviteOpen } = inviteStore()

  return (
    <div className="flex flex-col gap-[10px]">
      <Link to='/album' className={`${btnStyle}  bg-soft-blue`}>앨범</Link>
      <Link to='/feed' className={`${btnStyle}  bg-soft-blue`}>피드</Link>
      <Link to='/chat' className={`${btnStyle}  bg-soft-blue`}>채팅</Link>
      <div className={`${btnStyle}  bg-soft-blue`} onClick={() => setInviteOpen()}>초대</div>
      <Link to='/trash' className={`${btnStyle} bg-disabled-gray`}>휴지통</Link>
    </div>
  )
};

export default GroupMenu;