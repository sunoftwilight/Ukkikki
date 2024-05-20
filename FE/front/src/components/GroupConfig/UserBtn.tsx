import React from "react";
import { useStore } from "zustand";
import { memberInfoStore } from "../../stores/MemberInfoStore";
import { kickPartyUser, blockPartyUser, changePartyGrant } from "../../api/party";
import { useNavigate } from "react-router-dom";


const UserBtn: React.FC = () => {
	const memberInfo = useStore(memberInfoStore)
	const navi = useNavigate()
	
	const clickKickBtn = async () => {
		if (!memberInfo.data) return;
		await kickPartyUser(memberInfo.data?.partyId, memberInfo.data?.userId,
			() => {
				navi(-1)
			},
			(err) => {
				console.log(err)
			}
		)
	}

	const clickBlockBtn = async () => {
		if (!memberInfo.data) return;
		await blockPartyUser(memberInfo.data?.partyId, memberInfo.data?.userId,
			() => {
				navi(-1)
			},
			(err) => {
				console.log(err)
			}
		)
	}

	const clickGiveHostBtn = async () => {
		if (!memberInfo.data) return;
		const param = {
			memberRole: 'MASTER'
		}
		await changePartyGrant(memberInfo.data?.partyId, memberInfo.data?.userId, param,
			() => {
				navi(-1)
			},
			(err) => {
				console.error(err)
			}
	)
	}

	return (
		<div className="w-full p-2 h-[calc(100%-332px)] flex flex-col mt-8 text-xl text-black gap-4 relative">
			<div className="w-full flex flex-col gap-4">
				<div className="w-full h-16 bg-disabled-gray rounded-2xl flex items-center px-5" onClick={() => clickKickBtn()}>
					<p>'{memberInfo.data?.userName}' 추방</p>
				</div>
				<div className="w-full h-16 bg-disabled-gray rounded-2xl flex items-center px-5" onClick={() => clickBlockBtn()}>
					<p>'{memberInfo.data?.userName}' 차단</p>
				</div>
			</div>

			<div className="w-[calc(100%-16px)] h-16 bg-main-blue rounded-2xl flex items-center px-5 text-white absolute bottom-4 left-2" onClick={() => clickGiveHostBtn()}>
				<p>'{memberInfo.data?.userName}'에게 호스트 위임</p>
			</div>
		</div>
	)
};

export default UserBtn;