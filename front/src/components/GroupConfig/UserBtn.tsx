import React from "react";

const UserBtn: React.FC = () => {

  return (
    <div className="w-full p-2 h-[calc(100%-332px)] flex flex-col mt-8 text-xl text-black gap-4 relative">
			<div className="w-full h-16 bg-disabled-gray rounded-2xl flex items-center px-5">
				<p>'진하얀' 추방</p>
			</div>
			<div className="w-full h-16 bg-disabled-gray rounded-2xl flex items-center px-5">
				<p>'진하얀' 차단</p>
			</div>
			<div className="w-full h-16 bg-main-blue rounded-2xl flex items-center px-5 text-white absolute bottom-4">
				<p>'진하얀'에게 호스트 위임</p>
			</div>
    </div>
  )
};

export default UserBtn;