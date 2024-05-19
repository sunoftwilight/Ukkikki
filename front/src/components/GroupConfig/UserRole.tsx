import React, { useState } from "react";
import { useStore } from "zustand";
import { memberInfoStore } from "../../stores/MemberInfoStore";
import { changePartyGrant } from "../../api/party";
import { useNavigate, useParams } from "react-router-dom";

const UserRole: React.FC = () => {
	const memberInfo = useStore(memberInfoStore)
	const [selectedRole, setSelectedRole] = useState<string | null>(memberInfo.data?.memberRole || null)
	const {groupPk} = useParams()
	const navi = useNavigate();

	const changeGrant = async() => {
		if (!selectedRole || !memberInfo.data) return;
		const param = {
			memberRole: selectedRole
		}
		
		await changePartyGrant(Number(groupPk), memberInfo.data?.userId, param,
			() => {
				navi(`/group/${groupPk}/user`)
			},
			(err) => {
				console.error(err)
			}
	)
	}

  return (
    <div className="w-full p-2 text-black">
			<p className="text-xl mt-9 mb-3">권한 변경</p>
			<div className="h-40 px-4 py-4 w-full bg-soft-gray rounded-[15px] flex flex-col justify-evenly text-base gap-2">
				<div className="px-2 flex items-center gap-4">
					<input type="radio" name="role" id="edit" checked={selectedRole === 'EDITOR'} onChange={() => setSelectedRole('EDITOR')}/>
					<label htmlFor="edit">에디터</label>
				</div>
				<div className="px-2 flex items-center gap-4">
					<input type="radio" name="role" id="view" checked={selectedRole === 'VIEWER'} onChange={() => setSelectedRole('VIEWER')}/>
					<label htmlFor="view">뷰어</label>
				</div>
				<div className="w-full h-9 px-5 bg-soft-blue rounded-xl flex justify-center items-center" onClick={()=> changeGrant()}>
					<p className="font-pre-SB text-lg text-white">적용</p>
				</div>
			</div>
    </div>
  )
};

export default UserRole;