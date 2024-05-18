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

  const [imgres, setImgRes] = useState('')
  const getImgHandler = async () => {
    await getPartyThumb(
      url,
      opt,
      (res) => { 
        // console.log('res',res)

        // ArrayBuffer를 Blob으로 변환
        const blob = new Blob([res.data], { type: 'image/jpeg' });
        // Blob URL 생성
        const imageUrl = URL.createObjectURL(blob);
        // 이미지 데이터 설정
        // console.log(imageUrl)
        setImgRes(imageUrl);
      },
      (err) => { console.error(err) },
    );
  }


  return (
    location.pathname.startsWith('/album/detail') ?
      <img src={imgres} alt='로딩중' className="h-full object-contain" />
      :
      <img src={imgres} className="w-[106px] h-[90px] object-cover rounded-lg" />
  );
}

export default SecureImg;
