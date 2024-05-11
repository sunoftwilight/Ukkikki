import React, { useEffect, useState } from "react";
import close from "@/assets/Hamburger/close.png";
import ModalBackground from "./ModalBackground";
import { Link } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import { useStore } from "zustand";
import { headerStore } from "../../stores/HeaderStateStore";
import { getAlarm } from "../../api/alarm";
import { AlarmItemType } from "../../types/AlarmType";
import AlarmItem from "./AlarmItem";

// const alarmDummy = [
// 	{
// 		groupImg:
// 			"https://i.namu.wiki/i/VMIHkLm6DcUT4d9-vN4yFw7Yfitr8luT_U2YwJsugGodCQ01ooGH_kHX0D6sJ3HDS1YHfvy9B81al8rKCxqKYw.webp",
// 		groupName: "폼폼푸린은귀여워",
// 		member: "훈지훈",
// 		content: "이 사진 왜캐 잘나옴? 사기 ㄴ",
// 	},
// 	{
// 		groupImg:
// 			"https://i.namu.wiki/i/VMIHkLm6DcUT4d9-vN4yFw7Yfitr8luT_U2YwJsugGodCQ01ooGH_kHX0D6sJ3HDS1YHfvy9B81al8rKCxqKYw.webp",
// 		groupName: "폼폼푸린은귀여워",
// 		member: "성규규성",
// 		content: "오 이거 쩐다 레전드",
// 	},
// 	{
// 		groupImg:
// 			"https://i.namu.wiki/i/77QbC6SyC4wLmwtoCMzxvC-8OgmQ191Ve79xM2r5xMBb5-sqpqZem9lVMxNz9FffLio1RuGSDla-4gISPO4jAQ.webp",
// 		groupName: "시나모롤도귀여워",
// 		member: "용수용수선생",
// 		content: "ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ형님",
// 	},
// 	{
// 		groupImg:
// 			"https://i.namu.wiki/i/77QbC6SyC4wLmwtoCMzxvC-8OgmQ191Ve79xM2r5xMBb5-sqpqZem9lVMxNz9FffLio1RuGSDla-4gISPO4jAQ.webp",
// 		groupName: "시나모롤도귀여워",
// 		member: "상수시치",
// 		content: "에반데 ;;;;;;",
// 	},
// ];

const Hamburger: React.FC = () => {
	const menuList = [
		{ name: "카메라", router: "/camera" },
		{ name: "마이 앨범", router: "/mypage" },
		{ name: "참여 중인 그룹", router: "/grouplist" },
		{ name: "설정", router: "/setting" },
	];

  const [alarmList, setAlarmList] = useState<AlarmItemType[]>([])

	const { alarmOpen, setAlarmOpen } = useStore(headerStore)
  const { menuOpen, setMenuOpen } = useStore(headerStore)

	const closeHandler = () => {
		if (alarmOpen) {
			setAlarmOpen();
		} else if (menuOpen) {
			setMenuOpen();
		}
	};

  useEffect(() => {
    getAlarm(
      { pageNo: 1, pageSize: 20 },
      (res) => {
        setAlarmList(res.data.data.alarmList)
      },
      (err) => { console.error(err) }
    )
  }, [alarmOpen])

	return (
		<AnimatePresence>
			{(alarmOpen || menuOpen) && (
				<div className="fixed top-0 start-0 h-screen w-screen flex justify-end">
					<ModalBackground />

					<motion.div
						key={menuOpen ? "메뉴" : "알림함"}
						className="h-full w-72 z-20 bg-white p-4 flex flex-col gap-y-7 overflow-scroll scrollbar-hide"
						initial={{ opacity: 0, translateX: "88px" }}
						animate={{ opacity: 1, translateX: "0px" }}
						exit={{ opacity: 0, translateX: "88px" }}
					>
						{/* 햄버거 제목 & 닫기 버튼 */}
						<div className="flex justify-between items-center">
							<div className="font-gtr-B text-3xl text-black">
								{menuOpen ? "메뉴" : "알림함"}
							</div>
							<img
								src={close}
								onClick={() => closeHandler()}
								className="font-gtr-R text-black text-xl"
							/>
						</div>

						{/* 메뉴바 */}
						{menuOpen && (
							<div>
								{menuList.map((menuItem, idx) => (
									<Link
										onClick={() => closeHandler()}
										to={menuItem.router}
										key={idx}
										className="w-full h-12 flex items-center px-1"
									>
										{menuItem.name}
									</Link>
								))}
							</div>
						)}

						{/* 알림함 */}
						{alarmOpen &&
							alarmList.map((alarmItem, idx) => (
                <AlarmItem key={idx} alarmItem={alarmItem} />
							))}
					</motion.div>
				</div>
			)}
		</AnimatePresence>
	);
};

export default Hamburger;
