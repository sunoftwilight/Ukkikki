import React, { useEffect, useState } from "react";
import Img from "../components/DetailImg/Img";
import FootNav from "../components/DetailImg/FootNav";
import ThumbnailNav from "../components/DetailImg/ThumbnailNav";
import { getDetailImg } from "../api/directory";
import { useStore } from "zustand";
import { DetailImgStore } from "../stores/DetailImgStore";
import { currentDirStore } from "../stores/AlbumStore";
import { getDetailImgDataType } from "../types/AlbumType";

const DetailImg: React.FC = () => {
  const { currentImg } = useStore(DetailImgStore)
  const { currentDirId } = useStore(currentDirStore)

  const [highQualityUrl, setHighQualityUrl] = useState('')
  const [detailInfo, setDetailInfo] = useState<getDetailImgDataType>({
    isDownload: false,
    isLikes: false,
    url: ''
  })

  useEffect(() => {
    getDetailImg(
      currentDirId,
      currentImg,
      (res) => {
        setHighQualityUrl(res.data.data.url)
        console.log('high', res.data.data.url)
        setDetailInfo(res.data.data)
      },
      (err) => { console.error(err) }
    )
  }, [])
  
  // 좋아요 값 업데이트
  const updateLikes = (isLike : boolean) => {
    setDetailInfo(prevInfo => ({
      ...prevInfo, // 현재 상태의 나머지 부분을 복사합니다.
      isLikes: isLike // isLikes 값을 업데이트합니다.
    }))
  }

  return (
    <div className="w-full h-full">
      <Img url={highQualityUrl} />
      <FootNav info={detailInfo} updateLikes={updateLikes} />
      <ThumbnailNav />
    </div>
  )
};

export default DetailImg;