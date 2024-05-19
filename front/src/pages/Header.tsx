import React, { useEffect } from "react";
import { useLocation } from "react-router-dom";
import AlbumHeader from "../components/Header/AlbumHeader";
import BackHeader from "../components/Header/BackHeader";
import LogoHeader from "../components/Header/LogoHeader";
import SaveHeader from "../components/Header/SaveHeader";
import { EventSourcePolyfill } from "event-source-polyfill";
import { useStore } from "zustand";
import { AlarmOccuredStore } from "../stores/AlarmStore";
import toast, { Toaster } from "react-hot-toast";

const Header: React.FC = () => {
	const location = useLocation();
  const { setIsAlarmOccured } = useStore(AlarmOccuredStore)

	const groupBackPath = [
		"/group/:pk/list",
		"/group/:pk/create",
		"/creat/:pk/edone",
		"/group/:pk/config",
		"/group/:pk/env",
		"/group/:pk/profile",
		"/group/:pk/user",
		"/group/:pk/userdetail",
		"/group/:pk/ban",
		"/group/:pk/info",
		"/group/:pk/pass",
	];

	const basicPath = ["/", "/group", "/mypage",];
	const backPath = ["/setting", "/feed", "/chat", ...groupBackPath];
	const albumPath = ["/album", "/trash"];

	useEffect(() => {
		const stored = localStorage.getItem('USER_STORE');

    if (stored) {
      const obj = JSON.parse(stored);

      if (obj.state.accessToken !== '') {
				const sse = new EventSourcePolyfill(
					"https://k10d202.p.ssafy.io/api/alarm/sub",
					{
						headers: {
							authorization: obj.state.accessToken,
							"Content-Type": "text/event-stream",
						},
					},
				);

				// 알람 이벤트 발생 시 Alarm 발생 이미지 변경해주기
				sse.addEventListener("PASSWORD", (event: any) => {
					const e = event as MessageEvent; // 이벤트 타입을 MessageEvent로 캐스팅

					const alarmObject = JSON.parse(e.data);
					const partyName = alarmObject.partyName;

					toast.success(`${partyName} 그룹의 비밀번호가 변경되었습니다`,{icon: '🔐'})
					setIsAlarmOccured(true)
				});

				sse.addEventListener("COMMENT", (event: any) => {
					const e = event as MessageEvent; 

					const alarmObject = JSON.parse(e.data);
					const writerNick = alarmObject.writerNick;

					toast.success(`${writerNick}님께서 댓글을 작성했습니다`,{icon: '💬'})
					setIsAlarmOccured(true)
				});

				sse.addEventListener("REPLY", (event: any) => {
					const e = event as MessageEvent; 

					const alarmObject = JSON.parse(e.data);
					const writerNick = alarmObject.writerNick;

					toast.success(`${writerNick}님께서 대댓글을 작성했습니다`,{icon: '💬'})
					setIsAlarmOccured(true)
				});

				sse.addEventListener("CHAT", (event: any) => {
					const e = event as MessageEvent; 

					const alarmObject = JSON.parse(e.data);
					const writerNick = alarmObject.writerNick;

					toast.success(`${writerNick}님께서 회원님을 언급했습니다`,{icon: '💌'})
					setIsAlarmOccured(true)
				});

				sse.addEventListener("MEMO", (event: any) => {
					const e = event as MessageEvent; 

					const alarmObject = JSON.parse(e.data);
					const writerNick = alarmObject.writerNick;

					toast.success(`${writerNick}님께서 회원님을 언급했습니다`,{icon: '🖼'})
					setIsAlarmOccured(true)
				});

				sse.addEventListener("MENTION", (event: any) => {
					const e = event as MessageEvent; 

					const alarmObject = JSON.parse(e.data);
					const writerNick = alarmObject.writerNick;

					toast.success(`${writerNick}님께서 회원님을 언급했습니다`,{icon: '💬'})
					setIsAlarmOccured(true)
				});
		
				return () => sse.close(); // 컴포넌트 언마운트 시 EventSource 연결을 닫습니다.
    }
	}
	}, []);

	if (
		backPath.includes(location.pathname) ||
		location.pathname.startsWith("/album/detail") ||
		location.pathname.startsWith("/feed/") ||
		location.pathname.startsWith("/chat/") ||
		(location.pathname.startsWith("/group/") && !location.pathname.includes('attend') && !location.pathname.includes('list')) ||
		location.pathname.startsWith("/imagegroup")
	)
		return <BackHeader />;
	else if (
		basicPath.includes(location.pathname) ||
		(location.pathname.startsWith("/group/") && location.pathname.includes('list'))
	)
		return <LogoHeader />;
	else if (location.pathname.startsWith("/feed/img/")) return <SaveHeader />;
	else if (
		albumPath.includes(location.pathname) || 
		location.pathname.startsWith("/album") ||
		 location.pathname.startsWith("/trash")
		) 
		return <AlbumHeader />;

	return(
		<Toaster
			position="top-center"
			reverseOrder={false}
		/>
	)
};

export default Header;