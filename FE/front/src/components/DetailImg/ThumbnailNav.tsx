import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { useStore } from "zustand";
import { DetailImgStore } from "../../stores/DetailImgStore";
import { currentDirStore } from "../../stores/AlbumStore";
import { getThumbnailNav } from "../../api/directory";
import { thumbNailItemType } from "../../types/AlbumType";
import ThumbnailNavItem from "./ThumbnailNavItem";

const ThumbnailNav: React.FC = () => {
  const { setCurrentImg } = useStore(DetailImgStore)
  const { currentDirId } = useStore(currentDirStore)
  const [thumbnailList, setThumbnailList] = useState<thumbNailItemType[]>([])

  const { groupPk } = useParams();

  useEffect(() => {
    getThumbnailNav(
      currentDirId,
      (res) => {
        setThumbnailList(res.data.data)
      },
      (err) => { console.error(err) }
    )
  }, [])

  return (
    <div className="w-full h-11 flex fixed bottom-0 overflow-x-scroll scrollbar-hide">
      { thumbnailList.map((item, idx) => (
        <Link to={`/album/detail/${item.pk}/${groupPk}`} key={idx} onClick={() => setCurrentImg(item.photoId, item.pk, item.thumbUrl2)}>
          <ThumbnailNavItem url={item.thumbUrl2} />
        </Link>
      ))}
    </div>
  )
}; 

export default ThumbnailNav;