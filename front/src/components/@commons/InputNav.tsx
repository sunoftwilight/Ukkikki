import React, { useEffect, useState } from "react";
import { UserGrantData } from "../../types/GroupType";
import { getPartyUserList } from "../../api/party";
import { useStore } from "zustand";
import { currentGroupStore } from "../../stores/GroupStore";
import { createComment } from "../../api/Article/comment";
import { useParams } from "react-router-dom";

const InputNav: React.FC<{getCommentList : () => void;}> = ({getCommentList}) => {
	// 게시판 번호
	const {feedPk} = useParams();
	// 그룹 pk
	const group = useStore(currentGroupStore);
	// 그룹 유저 정보 가져오기.
	const [userList, setUserList] = useState<UserGrantData[]>([]);
	// 입력 값
	const [inputValue, setInputValue] = useState("");
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
	>([]);

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


	// 댓글 등록
	const commentEnter = async () => {

		if(inputValue.length === 0){
			return;
		}

		const params = {
			content : inputValue,
			tagList : tagUserList
		}

		await createComment (
			Number(feedPk),
			params,
			() => {
				setInputValue("");
				setIsShowUserList(false);
				setTagIndex(0);
				setTagValue("");
				setTagUserList([]);
				getCommentList();
			},
			(err) => {
				console.log(err);
			}

		)

	}

	// 태그 클릭
	const selectTag = (userId: number, userName: string) => {
		if (!tagUserList.some((tag) => tag.userId === userId)) {
			setTagUserList([...tagUserList, { userId: userId, userName: userName }]);
		}

		// @** 지우기
		setInputValue(inputValue.substring(0, tagIndex - 1));
		setIsShowUserList(false); // 태그 선택 후 사용자 리스트 숨기기
	};

	// 태그 삭제
	const removeTag = (index: number) => {
		const newTagList = [...tagUserList];
		newTagList.splice(index, 1);
		setTagUserList(newTagList);
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

	// 값이 변하면 저장.
	const changeValue = (e: React.ChangeEvent<HTMLInputElement>) => {
		setInputValue(e.target.value);

		
		if (e.target.value.endsWith("@")) {
			setIsShowUserList(true);
			setTagIndex(e.target.value.length);
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

	return (
		<div className="px-4 py-[6px] w-full fixed bottom-0 h-12 flex justify-center items-center gap-[6px] bg-white">
			<div className="flex flex-row items-center px-3 font-pre-M text-base rounded-xl border-[0.4px] border-solid border-point-gray text-black w-[calc(100%-50px)] h-full outline-none">
				{tagUserList.map((tag, index) => (
					<div
						key={index}
						className="flex items-center justify-center mr-1 rounded-lg px-1 text-sm bg-blue-200 w-auto text-nowrap"
					>
						{tag.userName}
						<button
							onClick={() => removeTag(index)}
							className="ml-2 bg-red-500 text-white rounded-full h-4 w-4 flex items-center justify-center"
						>
							x
						</button>
					</div>
				))}
				<input
					placeholder="댓글을 작성해보세요"
					className="font-pre-M text-base border-point-gray text-black w-full h-full outline-none"
					value={inputValue}
					onChange={changeValue}
					onCompositionStart={inputStart}
					onCompositionEnd={inputEnd}
				/>
			</div>
			<button className="bg-main-blue text-white font-pre-M text-base w-12 h-full rounded-xl"
				onClick={commentEnter}>
				전송
			</button>
			{isShowUserList && (
				<ul className="absolute bottom-9 left-4 w-[calc(100%-85px)] mb-2 bg-white border border-gray-200 rounded-lg">
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
                <img src={user.profileUrl} alt={user.profileUrl} className="rounded-full w-7 h-7 mr-2" />
								{user.userName}
							</li>
						))}
				</ul>
			)}
		</div>
	);
};

export default InputNav;
