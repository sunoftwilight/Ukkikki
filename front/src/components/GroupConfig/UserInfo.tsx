import React from "react";
import UserImg from "../../assets/GroupConfig/Ellipse 55.png";
const UserInfo: React.FC = () => {

  return (
    <div className="w-full h-20 flex p-2 items-center gap-7">
      <img src={UserImg} className="w-16 h-16"/>
			<p className="text-2xl">진하얀</p>
    </div>
  )
};

export default UserInfo;