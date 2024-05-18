import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { useStore } from "zustand";
import { DetailImgStore } from "../../stores/DetailImgStore";
import { currentDirStore } from "../../stores/AlbumStore";
import { getThumbnailNav } from "../../api/directory";
import { thumbNailItemType } from "../../types/AlbumType";
import { getPartyThumb } from "../../api/party";
import { userStore } from "../../stores/UserStore";

const ThumbnailNav: React.FC = () => {
  const { setCurrentImg } = useStore(DetailImgStore)
  const { currentDirId } = useStore(currentDirStore)
  const [thumbnailList, setThumbnailList] = useState<thumbNailItemType[]>([])

  const { groupKey } = useStore(userStore);
  const { groupPk } = useParams();

  useEffect(() => {
    getThumbnailNav(
      currentDirId,
      (res) => {
        setThumbnailList(res.data.data)
        res.data.data.forEach((item) => {
          getImg(item.thumbUrl2, groupKey[Number(groupPk)])
        })
      },
      (err) => { console.error(err) }
    )
  }, [])

  const [blobUrl, setBlobURl] = useState('')

  const getImg = async (url: string, key: string) => {
		const opt = {
			"x-amz-server-side-encryption-customer-key": key,
		};
		await getPartyThumb(
			url,
			opt,
      (res) => {
        const blob = new Blob([res.data], {type: 'image/png'})
        setBlobURl(URL.createObjectURL(blob))
      },
			(err) => { console.log(err) },
		);
	};

  return (
    <div className="w-full h-11 flex fixed bottom-0 overflow-x-scroll scrollbar-hide">
      { thumbnailList.map((item, idx) => (
        <Link to={`/album/detail/${item.pk}/${groupPk}`} key={idx} onClick={() => setCurrentImg(item.photoId, item.pk, item.thumbUrl2)}>
          <img 
            src={blobUrl}
            className="min-w-11 h-full border-r-[1px] border-white object-cover"
          />
        </Link>
      ))}
    </div>
  )
}; 

export default ThumbnailNav;