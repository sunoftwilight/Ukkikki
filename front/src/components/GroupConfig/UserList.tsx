import React from "react";

import clickBtn from "@/assets/GroupConfig/clickBtn.png";
import eclipes1 from "@/assets/GroupConfig/Ellipse 55.png";
import eclipes2 from "@/assets/GroupConfig/Ellipse 56.png";
import eclipes3 from "@/assets/GroupConfig/Ellipse 57.png";
import eclipes4 from "@/assets/GroupConfig/Ellipse 59.png";
import { useNavigate } from "react-router-dom";


const userList = [
	{name: '진하얀', src: eclipes1, role:'뷰어'},
	{name: '진하늘', src: eclipes2, role:'뷰어'},
	{name: '진보라', src: eclipes3, role:'뷰어'},
	{name: '진홍이', src: eclipes4, role:'뷰어'},
]

const banList = [
	{name: '훈이놈', src: eclipes1},
	{name: '민폐남', src: eclipes2},
	{name: '민폐녀', src: eclipes3},
	{name: '중성마녀', src: eclipes4},
]

interface ListType {
	type: string
}

const UserList: React.FC<ListType> = (listType) => {
	const navi = useNavigate()
	const type = listType.type
	const CallType = () => {
		switch (type) {
			case "users":
				return (
					userList.map((item, idx) => (
						<div key={idx} className="w-full h-16 flex bg-soft-gray rounded-2xl items-center px-3 gap-5 font-pre-R text-xl relative">
							<img src={item.src} className="w-12 h-12"/>
							<p>{item.name}</p>
							<div className="absolute right-3 flex text-base text-main-blue items-center gap-3">
								<p>뷰어</p>
								<img src={clickBtn} className="w-3 h-4" onClick={() => navi('/groupuserdetail')}/>
							</div>
						</div>
					))
				);
			case "banUsers":
				return(
					banList.map((item, idx) => (
						<div key={idx} className="w-full h-16 flex bg-soft-gray rounded-2xl items-center px-3 gap-5 font-pre-R text-xl relative">
							<img src={item.src} className="w-12 h-12"/>
							<p>{item.name}</p>
							<div className="absolute right-3  bg-main-blue text-base text-white w-[60px] h-[38px] flex justify-center items-center rounded-xl">
								<p>해제</p>
							</div>
						</div>
					))
				)
		}
	};


  return (
    <div className="w-full h-full flex flex-col gap-3">
			{CallType()}
    </div>
  )
};

export default UserList;