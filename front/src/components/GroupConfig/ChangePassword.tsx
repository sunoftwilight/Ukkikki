import React from "react";

const ChangePassword: React.FC = () => {
  return (
    <div className="flex flex-col w-full h-full font-pre-B px-4">

      <div className="w-full mb-10">
        <p className="text-lg mb-3">새 비밀번호</p>
        <div className="w-full h-14 flex justify-between">
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
        </div>
      </div>

      <div className="w-full mb-10">
        <p className="text-lg mb-3">새 비밀번호 확인</p>
        <div className="w-full h-14 flex justify-between">
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
          <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
        </div>
      </div>

      <div className="w-full text-xl text-white">
        <button className="w-full h-[60px] bg-main-blue rounded-2xl" >
          <p>변경</p>
        </button>
      </div>
    </div>
  )
}

export default ChangePassword