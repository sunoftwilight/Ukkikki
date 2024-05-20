import React from "react";
import { Link } from "react-router-dom";

const ErrorRedirect: React.FC = () => {
  return (
      <div className="w-screen h-screen flex flex-col gap-32 justify-center px-8">
        <div className="w-full flex flex-col items-start pl-3 gap-2">
          <div className="text-black text-8xl font-pre-EB text-start">404</div>
          <div>
            <div className="text-black text-3xl font-pre-EB text-start">Not Found</div>
            <div className="text-black text-xl font-pre-SB text-start">요청을 처리할 수 없습니다</div>
          </div>
        </div>
        <Link to={'/'} className="w-full h-16 flex justify-center items-center rounded-xl bg-main-blue text-white font-pre-EB text-2xl">
          메인 페이지로 돌아가기
        </Link>
      </div>
  )
};

export default ErrorRedirect;