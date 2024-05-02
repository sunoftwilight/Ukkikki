import React from "react";
import LoginLogo from "../../icons/256.png";
import LoginBtn from "@/assets/Login/kakaoLoginBtn.png";

const Login: React.FC = () => {
  return (
    <div className="w-screen h-screen flex flex-col justify-center items-center bg-white">
      <img src={LoginLogo} className="-mt-11 mb-5"/>
      <button >
        <img src={LoginBtn} />
      </button>
    </div>
  )
};

export default Login;