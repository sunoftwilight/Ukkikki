import React, { useState } from "react";
import trash from "@/assets/Trash/trash.png"
import restore from "@/assets/Trash/restore.png"
import { AnimatePresence } from "framer-motion";
import Modal from "../@commons/Modal";
import { delTrashBin, restoreTrashBin } from "../../api/trash-bin";
import { useParams } from "react-router-dom";
import { useStore } from "zustand";
import { selectTrashStore } from "../../stores/TrashStore";
import { userStore } from "../../stores/UserStore";
import { updateTrashStore } from "../../stores/TrashStore";
import { albumDoneStore } from "../../stores/HeaderStateStore";
import { selectModeStore } from "../../stores/AlbumStore";

const TrashOptions: React.FC = () => {
  const optionStyle = "flex rounded-[10px] w-full h-[30px] items-center px-3 gap-3 font-pre-R text-black text-sm bg-white/70"
  const [isDelete, setIsDelete] = useState(false)
  const [isRestore, setIsRestore] = useState(false)
  const { selectTrash, setSelectTrash } = useStore(selectTrashStore)
  const { groupPk } = useParams();
  const { groupKey } = useStore(userStore);
  const { setNeedUpdate } = useStore(updateTrashStore)
  const { setSelectMode } = selectModeStore()
  const { setIsDone } = albumDoneStore()

  const openHandler = (mode: string) => {
    if (mode === 'delete') {
      setIsDelete(true)
    } else if (mode === 'restore') {
      setIsRestore(true)
    }
  }

  const delHandler = async () => {
    await delTrashBin(
      Number(groupPk),
      {
        sseKey: groupKey[Number(groupPk)],
        trashIdList: selectTrash
      },
      () => {
        setIsDelete(false)
        doneHandler()
      },
      (err) => { console.error(err) }
    )
  }

  const restoreHandler = async () => {
    await restoreTrashBin(
      Number(groupPk),
      {
        sseKey: groupKey[Number(groupPk)],
        trashIdList: selectTrash
      },
      () => {
        setIsRestore(false)
        doneHandler()
      },
      (err) => { console.error(err) }
    )
  }

  const doneHandler = () => {
    setNeedUpdate()
    setSelectTrash('-1', false)
    setIsDone()
    setSelectMode()
  }

  return (
    <AnimatePresence>
      { isDelete && (
        <Modal
          key='isDelete'
          modalItems={{ title: '사진을 삭제합니다', content: '삭제된 사진은 복구 불가합니다', modalType: 'warn', btn: 2 }}
          onSubmitBtnClick={() => delHandler()}
          onCancelBtnClick={() => setIsDelete(false)}
        />
      )}
      { isRestore && (
        <Modal
          key='isRestore'
          modalItems={{ content: '선택한 사진/폴더를 복구합니다', modalType: 'txtOnly', btn: 2 }}
          onSubmitBtnClick={() => restoreHandler()}
          onCancelBtnClick={() => setIsRestore(false)}
        />
      )}
      <div className="flex flex-col px-2 py-[10px] gap-[5px] fixed top-14 right-4 w-40 bg-zinc bg-opacity-30 rounded-xl shadow-inner backdrop-blur-[50px]">
        <div className={`${optionStyle}`} onClick={() => openHandler('delete')} >
          <img src={trash} className="w-4" />
          완전 삭제
        </div>

        <div className={`${optionStyle}`} onClick={() => openHandler('restore')} >
          <img src={restore} className="w-4" />
          복구
        </div>
      </div>
    </AnimatePresence>
  )
};

export default TrashOptions;