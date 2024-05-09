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
    getUserInfo();
  })

  const getAccess = async () => {
    if (cookies.isLogin === 'true'){
      console.log('test1')
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
