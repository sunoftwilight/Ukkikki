import React from "react";

const UserRole: React.FC = () => {

  return (
    <div className="w-full p-2 text-black">
			<p className="text-xl mt-9 mb-3">권한 변경</p>
			<div className="h-40 px-4 py-4 w-full bg-soft-gray rounded-[15px] flex flex-col justify-evenly text-base gap-2">
				<div className="px-2 flex items-center gap-4">
					<input type="radio" name="startPage" id="camera" />
					<label htmlFor="camera">에디터</label>
				</div>
				<div className="px-2 flex items-center gap-4">
					<input type="radio" name="startPage" id="home" />
					<label htmlFor="home">뷰어</label>
				</div>
				<div className="w-full h-9 px-5 bg-soft-blue rounded-xl flex justify-center items-center">
					<p className="font-pre-SB text-lg text-white">적용</p>
				</div>
			</div>
    </div>
  )
};

export default UserRole;