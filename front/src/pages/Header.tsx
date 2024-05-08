import React, { useEffect } from "react";
import { useLocation } from "react-router-dom";
import AlbumHeader from "../components/Header/AlbumHeader";
import BackHeader from "../components/Header/BackHeader";
import LogoHeader from "../components/Header/LogoHeader";
import WriteHeader from "../components/Header/WriteHeader";
import SaveHeader from "../components/Header/SaveHeader";
import { connectAlarm } from "../api/alarm";

const Header: React.FC = () => {
	const location = useLocation();

	const groupBackPath = [
		"/grouplist",
		"/groupcreate",
		"/createdone",
		"/groupconfig",
		"/groupenv",
		"/groupprofile",
		"/groupuser",
		"/groupuserdetail",
		"/groupban",
		"/groupinfo",
		"/grouppass",
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
	}, []);

  useEffect(() => {
    localStorage.setItem('accessToken', 'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsImlkIjo1MiwidXNlcm5hbWUiOiLshLHqt5wiLCJwcm92aWRlcklkIjoia2FrYW8gMzQ1ODY4OTQzNyIsImlhdCI6MTcxNTEzMzkzNywiZXhwIjoxNzE1OTk3OTM3fQ.JZUCLuNRLK71yot5hBo13cfVkvKnEHDpZIebJUqX6dc')
  }, [])

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
