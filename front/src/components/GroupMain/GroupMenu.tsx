import React from "react";
import { inviteStore } from "../../stores/ModalStore";
import { Link, useParams } from "react-router-dom";
import { getPartyLink } from "../../api/party";

const GroupMenu: React.FC = () => {
  const btnStyle = "w-full h-[50px] rounded-[15p] font-pre-SB text-white text-xl flex justify-center items-center hover:scale-95 hover:bg-main-blue active:scale-95 active:bg-main-blue transition-transform duration-75";

  const { groupPk } = useParams();
  const { setInviteCode, setInviteOpen } = inviteStore()

  const handleInviteBtn = async () => {
    await getPartyLink(Number(groupPk), 
      (res) => {
        setInviteCode(res.data.data.partyLink)
        setInviteOpen()
      },
      (err) => {
        console.log(err)
    })
  }
  
  return (
    <div className="flex flex-col gap-[10px]">
      <Link to='/album' className={`${btnStyle}  bg-soft-blue`}>앨범</Link>
      <Link to='/feed' className={`${btnStyle}  bg-soft-blue`}>피드</Link>
      <Link to='/chat' className={`${btnStyle}  bg-soft-blue`}>채팅</Link>
      <div className={`${btnStyle}  bg-soft-blue`} onClick={() => handleInviteBtn()}>초대</div>
      <Link to='/trash' className={`${btnStyle} bg-disabled-gray`}>휴지통</Link>
    </div>
  )
};

export default GroupMenu;