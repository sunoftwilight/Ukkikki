import React from "react";
import UserList from "../../components/GroupConfig/UserList"

const GroupBan: React.FC = () => {
  return (
		<div className="p-4 w-full h-full">
      <UserList type="banUsers"/>
    </div>
  )
};

export default GroupBan;