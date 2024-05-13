// import React, { useEffect } from "react";
import React from "react";
import { motion, AnimatePresence } from "framer-motion"
import newFolder from "@/assets/Album/newFolder.png"
import { folderStore } from "../../stores/ModalStore";
import ModalBackground from "../@commons/ModalBackground";
import FolderItem from "./FolderItem";
// import { getPartyDetail } from "../../api/party";

const parentFolderList = [
  {depth: 0, name: '나는 부모폴더'},
  {depth: 0, name: '나는 이런폴더'},
  {depth: 0, name: '나는 폴더폴더'},
  {depth: 0, name: '나는 그냥폴더'},
  {depth: 0, name: '나는 하위폴더'},
  {depth: 0, name: '나는 하위폴더'},
  {depth: 0, name: '나는 하위폴더'},
  {depth: 0, name: '나는 하위폴더'},
  {depth: 0, name: '나는 하위폴더'},
  {depth: 0, name: '나는 하위폴더'},
  {depth: 0, name: '나는 하위폴더'},
  {depth: 0, name: '나는 하위폴더'},
  {depth: 0, name: '나는 하위폴더'},
]

const FolderModal: React.FC = () => {
  const btnStyle = "w-full h-full flex justify-center items-center rounded-xl font-pre-B text-white text-2xl"
  const { folderOpen, setFolderOpen } = folderStore()

  // useEffect(() => {
  //   getPartyDetail(
  //     1,
  //     (res) => {
  //       console.log(res.data)
  //     },
  //     (err) => { console.error(err) }
  //   )
  // }, [])

  return (
    <AnimatePresence>
      { folderOpen &&
        <motion.div 
          className={`fixed top-0 start-0 h-screen w-screen z-30 flex justify-end`}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
        >
          <div className="z-10" onClick={() => setFolderOpen()}><ModalBackground /></div>
          
          <div className="w-full h-full z-20 flex items-center justify-center">
            <div className="relative z-20 w-[320px] h-[540px] p-4 bg-white rounded-[15px] flex flex-col">
              <div className="flex justify-between items-center font-pre-B text-black text-2xl">
                폴더선택
                <img src={newFolder} className="w-[27px]" />
              </div>
              <div className="absolute top-[64px] bottom-[82px] flex flex-col overflow-scroll scrollbar-hide">
                { parentFolderList.map((item, idx) => (
                  <FolderItem key={idx} folder={item} />
                ))}
              </div>
              
              <div className="flex gap-3 absolute bottom-4 w-[calc(100%-32px)] h-[50px]">
                <div onClick={() => setFolderOpen()} className={`${btnStyle} bg-disabled-gray`}>취소</div>
                <div onClick={() => setFolderOpen()} className={`${btnStyle} bg-main-blue`}>이동</div>
              </div>
            </div>
          </div>
        </motion.div>
      }  
    </AnimatePresence>
  )
};

export default FolderModal;