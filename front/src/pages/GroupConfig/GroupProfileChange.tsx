import React, { useState, useRef } from "react";
import plusBtn from "@/assets/GroupConfig/plus-btn.png";
import defaultimg from "@/assets/GroupConfig/defaultimg.png";
import { changePartyProfile } from "../../api/party";
import { useNavigate, useParams } from "react-router-dom";
import { useStore } from "zustand";
import { userStore } from "../../stores/UserStore";

const GroupProfileConfig: React.FC = () => {
  const [selectedImage, setSelectedImage] = useState<string>('');
  const inputNameRef = useRef<HTMLInputElement>(null);
  const inputImgRef = useRef<HTMLInputElement>(null);
  const {groupPk} = useParams();
  const user = useStore(userStore);
  const navi = useNavigate();
  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];

    if (file) {
      const imageUrl = URL.createObjectURL(file);
      setSelectedImage(imageUrl);
    }
  };

  const handleChangeBtn = () => {
    changeProfile();
  }

  const changeProfile = async () => {
    if (!inputNameRef.current || inputNameRef.current.value === "" || !inputImgRef.current) return;

		const param = {
			sseKey: user.groupKey[Number(groupPk)],
      nickName: inputNameRef.current.value,
		};
    
		const formData = new FormData();
		const key = new Blob([JSON.stringify(param)], { type: "application/json" });
    const files = inputImgRef.current.files;
    if (!files || files.length === 0) return;
    
    const file = files[0];


		formData.append("profileDto", key);
		formData.append("photo", file);

		await changePartyProfile(
			formData,
      Number(groupPk),
			(response) => {
        console.log(response)
			},
			(error) => {
				console.error(error);
			},
		);
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
        <input
          ref={inputNameRef}
          type="text"
          className="w-full h-[60px] bg-soft-gray rounded-2xl mb-3 text-xl font-pre-SB text-center focus:none"
          placeholder="그룹내에 이름을 정해주세요."/>
        <button className="w-full h-[60px] bg-main-blue rounded-2xl mb-3" onClick={()=> handleChangeBtn()}>
          <p className="font-pre-B text-xl text-white">변경</p>
        </button>
        <button className="w-full h-[60px] bg-disabled-gray rounded-2xl" onClick={()=> navi(-1)}>
          <p className="font-pre-B text-xl text-white">취소</p>
        </button>
      </div>
    </div>
  )
};

export default GroupProfileConfig;