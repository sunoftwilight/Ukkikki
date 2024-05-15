import React, { useEffect, useState } from "react";
import folder from "@/assets/Album/folder.png"
import { useParams } from "react-router-dom";
import { useStore } from "zustand";
import { selectModeStore } from "../../stores/AlbumStore";
import { getTrashBin } from "../../api/trash-bin";
import { TrashItemType } from "../../types/TrashType";
import SecureImg from "../Album/SecureImg";
import TrashSelectModeImg from "./TrashSelectModeImg";
import { updateTrashStore } from "../../stores/TrashStore";

const TrashMain: React.FC = () => {
  const { selectMode } = useStore(selectModeStore)
  const [trashList, setTrashList] = useState<TrashItemType[]>([])
  const { needUpdate } = useStore(updateTrashStore)
  
  const { groupPk } = useParams();

  useEffect(() => {
    getTrashBin(
      Number(groupPk),
      (res) => {
        setTrashList(res.data.data)
      },
      (err) => { console.error(err) }
    )
  }, [groupPk, needUpdate])

  return (
    <div>
      <div className="px-4 mt-2 mb-4 rounded-xl py-2 font-pre-R text-red text-base mx-4 bg-soft-gray ">폴더는 단일 선택만 가능합니다</div>
      <div className="grid grid-cols-3 sm:grid-cols-5 md:grid-cols-6 lg:grid-cols-8 xl:grid-cols-10 2xl:grid-cols-12 px-4 gap-1 overflow-scroll scrollbar-hide ">
        {trashList!.map((item, idx) => (
          selectMode ? 
            <TrashSelectModeImg key={idx} item={item} />
          :
            item.type === 'DIRECTORY' ?
              <div key={idx} className="flex flex-col justify-center items-center gap-1">
                <img src={folder} className="w-[82px] h-[65px]" />
                <div className="font-pre-R text-center text-xs">{item.name}</div>
              </div>
            :
              <div key={idx} className="flex justify-center items-center">
                <SecureImg url={item.url} />
              </div>
        ))}
      </div>
    </div>
  )
};

export default TrashMain;