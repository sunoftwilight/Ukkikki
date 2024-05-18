import React, { useEffect, useState } from "react";
import editBtn from "@/assets/ArticleDetail/edit.png";
import deleteBtn from "@/assets/ArticleDetail/delete.png";
import { useNavigate, useParams } from "react-router-dom";
import { getArticleDetail, getArticleImg, deleteAritcle } from "../../api/article";
import { ArticleProps } from "../../types/ArticleType";
import { useStore } from "zustand";
import { userStore } from "../../stores/UserStore";

const Content: React.FC = () => {
  const navigate = useNavigate()
  const { groupPk, feedPk } = useParams();
  const [articleInfo, setArticleInfo] = useState<ArticleProps>();
  const {groupKey, userName} = useStore(userStore)
  const goImgDetailHandler = (imgPk:number) => {
    navigate(`/feed/${groupPk}/${feedPk}/${imgPk}`)
  }

  useEffect(() => {
    getArticle();
  }, [])

  const getArticle = async() => {
    const gp = Number(groupPk)
    const ap = Number(feedPk)
    await getArticleDetail(gp, ap,
      (res) => {
        console.log(res.data.data)
        const data = res.data.data
        setArticleInfo(data)
        if (data.photoList.length > 0){
          data.photoList.forEach(item => {
            getImg(data.partyId, item.photoUrl)
          })
        }
      },  
      (err) => {
        console.error(err)
      }
    )
  }

  const getImg = async (pk: number, url: string) => {
		const key = groupKey[pk]
    const opt = {
      "x-amz-server-side-encryption-customer-key": key,
    };
    await getArticleImg(
      url,
      opt,
      () => {},
      (err) => { console.log(err); },
    );
	}

  const sendDeleteArticle = async() => {
    const gp = Number(groupPk)
    const ap = Number(feedPk)
    await deleteAritcle(gp, ap,
      (res) => {
        console.log(res)
        navigate(`/feed/${gp}`)
      },
      (err) => {
        console.log(err)
      }
    )
  }

  return (
    <div className="flex flex-col w-full bg-white gap-1">
      {/* 작성자 프로필 */}
      <div className="w-full h-16 px-5 py-2 flex gap-[14px]">
        <img src={articleInfo?.writerUrl} className="w-12 h-12 rounded-full" />

        <div className="w-full flex flex-col gap-1">
          <div className="w-full flex justify-between items-center">
            <div className="font-pre-SB text-black text-base">{articleInfo?.writer}</div>
            <div className="flex gap-2">
              {(articleInfo?.writer === userName) && (
                <>
                  <img src={editBtn} className="w-[20px]" onClick={() => {navigate(`/modify/${groupPk}/${feedPk}`)}}/>
                  <img src={deleteBtn} className="w-[16px]" onClick={() => sendDeleteArticle()} />
                </>
              )}
            </div>
          </div>

          <div className="flex gap-2 font-pre-R text-point-gray text-sm">
            <div>{articleInfo?.createDate.split('T')[0]}</div>
            {articleInfo?.modify && (<div>(수정됨)</div>)}
          </div>
        </div>
      </div>

      {/* 본문 내용 */}
      <div className="flex flex-col p-3 bg-soft-gray gap-4 mx-4 rounded-xl">
        <div className="flex flex-col gap-3">
          <div className="font-pre-SB text-black text-lg">{articleInfo?.title}</div>
          <div className="font-pre-R text-black text-sm">{articleInfo?.content}</div>
        </div>

        <div className="flex gap-[6px] overflow-x-scroll scrollbar-hide">
          { articleInfo?.photoList.map((item, idx) => (
            <img key={idx} src={item.photoUrl} onClick={() => {goImgDetailHandler(item.photoId)}} className="w-full h-60 rounded-xl object-cover" />
          ))}
        </div>
      </div>
    </div>
  )
};

export default Content;