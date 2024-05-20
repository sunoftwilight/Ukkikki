import React, { useEffect, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import { getPartyThumb } from '../../api/party';
import { useStore } from 'zustand';
import { userStore } from '../../stores/UserStore';
import { updateAlbumStore } from '../../stores/AlbumStore';

interface ImgProps {
  url : string;
}

const SecureImg: React.FC<ImgProps> = ({ url }) => {
  const { groupKey } = useStore(userStore);
  const { groupPk } = useParams();
  const location = useLocation();
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
    location.pathname.startsWith('/album/detail') ?
      <img src={blobUrl} alt='로딩중' className="h-full object-contain" />
      :
      <img src={blobUrl} className="w-[106px] h-[90px] object-cover rounded-lg" />
  );
}

export default SecureImg;
