import React, { useState, useEffect } from 'react';
import { downloadFile } from '../../api/file';
import { useLocation } from 'react-router-dom';

const SecureImg: React.FC = () => {
  const location = useLocation();

  useEffect(() => {
    console.log('path', location.pathname)
  }, [location])

  const [imageUrl, setImageUrl] = useState('');

  useEffect(() => {
    downloadFile(
      'XlD0Bazmy98XN59LnysMn0FExeOA6guSmMsC69j/5RE=',
      {  
        fileId: '1',
        prefix: 'z',
      },
      (res) => { 
        const url = window.URL.createObjectURL(new Blob([res.data]))
        setImageUrl(url)
      },
      (err) => { 
        console.error(err)
        // alert('오류가 발생했습니다. 다시 시도하십시오.')
      },
    )
  }, [])

  return (
    location.pathname.startsWith('/album/') ?
      <img src={imageUrl} className="h-full object-contain" />
      :
      <img src={imageUrl} className="w-[106px] h-[90px] object-cover rounded-lg" />
    
  );
}

export default SecureImg;
