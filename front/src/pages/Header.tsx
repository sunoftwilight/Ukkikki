import React from "react";
import { useLocation } from "react-router-dom";
import AlbumHeader from "../components/commons/AlbumHeader";
import BackHeader from "../components/commons/BackHeader";
import LogoHeader from "../components/commons/LogoHeader";

const Header: React.FC = () => {
  const location = useLocation()

  const basicPath = ['/', '/group', '/mypage',]
  const backPath = ['/grouplist', '/setting', '/feed',]
  const albumPath = ['/album']

  if (basicPath.includes(location.pathname)) return <LogoHeader />
  else if (backPath.includes(location.pathname) || location.pathname.startsWith('/album/')) return <BackHeader />
  else if (albumPath.includes(location.pathname)) return <AlbumHeader />
};

export default Header;