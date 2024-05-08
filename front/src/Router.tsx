import { Routes, Route } from 'react-router-dom';
import { useStore } from 'zustand';
import { DetailImgStore } from './stores/DetailImgStore';
import MainLayout from './MainLayout'
import Main from './pages/Main';
import Login from './pages/Login';
import Camera from './pages/Camera';

import GroupMain from './pages/GroupMain';
import GroupList from './pages/GroupList';
import GroupCreate from './pages/GroupCreate';
import GroupAttendLogin from './pages/GroupAttendLogin';
import GroupAttend from './pages/GroupAttend';
import GroupConfig from './pages/GroupConfig/GroupConfig';
import GroupEnvConfig from './pages/GroupConfig/GroupEnvConfig';
import GroupProfile from './pages/GroupConfig/GroupProfile';
import GroupBan from './pages/GroupConfig/GroupBan';
import GroupInfo from './pages/GroupConfig/GroupInfo';
import GroupUser from './pages/GroupConfig/GroupUser';
import GroupUserDetail from './pages/GroupConfig/GroupUserDetail';
import GroupPass from './pages/GroupConfig/GroupPass';

import MyPage from './pages/MyPage';
import Setting from './pages/Setting';
import Album from './pages/Album';
import Feed from './pages/Feed';
import DetailImg from './pages/DetailImg';
import Write from './pages/Write';
import Chatting from './pages/Chatting';
import ArticleDetail from './pages/ArticleDetail';
import ArticleImg from './pages/ArticleImg';
import Trash from './pages/Trash';

import { useNavigate } from "react-router-dom";
import { useEffect } from 'react';
import { useCookies } from 'react-cookie';
import { userStore } from "./stores/UserStore";

export default function Router() {


  const navi = useNavigate();
  
  const [cookies] = useCookies(['isLogin']);
  const user = useStore(userStore)

  useEffect(() => {
    if (Boolean(cookies.isLogin) && user.accessToken !=='') {
      return;
    }
    else{
      navi('/login')
    }
  }, [cookies.isLogin, user.accessToken])

  const { currentImg, currentUrl } = useStore(DetailImgStore)

  return (
    <Routes location={location} key={location.pathname}>
      {/* 헤더 & 네브를 넣을 페이지 */}
      <Route element={<MainLayout />}> 
        <Route path="/" element={<Main />} />

        <Route path="/group" element={<GroupMain />} />
        <Route path="/grouplist" element={<GroupList />} />
        <Route path="/groupcreate" element={<GroupCreate />} />
        <Route path="/groupattendlogin" element={<GroupAttendLogin />} />
        <Route path="/groupattend" element={<GroupAttend />} />
        <Route path="/groupconfig" element={<GroupConfig />} />
        <Route path="/groupenv" element={<GroupEnvConfig />} />
        <Route path="/groupprofile" element={<GroupProfile />} />
        <Route path="/groupban" element={<GroupBan />} />
        <Route path="/groupinfo" element={<GroupInfo />} />
        <Route path="/groupuser" element={<GroupUser />} />
        <Route path="/groupuserdetail" element={<GroupUserDetail />} />
        <Route path="/grouppass" element={<GroupPass />} />

        <Route path="/mypage" element={<MyPage />} />
        <Route path="/setting" element={<Setting />} />
        <Route path="/album" element={<Album />} />
        <Route path={`/album/${currentImg}`} element={<DetailImg url={currentUrl} />} />
        <Route path="/feed" element={<Feed />} />
        <Route path="/feed/:pk" element={<ArticleDetail />} />
        <Route path="/feed/img/:pk" element={<ArticleImg />} />
        <Route path="/write" element={<Write />} />
        <Route path="/chat" element={<Chatting />} />
        <Route path="/trash" element={<Trash />} />
      </Route>

      {/* 헤더 & 네브가 필요 없는 페이지 */}
      <Route path="/login" element={<Login />} />
      <Route path="/camera" element={<Camera />} />
    </Routes>
  );
}
