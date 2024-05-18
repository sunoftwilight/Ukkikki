import React from "react";
import CreateArticle from "../components/Write/CreateArticle";


const Write: React.FC = () => {

  return (
    <div className="w-screen h-[calc(100vh-60px)]">
      <CreateArticle />
    </div>
  )
};

export default Write;