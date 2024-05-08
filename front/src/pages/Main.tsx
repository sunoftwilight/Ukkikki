import React, { useEffect } from "react";
import Banner from "../components/Main/Banner";
import Carousel from "../components/Main/Carousel";
import Buttons from "../components/Main/Buttons";


// 로그인용 작업중.
import { useStore } from "zustand";
import { userStore } from "../stores/UserStore";
import { useCookies } from 'react-cookie';
import { TokenRefresh, UserInfo } from "../api/user";

const Main: React.FC = () => {
  
  const user = useStore(userStore)
  const [cookies] = useCookies(['isLogin']);

  const GetAccessToken = async () => {
    await TokenRefresh(
      (res) => {
        user.setAccessToken(res.headers['access']);
    }, (err) => {
        console.log(err)
    })
  }
  
  const GetInfo = async () => {
    await UserInfo(
      (res) => {
        console.log(2)
        user.setUserId(res.data.data.userId)
        user.setUserName(res.data.data.userName)
        user.setUserProfile(res.data.data.profileUrl)
      }, (err) => {
        console.error(err)
      }
    )
  }

  useEffect(() => {
    if (Boolean(cookies.isLogin)) {
      GetAccessToken()
    }
  }, [cookies.isLogin])

  useEffect(() => {
    if (user.userId === "" && user.accessToken !== "") {
      console.log(1)
      GetInfo()
    }
  }, [user.accessToken])

  return (
    <div className="w-full h-full py-2 px-4 flex flex-col gap-9 mb-2">
      <div className="flex flex-col gap-[14px]">
        <Banner />
        <Carousel />
      </div>

      <div className="flex flex-col gap-4 h-full">
        {/* 유저 정보 */}
        <div>
          <div className="font-pre-SB text-lg text-black">
            <span className="font-pre-B text-black text-2xl">이해진</span> 님, 반갑습니다
          </div>
          <div className="font-pre-R text-black text-base">
            현재 업로딩 그룹은 <span className="font-pre-B text-main-blue text-lg">그룹이름보단그루비룸</span> 입니다
          </div>
        </div>

        <Buttons />
      </div>
    </div>
  )
};

export default Main;