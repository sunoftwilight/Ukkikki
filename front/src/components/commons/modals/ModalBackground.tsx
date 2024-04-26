import React from "react";
import OneBtn from './OneBtn'

const ModalBackground: React.FC = () => {
  return (
    <div className="z-10 fixed w-full h-full flex justify-center items-center bg-white/5 shadow-inner backdrop-blur-sm">
      <OneBtn 
        modalItems={{
          content: '경고합니다', 
          modalType: 'warn' 
        }} 
      />
    </div>
  )
};

export default ModalBackground;