import React, { useState, useEffect } from "react";

import Banner from "../components/Main/Banner";
import Carousel from "../components/Main/Carousel";
import Buttons from "../components/Main/Buttons";


// 로그인용 작업중.
import { useStore } from "zustand";
import { userStore } from "../stores/UserStore";
import { getPartyList } from "../api/party";

const Main: React.FC = () => {
  const user = useStore(userStore);
  const [namePlate, setNamePlate] = useState<string>('');
  useEffect(() => {
		getList();
	}, []);

	const getList = async () => {
		await getPartyList(
			(res) => {
        if(res.data.data.length > 0) {
          console.log(1)
          console.log(user.uploadGroupId)
          console.log(res.data.data)
          const data = res.data.data.find(item => item.id === user.uploadGroupId)
          if(data)
            setNamePlate(data.partyName)
        }
			},
			(err) => {
				console.log(err);
			},
		);
	};


  return (
    <div className="w-full h-full py-2 px-4 flex flex-col gap-9 mb-2">
      <div className="flex flex-col gap-[14px]">
        <Banner />
        <Carousel />
      </div>

      <div className="flex flex-col gap-4 h-full pb-4">
        {/* 유저 정보 */}
        <div>
          <div className="font-pre-SB text-lg text-black">
            <span className="font-pre-B text-black text-2xl">{user.userName}</span> 님, 반갑습니다
          </div>
          <div className="font-pre-R text-black text-base">
            {user.uploadGroupId && <p>현재 업로딩 그룹은 <span className="font-pre-B text-main-blue text-lg">{namePlate}</span> 입니다</p>}
            {!user.uploadGroupId && <p>현재 업로딩 그룹이 <span className="font-pre-B text-main-blue text-lg">미설정 상태</span> 입니다</p>}
          </div>
        </div>

        <Buttons />
      </div>
    </div>
  )
};

export default Main;