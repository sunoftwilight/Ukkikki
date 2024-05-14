import React, { useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion"
import newFolder from "@/assets/Album/newFolder.png"
import { folderStore } from "../../stores/ModalStore";
import ModalBackground from "../@commons/ModalBackground";
import { useStore } from "zustand";
import { copyFiles, getDirStructure, moveFiles } from "../../api/directory";
import { childDirItem } from "../../types/AlbumType";
import { currentDirStore, selectModeStore, selectStore, updateAlbumStore } from "../../stores/AlbumStore";
import { albumDoneStore } from "../../stores/HeaderStateStore";

const FolderModal: React.FC = () => {
  const btnStyle = "w-full h-full flex justify-center items-center rounded-xl font-pre-B text-white text-2xl"
  const { folderOpen, folderMode, setFolderOpen } = folderStore()
  const { currentDirId } = useStore(currentDirStore)
  const { selectListForPk, setSelectList, setSelectListForPk } = useStore(selectStore)
  const [folderList, setFolderList] = useState<childDirItem[]>([])
  const [selectDir, setSelectDir] = useState<string>('')
  const { setNeedUpdate } = useStore(updateAlbumStore)
  const { setSelectMode } = selectModeStore()
  const { setIsDone } = albumDoneStore()

  useEffect(() => {
    getDirStructure(
      currentDirId,
      (res) => {
        setFolderList(res.data.data)
      },
      (err) => { console.error(err) }
    )
  }, [currentDirId])
  
  const moveHandler = () => {
    moveFiles(
      currentDirId,
      {
        toDirId: selectDir,
        fileIdList: selectListForPk
      },
      () => {
        doneHandler()
      },
      (err) => { 
        console.error(err) 
        alert('오류가 발생했습니다. 다시 시도하십시오.')
      }
    )
  }
  
  const copyHandler = () => {
    copyFiles(
      currentDirId,
      {
        toDirId: selectDir,
        fileIdList: selectListForPk
      },
      () => {
        doneHandler()
      },
      (err) => {
        console.error(err)
        alert('오류가 발생했습니다. 다시 시도하십시오.')
      }
    )
  }

  const doneHandler = () => {
    setFolderOpen('')
    setSelectList(-1, false)
    setSelectListForPk('-1', false)
    setNeedUpdate()
    setSelectDir('')

    setTimeout(() => {
      setIsDone()
      setSelectMode()
    }, 100)
  }

  return (
    <AnimatePresence>
      { folderOpen &&
        <motion.div 
          className={`fixed top-0 start-0 h-screen w-screen z-30 flex justify-end`}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
        >
          <div className="z-10" onClick={() => setFolderOpen('')}><ModalBackground /></div>
          
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
                      paddingLeft: (item.depth === 0 || item.depth === 1) ? '8px' : `${item.depth * 20}px`, 
                      marginTop: idx === 0  ? '0px' : (item.depth === 0 || item.depth === 1) ? '20px' : '0px', 
                      backgroundColor: item.pk === selectDir ? '#88C6FF' : (item.depth === 0 || item.depth === 1) ? '#FFFFFF' : '#F2F2F2',
                    }}
                  > 
                    <div className="w-6 h-6 rounded-lg flex justify-center items-center" style={{ backgroundColor: item.pk === selectDir ? '#FFFFFF' : (item.depth % 2 || item.depth === 0) ? '#007BFF' : '#88C6FF'  }} />
                    <div className="truncate font-pre-M text-black text-lg">{item.depth === 0 ? '전체 앨범' : item.name}</div>
                  </div>
                ))}
              </div>
              
              <div className="flex gap-3 absolute bottom-4 w-[calc(100%-32px)] h-[50px]">
                <div onClick={() => doneHandler()} className={`${btnStyle} bg-disabled-gray`}>취소</div>
                <div onClick={() => {folderMode === 'move' ? moveHandler() : copyHandler()}} className={`${btnStyle} bg-main-blue`}>{folderMode === 'move' ? '이동' : '복사'}</div>
              </div>
            </div>
          </div>
        </motion.div>
      }  
    </AnimatePresence>
  )
};

export default FolderModal;