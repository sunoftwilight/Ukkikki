import React from "react";
import write from '@/assets/DetailImg/write.png'

const articleList = [
  {title: '워녕쟝 카와이', writer: '콩쥐팥쥐햄쥐'},
  {title: '원영이는 너무 예뻐', writer: '지니지니야'},
  {title: '워녕워녕러뷰', writer: 'sun'},
  {title: '장원영은신인인가?아니장원영은신이다', writer: 'Felix'},
]

const ArticleList: React.FC = () => {
  return (
    <div className="fixed bottom-24 right-3 w-64 bg-white bg-opacity-50 rounded-[15px] shadow-inner backdrop-blur-[20px] z-[1px] p-[10px] flex flex-col gap-2 transition-transform">
      <div className="flex justify-between items-center">
        <div className="font-gtr-B text-black text-lg">작성된 게시글</div>
        <img src={write} className="w-6" />
      </div>
      <div className="flex flex-col gap-1">
        { articleList.map((item, idx) => (
          <div key={idx} className="h-10 flex justify-between items-center rounded-xl bg-white bg-opacity-70 px-2">
            <div className="font-pre-R text-black text-base truncate w-32">{item.title}</div>
            <div className="font-pre-M text-black text-sm truncate w-20 text-end">by {item.writer}</div>
          </div>
        ))}
      </div>
    </div>
  )
}; 

export default ArticleList;