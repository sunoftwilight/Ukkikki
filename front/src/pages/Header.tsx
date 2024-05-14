import React, { useEffect } from "react";
import { useLocation } from "react-router-dom";
import AlbumHeader from "../components/Header/AlbumHeader";
import BackHeader from "../components/Header/BackHeader";
import LogoHeader from "../components/Header/LogoHeader";
import WriteHeader from "../components/Header/WriteHeader";
import SaveHeader from "../components/Header/SaveHeader";
import { EventSourcePolyfill } from "event-source-polyfill";

const Header: React.FC = () => {
	const location = useLocation();

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
			console.log(obj)
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
		
				sse.addEventListener("CHECK", (event: any) => {
					const e = event as MessageEvent; // 이벤트 타입을 MessageEvent로 캐스팅
					console.log("connect event data: ", e.data); // 이제 e.data를 안전하게 사용할 수 있습니다.
				});
		
				sse.addEventListener("COMMENT", (event: any) => {
					const e = event as MessageEvent; // 이벤트 타입을 MessageEvent로 캐스팅
					console.log("connect event data: ", e.data); // 이제 e.data를 안전하게 사용할 수 있습니다.
				});
		
				return () => sse.close(); // 컴포넌트 언마운트 시 EventSource 연결을 닫습니다.
    }
	}
	}, []);

	if (
		backPath.includes(location.pathname) ||
		location.pathname.startsWith("/album/detail") ||
		location.pathname.startsWith("/feed/") ||
		(location.pathname.startsWith("/group/") && !location.pathname.includes('attend'))
	)
		return <BackHeader />;
	else if (
		basicPath.includes(location.pathname)
		
	)
		return <LogoHeader />;
	else if (location.pathname.startsWith("/feed/img/")) return <SaveHeader />;
	else if (albumPath.includes(location.pathname) || location.pathname.startsWith("/album")) return <AlbumHeader />;
	else if (location.pathname === "/write") return <WriteHeader />;
};

export default Header;