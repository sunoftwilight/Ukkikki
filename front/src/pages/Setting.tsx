import React from "react";
import logougImg from "@/assets/Setting/logout.png";
import uploadImg from "@/assets/Setting/upload.png";
import startPageImg from "@/assets/Setting/startPage.png";
import { useNavigate } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion"
import Modal from "../components/@commons/Modal";
import { optionStore } from "../stores/OptionStore";
import { useStore } from "zustand";
import { logOut } from "../api/user";

const Setting: React.FC = () => {
  const navi = useNavigate();

  const { startPageOpen, setStartPageOpen } = useStore(optionStore);
  const { logoutOpen, setLogoutOpen } = useStore(optionStore);

	const closeHandler = () => {
		if (startPageOpen) {
			setStartPageOpen();
		} else if (logoutOpen) {
			setLogoutOpen();
		}
	};

  const reqLogOut = async() => {
    await logOut(
      ()=>{
        localStorage.clear();
        navi('/login')
      },
      ()=>{},
    )
  }

  return (
    <AnimatePresence>
      <div className="p-4 w-full h-[calc(100%-48px)] flex flex-col gap-2 relative font-pre-R text-xl">
        <div className="px-4 w-full h-[60px] bg-soft-gray rounded-xl flex items-center" onClick={() => navi('/grouplist')}>
          <img src={uploadImg} className="w-6 h-6 me-4"/>
          <p>업로드 그룹 설정</p>
        </div>
        <div className="px-4 w-full h-[60px] bg-soft-gray rounded-xl flex items-center" onClick={() => {setStartPageOpen()}}>
          <img src={startPageImg} className="w-6 h-4 me-4"/>
          <p>시작 화면 설정</p>
        </div>
        {startPageOpen && (
            <motion.div
              key={'startPageOpt'}
              initial={{ opacity: 0, y: "-88px" }}
              animate={{ opacity: 1, y: "0px" }}
              exit={{ opacity: 0, y: "-88px" }}
              >
              <div className="px-4 py-4 w-full bg-soft-blue rounded-[15px] flex flex-col justify-evenly text-base gap-2">
                <div className="px-2 flex items-center gap-4">
                  <input type="radio" name="startPage" id="camera" />
                  <label htmlFor="camera">카메라</label>
                </div>
                <div className="px-2 flex items-center gap-4">
                  <input type="radio" name="startPage" id="home" />
                  <label htmlFor="home">홈 화면</label>
                </div>
              </div>
            </motion.div>
        )}

        <div 
          className="px-4 w-[calc(100%-32px)] h-[60px] bg-soft-gray absolute bottom-0 rounded-xl flex items-center"
          onClick={() => setLogoutOpen()}>
          <img src={logougImg} className="w-6 h-6 me-4"/>
          <p>로그아웃</p>
        </div>

        {logoutOpen &&(
          <Modal
            modalItems={{content: '로그아웃 하시겠습니까?', modalType:'warn', btn:2 }}
            onSubmitBtnClick = {() => {closeHandler(); reqLogOut();}}
            onCancelBtnClick = {() => {closeHandler();}}/>
        )}

      </div>
    </AnimatePresence>
  )
};

export default Setting;