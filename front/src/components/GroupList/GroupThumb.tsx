import React, { useEffect, useState } from 'react';
import { getPartyThumb } from '../../api/party';
import { useStore } from 'zustand';
import { userStore } from '../../stores/UserStore';
import { PartyListData } from '../../types/GroupType';
import logo from '../../../icons/512.png'

interface ImgProps {
  info : PartyListData;
}

const GroupThumb: React.FC<ImgProps> = ({ info }) => {
  const user = useStore(userStore);
	const keys = user.groupKey;

  useEffect(() => {
    checkStateAndImg(info)
    console.log(info)
  }, [info])

  const [blobUrl, setBlobURl] = useState('')
	const checkStateAndImg = async (data: PartyListData) => {
		const key = keys[data.id]
		if (key === "expired") {
			data.partyProfile = ''
			data.expired = true;
		}
		else {
      if (!data.partyProfile) return
			data.expired = false
			const opt = {
				"x-amz-server-side-encryption-customer-key": key,
			};
			await getPartyThumb(
				data.partyProfile,
				opt,
				(res) => { 
					const blob = new Blob([res.data], {type: 'image/png'})
					setBlobURl(URL.createObjectURL(blob))
				},
				(err) => { console.log(err); },
			);
		}
	}


  return (
    <img src={info.partyProfile ? blobUrl : logo} className="w-14 h-14 rounded-full ms-4 me-5" />
  );
}

export default GroupThumb;
