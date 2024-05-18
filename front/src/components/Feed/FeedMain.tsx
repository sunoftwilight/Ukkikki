import React, { useEffect, useState } from "react";
import write from "@/assets/DetailImg/write.png"
import { Link, useParams } from "react-router-dom";
import { ArticleProps } from "../../types/ArticleType";
import { getArticleList, getArticleImg } from "../../api/article";
import { useStore } from "zustand";
import { userStore } from "../../stores/UserStore";

const FeedMain: React.FC = () => {
  const { groupPk }= useParams();
  const [articleList, setArticleList] = useState<ArticleProps[]>([])
  const keys = useStore(userStore).groupKey

  useEffect(() => {
    getList()
  }, [])

  const getList = async() => {
    const pk = Number(groupPk)
    await getArticleList(pk,
      (res) => {
        setArticleList(res.data.articleDtoList)
        res.data.articleDtoList.forEach(item => checkStateAndImg(item))
      },
      (err) => {
        console.log(err)
      }
    )
  }

  const checkStateAndImg = async (data:ArticleProps) => {
		const key = keys[data.partyId]
    const opt = {
      "x-amz-server-side-encryption-customer-key": key,
    };
    if (data.photoList.length > 0) {
      await getArticleImg(
        data.photoList[0].photoUrl,
        opt,
        () => {},
        (err) => { console.log(err); },
      );
    }
	}

  return (
    <div className="flex flex-col w-full h-full overflow-scroll scrollbar-hide">
      <div className="px-4 justify-end flex">
        <Link to={`/write/${groupPk}`}>
          <img src={write} className="w-6" />
        </Link>
      </div>
      { articleList.map((item, idx) => (        
        <Link to={`/feed/${groupPk}/${item.id}`} key={idx} className="px-4 py-2 flex flex-col gap-2">
          <div className="w-full flex gap-3 items-center">
            <img src={item.writerUrl} className="rounded-full w-12 h-12 object-cover" />
            <div className="flex flex-col gap-1">
              <div className="font-pre-SB text-black text-base">{item.writer}</div>
              <div className="font-pre-R text-point-gray text-sm">{item.createDate.substring(0,10) + ' ' + item.createDate.substring(11, 16)}</div>
            </div>
          </div>
          <div className="w-full p-3 flex flex-col gap-5 bg-soft-gray rounded-xl">
            <div className="flex flex-col gap-3">
              <div className="truncate font-pre-SB text-lg text-black">{item.title}</div>
              <div className="line-clamp-4 font-pre-R text-sm text-black">{item.content}</div>
            </div>
            {item.photoList.length > 0 && (
              <img src={item.photoList[0].photoUrl} className="rounded-xl h-52 object-cover" />
            )}
          </div>
        </Link>
      ))}
    </div>
  )
};

export default FeedMain;