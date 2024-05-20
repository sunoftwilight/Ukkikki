import React from "react";
import ModalBackground from "../@commons/ModalBackground";
import { memberStore } from "../../stores/ModalStore";
import close from '@/assets/Hamburger/close.png'
import { motion, AnimatePresence } from "framer-motion"
import MemeberImg from "./MemberImg";

const GroupMemberModal: React.FC = () => {
  const { members, memberOpen, setMemberOpen } = memberStore()
	return (
    <AnimatePresence>
      { memberOpen &&
        <motion.div 
          className="fixed top-0 start-0 h-screen w-screen flex justify-end"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
        >
        <div onClick={() => setMemberOpen()}><ModalBackground /></div>
        <div className="flex justify-center items-center w-full h-full">
          <div className="z-20 w-[300px] h-96 bg-white rounded-[15px] px-4 py-5 flex flex-col gap-5 overflow-scroll scrollbar-hide">
            
            <div className="flex gap-20 justify-end items-center">
              <div className="font-pre-SB text-xl text-black text-center">그룹 멤버</div>
              <img src={close} onClick={() => setMemberOpen()} />
            </div>

            <div className="flex flex-col p-2">
              {members && members.map((item, idx) => (
                <div key={idx} className="flex w-full h-14 gap-3 items-center">
                  { item.profileUrl.startsWith('blob') ?
                    <MemeberImg url={item.profileUrl} />
                    :
                    <img src={item.profileUrl} className=" w-10 h-10 rounded-full" />
                  }
                  <div className="font-pre-R text-base text-black">{item.nickname}</div>
                </div>
              ))}
            </div>

          </div>
        </div>
        </motion.div>
      }
    </AnimatePresence>
  )
};

export default GroupMemberModal;