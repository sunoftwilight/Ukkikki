import React, { useEffect, useState } from "react";
import editBtn from "@/assets/ArticleDetail/edit.png";
import deleteBtn from "@/assets/ArticleDetail/delete.png";
import { CommentTopInterface } from "../../../types/Comment/CommentInterface";
import { userStore } from "../../../stores/UserStore";
import { useStore } from "zustand";
import { getPartyUserList } from "../../../api/party";
import { currentGroupStore } from "../../../stores/GroupStore";
import { UserGrantData } from "../../../types/GroupType";

const CommentTop: React.FC<CommentTopInterface> = ({ comment, isModify, commentDelete, switchIsModify, createReply}) => {

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
		return currentUser && (Number(user.userId) === comment.userId || currentUser.memberRole === "MASTER");
	  };


	return (
		<div className="flex justify-between">
			<div className="flex gap-2">
				<div className="font-pre-SB text-black text-xs">{comment.userName}</div>
				<div className="font-pre-L text-point-gray text-[10px]">
					{comment.createdDate}
				</div>
			</div>

			{!isModify && (
				<div className="flex gap-2">
					{ Number(user.userId) === comment.userId && <img src={editBtn} className="w-4" onClick={switchIsModify} />}
					{ isUserAuthorizedToDelete()  && <img src={deleteBtn} className="w-3" onClick={commentDelete} />}
					<span className="text-blue-500 text-xs" onClick={createReply}>답글달기</span>
				</div>
			)}
		</div>
	);
};

export default CommentTop;
