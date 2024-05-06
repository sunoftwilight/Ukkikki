import React from "react";

const userInfo = {
  img: 'https://pbs.twimg.com/profile_images/1044938826727055361/_2tCfp7B_400x400.jpg',
  name: '리해진',
  member: [ 
    { profile: 'https://i.pinimg.com/550x/32/6b/5b/326b5b0f5d59873dd9d117612cb87cdf.jpg', nickname: 'matt'},
  ]
}

const Profile: React.FC = () => { 


  return (
    <div className="flex justify-start items-center gap-4 px-2">
      <div className="w-[90px] h-[90px] rounded-full border-disabled-gray border-[1px] flex justify-center items-center">
        <img src={userInfo.img} className="w-[84px] h-[84px] rounded-full" />
      </div>

      <div className="flex flex-col gap-4">
        <div className="text-black text-xl font-gtr-B">{userInfo.name}</div>
        <div className="w-48 h-8 bg-soft-blue font-pre-M text-white flex justify-center items-center rounded-lg">
          <p>개인 설정</p>
        </div>
      </div>
    </div>
  )
};

export default Profile;