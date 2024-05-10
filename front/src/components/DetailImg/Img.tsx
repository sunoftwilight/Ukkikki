import React from "react";
import { DetailImgProps } from "../../types/AlbumType";
import SecureImg from "../Album/SecureImg";

const Img: React.FC<DetailImgProps> = ({ url }) => {
  console.log(url)
  return (
    <div className="w-full h-[calc(100%-88px)] flex items-center justify-center">
      {/* <div className="h-full object-contain"> */}
        <SecureImg />
      {/* </div> */}
    </div>
  )
}; 

export default Img;