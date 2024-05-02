import React, { useState, useRef } from "react";

interface InsertInfoProps {
  onNextBtnClick: () => void;
}



const InsertInfo:React.FC<InsertInfoProps> = ({ onNextBtnClick }) => {
  const [selectedImage, setSelectedImage] = useState<String>('');
  const inputImgRef = useRef<HTMLInputElement>(null);


  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files?.[0];
    
  };


  const handleNextBtnClick = () => {
    onNextBtnClick()
  }

  return (
    <div className="flex flex-col w-full h-full">
      <input type="file" accept="image/*" onChange={handleImageChange} />
      <button className="w-full h-[60px] bg-main-blue rounded-2xl" onClick={handleNextBtnClick}>
        <p>다음</p>
      </button>
    </div>
  )
}

export default InsertInfo;

