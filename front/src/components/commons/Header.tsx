import React from "react";
import logo from "@/assets/Header/headerLogo.png"
import back from "@/assets/Header/back.png"
import timeOut from "@/assets/Header/timeOut.png"
import alarm from "@/assets/Header/alarm.png"
import noAlarm from "@/assets/Header/noAlarm.png"
import hamburger from "@/assets/Header/hamburger.png"
import albumOption from "@/assets/Header/albumOption.png"
import headerStore from "../../stores/headerStore";
import { Link, useLocation, useNavigate } from "react-router-dom";

const DefaultHeader: React.FC = () => {
  const location = useLocation()
  const navigate = useNavigate()

  const goBackHandler = () => {
    navigate(-1)
  }

  const basicPath = ['/', '/group', '/mypage',]
  const backPath = ['/grouplist', '/setting', '/feed',]
  const albumPath = ['/album', ]

  const isGuest = false
  const isAlaram = false

  const { setAlarmOpen } = headerStore()
  const { setMenuOpen } = headerStore()

  if (basicPath.includes(location.pathname)) {
    return (
      <div className="fixed flex justify-between items-center px-4 w-full h-14 bg-white">
        <Link to={'/'}><img src={logo} /></Link>

      <div className="flex gap-6">
        { isGuest ?
          <div className="flex items-center gap-1">
            <img src={timeOut} />
            <div className="font-pre-R text-black">13 : 28</div>
          </div>
          : isAlaram ?
            <img src={alarm} onClick={() => setAlarmOpen()} />
            : <img src={noAlarm} onClick={() => setAlarmOpen()} />
        }

        <img src={hamburger} onClick={() => setMenuOpen()} />
      </div>
    </div>
    )
  }

  else if (backPath.includes(location.pathname)) {
    return (
      <div className="fixed flex justify-between items-center px-4 w-full h-14 bg-white">
        <img src={back} onClick={() => goBackHandler()} />
  
        <div className="flex gap-6">
          { isGuest ?
            <div className="flex items-center gap-1">
              <img src={timeOut} />
              <div className="font-pre-R text-black">13 : 28</div>
            </div>
            : isAlaram ?
              <img src={alarm} onClick={() => setAlarmOpen()} />
              : <img src={noAlarm} onClick={() => setAlarmOpen()} />
          }
  
          <img src={hamburger} onClick={() => setMenuOpen()} />
        </div>
      </div>
    )
  }

  else if (albumPath.includes(location.pathname)) {
    return (
      <div className="fixed flex justify-between items-center px-4 w-full h-14 bg-white">
        <img src={back} onClick={() => goBackHandler()} />

        <div className="flex gap-6 items-center">
          { isGuest &&
            <div className="flex items-center gap-1">
              <img src={timeOut} />
              <div className="font-pre-R text-black">13 : 28</div>
            </div>}
          
          <img src={albumOption} className="w-6 h-6" />
          <button className="rounded-[10px] font-pre-SB text-white text-lg justify-center flex items-center bg-main-blue w-14 h-8">선택</button>
        </div>
      </div>
    )
  }
};

export default DefaultHeader;