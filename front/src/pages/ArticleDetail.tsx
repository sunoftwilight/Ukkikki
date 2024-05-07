import React from "react";
import Content from "../components/ArticleDetail/Content";
import Comment from "../components/ArticleDetail/Comment";
import InputNav from "../components/@commons/InputNav";

const ArticleDetail: React.FC = () => {
  return (
    <>
      <div className="flex flex-col w-full h-[calc(100%-48px)] overflow-scroll scrollbar-hide gap-7">
        <Content />
        <Comment />
      </div>
      <InputNav />
    </>
  )
};

export default ArticleDetail;