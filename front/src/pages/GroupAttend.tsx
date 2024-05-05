import React from "react";
import InfoIcon from "@/assets/GroupAttend/info_icon.png";

const GroupAttend:React.FC = () => {
  return (
    <div className="w-full h-full flex flex-col bg-white p-4 font-pre-SB text-lg gap-3">
      <p>그룹 비밀번호</p>
      <div className="w-80 h-14 flex justify-between">
        <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
        <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
        <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
        <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
        <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
        <input type="password" maxLength={1} className="h-14 w-12 rounded-lg bg-gray text-center"/>
      </div>
      <div className="my-5 w-80 h-[45px] bg-disabled-gray font-gtr-B text-base text-white rounded-lg flex justify-center items-center">
        <p>다음</p>
      </div>
      <div className="w-80 h-[180px] bg-gray flex flex-col p-2 rounded-xl text-sm font-pre-M">
        <div className="m-2 flex items-center">
          <img src={InfoIcon} className="w-8 h-8 me-3"/>
          <div>
            <p>서비스 보안에 의해 게스트 로그인은</p>
            <p>일부 기능에 대한 이용이 제한됩니다.</p>
          </div>
        </div>
        <div className="mx-5 flex flex-col gap-2">
          <li>
            15 분간 서비스를 이용할 수 있습니다.
          </li>
          <li>
            갱신 버튼을 통해 이용 시간을 <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;연장할 수 있습니다.
          </li>
          <li>
            사진을 다운로드만 할 수 있습니다.
          </li>
        </div>
      </div>
    </div>
  )
}

export default GroupAttend;

