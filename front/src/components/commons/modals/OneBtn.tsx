import React from "react";
import { ModalProps } from "../../../types/Modal";
import warn from '@/assets/modal/warn.png'
import done from '@/assets/modal/done.png'

const OneBtnModal: React.FC<ModalProps> = ({modalItems}) => {
  const containClass: string = 'flex justify-center items-center w-full'
  const contentClass: string = 'flex font-pre-R text-lg w-full items-center'
  
  const modalType = modalItems.modalType

  const contentHandler = () => {
    switch (modalType) {
      case "txtOnly" :
        return (
          <div className={containClass}>
            <div className={`${contentClass} justify-center h-[70px]`}>{modalItems.content}</div>
          </div>
        )
      
      case "warn" :
        return (
          <div className={`${containClass} gap-5`}>
            <img src={warn} />
            <div className={`${contentClass} h-[70px]`}>{modalItems.content}</div>
          </div>
        )
      
      case "done" :
        return (
          <div className={`${containClass} flex-col gap-2`}>
            <img src={done} />
            <div className={`${contentClass} justify-center`}>{modalItems.content}</div>
          </div>
        )
    }
  }

  return (
    <div className="z-20 w-[300px] h-[174px] bg-white rounded-[15px] p-6 flex flex-wrap content-between">
      {contentHandler()}

      <div className="flex w-full justify-center">
        <button className="w-[130px] h-[35px] font-gtr-B text-lg bg-main-blue text-white rounded-[15px]">확인</button>
      </div>
    </div>
  )
};

export default OneBtnModal;