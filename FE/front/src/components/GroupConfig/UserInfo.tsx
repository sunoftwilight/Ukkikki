import React from "react";
import { useStore } from "zustand";
import { memberInfoStore } from "../../stores/MemberInfoStore";
const UserInfo: React.FC = () => {
  const memberInfo = useStore(memberInfoStore)
  
  return (
    <div className="w-full h-20 flex p-2 items-center gap-7">
      <img src={memberInfo.data?.profileUrl} className="w-16 h-16 rounded-full"/>
			<p className="text-2xl">{memberInfo.data?.userName}</p>
    </div>
  )
};

export default UserInfo;