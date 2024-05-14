import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import env from "@/assets/GroupConfig/env.png";
import profile from "@/assets/GroupConfig/profile.png";
import groupOut from "@/assets/GroupConfig/groupOut.png";
import Modal from "../../components/@commons/Modal";
import { exitParty } from "../../api/party";

const GroupConfig: React.FC = () => {
  const { groupPk } = useParams()
	const navi = useNavigate()
  const [ isGroupOut, setIsGroupOut ] = useState<boolean>(false)

  const exitPartyFunc = async() => {
    await exitParty(
      Number(groupPk),
      () => {
        navi('/group/list')
      },
      (err) => {
        console.log(err)
      }
    )
  }
  return (
		<div className="p-4 w-full h-[calc(100%-48px)] flex flex-col gap-2 relative font-pre-R text-xl">
        <div className="px-4 w-full h-[60px] bg-soft-gray rounded-xl flex items-center" onClick={() => navi(`/group/${groupPk}/profile`)}>
          <img src={profile} className="w-6 h-6 me-4"/>
          <p>그룹 프로필 변경</p>
        </div>
        <div className="px-4 w-full h-[60px] bg-soft-gray rounded-xl flex items-center" onClick={() => navi(`/group/${groupPk}/env`)}>
          <img src={env} className="w-6 h-6 me-4"/>
          <p>그룹 환경 설정</p>
        </div>

        <div 
          className="px-4 w-[calc(100%-32px)] h-[60px] bg-soft-gray absolute bottom-0 rounded-xl flex items-center"
          onClick={() => {setIsGroupOut(true);}}>
          <img src={groupOut} className="w-6 h-6 me-4"/>
          <p>그룹 나가기</p>
        </div>

        {isGroupOut &&(
          <Modal
            modalItems={{content: '그룹을 나가시겠습니까?', modalType:'warn', btn:2 }}
            onSubmitBtnClick = {() => {setIsGroupOut(false); exitPartyFunc();}}
            onCancelBtnClick = {() => {setIsGroupOut(false);}}/>
        )}

      </div>
  )
};

export default GroupConfig;