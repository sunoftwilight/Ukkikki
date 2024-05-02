import React, { useState, useRef } from "react";

interface InsertInfoProps {
  onNextBtnClick: () => void;
}



const InsertInfo:React.FC<InsertInfoProps> = ({ onNextBtnClick }) => {
  const [selectedImage, setSelectedImage] = useState<string>('');
  const inputImgRef = useRef<HTMLInputElement>(null);

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];

    if (file) {
      const imageUrl = URL.createObjectURL(file);
      setSelectedImage(imageUrl);
    }
  };


  const handleNextBtnClick = () => {
    onNextBtnClick()
  }

  return (
    <div className="flex flex-col w-full h-full items-center">
      <div className="w-full mb-14">
        <p className="text-3xl ps-4">그룹 대표정보</p>
      </div>
      <div className="mb-10">
        <img
          className="w-36 h-36 rounded-full bg-gray"
          src={selectedImage}
          onClick={() => inputImgRef.current?.click()}/>
        <input
          className="hidden"
          type="file"
          accept="image/*"
          ref={inputImgRef}
          onChange={handleImageChange}/>
      </div>
      <div className="px-4 w-full">
        <input type="text" className="w-full h-[60px] bg-soft-gray rounded-2xl mb-3 text-xl font-pre-SB text-center focus:none" />
        <button className="w-full h-[60px] bg-main-blue rounded-2xl" onClick={handleNextBtnClick}>
          <p className="font-pre-B text-xl text-white">다음</p>
        </button>
      </div>
    </div>
  )
}

export default InsertInfo;

