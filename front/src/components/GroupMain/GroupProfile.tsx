import React from "react";
import settings from "@/assets/GroupMain/settings.png"
import etc from "@/assets/GroupMain/etc.png"
import { memberStore } from "../../stores/ModalStore";
import { Link, useParams } from "react-router-dom";

const groupInfo = {
  img: 'https://pbs.twimg.com/profile_images/1044938826727055361/_2tCfp7B_400x400.jpg',
  name: '그룹이름보단그루비룸',
  member: [ 
    { profile: 'https://i.pinimg.com/550x/32/6b/5b/326b5b0f5d59873dd9d117612cb87cdf.jpg', nickname: 'felix'},
    { profile: 'https://i.pinimg.com/550x/32/6b/5b/326b5b0f5d59873dd9d117612cb87cdf.jpg', nickname: 'matt'},
    { profile: 'https://mblogthumb-phinf.pstatic.net/data25/2007/4/7/30/%C6%C4%C4%A1%B8%AE%BD%BA-bjh2109.gif?type=w420', nickname: 'eddy'},
    { profile: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnmMfg6aY0GjfzVJq_ZPGhAaGu8CKPsiLJh8VhAGcQeKa12Jt9w1B1yAhFhod3jQi3xd4&usqp=CAU', nickname: 'gyu'},
    { profile: 'https://img2.joongna.com/media/original/2022/04/30/16513270770111u5_Oq5Ki.jpg?impolicy=thumb', nickname: 'spring'},
    { profile: 'https://file3.instiz.net/data/file3/2018/02/04/2/5/f/25f8f429ba61ad9839429e0f224f962e.jpg', nickname: 'sun'},
  ]
}

const GroupProfile: React.FC = () => { 
  const { setMemberOpen } = memberStore()
  const { groupPk } = useParams();
  
  const memberThumb = () => {
    const memberThumbs = []

    for (let i = 0; i < 5; i++) {
      memberThumbs.push(
        <img key={i} src={groupInfo.member[i].profile} className="rounded-full object-cover w-8 h-8 border-white border-2 -mr-2" />
    )}

    return memberThumbs
  }

  return (
    <div className="flex flex-col justify-center items-center gap-4">
      <div className="w-[90px] h-[90px] rounded-full border-disabled-gray border-[1px] flex justify-center items-center">
        <img src={groupInfo.img} className="w-[84px] h-[84px] rounded-full" />
      </div>

      <div className="flex items-center gap-4">
        <div className="text-black text-xl font-gtr-B">{groupInfo.name}</div>
        <Link to={`/group/${groupPk}/config`}>
          <img src={settings} className="w-3 h-3" />
        </Link>
      </div>

      <div className="flex" onClick={() => setMemberOpen()}>
        { memberThumb() }
        <div className="w-8 h-8 rounded-full bg-gray flex justify-center items-center border-white border-2">
          <img src={etc} className="w-[13px]" />
        </div>
      </div>
    </div>
  )
};

export default GroupProfile;