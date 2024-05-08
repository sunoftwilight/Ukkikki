import React, { useEffect } from 'react';
import LoginLogo from "../../icons/256.png";
import LoginBtn from "@/assets/Login/kakaoLoginBtn.png";

import { useCookies } from 'react-cookie';
import { useNavigate } from 'react-router';
import { userStore } from '../stores/UserStore';
import { useStore } from 'zustand';

import { tokenRefresh, userInfo } from '../api/user';


const Login: React.FC = () => {
  const [cookies] = useCookies(['isLogin']);
  const navi = useNavigate();
  const user = useStore(userStore);

  useEffect(() => {
    getAccess();
    getUserInfo();
  })

  const getAccess = async () => {
    if (cookies.isLogin === 'true'){
      await tokenRefresh(
        (response) => {
          user.setAccessToken(response.headers['access']);
          user.setIsLogin(true);
        },
        (error) => {
          console.error(error)
        }
      )
    }
  }

  const getUserInfo = async () => {
    if (user.accessToken !== '') {
      await userInfo(
        (response) => {
          const userData = response.data.data;
          user.setUserId(userData.userId);
          user.setUserName(userData.userName);
          user.setUserProfile(userData.profileUrl);

        },
        (error) => {
          console.error(error)
        }
      )
    }
    navi('/')
  }
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