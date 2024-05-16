import React, { useEffect } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import { getPartyThumb } from '../../api/party';
import { useStore } from 'zustand';
import { userStore } from '../../stores/UserStore';

interface ImgProps {
  url : string;
}

const SecureImg: React.FC<ImgProps> = ({ url }) => {
  const { groupKey } = useStore(userStore);
  const { groupPk } = useParams();
  const location = useLocation();

  const opt = {
    "x-amz-server-side-encryption-customer-key": groupKey[Number(groupPk)],
    // "x-amz-server-side-encryption-customer-key": 'XlD0Bazmy98XN59LnysMn0FExeOA6guSmMsC69j/5RE=',
  };
  
  useEffect(() => {
    getPartyThumb(
      url,
      opt,
      () => { console.log('성공')},
      (err) => { console.error(err) },
    );
  }, [url])

  return (
    location.pathname.startsWith('/album/detail') ?
      <img src={url} alt='로딩중' className="h-full object-contain" />
      :
      <img src={url} className="w-[106px] h-[90px] object-cover rounded-lg" />
    
  );
}

export default SecureImg;
