import React, { useEffect } from 'react';
import LoginLogo from "../../icons/256.png";
import LoginBtn from "@/assets/Login/kakaoLoginBtn.png";

// import { useCookies } from 'react-cookie';
// import { useNavigate } from 'react-router';
// import { userStore } from '../stores/UserStore';
// import { useStore } from 'zustand';

// import { tokenRefresh, userInfo } from '../api/user';


const Login: React.FC = () => {
  // const [cookies] = useCookies(['isLogin']);
  // const navi = useNavigate();
  // const accessToken = userStore.getState().accessToken;
  // const isLogin = userStore.getState().isLogin;
  const login = () => {
    window.location.href = "https://k10d202.p.ssafy.io/api/oauth2/authorization/kakao"
  }


  return (
    <div className="w-screen h-screen flex flex-col justify-center items-center bg-white">
      <img src={LoginLogo} className="-mt-11 mb-5"/>
      <img src={LoginBtn} onClick={() => login()}/>
    </div>
  )
};

export default Login;