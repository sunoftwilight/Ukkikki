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
          user.setAccessToken(response.headers['access']);
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
    console.log(user.accessToken)
    if (user.accessToken !== '') {
      console.log('plz')
      await userInfo(
        (response) => {
          const userData = response.data.data;
          user.setUserId(userData.userId);
          user.setUserName(userData.userName);
          user.setUserProfile(userData.profileUrl);
          console.log('test4');
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
