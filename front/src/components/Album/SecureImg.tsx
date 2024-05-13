import React, { useState, useEffect } from 'react';
import { downloadFile } from '../../api/file';
import { useStore } from 'zustand';
import { DetailImgStore } from '../../stores/DetailImgStore';

const SecureImg: React.FC = () => {
  const [imageUrl, setImageUrl] = useState('');
  const { currentImg } = useStore(DetailImgStore)

  useEffect(() => {
    downloadFile(
      'XlD0Bazmy98XN59LnysMn0FExeOA6guSmMsC69j/5RE=',
      {  
        fileId: currentImg,
        prefix: 'tmp',
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
