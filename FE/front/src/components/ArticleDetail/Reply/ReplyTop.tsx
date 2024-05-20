import React, { useEffect, useState } from "react";
import editBtn from "@/assets/ArticleDetail/edit.png";
import deleteBtn from "@/assets/ArticleDetail/delete.png";
import { ReplyTopInterface } from "../../../types/Comment/CommentInterface";
import { useStore } from "zustand";
import { userStore } from "../../../stores/UserStore";
import { currentGroupStore } from "../../../stores/GroupStore";
import { UserGrantData } from "../../../types/GroupType";
import { getPartyUserList } from "../../../api/party";

const ReplyTop: React.FC<ReplyTopInterface> = ({ reply, isModify, commentDelete, switchIsModify}) => {
	
	// 그룹 pk
	const group = useStore(currentGroupStore);
	// 그룹 유저 정보 가져오기.
	const [userList, setUserList] = useState<UserGrantData[]>([]);
	
	const user = useStore(userStore);

	useEffect(() => {
		getUserList()
	},[])

	const getUserList = async () => {
		await getPartyUserList(
			Number(group.currentGroup),
			(res) => {
				setUserList(res.data.data);
			},
			(err) => {
				console.log(err);
			},
		);
	};


	const isUserAuthorizedToDelete = () => {
		const currentUser = userList.find(u => u.userId === Number(user.userId));
		// 현재 로그인한 사용자가 글을 쓴 사용자이거나 마스터 권한을 가진 경우 true를 반환
		return currentUser && (Number(user.userId) === reply.userId || currentUser.memberRole === "MASTER");
	  };

	return (
		<div className="flex w-full justify-between">
			<div className="flex gap-2 w-full">
				<div className="font-pre-SB text-black text-xs">{reply.userName}</div>
				<div className="font-pre-L text-point-gray text-[10px]">
					{reply.createdDate}
				</div>
			</div>

			{!isModify && (
				<div className="flex gap-2">
					{ Number(user.userId) === reply.userId && <img src={editBtn} className="w-4" onClick={switchIsModify} />}
					{ isUserAuthorizedToDelete() && <img src={deleteBtn} className="w-3" onClick={commentDelete} />}
				</div>
			)}
		</div>
	);
};

export default ReplyTop;
