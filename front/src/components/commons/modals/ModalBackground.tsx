import React from "react";
import OneBtn from './OneBtn'

const ModalBackground: React.FC = () => {
  return (
    <div className="z-10 fixed w-full h-full flex justify-center items-center bg-white/5 shadow-inner backdrop-blur-sm">
      <OneBtn 
        modalItems={{
          title: 'zz',
          content: '업로드 진행 중입니다', 
          modalType: 'warn',
          btn: 1
        }} 
      />
    </div>
  )
};

export default ModalBackground;