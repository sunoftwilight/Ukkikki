import React from "react";
import Img from "../components/DetailImg/Img";
import { DetailImgProps } from "../types/AlbumType";
import FootNav from "../components/DetailImg/FootNav";

const DetailImg: React.FC<DetailImgProps> = ({ url }) => {

  return (
    <div className="w-full h-full">
      <Img url={url} />
      <FootNav />
    </div>
  )
};

export default DetailImg;