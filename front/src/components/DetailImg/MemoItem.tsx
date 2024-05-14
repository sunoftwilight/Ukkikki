import React, { useEffect, useState } from "react";
import edit from "@/assets/ArticleDetail/edit.png"
import deleteIcon from "@/assets/ArticleDetail/delete.png"
import { useStore } from "zustand";
import { userStore } from "../../stores/UserStore";

import * as M from "../../api/detailImg";

interface MemoItemProps {
  info : {
    memoId : number;
    content : string;
    userId : string;
    username : string;
    date : string;
  }

  deleteMemo : (
    memoId : number
  ) => void;
}

const MemoItem: React.FC<MemoItemProps> = ({ info, deleteMemo }) => {
  const [isMine,setIsMine] = useState(false);
  const [isModify, setIsModify] = useState(false);

  const [content,setContent] = useState("");

  const user = useStore(userStore);

  useEffect(()=>{
    mineCheck();
  },[])

  const mineCheck = () => {
    if(user.userId === info.userId){
      setIsMine(true);
    }
  }

  const memoDelete = async () => {
    await M.deleteMemo(
      info.memoId,
      () => {
        deleteMemo(info.memoId);
      },
      (err) => {
        console.log(err)
      }
    )
  }

  const memoModify = async () => {
    if(content.length === 0){
      return;
    }

    await M.modifyMemo(
      info.memoId,
      content,
      () => {
        info.content = content;
      },
      (err) => {
        console.log(err);
      }
    )
    switchModify();
  }

  const switchModify = () => {
    setIsModify(!isModify);
  }

  return (
    <div className="w-full p-2 flex bg-white/70 rounded-[15px] gap-2">
      {/* <img src={info.profileUrl} className="w-[50px] h-[50px] rounded-full" /> */}

      <div className="w-full flex flex-col gap-2">
        <div className="flex justify-between">
          <div className="flex gap-2">
            <div className="font-pre-SB text-black text-xs">{info.username}</div>
            <div className="font-pre-L text-point-gray text-[10px]">{info.date}</div>
          </div>

          {isMine && !isModify ? 
            <div className="gap-1 flex">
              <img src={edit} className="w-[14px]"  onClick={switchModify}/>
              <img src={deleteIcon} className="w-[12px]" onClick={memoDelete}/>
            </div>
            : <></>
          }
        </div>

        {!isModify ?
          <div className="font-pre-L text-black text-sm">{info.content}</div>
          : 
          <div className="flex flex-row">
            <input 
            placeholder={info.content}
            className="py-2 px-3 w-[calc(100%-66px)] font-pre-M text-base rounded-xl text-black h-7 outline-none"
            value={content}
            onChange={(e) => {setContent(e.target.value)}}
            />
            <button className="bg-main-blue text-white font-pre-M text-base w-12 h-7 rounded-xl" 
            onClick={switchModify}
             >취소</button>
            <button className="ml-2 bg-main-blue text-white font-pre-M text-base w-12 h-7 rounded-xl" 
            onClick={memoModify}
             >수정</button>
          </div>
          }
      </div>
    </div>
  )
}; 

export default MemoItem;