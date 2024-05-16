import React from "react";
import CreateWrite from "../components/Write/CreateWrite";


const Write: React.FC = () => {

  return (
    <div className="w-screen h-[calc(100vh-60px)]">
      <CreateWrite />
    </div>
  )
};

export default Write;