import React from 'react';
import { useLocation } from 'react-router-dom';
import { getPartyThumb } from '../../api/party';

interface ImgProps {
  url : string;
}

const SecureImg: React.FC<ImgProps> = ({ url }) => {
  const location = useLocation();
  const opt = {
    "x-amz-server-side-encryption-customer-key": 'XlD0Bazmy98XN59LnysMn0FExeOA6guSmMsC69j/5RE=',
  };
  
  getPartyThumb(
    url,
    opt,
    () => {},
    (err) => { console.error(err) },
  );

  return (
    location.pathname.startsWith('/album/detail') ?
      <img src={url} alt='로딩중' className="h-full object-contain" />
      :
      <img src={url} className="w-[106px] h-[90px] object-cover rounded-lg" />
    
  );
}

export default SecureImg;
