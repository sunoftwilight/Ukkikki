import React, { useState, useRef } from "react";
import plusBtn from "@/assets/GroupConfig/plus-btn.png";
import defaultimg from "@/assets/GroupConfig/defaultimg.png";

const GroupProfileConfig: React.FC = () => {
  const [selectedImage, setSelectedImage] = useState<string>('');
  const inputImgRef = useRef<HTMLInputElement>(null);

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];

    if (file) {
      const imageUrl = URL.createObjectURL(file);
      setSelectedImage(imageUrl);
    }
  };
  return (
		<div className="p-4 w-full h-[calc(100%-48px)] flex flex-col gap-2 relative font-pre-R text-xl items-center">
      <div className="w-full mb-14">
        <p className="text-3xl ps-4 font-pre-B">그룹 프로필 변경</p>
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
        <input type="text" className="w-full h-[60px] bg-soft-gray rounded-2xl mb-3 text-xl font-pre-SB text-center focus:none" placeholder="그룹이름을 설정해 주세요.          "/>
        <button className="w-full h-[60px] bg-main-blue rounded-2xl">
          <p className="font-pre-B text-xl text-white">변경</p>
        </button>
      </div>
    </div>
  )
};

export default GroupProfileConfig;