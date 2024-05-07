import React from "react";
import UserList from "../../components/GroupConfig/UserList"

const GroupUser: React.FC = () => {

  return (
		<div className="p-4 w-full h-full">
      <UserList type="users"/>
    </div>
  )
};

export default GroupUser;