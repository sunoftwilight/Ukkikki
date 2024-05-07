import React from "react";
import ChattingRoom from "../components/Chatting/ChattingRoom";
import logo from '../../icons/512.png'
import InputNav from "../components/@commons/InputNav";

const Chatting: React.FC = () => {
  return (
    <>
      <div className="px-4 pt-[10px] w-full h-[calc(100%-48px)] justify-center items-center">
        <div className="fixed w-[calc(100%-32px)] h-full flex justify-center items-center">
          <img src={logo} className="w-48" />
        </div>
        <ChattingRoom />
      </div>

      <div className="w-full bg-white ">
        <InputNav />
      </div>
    </>
  )
};

export default Chatting;