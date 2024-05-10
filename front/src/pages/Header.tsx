import React, { useEffect } from "react";
import { useLocation } from "react-router-dom";
import AlbumHeader from "../components/Header/AlbumHeader";
import BackHeader from "../components/Header/BackHeader";
import LogoHeader from "../components/Header/LogoHeader";
import WriteHeader from "../components/Header/WriteHeader";
import SaveHeader from "../components/Header/SaveHeader";
import { connectAlarm } from "../api/alarm";
import { EventSourcePolyfill } from 'event-source-polyfill';

const Header: React.FC = () => {
	const location = useLocation();	

	const groupBackPath = [
		"/grouplist", "/groupcreate", "/createdone", "/groupconfig",
		"/groupenv", "/groupprofile", "/groupuser", "/groupuserdetail",
		"/groupban", "/groupinfo", "/grouppass"
	];
	const basicPath = ["/", "/group", "/mypage", "/groupattend"];
	const backPath = ["/setting", "/feed", "/chat", ...groupBackPath];
	const albumPath = ["/album", "/trash"];

	useEffect(() => {
		connectAlarm(
			(res) => {
				console.log("연결 성공", res);
			},
			(err) => {
				console.error("연결 실패", err);
			},
		);
		const eventSource = new EventSourcePolyfill('/api/alarm/sub',{
			headers: {
				'authorization': `Bearer eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsImlkIjoxLCJ1c2VybmFtZSI6IuyEseq3nCIsInByb3ZpZGVySWQiOiJrYWthbyAzNDU4Njg5NDM3IiwiaWF0IjoxNzE1MjM1ODk5LCJleHAiOjE3MTYwOTk4OTl9.mdm4F9ymRYeyAKJcds4sl1_j_g-5oRfSMkQZJBcNVHk`,
			},
		});
	
		eventSource.addEventListener('connect', () => {
			//'new_thread' 이벤트가 오면 할 동작
			console.log('연결')
		});
	
		eventSource.onerror = () => {
			//에러 발생시 할 동작
			console.log('에러')
			eventSource.close(); //연결 끊기
		};
		
		return () => {
			eventSource.close();
		};
	}, []);

	if (basicPath.includes(location.pathname)) return <LogoHeader />;
	else if (location.pathname.startsWith("/feed/img/")) return <SaveHeader />;
	else if (
		backPath.includes(location.pathname) ||
		location.pathname.startsWith("/album/") ||
		location.pathname.startsWith("/feed/")
	)
		return <BackHeader />;
	else if (albumPath.includes(location.pathname)) return <AlbumHeader />;
	else if (location.pathname === "/write") return <WriteHeader />;
};

export default Header;



// "id:1_1715326312683
// event:CHECK
// data:{
// 	"partyId":null,
// 	"contentsId":null,
// 	"targetId":null,
// 	"alarmType":"CHECK",
// 	"content":null,
// 	"createDate":null,
// 	"identifier":null,
// 	"read":false
// }
// "
