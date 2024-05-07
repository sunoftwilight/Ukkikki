import React from "react";
import ModalBackground from "../@commons/ModalBackground";
import { memberStore } from "../../stores/ModalStore";
import close from '@/assets/Hamburger/close.png'
import { motion, AnimatePresence } from "framer-motion"

const groupInfo = {
  img: 'https://pbs.twimg.com/profile_images/1044938826727055361/_2tCfp7B_400x400.jpg',
  name: '그룹이름보단그루비룸',
  member: [ 
    { profile: 'https://i.pinimg.com/550x/32/6b/5b/326b5b0f5d59873dd9d117612cb87cdf.jpg', nickname: 'felix'},
    { profile: 'https://i.pinimg.com/550x/32/6b/5b/326b5b0f5d59873dd9d117612cb87cdf.jpg', nickname: 'matt'},
    { profile: 'https://mblogthumb-phinf.pstatic.net/data25/2007/4/7/30/%C6%C4%C4%A1%B8%AE%BD%BA-bjh2109.gif?type=w420', nickname: 'eddy'},
    { profile: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnmMfg6aY0GjfzVJq_ZPGhAaGu8CKPsiLJh8VhAGcQeKa12Jt9w1B1yAhFhod3jQi3xd4&usqp=CAU', nickname: 'gyu'},
    { profile: 'https://img2.joongna.com/media/original/2022/04/30/16513270770111u5_Oq5Ki.jpg?impolicy=thumb', nickname: 'spring'},
    { profile: 'https://file3.instiz.net/data/file3/2018/02/04/2/5/f/25f8f429ba61ad9839429e0f224f962e.jpg', nickname: 'sun'},
  ]
}

const GroupMemberModal: React.FC = () => {
  const { memberOpen, setMemberOpen } = memberStore()

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
              {groupInfo.member.map((item, idx) => (
                <div key={idx} className="flex w-full h-14 gap-3 items-center">
                  <img src={item.profile} className=" w-10 h-10 rounded-full" />
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