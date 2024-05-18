import React, { useEffect, useState } from "react";
import { getGroupDetail, getPartyThumb } from "../../api/party";
import { useParams } from "react-router-dom";
import { imgGroupStore } from "../../stores/AlbumStore";
import { userStore } from "../../stores/UserStore";


const GroupAlbum: React.FC = () => {

  const { groupPk } = useParams();
  const { type, groupName } = imgGroupStore();
  const [ imgList, setImgList ] = useState<{photoId:number, photoUrl:string, thumbnailUrl:string}[]>([]);
  const {groupKey} = userStore();

  useEffect(() => {
    getImgDetail(type, groupName);
  }, []);

  useEffect(() => {
    imgList.forEach((item) => {
      getImg(item.thumbnailUrl, groupKey[Number(groupPk)])
    })
  }, [imgList])

  const getImgDetail = async (type:number, groupName:string) => {
    const key = Number(groupPk)
    await getGroupDetail(type, groupName, key,
      (res) => {
        setImgList(res.data.data)
      },
      (err) => {
        console.log(err)
      })
  }

  const getImg = async (url: string, key: string) => {
		const opt = {
			"x-amz-server-side-encryption-customer-key": key,
		};
		await getPartyThumb(
			url,
			opt,
			() => {console.log()
      },
			(err) => { console.log(err); },
		);
	};

  return (
    <div className="grid grid-cols-3 sm:grid-cols-5 md:grid-cols-6 lg:grid-cols-8 xl:grid-cols-10 2xl:grid-cols-12 px-4 gap-1 overflow-scroll scrollbar-hide ">
      {imgList.map((item, idx) => (
        (<img src={item.thumbnailUrl} key={idx} />)
      ))}
    </div>
  )
};

export default GroupAlbum;