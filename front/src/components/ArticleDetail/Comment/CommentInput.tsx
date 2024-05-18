import React, { useEffect, useState } from "react";
import { CommentInputInterface } from "../../../types/Comment/CommentInterface";
import { UserGrantData } from "../../../types/GroupType";
import { getPartyUserList } from "../../../api/party";
import { currentGroupStore } from "../../../stores/GroupStore";
import { useStore } from "zustand";

import ReplyImg from "@/assets/ArticleDetail/lucide_reply.png"

const CommentInput: React.FC<CommentInputInterface> = ({
	userId,
	content,
	tag,
	commentModify,
	switchIsModify,
}) => {
	// 그룹 pk
	const group = useStore(currentGroupStore);
	// 그룹 유저 정보 가져오기.
	const [userList, setUserList] = useState<UserGrantData[]>([]);
	// 입력 값
	const [inputValue, setInputValue] = useState(content);
	// 태그 인덱스
	const [tagIndex, setTagIndex] = useState(0);
	// 태그 이후 값
	const [tagValue, setTagValue] = useState("");
	// 유저 리스트 보여주기
	const [isShowUserList, setIsShowUserList] = useState(false);
	// 입력 중인지 체크
	const [isInput, setIsInput] = useState(false);
	// 태그된 유저 목록
	const [tagUserList, setTagUserList] = useState<
		{ userId: number; userName: string }[]
	>(tag);

	// 처음 한번 유저 리스트를 불러온다.
	useEffect(() => {
		getUserList();
	}, []);

	// useEffect를 사용해 isInput의 값이 변경될 때마다 changeValue를 호출합니다.
	useEffect(() => {
		if (!isInput) {
			tagCheck();
		}
	}, [isInput]);

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

	// 태그 클릭
	const selectTag = (userId: number, userName: string) => {
		if (!tagUserList.some((tag) => tag.userId === userId)) {
			setTagUserList([...tagUserList, { userId: userId, userName: userName }]);
		}

		// @** 지우기
		setInputValue(inputValue.substring(0, tagIndex - 1));
		setIsShowUserList(false); // 태그 선택 후 사용자 리스트 숨기기
	};

	// 태그 이후 값을 입력하면서 값을 포함하고 있는 유저가 없다면
	// 리스트를 숨기고 false로 변경한다.
	const tagCheck = () => {
		if (isShowUserList) {
			const newText = inputValue.substring(tagIndex);
			setTagValue(newText);

			// 사용자 리스트를 필터링하여 newText를 포함하는 유저가 있는지 확인합니다.
			const filteredUsers = userList.filter((user) =>
				user.userName.includes(newText),
			);

			// 필터링된 유저가 없다면 isShowUserList를 false로 설정합니다.
			if (filteredUsers.length === 0) {
				setIsShowUserList(false);
				setTagIndex(0);
				setTagValue("");
			}
		}
	};

	// 태그 삭제
	const removeTag = (index: number) => {
		const newTagList = [...tagUserList];
		newTagList.splice(index, 1);
		setTagUserList(newTagList);
	};

	// 값이 변하면 저장.
	const changeValue = (e: React.ChangeEvent<HTMLInputElement>) => {
		setInputValue(e.target.value);
	};

	// 키 값을 체크하여 @과 esc는 다르게 한다.
	const checkValue = (e: React.KeyboardEvent<HTMLInputElement>) => {
		setInputValue(e.currentTarget.value);
		if (e.key === "@") {
			setIsShowUserList(true);
			setTagIndex(e.currentTarget.value.length);
		}

		if (e.key === "Escape") {
			setIsShowUserList(false);
			setTagIndex(0);
			setTagValue("");
		}
	};

	// 처음 입력이 시작되면.
	const inputStart = () => {
		setIsInput(true);
	};

	// 해당 인덱스의 입력이 끝나면..
	const inputEnd = () => {
		// 입력 체크 변경.
		setIsInput(false);
	};

	// 취소시 초기화
	const inputCancel = () => {
		setInputValue(content);
		setTagUserList(tag);
		setTagIndex(0);
		setIsShowUserList(false);
		setTagValue("");

		switchIsModify();
	};

	// 댓글 수정
	const inputModify = () => {
		if (inputValue.length === 0) {
			return;
		}

		commentModify(inputValue, tagUserList);
		switchIsModify();
		setTagIndex(0);
		setIsShowUserList(false);
		setTagValue("");
	};

	return (
		<div className="flex flex-col">
			{isShowUserList && (
				<ul className="left-4 w-[calc(100%-85px)] mb-2 bg-white border border-gray-200 rounded-lg">
					{userList
						.filter(
							(user) => tagValue === "" || user.userName.includes(tagValue),
						)
						.map((user) => (
							<li
								key={user.userId}
								className="flex items-center p-2 hover:bg-gray-100 cursor-pointer"
								onClick={() => selectTag(user.userId, user.userName)}
							>
								<img
									src={user.profileUrl}
									alt={user.profileUrl}
									className="rounded-full w-7 h-7 mr-2"
								/>
								{user.userName}
							</li>
						))}
				</ul>
			)}
			<div className="flex items-center">
				{userId === -1 && (<img src={ReplyImg} alt="reply" className="w-4 mr-2" />)}
				{tagUserList.map((tag, index) => (
					<div className="flex items-center justify-center mr-1 rounded-lg px-1 text-xs bg-blue-200 w-auto text-nowrap">
						{tag.userName}
						<button
							onClick={() => removeTag(index)}
							className="text-lg text-white rounded-full h-4 w-4 flex items-center justify-center"
						>
							x
						</button>
					</div>
				))}
				<input
					placeholder={content}
					className="py-3 px-2 w-[calc(100%-66px)] font-pre-L text-sm text-black h-5 outline-none border-b border-point-gray border-gray-300 mr-4"
					value={inputValue}
					onChange={changeValue}
					onKeyUp={checkValue}
					onCompositionStart={inputStart}
					onCompositionEnd={inputEnd}
				/>
				<button
					className="bg-main-blue text-white font-pre-M text-sm w-12 h-6 flex items-center justify-center rounded-lg px-1 py-1"
					onClick={inputCancel}
				>
					취소
				</button>
				<button
					className="ml-2 bg-main-blue text-white font-pre-M text-sm w-12 h-6 flex items-center justify-center rounded-lg px-1 py-1"
					onClick={inputModify}
				>
					{userId === -1 ? <>등록</> : <>수정</>}
				</button>
			</div>
		</div>
	);
};

export default CommentInput;
