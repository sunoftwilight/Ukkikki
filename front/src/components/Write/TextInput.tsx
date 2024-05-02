import React from "react";

const addMediaList = [
  { pk: 1, thumbNailUrl: 'https://pbs.twimg.com/media/FxRrO81acAA-17_.jpg' },
  { pk: 2, thumbNailUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPMeyq6NNso3fb7fsTOhKvzWN9-VljUXAxGFdDj8KUkA&s' },
  { pk: 3, thumbNailUrl: 'https://d2u3dcdbebyaiu.cloudfront.net/uploads/atch_img/433/12eef34ac4e0fa51d3c8fc23939d68ca_res.jpeg' },
  { pk: 4, thumbNailUrl: 'https://i.ytimg.com/vi/f8_JB4t34S0/hqdefault.jpg' },
]
const WriteMain: React.FC = () => {
  return (
    <div className="flex flex-col w-full h-[calc(100%-70px)] px-4 pb-4 mt-3">
      <input
        type="text" 
        placeholder="제목을 입력하세요"
        className="w-full font-pre-SB text-black text-2xl outline-none h-12" 
      />
      <div className="flex flex-col gap-3 h-full">
        <textarea
          placeholder="내용을 입력하고 추억을 공유해보세요"
          className="w-full h-full font-pre-R text-black text-lg outline-none"
        />
        <div className="pl-4 flex overflow-x-scroll scrollbar-hide gap-2 h-48">
          { addMediaList.map((item, idx) => (
            <img key={idx} src={item.thumbNailUrl}
              className="w-36 h-36 object-cover"
            />
          ))}
        </div>
      </div>
    </div>
  )
};

export default WriteMain;