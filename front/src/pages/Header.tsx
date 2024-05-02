import React from "react";
import { useLocation } from "react-router-dom";
import AlbumHeader from "../components/Header/AlbumHeader";
import BackHeader from "../components/Header/BackHeader";
import LogoHeader from "../components/Header/LogoHeader";
import WriteHeader from "../components/Header/WriteHeader";

const Header: React.FC = () => {
  const location = useLocation()

  const basicPath = ['/', '/group', '/mypage',]
  const backPath = ['/grouplist', '/setting', '/feed','/groupcreate', ]
  const albumPath = ['/album']

  if (basicPath.includes(location.pathname)) return <LogoHeader />
  else if (backPath.includes(location.pathname) || location.pathname.startsWith('/album/')) return <BackHeader />
  else if (albumPath.includes(location.pathname)) return <AlbumHeader />
  else if (location.pathname === '/write') return <WriteHeader />
};

export default Header;