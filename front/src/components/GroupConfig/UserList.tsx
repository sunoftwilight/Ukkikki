import React, { useEffect, useState } from "react";
import clickBtn from "../../assets/GroupConfig/clickBtn.png";

import { getPartyUserList, getPartyBlockUserList, kickPartyUser} from "../../api/party";
import { UserGrantData } from "../../types/GroupType";
import { useNavigate, useParams } from "react-router-dom";
import { memberInfoStore } from "../../stores/MemberInfoStore";
import { useStore } from "zustand";

interface ListType {
	type: string
}

const UserList: React.FC<ListType> = (listType) => {

	const [userList, setUserList] = useState<UserGrantData[]>([])
	const type = listType.type;
	const navi = useNavigate();
	const { groupPk } = useParams();
	const memberInfo = useStore(memberInfoStore);

	useEffect(() => {
		if(type === 'users') {
			loadUserList()
		}
		else if(type === 'banUsers') {
			loadBanList()
		}
	}, [])

	const loadUserList = async () => {
		await getPartyUserList(Number(groupPk),
			(res) => {
				setUserList(res.data.data)
			},
			(err) => {
				console.log(err)
			})
	}

	const loadBanList = async () => {
		await getPartyBlockUserList(Number(groupPk),
			(res) => {
				setUserList(res.data.data)
			},
			(err) => {
				console.log(err)
			})
	}

	const clickKickBtn = async (userId:number) => {
		if (!memberInfo.data) return;
		await kickPartyUser(memberInfo.data?.partyId, userId,
			() => {
				loadBanList();
			},
			(err) => {
				console.log(err)
			}
		)
	}


	const CallType = () => {
		switch (type) {
			case "users":
				return (
					userList.map((item, idx) => (
						<div key={idx} className="w-full h-16 flex bg-soft-gray rounded-2xl items-center px-3 gap-5 font-pre-R text-xl relative">
							<img src={item.profileUrl} className="w-12 h-12 rounded-full"/>
							<p>{item.userName}</p>
							<div className="absolute right-3 flex text-base text-main-blue items-center gap-3">
								<p>{item.memberRole === 'MASTER' ? '호스트' : '뷰어'}</p>
								<img src={clickBtn} className="w-3 h-4" onClick={() => {memberInfo.setData(item); navi(`/group/${groupPk}/userdetail`);}}/>
							</div>
						</div>
					))
				);
			case "banUsers":
				return(
					userList.map((item, idx) => (
						<div key={idx} className="w-full h-16 flex bg-soft-gray rounded-2xl items-center px-3 gap-5 font-pre-R text-xl relative">
							<img src={item.profileUrl} className="w-12 h-12 rounded-full"/>
							<p>{item.userName}</p>
							<div className="absolute right-3  bg-main-blue text-base text-white w-[60px] h-[38px] flex justify-center items-center rounded-xl" onClick={()=> clickKickBtn(item.userId)}>
								<p>해제</p>
							</div>
						</div>
					))
				)
		}
	};


  return (
    <div className="w-full h-full flex flex-col gap-3">
			{CallType()}
    </div>
  )
};

export default UserList;