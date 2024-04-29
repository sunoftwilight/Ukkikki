import React from "react";
import logo from "@/assets/Header/headerLogo.png"
import back from "@/assets/Header/back.png"
import timeOut from "@/assets/Header/timeOut.png"
import alaram from "@/assets/Header/alaram.png"
import noAlaram from "@/assets/Header/noAlaram.png"
import hamburger from "@/assets/Header/hamburger.png"

const DefaultHeader: React.FC = () => {
  const isBack = false
  const isGuest = false
  const isAlaram = false

  return (
    <div className="fixed flex justify-between items-center px-4 w-full h-14 bg-white">
      { isBack ?
        <img src={back} />
        : <img src={logo} />
      }
      <div className="flex gap-6">
        { isGuest ?
          <div className="flex items-center gap-1">
            <img src={timeOut} />
            <div className="font-pre-R text-black">13 : 28</div>
          </div>
          : isAlaram ?
            <img src={alaram} />
            : <img src={noAlaram} />
        }

        <img src={hamburger} />
      </div>
    </div>
  )
};

export default DefaultHeader;