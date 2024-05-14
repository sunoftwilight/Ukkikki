import React, { useEffect, useState } from "react";
// import React from "react";
import { motion, AnimatePresence } from "framer-motion"
import newFolder from "@/assets/Album/newFolder.png"
import { folderStore } from "../../stores/ModalStore";
import ModalBackground from "../@commons/ModalBackground";
import FolderItem from "./FolderItem";
import { useStore } from "zustand";
import { currentGroupStore } from "../../stores/GroupStore";
import { getPartyDetail } from "../../api/party";
import { getChildDir, getDirStructure } from "../../api/directory";
import { childDirItem } from "../../types/AlbumType";
import { currentDirStore } from "../../stores/AlbumStore";

const parentFolderList = [
  {depth: 0, name: '나는 폴더1', pk: '1'},
  {depth: 1, name: '나는 폴더1자식', pk: '2'},
  {depth: 0, name: '나는 폴더2', pk: '3'},
  {depth: 1, name: '나는 폴더2자식', pk: '4'},
  {depth: 1, name: '나는 폴더2자식', pk: '5'},
  {depth: 0, name: '나는 폴더3', pk: '6'},
  {depth: 0, name: '나는 폴더4', pk: '7'},
  {depth: 1, name: '나는 폴더4자식', pk: '8'},
  {depth: 2, name: '나는 폴더4자식자식', pk: '9'},
  {depth: 2, name: '나는 폴더4자식자식', pk: '10'},
  {depth: 3, name: '나는 폴더4자식자식자식', pk: '11'},
  {depth: 0, name: '나는 폴더5', pk: '12'},
  {depth: 0, name: '나는 폴더6', pk: '13'},
]

const FolderModal: React.FC = () => {
  const btnStyle = "w-full h-full flex justify-center items-center rounded-xl font-pre-B text-white text-2xl"
  const { folderOpen, setFolderOpen } = folderStore()
  const { currentGroup } = useStore(currentGroupStore)
  // const { currentDirId } = useStore(currentDirStore)
  const [rootDir, setRootDir] = useState('')
  const [folderList, setFolderList] = useState<childDirItem[]>([])
  const [selectDir, setSelectDir] = useState<string>('')

  useEffect(() => {
    getPartyDetail(
      currentGroup,
      (res) => {
        console.log(res.data.data.rootDirId, currentGroup)
        setRootDir(res.data.data.rootDirId)
      },
      (err) => { console.error(err) }
    )
  }, [currentGroup])

  useEffect(() => {
    getDirStructure(
      rootDir,
      // currentDirId,
      (res) => {
        setFolderList(res.data.data)
        console.log(res.data.data)
      },
      (err) => { console.error(err) }
    )
    console.log(rootDir)
  }, [rootDir])

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
              <div className="mb-4 flex justify-between items-center font-pre-B text-black text-2xl">
                폴더선택
                <img src={newFolder} className="w-[27px]" />
              </div>
              <div className="h-[calc(100%-112px)] w-full top-[64px] bottom-[82px] flex flex-col overflow-scroll scrollbar-hide">
                { folderList.map((item, idx) => (
                  <div 
                    onClick={() => setSelectDir(item.pk!)}
                    key={idx} className=" h-[50px] px-2 py-2 flex gap-4 items-center" 
                    style={{ 
                      paddingLeft: item.depth === 0 ? '8px' : `${item.depth * 30}px`, 
                      marginTop: idx === 0 ? '0px' : item.depth === 0 ? '20px' : '0px', 
                      backgroundColor: item.pk === selectDir ? '#88C6FF' : item.depth === 0 ? '#FFFFFF' : '#F2F2F2',
                    }}
                  > 
                    <div className="w-6 h-6 rounded-lg flex justify-center items-center" style={{ backgroundColor: item.pk === selectDir ? '#FFFFFF' : item.depth % 2 ? '#88C6FF' : '#007BFF'  }} />
                    <div className="truncate font-pre-M text-black text-lg">{item.name}</div>
                  </div>
                ))}
              </div>
              
              <div className="flex gap-3 absolute bottom-4 w-[calc(100%-32px)] h-[50px]">
                <div onClick={() => setFolderOpen()} className={`${btnStyle} bg-disabled-gray`}>취소</div>
                <div onClick={() => setFolderOpen()} className={`${btnStyle} bg-main-blue`}>선택</div>
              </div>
            </div>
          </div>
        </motion.div>
      }  
    </AnimatePresence>
  )
};

export default FolderModal;