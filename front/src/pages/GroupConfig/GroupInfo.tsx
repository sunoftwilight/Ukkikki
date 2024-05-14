import React, { useState, useRef } from "react";
import plusBtn from "@/assets/GroupConfig/plus-btn.png";
import defaultimg from "@/assets/GroupConfig/defaultimg.png";
import { useNavigate, useParams } from "react-router-dom";
import { useStore } from "zustand";
import { userStore } from "../../stores/UserStore";
import { changePartyInfo } from "../../api/party";

const GroupInfoConfig: React.FC = () => {
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
    changeInfo();
  }

  const changeInfo = async () => {
    if (!inputNameRef.current || inputNameRef.current.value === "" || !inputImgRef.current) return;

		const param = {
      partyName: inputNameRef.current.value,
			key: user.groupKey[Number(groupPk)],
      simplePassword: user.simplePass
		};
    
		const formData = new FormData();
		const key = new Blob([JSON.stringify(param)], { type: "application/json" });
    const files = inputImgRef.current.files;
    if (!files || files.length === 0) return;
    
    const file = files[0];

		formData.append("changeThumbDto", key);
		formData.append("photo", file);

		await changePartyInfo(
			formData,
      Number(groupPk),
			() => {
        navi(-1)
			},
			(error) => {
				console.error(error);
			},
		);
	};
  return (
		<div className="p-4 w-full h-[calc(100%-48px)] flex flex-col gap-2 relative font-pre-R text-xl items-center">
      <div className="w-full mb-14">
        <p className="text-3xl ps-4 font-pre-B">그룹 대표정보 수정</p>
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
          placeholder="그룹이름을 설정해 주세요."/>
        <button className="w-full h-[60px] bg-main-blue rounded-2xl" onClick={()=> handleChangeBtn()}>
          <p className="font-pre-B text-xl text-white">변경</p>
        </button>
      </div>
    </div>
  )
};

export default GroupInfoConfig;