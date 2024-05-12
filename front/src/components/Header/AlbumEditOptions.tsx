import React, { useState } from "react";
import upload from "@/assets/Header/AlbumEditOptions/upload.png";
import addFolder from "@/assets/Header/AlbumEditOptions/addFolder.png";
import edit from "@/assets/Header/AlbumEditOptions/edit.png";
import trash from "@/assets/Header/AlbumEditOptions/trash.png";
import { albumEditStore } from "../../stores/HeaderStateStore";
import { AnimatePresence } from "framer-motion";
import Modal from "../@commons/Modal";
import { useStore } from "zustand";
import { currentDirStore, prefixStore } from "../../stores/AlbumStore";
import { createDirectory, delDirectory } from "../../api/directory";

const AlbumEditOptions: React.FC = () => {
  const optionStyle = "flex rounded-[10px] w-full h-[30px] items-center px-3 gap-3 font-pre-R text-black text-sm bg-white/70"
  const { setIsEdit } = albumEditStore()
  const { prefix } = useStore(prefixStore)
  const { currentDir, parentDir, setCurrentDir } = useStore(currentDirStore)
  const [isOkOpen, setIsOkOpen] = useState(false)
  const [isNamingOpen, setIsNamingOpen] = useState(false)

  const openHandler = (mode: string) => {
    if (mode === 'delete') {
      setIsOkOpen(true)
    } else if (mode === 'add Folder') {
      setIsNamingOpen(true)
    }
  }

  // 
  const deleteFolderHandler = () => {
    delDirectory(
      currentDir,
      () => {
        setCurrentDir(parentDir)
        doneHandler()
      },
      (err) => {
        console.error(err)
        alert('오류가 발생했습니다. 다시 시도해주세요.')
      }
    )
  }

  const createFolderHandler = () => {
    createDirectory(
      { parentDirId: currentDir, dirName: prefix },
      (res) => {
        console.log(res.data)
        doneHandler()
        setCurrentDir(currentDir)
      },
      (err) => {
        console.error(err)
      }      
    )
  }

  const doneHandler = () => {
    setIsOkOpen(false)
    setIsNamingOpen(false)
    setIsEdit()
  }

  return (
    <AnimatePresence>
      { isOkOpen && 
        <Modal 
        key='isOkOpen'
        modalItems={{ title: '현재 폴더와 사진을 모두 삭제합니다', content: '삭제된 사진은 휴지통에 2주간 보관되며 기간 내 복구 가능합니다', modalType: 'warn', btn: 2 }}
        onSubmitBtnClick={() => deleteFolderHandler()}
        onCancelBtnClick={() => setIsOkOpen(false)}
        />
      }
      { isNamingOpen && 
        <Modal 
        key='isNamingOpen'
        modalItems={{ title: '폴더명', content: '', modalType: 'input', btn: 2 }}
        onSubmitBtnClick={() => createFolderHandler()}
        onCancelBtnClick={() => setIsNamingOpen(false)}
        />
      }
      <div className="flex flex-col px-2 py-[10px] gap-[5px] fixed top-14 right-4 w-40 h-[160px] bg-zinc bg-opacity-30 rounded-xl shadow-inner backdrop-blur-[50px]">
        <div className={`${optionStyle}`} onClick={() => setIsEdit()}>
          <img src={upload} className="w-4" />
          업로드
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('add Folder')}>
          <img src={addFolder} className="w-4" />
          하위 폴더 생성
        </div>

        <div className={`${optionStyle}`} onClick={() => setIsEdit()}>
          <img src={edit} className="w-4" />
          현재 폴더명 수정
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('delete')}>
          <img src={trash} className="w-4" />
          현재 폴더 삭제
        </div>
      </div>
    </AnimatePresence>
  )
};

export default AlbumEditOptions;