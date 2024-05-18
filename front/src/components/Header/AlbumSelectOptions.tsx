import React, { useState } from "react";
import { folderStore } from "../../stores/ModalStore";
import { albumDoneStore } from "../../stores/HeaderStateStore";
import { currentDirStore, selectModeStore, updateAlbumStore } from "../../stores/AlbumStore";
import { AnimatePresence } from "framer-motion";
import download from "@/assets/Header/AlbumSelectOptions/download.png";
import move from "@/assets/Header/AlbumSelectOptions/move.png";
import copy from "@/assets/Header/AlbumSelectOptions/copy.png";
import trash from "@/assets/Header/AlbumEditOptions/trash.png";
import edit from "@/assets/Header/AlbumEditOptions/edit.png";
import Modal from "../@commons/Modal";
import { multiDownloadFile } from "../../api/file";
import { useStore } from "zustand";
import { prefixStore, selectStore } from "../../stores/AlbumStore";
import { delFiles } from "../../api/directory";
import { useParams } from "react-router-dom";
import { userStore } from "../../stores/UserStore";

const AlbumSelectOptions: React.FC = () => {
  const optionStyle = "flex rounded-[10px] w-full h-[30px] items-center px-3 gap-3 font-pre-R text-black text-sm bg-white/70"
  const { groupKey } = useStore(userStore);
  const { groupPk } = useParams();

  const [isPrefixOpen, setIsPrefixOpen] = useState(false)
  const [isIng, setIsIng] = useState(false)
  const [isDownDone, setIsDownDone] = useState(false)
  const [isDelete, setIsDelete] = useState(false)

  const { setFolderOpen } = folderStore()
  const { prefix } = useStore(prefixStore)
  const { setSelectMode } = selectModeStore()
  const { setIsDone } = albumDoneStore()
  const { currentDirId } = useStore(currentDirStore)
  const { setNeedUpdate } = useStore(updateAlbumStore)

  const { selectList, setSelectList, selectListForPk, setSelectListForPk } = useStore(selectStore)
  
  const openHandler = (mode: string) => {
    if (mode === 'move') {
      setFolderOpen('move')
    } else if (mode === 'copy') {
      setFolderOpen('copy')
    } else if (mode === 'down') {
      setIsPrefixOpen(true)
    } else if (mode === 'delete') {
      setIsDelete(true)
    }
  }

  const prefixHandler = async () => {
    setIsPrefixOpen(false)
    setIsIng(true)
    await multiDownloadFile(
      groupKey[Number(groupPk)],
      // 'ㅊ',
      {  
        fileIdList: selectList,
        prefix: prefix,
      },
      (res) => { 
        const url = window.URL.createObjectURL(new Blob([res.data]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `${prefix}.zip`)
        document.body.appendChild(link)
        link.click()
        doneHandler('down')
        setNeedUpdate()
      },
      (err) => { 
        setIsPrefixOpen(true)
        setIsIng(false)

        console.error(err)
        alert('오류가 발생했습니다. 다시 시도하십시오.')
      },
    )
  }

  const delFileHandler = async () => {
    await delFiles(
      currentDirId,
      { 
        data: {
          sseKey: groupKey[Number(groupPk)],
          fileIdList: selectListForPk
        }
      },
      (res) => {
        doneHandler('del')
        setNeedUpdate()
      },
      (err) => { 
        console.error(err)
        alert('오류가 발생했습니다. 다시 시도하십시오.')
      }
    )
  }
      
  const doneHandler = (mode: string) => {
    if (mode === 'down') {
      setIsDownDone(true)
    }
    setIsIng(false)
    setIsDelete(false)
    // 셀렉 리스트 초기화
    setSelectList(-1, false)
    setSelectListForPk('-1', false)
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

      { isIng && (
        <Modal
          key='isIng'
          modalItems={{ content: '다운로드 진행 중입니다', modalType: 'ing', btn: 0 }}
          onSubmitBtnClick={() => prefixHandler()}
          onCancelBtnClick={() => setIsPrefixOpen(false)}
        />
      )}

      { isDelete && (
        <Modal
          key='isDelete'
          modalItems={{ title: '사진을 삭제합니다', content: '삭제된 사진은 휴지통에 2주간 보관되며 기간 내 복구 가능합니다', modalType: 'warn', btn: 2 }}
          onSubmitBtnClick={() => delFileHandler()}
          onCancelBtnClick={() => setIsDelete(false)}
        />
      )}

      <div className="flex flex-col px-2 py-[10px] gap-[5px] fixed top-14 right-4 w-40 h-[186px] bg-zinc bg-opacity-30 rounded-xl shadow-inner backdrop-blur-[50px]">
        <div className={`${optionStyle}`} onClick={() => openHandler('down')}>
          <img src={download} className="w-4" />
          다운로드
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('move')}>
          <img src={move} className="w-4" />
          이동
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('copy')}>
          <img src={copy} className="w-4" />
          복제
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('delete')}>
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