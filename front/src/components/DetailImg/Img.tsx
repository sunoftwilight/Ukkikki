import React from "react";
import { DetailImgProps } from "../../types/AlbumType";

const Img: React.FC<DetailImgProps> = ({ url }) => {
  return (
    <div className="w-full h-[calc(100%-44px)] flex items-center justify-center">
      <img src={url} className="object-contain" />
    </div>
  )
}; 

export default Img;