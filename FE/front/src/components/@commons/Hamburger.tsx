import React, { useEffect, useState } from "react";
import close from "@/assets/Hamburger/close.png";
import ModalBackground from "./ModalBackground";
import { Link } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import { useStore } from "zustand";
import { headerStore } from "../../stores/HeaderStateStore";
import { getAlarm } from "../../api/alarm";
import AlarmItem from "./AlarmItem";
import LoadingGif from "./LoadingGif";
import { AlarmListStore, AlarmOccuredStore } from "../../stores/AlarmStore";

const Hamburger: React.FC = () => {
  const { alarmList, setAlarmList } = useStore(AlarmListStore)
  const { setIsAlarmOccured } = useStore(AlarmOccuredStore)
  
  const { menuOpen, setMenuOpen } = useStore(headerStore)
  const { alarmOpen, setAlarmOpen } = useStore(headerStore)

	const menuList = [
		{ name: "카메라", router: "/camera" },
		{ name: "참여 중인 그룹", router: "/grouplist" },
		{ name: "설정", router: "/setting" },
	];

	const closeHandler = () => {
		if (alarmOpen) {
			setAlarmOpen();
		} else if (menuOpen) {
			setMenuOpen();
		}
	};

  // ========== 무한스크롤 ==================
  const [page, setPage] = useState<number>(1)
  const [isLoading, setIsLoading] = useState<boolean>(false)
  const [isLast, setIsLast] = useState<boolean>(false)

  // observer 컴포넌트 만나면 발생하는 콜백 함수 -> loading중 표시
  const handleObserver = (entries: IntersectionObserverEntry[]) => {
    const target = entries[0];

    if (target.isIntersecting && !isLoading && !isLast) {
      setIsLoading(true)
    }
  };
  
  // threshold : Intersection Observer의 옵션, 0 ~ 1 (0: 일 때는 교차점이 한 번만 발생해도 실행, 1은 모든 영역이 교차해야 콜백 함수가 실행)
  const observer = new IntersectionObserver(handleObserver, { threshold: 0 });

  useEffect(() => {
    // 최하단 요소를 관찰 대상으로 지정함
    const observerTarget = document.getElementById("observer");
    // 관찰 시작
    if (observerTarget) {
      observer.observe(observerTarget);
    }
  }, [])

  // 로딩중이면 페이지 상승 + api 요청
  // useEffect가 isLoading의 상태 변화를 계속 추적하며 api 쏘므로
  // setTimeout을 통해 api 요청 한번만 갈 수 있도록 수정
  useEffect(() => {
    if (isLast === true) return
    if (isLoading) {
      setPage((page) => page + 1);
      setTimeout(() => {
        fetchDataHandler();
      }, 10)
    }
  }, [isLoading])

  useEffect(() => {
    setIsAlarmOccured(false)
    setAlarmList('')
    setPage(1)
    setIsLoading(true)
    setIsLast(false)
  }, [alarmOpen])

  // 데이터 추가 및 loading상태 변경
  const fetchDataHandler = async () => {
    await getAlarm(
      { pageNo: page, pageSize: 20 },
      (res) => {
        setAlarmList(res.data.data.alarmList)
        if (res.data.data.last === true) {
          setIsLast(true)
        }
      },
      (err) => { console.error(err) }
    )
    setIsLoading(false)
  }

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

            <div className=" overflow-scroll scrollbar-hide">
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
                <div className="flex flex-col gap-y-5">
                  {alarmList.map((alarmItem, idx) => (
                    <AlarmItem key={idx} alarmItem={alarmItem} />
                  ))}
                  <div id='observer' className="h-[30px] w-full flex justify-center">
                    {(isLoading && !isLast) && <LoadingGif /> }
                  </div>
                </div>
              }
            </div>
					</motion.div>
				</div>
			)}
		</AnimatePresence>
	);
};

export default Hamburger;
