import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getPartyThumb } from '../../api/party';
import { useStore } from 'zustand';
import { userStore } from '../../stores/UserStore';

interface ImgProps {
  url : string;
}

const ChatUserThumb: React.FC<ImgProps> = ({ url }) => {
  const { groupKey } = useStore(userStore);
  const { groupPk } = useParams();

  const opt = {
    "x-amz-server-side-encryption-customer-key": groupKey[Number(groupPk)],
  };
  
  useEffect(() => {
    getImgHandler()
  }, [url])

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
    <img src={blobUrl} className="rounded-full w-[50px] h-[50px]" />
  );
}

export default ChatUserThumb;
