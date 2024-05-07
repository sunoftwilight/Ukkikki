import React from "react";
import ModalBackground from "./ModalBackground";
import { inviteStore } from "../../stores/ModalStore";
import clipboard from '@/assets/Modal/clipboard.png'
import close from '@/assets/Hamburger/close.png'
import toast, { Toaster } from "react-hot-toast";
import { motion, AnimatePresence } from "framer-motion"

const InviteModal: React.FC = () => {
  const { inviteOpen, setInviteOpen } = inviteStore()

  const inviteInfo = {
    url: 'https://www.sj10dssafy202.com/secretkey',
    code: 'OOb28K'
  }

  const clipTxt = `우리의 추억에 당신을 초대합니다! \n ${inviteInfo.url} \n 참여 코드: ${inviteInfo.code}`

  const copyUrlHandler = async () => {
    try {
      navigator.clipboard.writeText(clipTxt)
      toast.success('클립보드에 복사되었습니다!')
    } catch (err) {
      toast.error('다시 시도해주세요')
    }
  }

	return (
    <>
      <Toaster />
      <AnimatePresence>
        { inviteOpen &&
          <motion.div 
            className={`fixed top-0 start-0 h-screen w-screen flex justify-end`}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
          >
            <div onClick={() => setInviteOpen()}><ModalBackground /></div>

            <div className="flex justify-center items-center w-full h-full">
              <div className="z-20 w-[300px] h-[174px] bg-white rounded-[15px] px-4 py-5 flex flex-col gap-y-5">
                <div className="flex justify-end items-center gap-20">
                  <div className="text-center font-pre-SB text-black text-xl">초대 링크</div>
                  <img src={close} onClick={() => setInviteOpen()} />
                </div>

                <div className="flex flex-col gap-4">
                  {/* 초대 링크 복사 */}
                  <div className="flex">
                    <div className="w-[calc(100%-34px)] h-[34px] border-disabled-gray border-[0.5px] rounded-tl-[10px] rounded-bl-[10px] flex items-center px-2 font-pre-L text-black text-xs">
                      <div className="truncate">{inviteInfo.url}</div>
                    </div>

                    <button 
                      onClick={() => copyUrlHandler()} 
                      className="w-[34px] h-[34px] bg-disabled-gray rounded-tr-[10px] rounded-br-[10px] flex justify-center items-center active:scale-125 transition-transform"
                    >
                      <img src={clipboard} className="w-6 h-6" />
                    </button>
                  </div>

                  {/* 가입 코드  */}
                  <div className="flex gap-3 justify-end items-center">
                    <div className="text-main-blue text-base font-pre-SB">가입 코드</div>
                    <div className="flex items-center justify-center w-20 h-8 rounded-[10px] bg-main-blue text-white text-base font-pre-M">{inviteInfo.code}</div>
                  </div>
                </div>
              </div>
            </div>
          </motion.div>
        }
      </AnimatePresence>
    </>
  )
};

export default InviteModal;