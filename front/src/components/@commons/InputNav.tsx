import React from "react";

const InputNav: React.FC = () => {
  return (
    <div className="px-4 py-[6px] w-full fixed bottom-0 h-12 flex justify-center items-center gap-[6px] bg-white">
      <input 
        placeholder="댓글을 작성해보세요"
        className="py-2 px-3 font-pre-M text-base rounded-xl border-[0.4px] border-solid border-point-gray text-black w-[calc(100%-50px)] h-full outline-none" 
      />
      <button className="bg-main-blue text-white font-pre-M text-base w-12 h-full rounded-xl">전송</button>
    </div>
  )
};

export default InputNav;