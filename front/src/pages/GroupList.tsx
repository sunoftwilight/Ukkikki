import React from "react";
import { Link } from "react-router-dom";
import ListComponent from "../components/GroupList/ListComponent";

const GroupList: React.FC = () => {
  return (
    <div className="w-full h-full px-4 flex flex-col gap-[15px] py-4">
      <div className="w-full h-full">
        <ListComponent />
      </div>
      <div className="pt-4 w-full">
        <Link to={'/groupcreate'}>
          <button className="w-full h-[60px] bg-main-blue rounded-2xl">
            <p className="text-white text-2xl font-gtr-B">그룹 생성</p>
          </button>
        </Link>
      </div>
    </div>
  )
};

export default GroupList;