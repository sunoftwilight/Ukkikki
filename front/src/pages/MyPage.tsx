import React from "react";
import Profile from "../components/MyPage/Profile";
import ImageGrouping from "../components/MyPage/ImageGrouping";
import MyAlbum from "../components/MyPage/MyAlbum";

const MyPage: React.FC = () => {
  return (
    <div className="w-full h-full px-4 flex flex-col gap-4 py-4">
      <Profile />
      <div className="flex flex-col gap-4">
        <ImageGrouping />
        <MyAlbum />
      </div>
    </div>
  )
};

export default MyPage;