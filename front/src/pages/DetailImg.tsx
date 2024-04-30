import React from "react";
import Img from "../components/DetailImg/Img";
import { DetailImgProps } from "../types/AlbumType";
import FootNav from "../components/DetailImg/FootNav";
import ThumbnailNav from "../components/DetailImg/ThumbnailNav";

const DetailImg: React.FC<DetailImgProps> = ({ url }) => {

  return (
    <div className="w-full h-full">
      <Img url={url} />
      <FootNav />
      <ThumbnailNav />
    </div>
  )
};

export default DetailImg;