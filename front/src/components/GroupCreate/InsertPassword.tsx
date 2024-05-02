import React from "react";

const InsertPassword:React.FC = () => {

    return (
      <div className="flex flex-col w-full h-full">
        <button className="w-full h-[60px] bg-main-blue rounded-2xl">
          <p>완료</p>
        </button>
        <button className="w-full h-[60px] bg-gray mt-2 rounded-2xl">
          <p>이전</p>
        </button>
      </div>
    )
}

export default InsertPassword;

