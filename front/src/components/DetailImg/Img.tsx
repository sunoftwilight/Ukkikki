import React from "react";
import SecureImg from "../Album/SecureImg";

interface DetailProps {
  url : string
}

const Img: React.FC<DetailProps> = ({ url }) => {
  console.log('img',url)
  return (
    <div className="w-full h-[calc(100%-88px)] flex items-center justify-center">
      <SecureImg url={url} />
    </div>
  )
}; 

export default Img;