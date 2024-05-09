import React, { useState, useRef } from "react";
import plusBtn from "@/assets/GroupCreate/plus-btn.png";
import defaultimg from "@/assets/GroupCreate/defaultimg.png";
import { InsertInfoProps } from "../../types/Group";


const InsertInfo:React.FC<InsertInfoProps> = ({ onNextBtnClick, createData }) => {
  const [selectedImage, setSelectedImage] = useState<string>('');
  
  const inputImgRef = useRef<HTMLInputElement>(null);
  const inputNameRef = useRef<HTMLInputElement>(null);

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];

    if (file) {
      console.log(file)
      const imageUrl = URL.createObjectURL(file);
      setSelectedImage(imageUrl);
      createData.partyProfile = file;
    }

  };


  const handleNextBtnClick = () => {
    if (!inputNameRef.current || inputNameRef.current.value === '') return;
    createData.partyName = inputNameRef.current.value;
    onNextBtnClick('pass', createData);
  }

  return (
    <div className="flex flex-col w-full h-full items-center">
      <div className="w-full mb-14">
        <p className="text-3xl ps-4 font-pre-B">그룹 대표정보</p>
      </div>
      <div className="w-36 h-36 mb-10 relative rounded-full bg-gray flex justify-center items-center">
        <img
          className={selectedImage === '' ? 'object-fill w-20 h-20' : 'w-36 h-36 rounded-full'}
          src={selectedImage === '' ? defaultimg : selectedImage}
          onClick={() => inputImgRef.current?.click()}/>
        <img 
          className="w-10 h-10 absolute bottom-1 right-1"
          src={plusBtn}
          onClick={() => inputImgRef.current?.click()}/>
        <input
          className="hidden"
          type="file"
          accept="image/*"
          ref={inputImgRef}
          onChange={handleImageChange}/>
      </div>
      <div className="px-4 w-full">
        <input ref={inputNameRef} type="text" className="w-full h-[60px] bg-soft-gray rounded-2xl mb-3 text-xl font-pre-SB text-center focus:none" placeholder="그룹이름을 설정해 주세요."/>
        <button className="w-full h-[60px] bg-main-blue rounded-2xl" onClick={handleNextBtnClick}>
          <p className="font-pre-B text-xl text-white">다음</p>
        </button>
      </div>
    </div>
  )
}

export default InsertInfo;

