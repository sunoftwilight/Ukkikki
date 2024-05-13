import React from "react";
import SecureImg from "../Album/SecureImg";

const Img: React.FC = () => {
  return (
    <div className="w-full h-[calc(100%-88px)] flex items-center justify-center">
        <SecureImg />
    </div>
  )
}; 

export default Img;