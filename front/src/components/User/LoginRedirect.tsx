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
  },[])

  const getAccess = async () => {
    if (Boolean(cookies.isLogin)){
      await tokenRefresh(
        (response) => {
          user.setAccessToken(response.headers['authorization']);
          user.setIsLogin(true);
          getUserInfo();
        },
        () => {
          navi('/login')
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
          console.log(userData)
          user.setUserId(userData.userId);
          user.setUserName(userData.userName);
          user.setUserProfile(userData.profileUrl);
          user.setUploadGroupId(userData.uploadGroupId);
          user.setIsInsert(userData.insertPass);
          
          if(userData.insertPass) navi('/simpleCheck');
          else navi('/simpleInsert')
        },
        () => {
          navi('/login')
        }
      )
    }
  }


  return <></>;
};

export default LoginRedirect;
