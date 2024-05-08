import React, { useEffect } from 'react';

import { useCookies } from 'react-cookie';
import { useNavigate } from 'react-router';
import { userStore } from '../../stores/UserStore';
import { useStore } from 'zustand';

import { tokenRefresh, userInfo } from '../../api/user';

const LoginRedirect: React.FC = () => {
  const [cookies] = useCookies(['isLogin']);
  const navi = useNavigate();
  const user = useStore(userStore);

  useEffect(() => {
    console.log(user.isLogin)
    getAccess();
    getUserInfo();
  })

  const getAccess = async () => {
    if (cookies.isLogin === 'ture'){
      await tokenRefresh(
        (response) => {
          console.log(response)
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
          console.log(response)
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


  return null;
};

export default LoginRedirect;
