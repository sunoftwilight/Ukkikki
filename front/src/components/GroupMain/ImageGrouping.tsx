import React, { useEffect, useState } from "react";
import { getGroup, getPartyThumb } from "../../api/party";
import { useNavigate, useParams } from "react-router-dom";
import { ImgGroupListData } from "../../types/GroupType";
import { userStore } from "../../stores/UserStore";
import { imgGroupStore } from "../../stores/AlbumStore";
import ImageGroupThumb from "./ImageGroupThumb";


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

  const nameHandler = (type: number, groupName: string) => {
    switch (type) {
      case 1: 
        switch (groupName) {
          case 'PORTRAIT':
            return '정면'
          case 'INDIVIDUAL':
            return '개인'
          case 'GROUP':
            return '단체'
          case 'FULL_BODY_SHOT':
            return '전신'
          case 'CANDID':
            return '일상'
          case 'ENVIRONMENTAL_PORTRAIT':
            return '특징'
          case 'WILDLIFE':
            return '야생동물'
          case 'PET':
            return '애완동물'
          case 'BIRD':
            return '새'
          case 'MICRO_ANIMAL':
            return '소동물'
          case 'LANDSCAPE':
            return '풍경'
          case 'SEASCAPE':
            return '바다'
          case 'ASTRO':
            return '우주'
          case 'CITYSCAPE':
            return '도시'
          case 'WESTERN_FOOD':
            return '양식'
          case 'KOREAN_FOOD':
            return '한식'
          case 'CHINESE_FOOD':
            return '중식'
          case 'JAPANESE_FOOD':
            return '일식'
          case 'ETC':
            return '기타'
        }
        return
      case 2:
        return `인물 ${groupName}`
      case 3:
        return '좋아요'
    }
  }

  return (
    <div className="w-full h-[90px] flex rounded-xl bg-soft-gray items-center ps-[10px] overflow-x-auto scrollbar-hide">
      <div style={{ minWidth: `${groupingList.length * 72}px` }} className="flex gap-5">
        { groupingList.map((item, idx) => (
          <div className="flex flex-col items-center justify-center gap-1">
            <div key={idx} className="w-[52px] h-[52px] rounded-full border-disabled-gray border-[1px] flex justify-center items-center hover:scale-125" onClick={()=> linkHandler(item.type, item.groupName)}>
              <ImageGroupThumb url={item.thumbnailUrl} />
            </div>
            <div className="font-pre-L text-xs text-black">
              {nameHandler(item.type, item.groupName)}
            </div>
          </div>
        ))}
      </div>
    </div>
  )
};

export default ImageGrouping;