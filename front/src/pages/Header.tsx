import React, { useEffect } from "react";
import { useLocation } from "react-router-dom";
import AlbumHeader from "../components/Header/AlbumHeader";
import BackHeader from "../components/Header/BackHeader";
import LogoHeader from "../components/Header/LogoHeader";
import WriteHeader from "../components/Header/WriteHeader";
import SaveHeader from "../components/Header/SaveHeader";
import { connectAlarm } from "../api/alarm";

const Header: React.FC = () => {
  const location = useLocation()

  const groupBackPath = ['/grouplist',
                         '/groupcreate',
                         '/createdone',
                         '/groupconfig',
                         '/groupenv',
                         '/groupprofile',
                         '/groupuser',
                         '/groupuserdetail',
                         '/groupban',
                         '/groupinfo',
                         '/grouppass']

  const basicPath = ['/', '/group', '/mypage', '/groupattend']
  const backPath = ['/setting', '/feed', '/chat', ...groupBackPath]
  const albumPath = ['/album', '/trash']

  useEffect(() => {
    connectAlarm(
      (res) => { console.log('연결 성공', res)},
      (err) => { console.error('연결 실패', err)}
    )
  }, [])

  if (basicPath.includes(location.pathname)) return <LogoHeader />
  else if (location.pathname.startsWith('/feed/img/')) return <SaveHeader />
  else if (backPath.includes(location.pathname) || location.pathname.startsWith('/album/') || location.pathname.startsWith('/feed/')) return <BackHeader />
  else if (albumPath.includes(location.pathname)) return <AlbumHeader />
  else if (location.pathname === '/write') return <WriteHeader />
};

export default Header;