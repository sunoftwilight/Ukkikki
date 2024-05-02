import React from "react";

const InsertPassword:React.FC = () => {

    return (
      <div className="flex flex-col w-full h-full font-pre-B px-4">

        <div className="w-full mb-12">
          <p className="text-3xl">비밀번호 설정</p>
        </div>

        <div className="w-full mb-10">
          <p className="text-lg mb-3">그룹 비밀번호</p>
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
          <p className="text-lg mb-3">그룹 비밀번호 확인</p>
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
          <button className="w-full h-[60px] bg-main-blue rounded-2xl">
            <p>완료</p>
          </button>
          <button className="w-full h-[60px] bg-gray mt-2 rounded-2xl">
            <p>이전</p>
          </button>
        </div>
      </div>
    )
}

export default InsertPassword;

