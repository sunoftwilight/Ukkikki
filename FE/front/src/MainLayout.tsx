import { Outlet } from "react-router-dom";
import Header from "./pages/Header";
import Hamburger from "./components/@commons/Hamburger";
import InviteModal from "./components/@commons/InviteModal";
import GroupMemberModal from "./components/GroupMain/GroupMemberModal";
import FolderModal from "./components/Album/FolderModal";
import { headerStore } from "./stores/HeaderStateStore";
import { useStore } from "zustand";
import { folderStore } from "./stores/ModalStore";

// 헤더가 필요한 곳에 대한 설정
function App() {
	const { alarmOpen, menuOpen } = useStore(headerStore);
  const { folderOpen } = useStore(folderStore)

	return (
    <div className="w-screen h-screen">
      <Header />

      <div className="fixed top-14 w-full h-[calc(100%-56px)] overflow-scroll scrollbar-hide">
        <Outlet />
      </div>

      { (alarmOpen || menuOpen)  && <Hamburger /> }
      <InviteModal />
      <GroupMemberModal />
      { folderOpen && <FolderModal /> }
    </div>
	);
}

export default App;
