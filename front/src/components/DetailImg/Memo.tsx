import React, { useEffect, useState } from "react";
// import MemoItem from "./MemoItem";
import * as M from "../../api/detailImg";

import { DetailImgStore } from "../../stores/DetailImgStore";
import { useStore } from "zustand";
import { MemoListType } from "../../types/DetailImgType";
import MemoItem from "./MemoItem";


const Memo: React.FC = () => {

  const [inputMemo,setInputMemo] = useState("");
  const detail = useStore(DetailImgStore);

  const [memoList,setMemoList] = useState<MemoListType>([]);
  
  useEffect(() => {
    getMemoList();
  },[]);

  // 메모 가져오기
  const getMemoList = async () => {
    await M.getMemo(
      detail.currentImg,
      (res) => {
        setMemoList(res.data.data);
      },
      (err) => {
        console.log(err)
      }
    )
  }

  // 메모 작성
  const sendMemo = async () => {

    if(inputMemo.length === 0){
      return ;
    }

    await M.createMemo(
      detail.currentImg,
      inputMemo,
      () => {
        getMemoList();
      },
      (err) => {
        console.log(err)
      }
    )
  }

  // 메모 삭제
  const deleteMemo = (memoId : number) => {
    setMemoList(memoList.filter(memo => memo.memoId !== memoId));
  }

  return (
    <div className="fixed bottom-24 h-[calc(100%-162px)] left-3 right-3 w-[calc(100%-24px)] bg-white bg-opacity-50 rounded-[15px] shadow-inner backdrop-blur-[20px] z-[1px] p-[10px] flex flex-col gap-6 transition-transform">
      <div className="flex flex-col h-[calc(100%-72px)] gap-2 overflow-scroll scrollbar-hide">
        {memoList.map((item, idx) => (
          <MemoItem key={idx} info={item} deleteMemo={deleteMemo}/>
        ))}
      </div>


      <div className="absolute bottom-[10px] p-1 w-[calc(100%-20px)] h-12 flex justify-between items-center gap-[6px] bg-white rounded-[15px]">
        <input 
          placeholder="그룹원에게 메모를 남겨보세요!"
          className="py-2 px-3 w-[calc(100%-66px)] font-pre-M text-base rounded-xl text-black h-full outline-none" 
          value={inputMemo}
          onChange={(e) => setInputMemo(e.target.value)}
        />
        <button className="bg-main-blue text-white font-pre-M text-base w-14 h-full rounded-xl" 
          onClick={sendMemo}>작성</button>
      </div>
    </div>
  )
}; 

export default Memo;