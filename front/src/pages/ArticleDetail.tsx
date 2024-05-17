import React, { useEffect, useState } from "react";
import Content from "../components/ArticleDetail/Content";
import Comment from "../components/ArticleDetail/Comment";
import InputNav from "../components/@commons/InputNav";
import { getComment } from "../api/Article/comment";
import { CommentListType } from "../types/Comment/CommentType";
import { useParams } from "react-router-dom";

const ArticleDetail: React.FC = () => {

  const {feedPk} = useParams();

  const [commentList, setCommentList] = useState<CommentListType>();

  useEffect(() => {
    getCommentList();
  },[])

  const getCommentList = async () => {
    await getComment(
      Number(feedPk),
      (res) => {
        setCommentList(res.data.data);
      },
      (err) => {
        console.log(err);
      }
    )
  }

  return (
    <>
      <div className="flex flex-col w-full h-[calc(100%-48px)] overflow-scroll scrollbar-hide gap-7">
        <Content />
        <Comment getCommentList={getCommentList} commentList={commentList}/>
      </div>
      <InputNav getCommentList={getCommentList}/>
    </>
  )
};

export default ArticleDetail;