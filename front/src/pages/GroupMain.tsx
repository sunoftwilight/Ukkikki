import React from "react";
import GroupProfile from "../components/GroupMain/GroupProfile";
import ImageGrouping from "../components/GroupMain/ImageGrouping";
import GroupMenu from "../components/GroupMain/GroupMenu";

const Group: React.FC = () => {
  return (
    <div className="w-full h-full px-4 flex flex-col gap-[52px] py-4">
      <GroupProfile />
      <div className="flex flex-col gap-16">
        <ImageGrouping />
        <GroupMenu />
      </div>
    </div>
  )
};

export default Group;