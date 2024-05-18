import React, { useEffect } from "react";
import GroupProfile from "../components/GroupMain/GroupProfile";
import ImageGrouping from "../components/GroupMain/ImageGrouping";
import GroupMenu from "../components/GroupMain/GroupMenu";
import { useParams } from "react-router-dom";
import { useStore } from "zustand";
import { currentGroupStore } from "../stores/GroupStore";

const Group: React.FC = () => {
  // 그룹 Pk 호출 완료.
  // 이 Pk로 그룹 관련 API들을 사용할 생각
  const { groupPk } = useParams();
  const { setCurrentGroup } = useStore(currentGroupStore)

  useEffect(() => {
    setCurrentGroup(Number(groupPk))
  }, [])

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