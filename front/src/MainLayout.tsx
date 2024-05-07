import { Outlet } from "react-router-dom";
import Header from "./pages/Header";
import Hamburger from "./components/@commons/Hamburger";
import InviteModal from "./components/@commons/InviteModal";
import GroupMemberModal from "./components/GroupMain/GroupMemberModal";
import FolderModal from "./components/Album/FolderModal";

// 헤더가 필요한 곳에 대한 설정
function App() {
	return (
    <div className="w-screen h-screen">
      <Header />

      <div className="fixed top-14 w-full h-[calc(100%-56px)] overflow-scroll scrollbar-hide">
        <Outlet />
        <Hamburger />
        <InviteModal />
        <GroupMemberModal />
        <FolderModal />
      </div>
    </div>
	);
}

export default App;
