import React from "react";
import logo from "@/assets/Header/headerLogo.png"
import timeOut from "@/assets/Header/timeOut.png"
import alarm from "@/assets/Header/alarm.png"
import noAlarm from "@/assets/Header/noAlarm.png"
import hamburger from "@/assets/Header/hamburger.png"
import headerStore from "../../stores/headerStore";
import { Link } from "react-router-dom";

const LogoHeader: React.FC = () => {
  const isGuest = false
  const isAlaram = false

  const { setAlarmOpen } = headerStore()
  const { setMenuOpen } = headerStore()

  return (
    <div className="flex justify-between items-center px-4 w-full h-14 bg-white">
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
};

export default LogoHeader;