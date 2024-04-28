import React from "react";
import camera from "@/assets/Main/Buttons/camera.png"
import gallery from "@/assets/Main/Buttons/gallery.png"
import group from "@/assets/Main/Buttons/group.png"
import setting from "@/assets/Main/Buttons/setting.png"

const buttonList = [
  { img: camera, name: '카메라' },
  { img: gallery, name: '마이 앨범' },
  { img: group, name: '참여 중인 그룹' },
  { img: setting, name: '설정' }
]

const Buttons: React.FC = () => {
  return (
    <div className="flex flex-col gap-3">
      { buttonList.map((item, idx) => (
        <div key={idx} className="bg-soft-gray w-full h-[70px] rounded-xl py-5 px-7 flex gap-6 items-center">
          <img src={item.img} className="w-[30px] h-[30px]" />
          <div className="font-gtr-B text-black text-xl">{item.name}</div>
        </div>
      ))}
    </div>
  )
};

export default Buttons;