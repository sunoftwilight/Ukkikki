import React, { useState } from "react";
import { folderStore } from "../../stores/ModalStore";
import { albumDoneStore } from "../../stores/HeaderStateStore";
import { selectModeStore } from "../../stores/AlbumStore";
import { AnimatePresence } from "framer-motion";
import download from "@/assets/Header/AlbumSelectOptions/download.png";
import move from "@/assets/Header/AlbumSelectOptions/move.png";
import copy from "@/assets/Header/AlbumSelectOptions/copy.png";
import trash from "@/assets/Header/AlbumEditOptions/trash.png";
import edit from "@/assets/Header/AlbumEditOptions/edit.png";
import Modal from "../@commons/Modal";
import { multiDownloadFile } from "../../api/file";
import { useStore } from "zustand";
import { prefixStore } from "../../stores/AlbumStore";

const AlbumSelectOptions: React.FC = () => {
  const optionStyle = "flex rounded-[10px] w-full h-[30px] items-center px-3 gap-3 font-pre-R text-black text-sm bg-white/70"
  
  const [isPrefixOpen, setIsPrefixOpen] = useState(false)
  const [isDownDone, setIsDownDone] = useState(false)
  const { setFolderOpen } = folderStore()
  const { prefix } = useStore(prefixStore)

  const { setSelectMode } = selectModeStore()
  const { setIsDone } = albumDoneStore()

    
  
  const openHandler = (mode: string) => {
    if (mode === 'folder') {
      setFolderOpen()
    } else if (mode === 'down') {
      setIsPrefixOpen(true)
    }
  }

  const prefixHandler = async () => {
    await multiDownloadFile(
      'mykey',
      {  
        fileIdList: [1, 2],
        prefix: prefix,
      },
      (res) => { 
        const url = window.URL.createObjectURL(new Blob([res.data]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `${prefix}.zip`)
        document.body.appendChild(link)
        link.click()
        doneHandler()
      },
      (err) => { 
        console.error(err)
        alert('오류가 발생했습니다. 다시 시도하십시오.')
      },
    )
  }
      
  const doneHandler = () => {
    setIsPrefixOpen(false)
    setIsDownDone(true)
    setTimeout(() => {
      setIsDone()
      setSelectMode()
    }, 2000)
  }

  return (
    <AnimatePresence>
      { isPrefixOpen && (
        <Modal 
          key='isPrefixOpen'
          modalItems={{ title: '파일명 수정', content: '', modalType: 'input', btn: 2 }}
          onSubmitBtnClick={() => prefixHandler()}
          onCancelBtnClick={() => setIsPrefixOpen(false)}
        />
      )}

      { isDownDone && (
          <Modal
            key='isDownDone'
            modalItems={{ content: '다운로드가 완료되었습니다.', modalType: 'done', btn: 1 }}
            onSubmitBtnClick={() => setIsDownDone(false)}
          />
        )}

      {/* { isLoading && 
        <Modal modalItems={{ content: '다운로드 진행 중입니다', modalType: 'ing', btn: 0 }} />
      } */}

      <div className="flex flex-col px-2 py-[10px] gap-[5px] fixed top-14 right-4 w-40 h-[186px] bg-zinc bg-opacity-30 rounded-xl shadow-inner backdrop-blur-[50px]">
        <div className={`${optionStyle}`} onClick={() => openHandler('down')}>
          <img src={download} className="w-4" />
          다운로드
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('folder')}>
          <img src={move} className="w-4" />
          이동
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('folder')}>
          <img src={copy} className="w-4" />
          복제
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('')}>
          <img src={trash} className="w-4" />
          삭제
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('')}>
          <img src={edit} className="w-4" />
          게시글 작성
        </div>
      </div>
    </AnimatePresence>
  )
};

export default AlbumSelectOptions;