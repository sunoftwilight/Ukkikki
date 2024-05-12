import React from "react";
import { ChattingRoomProps } from "../../types/ChatType";

const ChattingRoom: React.FC<ChattingRoomProps> = ({ list }) => {
  return (
    <div className="flex flex-col w-full h-full overflow-scroll scrollbar-hide z-10">
      { list.map((item, idx) => (
        item.chatType === 'ENTER' 
        ? 
        <div key={idx} className="w-full mb-[10px] rounded-[15px] py-2 bg-disabled-gray opacity-80 font-pre-R text-white text-sm flex justify-center items-center">
          {item.memberName} 님이 참여했습니다.
        </div>
        : item.chatType === 'EXIT' ?
        <div key={idx} className="w-full mb-[10px] rounded-[15px] py-2 bg-disabled-gray opacity-80 font-pre-R text-white text-sm flex justify-center items-center">
          {item.memberName} 님이 퇴장했습니다.
        </div>
        :
        <div key={idx} className="w-full mb-[10px] rounded-[15px] bg-soft-gray opacity-80 flex py-2 px-[10px] gap-[10px]">
          <img src={item.profileUrl} className="rounded-full w-[50px] h-[50px]" />
          <div className="flex flex-col gap-2">
            <div className="flex justify-between">
              <div className="flex gap-2 items-center mt-[2px]">
                <div className="font-pre-SB text-black text-xs">{item.memberName}</div>
                <div className="font-pre-L text-point-gray text-[10px]">{item.createDate.split('T')[0]} &nbsp; {item.createDate.split('T')[1]}</div>
              </div>
              <div className="font-pre-L text-point-gray text-[10px]">{item.readNum}명 읽음</div>
            </div>
            <div className="font-pre-L text-black text-sm">{item.memberName}</div>
          </div>
        </div>
      ))}
    </div>
  )
};

export default ChattingRoom;