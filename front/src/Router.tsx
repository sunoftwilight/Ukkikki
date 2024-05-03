import { Routes, Route  } from 'react-router-dom';
import { useStore } from 'zustand';
import { DetailImgStore } from './stores/DetailImgStore';
import MainLayout from './MainLayout'
import Main from './pages/Main';
import Login from './pages/Login';
import Camera from './pages/Camera';
import GroupMain from './pages/GroupMain';
import GroupCreate from './pages/GroupCreate';
import MyPage from './pages/MyPage';
import Setting from './pages/Setting';
import GroupList from './pages/GroupList';
import Album from './pages/Album';
import Feed from './pages/Feed';
import DetailImg from './pages/DetailImg';
import Write from './pages/Write';
import Chatting from './pages/Chatting';
import ArticleDetail from './pages/ArticleDetail';

export default function Router() {
  const { currentImg, currentUrl } = useStore(DetailImgStore)

  return (
    <Routes location={location} key={location.pathname}>
      {/* 헤더 & 네브를 넣을 페이지 */}
      <Route element={<MainLayout />}>
        <Route path="/" element={<Main />} />
        <Route path="/group" element={<GroupMain />} />
        <Route path="/grouplist" element={<GroupList />} />
        <Route path="/groupcreate" element={<GroupCreate />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/setting" element={<Setting />} />
        <Route path="/album" element={<Album />} />
        <Route path={`/album/${currentImg}`} element={<DetailImg url={currentUrl} />} />
        <Route path="/feed" element={<Feed />} />
        <Route path="/feed/:pk" element={<ArticleDetail />} />
        <Route path="/write" element={<Write />} />
        <Route path="/chat" element={<Chatting />} />
      </Route>

      {/* 헤더 & 네브가 필요 없는 페이지 */}
      <Route path="/login" element={<Login />} />
      <Route path="/camera" element={<Camera />} />
    </Routes>
  );
}
