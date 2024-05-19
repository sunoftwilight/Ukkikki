import React from "react";
import write from '@/assets/DetailImg/write.png'
import { useNavigate } from "react-router-dom";
import { currentGroupStore } from "../../stores/GroupStore";

const ArticleList: React.FC = () => {
  const navi = useNavigate()
  const {currentGroup} = currentGroupStore();
  return (
    <div className="fixed bottom-24 right-3 w-64 bg-white bg-opacity-50 rounded-[15px] shadow-inner backdrop-blur-[20px] z-[1px] p-[10px] flex flex-col gap-2 transition-transform">
      <div className="flex justify-between items-center">
        <div className="font-gtr-B text-black text-lg">게시글 작성</div>
        <img src={write} className="w-6" onClick={() => navi(`/write/${currentGroup}`)}/>
      </div>
      <div className="flex flex-col gap-1">
      </div>
    </div>
  )
}; 

export default ArticleList;