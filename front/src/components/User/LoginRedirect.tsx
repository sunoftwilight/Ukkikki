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
    getAccess();
  })

  const getAccess = async () => {
    if (Boolean(cookies.isLogin)){
      await tokenRefresh(
        (response) => {
          user.setAccessToken(response.headers['Authorization']);
          user.setIsLogin(true);
          getUserInfo();
        },
        (error) => {
          console.error(error)
        }
      )
    }
  }

  const getUserInfo = async () => {
    const data = userStore.getState().accessToken
    if (data !== '') {
      await userInfo(
        (response) => {
          const userData = response.data.data;
          user.setUserId(userData.userId);
          user.setUserName(userData.userName);
          user.setUserProfile(userData.profileUrl);
          navi('/')
        },
        (error) => {
          console.error(error)
        }
      )
    }
  }


  return <></>;
};

export default LoginRedirect;
