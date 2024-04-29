import { Routes, Route  } from 'react-router-dom';
import MainLayout from './MainLayout'
import Main from './pages/Main';
import Login from './pages/Login';
import Camera from './pages/Camera';
import GroupMain from './pages/GroupMain';
import MyPage from './pages/MyPage';
import Setting from './pages/Setting';
import GroupList from './pages/GroupList';
import Album from './pages/Album';

export default function Router() {
  return (
    <Routes>
      {/* 헤더 & 네브를 넣을 페이지 */}
      <Route element={<MainLayout />}>
        <Route path="/" element={<Main />} />
        <Route path="/group" element={<GroupMain />} />
        <Route path="/grouplist" element={<GroupList />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/setting" element={<Setting />} />
        <Route path="/album" element={<Album />} />
      </Route>

      {/* 헤더 & 네브가 필요 없는 페이지 */}
      <Route path="/login" element={<Login />} />
      <Route path="/camera" element={<Camera />} />
    </Routes>
  );
}
