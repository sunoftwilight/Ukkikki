import React, { useEffect } from "react";
import LoginLogo from "../../icons/512.png";
import LoginBtn from "@/assets/Login/kakaoLoginBtn.png";
import InfoIcon from "@/assets/GroupAttend/info_icon.png";
import { guestStore } from "../stores/GuestStore";
import { useStore } from "zustand";
import { useNavigate, useParams } from "react-router-dom";

const GroupAttendLogin:React.FC = () => {
  const guest = useStore(guestStore);
  const navi = useNavigate();
  const { groupPk } =useParams();

  useEffect(() => {
    guest.setPartyPk(Number(groupPk));
    guest.setIsInvite(true);
  })
  const login = () => {
    // window.location.href = "https://k10d202.p.ssafy.io/api/oauth2/authorization/kakao"
    window.location.href = "http://localhost:5000/api/oauth2/authorization/kakao"
  }

  const guestBtn = () => {
    guest.setIsGuest(true);
    navi(`/group/${guest.viewPartyPk}/attend`)
  }

  return (
    <div className="w-full h-full flex flex-col justify-center items-center bg-white">
      <img src={LoginLogo} className="-mt-11 mb-5 w-[170px]"/>
      <div className="flex flex-col justify-center items-center mb-16">
        <img src={LoginBtn} onClick={() => login()} className="w-80 mb-4"/>
        <div className="w-80 h-[45px] bg-point-gray font-gtr-B text-base text-white rounded-md flex justify-center items-center" onClick={() => guestBtn()}>
          <p>게스트로 시작하기</p>
        </div>
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

export default GroupAttendLogin;

