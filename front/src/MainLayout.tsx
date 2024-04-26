import { Outlet } from 'react-router-dom';
import Header from './components/commons/Header';

// 헤더가 필요한 곳에 대한 설정
function App() {
  return (
    <>
      <Header/>
      <div className='fixed top-14 w-full h-full overflow-scroll scrollbar-hide'>
        <Outlet />
      </div>
    </>
  );
}

export default App;
