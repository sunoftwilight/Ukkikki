import React, {useEffect} from "react";
import LoginLogo from "../../icons/256.png";
import LoginBtn from "@/assets/Login/kakaoLoginBtn.png";

import { userStore } from "../stores/UserStore";
import { useStore } from "zustand";
import { TokenRefresh } from "../api/user";
import { useCookies } from 'react-cookie';

const Login: React.FC = () => {
  const user = useStore(userStore)
  const [cookies] = useCookies(['isLogin']);

  const GetAccessToken = async () => {
    console.log(2)
    await TokenRefresh(
      async (response) => {
        console.log(3)
        user.setAccessToken(response.headers['access']);
        console.log(4)
        user.setIsLogin(true)
      },
      (error) => {
        console.error('Failed to get access token:', error);
      }
    );
  };

  useEffect(() => {
    if (cookies.isLogin === 'true') {
      console.log(1)
      GetAccessToken();
      console.log(5)
    }
  }, [cookies.isLogin, user.isLogin])

  return (
    <div className="w-screen h-screen flex flex-col justify-center items-center bg-white">
      <img src={LoginLogo} className="-mt-11 mb-5"/>
      <a href="https://k10d202.p.ssafy.io/api/oauth2/authorization/kakao">
        <img src={LoginBtn} />
      </a>
    </div>
  )
};

export default Login;