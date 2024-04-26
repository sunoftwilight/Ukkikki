import { Outlet } from 'react-router-dom';
import Header from './components/commons/Header';
import ModalBackground from './components/commons/modals/ModalBackground';

// 헤더가 필요한 곳에 대한 설정
function App() {
  return (
    <>
      <Header/>
      <ModalBackground/>
      <div className='fixed top-14 w-full h-full overflow-scroll scrollbar-hide bg-main-blue'>
        <Outlet />
      </div>
    </>
  );
}

export default App;
