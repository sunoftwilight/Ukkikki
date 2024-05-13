import React from "react";
import UserInfo from "../../components/GroupConfig/UserInfo"
import UserRole from "../../components/GroupConfig/UserRole"
import UserBtn from "../../components/GroupConfig/UserBtn"

const GroupUserDetail: React.FC = () => {
  return (
    <div className="p-4 w-full h-full flex flex-col font-gtr-B">
      <UserInfo />
      <UserRole />
      <UserBtn />
    </div>
  )
};

export default GroupUserDetail;