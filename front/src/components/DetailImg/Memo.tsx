import React from "react";
import MemoItem from "./MemoItem";
import InputNav from "../commons/InputNav";

const memoList = [
  {
    profileUrl: 'https://i.pinimg.com/564x/c5/5c/76/c55c762ce418abefd071aa7e81c5a213.jpg',
    name: '나는이해진은아님',
    date: '12.13.12',
    content: '미모무슨일임미쳣다미쳣어'
  },
  {
    profileUrl: 'https://mblogthumb-phinf.pstatic.net/MjAxODAzMTdfMTQz/MDAxNTIxMjQzOTYzNzQ4.JLij0p2Lw0gHxyKR8Jl6QJG2aMXBWIXMHEIASV5wOpkg.ytIpqFGKa3dFKQI6ELk0NY2bxhqZzAKyIFeeToG1yy8g.JPEG.dltnwjd49/downloadfile-3.jpg?type=w800',
    name: '나는이해진은진짜아님',
    date: '12.13.12',
    content: '민주가 세상을 구하고 나는 세상을 말아먹는다 민주야 세상을 잘 부탁해'
  },
  {
    profileUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfpXOlTKED93nnXGthEnZB8LFkznyV0_YO04vb99c6Tg&s',
    name: '나는이해진일수가없음',
    date: '12.13.12',
    content: '왁 천사가 있다면 민주겠지'
  },
  {
    profileUrl: 'https://i.pinimg.com/236x/99/7a/9b/997a9b2cd93277769ca9b3d109bceed7.jpg',
    name: '나는이해진일리없음',
    date: '12.13.12',
    content: '우우우~ 예쁜누나다'
  },
  {
    profileUrl: 'https://i.pinimg.com/564x/c5/5c/76/c55c762ce418abefd071aa7e81c5a213.jpg',
    name: '나는이해진은아님',
    date: '12.13.12',
    content: '미모무슨일임미쳣다미쳣어'
  },
  {
    profileUrl: 'https://mblogthumb-phinf.pstatic.net/MjAxODAzMTdfMTQz/MDAxNTIxMjQzOTYzNzQ4.JLij0p2Lw0gHxyKR8Jl6QJG2aMXBWIXMHEIASV5wOpkg.ytIpqFGKa3dFKQI6ELk0NY2bxhqZzAKyIFeeToG1yy8g.JPEG.dltnwjd49/downloadfile-3.jpg?type=w800',
    name: '나는이해진은진짜아님',
    date: '12.13.12',
    content: '민주가 세상을 구하고 나는 세상을 말아먹는다 민주야 세상을 잘 부탁해'
  },
  {
    profileUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfpXOlTKED93nnXGthEnZB8LFkznyV0_YO04vb99c6Tg&s',
    name: '나는이해진일수가없음',
    date: '12.13.12',
    content: '왁 천사가 있다면 민주겠지'
  },
  {
    profileUrl: 'https://i.pinimg.com/236x/99/7a/9b/997a9b2cd93277769ca9b3d109bceed7.jpg',
    name: '나는이해진일리없음',
    date: '12.13.12',
    content: '우우우~ 예쁜누나다'
  },
  {
    profileUrl: 'https://i.pinimg.com/564x/c5/5c/76/c55c762ce418abefd071aa7e81c5a213.jpg',
    name: '나는이해진은아님',
    date: '12.13.12',
    content: '미모무슨일임미쳣다미쳣어'
  },
  {
    profileUrl: 'https://mblogthumb-phinf.pstatic.net/MjAxODAzMTdfMTQz/MDAxNTIxMjQzOTYzNzQ4.JLij0p2Lw0gHxyKR8Jl6QJG2aMXBWIXMHEIASV5wOpkg.ytIpqFGKa3dFKQI6ELk0NY2bxhqZzAKyIFeeToG1yy8g.JPEG.dltnwjd49/downloadfile-3.jpg?type=w800',
    name: '나는이해진은진짜아님',
    date: '12.13.12',
    content: '민주가 세상을 구하고 나는 세상을 말아먹는다 민주야 세상을 잘 부탁해'
  },
  {
    profileUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfpXOlTKED93nnXGthEnZB8LFkznyV0_YO04vb99c6Tg&s',
    name: '나는이해진일수가없음',
    date: '12.13.12',
    content: '왁 천사가 있다면 민주겠지'
  },
  {
    profileUrl: 'https://i.pinimg.com/236x/99/7a/9b/997a9b2cd93277769ca9b3d109bceed7.jpg',
    name: '나는이해진일리없음',
    date: '12.13.12',
    content: '우우우~ 예쁜누나다akwlakr'
  },
]

const Memo: React.FC = () => {
  return (
    <div className="fixed bottom-24 h-[calc(100%-162px)] left-3 right-3 w-[calc(100%-24px)] bg-white bg-opacity-50 rounded-[15px] shadow-inner backdrop-blur-[20px] z-[1px] p-[10px] flex flex-col gap-6 transition-transform">
      <div className="flex flex-col h-[calc(100%-72px)] gap-2 overflow-scroll scrollbar-hide">
        {memoList.map((item, idx) => (
          <MemoItem key={idx} info={item} />
        ))}
      </div>


      <div className="absolute bottom-[10px] p-1 w-[calc(100%-20px)] h-12 flex justify-between items-center gap-[6px] bg-white rounded-[15px]">
        <input 
          placeholder="그룹원에게 메모를 남겨보세요!"
          className="py-2 px-3 w-[calc(100%-66px)] font-pre-M text-base rounded-xl text-black h-full outline-none" 
        />
        <button className="bg-main-blue text-white font-pre-M text-base w-14 h-full rounded-xl">작성</button>
      </div>
    </div>
  )
}; 

export default Memo;