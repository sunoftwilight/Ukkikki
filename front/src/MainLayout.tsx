import { Outlet } from "react-router-dom";
import Header from "./pages/Header";
import Hamburger from "./components/commons/Hamburger";
import InviteModal from "./components/commons/InviteModal";
import GroupMemberModal from "./components/GroupMain/GroupMemberModal";

// 헤더가 필요한 곳에 대한 설정
function App() {
	return (
    <div className="w-screen h-screen">
      <div className="fixed top-0 w-full h-14 z-10">
        <Header />
      </div>

      <div className="fixed top-14 w-full h-[calc(100%-56px)] overflow-scroll scrollbar-hide">
        <Outlet />
        <Hamburger />
        <InviteModal />
        <GroupMemberModal />
      </div>
    </div>
	);
}

export default App;
