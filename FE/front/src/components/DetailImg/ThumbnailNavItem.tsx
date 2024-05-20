import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getPartyThumb } from '../../api/party';
import { useStore } from 'zustand';
import { userStore } from '../../stores/UserStore';
import { updateAlbumStore } from '../../stores/AlbumStore';

interface ImgProps {
  url : string;
}

const ThumbnailNavItem: React.FC<ImgProps> = ({ url }) => {
  const { groupKey } = useStore(userStore);
  const { groupPk } = useParams();
  const { needUpdate } = useStore(updateAlbumStore)

  const opt = {
    "x-amz-server-side-encryption-customer-key": groupKey[Number(groupPk)],
  };
  
  useEffect(() => {
    getImgHandler()
  }, [url, needUpdate])

  const [blobUrl, setBlobURl] = useState('')

  const getImgHandler = async () => {
    await getPartyThumb(
      url,
      opt,
      (res) => {
        const blob = new Blob([res.data], {type: 'image/png'})
        setBlobURl(URL.createObjectURL(blob))
      },
      (err) => { console.error(err) },
    );
  }


  return (
    <img src={blobUrl} className="min-w-11 h-full border-r-[1px] border-white object-cover"/>
  );
}

export default ThumbnailNavItem;
