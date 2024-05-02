import React from "react";

const MediaBtnDropUp: React.FC = () => {
  const optionStyle = "w-full h-11 bg-soft-gray px-4 font-pre-R text-black text-lg flex items-center active:bg-gray"

  return (
    <div className="flex flex-col items-center">
      <div className={`${optionStyle} pt-4 pb-3 rounded-t-[15px]`}>
        <label htmlFor="fromAlbum">공유 앨범에서 첨부하기</label>
        <input id="fromAlbum" type="file" className="hidden"/>
      </div>
      <div className={`${optionStyle} pt-3 pb-4`}>
        <label htmlFor="fromDevice">디바이스에서 첨부하기</label>
        <input id="fromDevice" type="file" className="hidden"/>
      </div>
    </div>
  )
};

export default MediaBtnDropUp;