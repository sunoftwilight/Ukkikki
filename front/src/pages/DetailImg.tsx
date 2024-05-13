import React from "react";
import Img from "../components/DetailImg/Img";
import FootNav from "../components/DetailImg/FootNav";
import ThumbnailNav from "../components/DetailImg/ThumbnailNav";

const DetailImg: React.FC = () => {

  return (
    <div className="w-full h-full">
      <Img />
      <FootNav />
      <ThumbnailNav />
    </div>
  )
};

export default DetailImg;