import { Routes, Route  } from 'react-router-dom';
import MainLayout from './MainLayout'
import Main from './pages/Main';
import Login from './pages/Login';
import Camera from './pages/Camera';

export default function Router() {
  return (
    <Routes>
      {/* 헤더 & 네브를 넣을 페이지 */}
      <Route element={<MainLayout />}>
        <Route path="/" element={<Main />} />
      </Route>

      {/* 헤더 & 네브가 필요 없는 페이지 */}
      <Route path="/login" element={<Login />} />
      <Route path="/camera" element={<Camera />} />
    </Routes>
  );
}
