import React, { useState, useEffect } from "react";
import { simpleInsert, simpleCheck } from "../api/user";
import { httpStatusCode } from "../utils/http-status";
import { useStore } from "zustand";
import { userStore } from "../stores/UserStore";
import { useNavigate } from "react-router-dom";
import { GroupKey } from "../types/GroupType";
import { guestStore } from "../stores/GuestStore";
interface SimplePassProps {
	type: string;
}

const SimplePass: React.FC<SimplePassProps> = ({ type }) => {
	const [password, setPassword] = useState<string>("");
	const [numbers, setNumbers] = useState<string[]>([]);
	const [pageTitle, setPageTitle] = useState<string>("");
	const [isCheck, setIsCheck] = useState<boolean>(false);
	const [forCheck, setForCheck] = useState<string>("");
	const gridCols = "w-full grid grid-cols-3";
	const colOpt = "flex justify-center items-center";
	const pageType = type;
	const user = useStore(userStore);
	const guest = useStore(guestStore);
	const navi = useNavigate();

	useEffect(() => {
		setNumbers(
			Array.from({ length: 10 }, (_, index) => String(index)).sort(
				() => Math.random() - 0.5,
			),
		);
	}, []);

	useEffect(() => {
		if (pageType === "insert" && !isCheck) setPageTitle("간편 비밀번호 등록");
		else setPageTitle("간편 비밀번호 확인");
	}, [pageType, isCheck]);

	useEffect(() => {
		if (password.length === 4) {
			if (pageType === "insert" && !isCheck) {
				setIsCheck(true);
				setForCheck(password);
				setPassword("");
				return;
			} else if (pageType === "insert" && isCheck) {
				if (forCheck !== password) {
					alert("다시 입력하세요");
					setPassword("");
					return;
				}
				sendInsertPass();
			} else if (pageType === "check") {
				sendCheckPass();
			}
		}
	}, [password]);

	const sendInsertPass = async () => {
		const param = { password: password };
		await simpleInsert(
			param,
			(response) => {
				if (response.status === httpStatusCode.OK) {
					user.setIsInsert(true);
					sendCheckPass();
				}
			},
			(err) => {
				alert("잘못된 접근입니다. 잠시후 다시 시도해주세요.");
				console.log(err);
			},
		);
	};

	const sendCheckPass = async () => {
		const config = {
			password: password,
		};
		await simpleCheck(
			config,
			(response) => {
				user.setIsCheck(true);
				user.setSimplePass(password);
				groupKeySetting(response.data.data);

				if (guest.isInvite) {
					const rootPk = guest.viewPartyPk;
					guest.setPartyPk(0);
					guest.setIsInvite(false);
					navi(`/group/${rootPk}/attend`)
				} else {
					navi("/");
				}
			},
			(err) => {
				alert("잘못된 접근입니다. 잠시후 다시 시도해주세요.");
				console.log(err);
			},
		);
	};

	// 비밀번호 한 자리를 입력하는 함수
	const handleInput = (value: string) => {
		if (password.length === 4) return;
		const newPass = password + value;
		setPassword(newPass);
	};

	// 비밀번호 한 자리를 지우는 함수
	const handleDelete = (current: string) => {
		if (current.length === 0) return;
		const newPass = current.slice(0, -1);
		setPassword(newPass);
	};

	// 비밀번호를 초기화하는 함수
	const handleReset = () => {
		setPassword("");
	};

	const groupKeySetting = (data: GroupKey[]) => {
		const keys: Record<number, string> = {};

		data.forEach((item) => {
			keys[item.partyId] = item.sseKey;
		});
		user.setGroupKey(keys);
	};

	return (
		<div className="w-screen h-screen">
			<div className="w-full h-36 p-4 flex flex-col items-center justify-end bg-main-blue">
				<p className="text-3xl ps-4 font-pre-B text-white">{pageTitle}</p>
			</div>
			<div className="flex w-full h-[272px] justify-center gap-6 items-center bg-main-blue">
				<div
					className={
						(password.length >= 1 ? "bg-white" : "bg-disabled-gray") +
						" w-10 h-10 rounded-full"
					}
				></div>
				<div
					className={
						(password.length >= 2 ? "bg-white" : "bg-disabled-gray") +
						" w-10 h-10 rounded-full"
					}
				></div>
				<div
					className={
						(password.length >= 3 ? "bg-white" : "bg-disabled-gray") +
						" w-10 h-10 rounded-full"
					}
				></div>
				<div
					className={
						(password.length >= 4 ? "bg-white" : "bg-disabled-gray") +
						" w-10 h-10 rounded-full"
					}
				></div>
			</div>
			<div className="w-full h-[calc(100%-416px)] grid grid-rows-4 text-2xl font-pre-SB text-black">
				<div className={gridCols}>
					<div onClick={() => handleInput(numbers[0])} className={colOpt}>
						{numbers[0]}
					</div>
					<div onClick={() => handleInput(numbers[1])} className={colOpt}>
						{numbers[1]}
					</div>
					<div onClick={() => handleInput(numbers[2])} className={colOpt}>
						{numbers[2]}
					</div>
				</div>
				<div className={gridCols}>
					<div onClick={() => handleInput(numbers[3])} className={colOpt}>
						{numbers[3]}
					</div>
					<div onClick={() => handleInput(numbers[4])} className={colOpt}>
						{numbers[4]}
					</div>
					<div onClick={() => handleInput(numbers[5])} className={colOpt}>
						{numbers[5]}
					</div>
				</div>
				<div className={gridCols}>
					<div onClick={() => handleInput(numbers[6])} className={colOpt}>
						{numbers[6]}
					</div>
					<div onClick={() => handleInput(numbers[7])} className={colOpt}>
						{numbers[7]}
					</div>
					<div onClick={() => handleInput(numbers[8])} className={colOpt}>
						{numbers[8]}
					</div>
				</div>
				<div className={gridCols}>
					<div
						onClick={() => handleDelete(password)}
						className={colOpt + " text-base"}
					>
						Delete
					</div>
					<div onClick={() => handleInput(numbers[9])} className={colOpt}>
						{numbers[9]}
					</div>
					<div onClick={handleReset} className={colOpt + " text-base"}>
						Reset
					</div>
				</div>
			</div>
		</div>
	);
};

export default SimplePass;
