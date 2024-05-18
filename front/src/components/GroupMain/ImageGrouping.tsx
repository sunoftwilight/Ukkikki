import React, { useEffect, useState } from "react";
import { getGroup, getPartyThumb } from "../../api/party";
import { useNavigate, useParams } from "react-router-dom";
import { ImgGroupListData } from "../../types/GroupType";
import { userStore } from "../../stores/UserStore";
import { imgGroupStore } from "../../stores/AlbumStore";


const ImageGrouping: React.FC = () => {
  const {groupPk} = useParams();
  const [groupingList, setGroupingList] = useState<ImgGroupListData[]>([])
  const { groupKey } = userStore();
  const { setGroupName, setType } = imgGroupStore();
  const navigate = useNavigate();

  useEffect(() => {
    getGroupList()
  }, [])

  const getGroupList = async () => {
    const key = Number(groupPk)
    await getGroup(key,
    (res) => {
      console.log(res.data.data)
      const data = res.data.data
      data.forEach((item) => (
        getImg(item.thumbnailUrl, groupKey[key])
      ))
      setGroupingList(data)
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
			() => {},
			(err) => { console.log(err);
      },
		);
	};

  const linkHandler = (type: number, groupName: string) => {
    setType(type);
    setGroupName(groupName);
    navigate(`/imagegroup/${groupPk}`);
  } 


  return (
    <div className="w-full h-[72px] flex rounded-xl bg-soft-gray items-center ps-[10px] overflow-x-auto scrollbar-hide">
      <div style={{ minWidth: `${groupingList.length * 72}px` }} className="flex gap-5">
        { groupingList.map((item, idx) => (
          <div key={idx} className="w-[52px] h-[52px] rounded-full border-disabled-gray border-[1px] flex justify-center items-center hover:scale-125" onClick={()=> linkHandler(item.type, item.groupName)}>
            <img src={item.thumbnailUrl} className="w-12 h-12 object-cover rounded-full" />
          </div>
        ))}
      </div>
    </div>
  )
};

export default ImageGrouping;