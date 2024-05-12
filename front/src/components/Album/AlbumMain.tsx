import React, { useEffect, useState } from "react";
import folder from "@/assets/Album/folder.png"
import { Link } from "react-router-dom";
import { useStore } from "zustand";
import { DetailImgStore } from "../../stores/DetailImgStore";
import { currentDirStore, selectModeStore } from "../../stores/AlbumStore";
import SelectModeImg from "./SelectModeImg";
import { getDirectory } from "../../api/directory";
import { contentListData } from "../../types/AlbumType";
import SecureImg from "./SecureImg";

const AlbumMain: React.FC = () => {
  const { setCurrentImg } = useStore(DetailImgStore)
  const { selectMode } = useStore(selectModeStore)
  const { currentDir, setCurrentDir, parentDir, setParentDir } = useStore(currentDirStore)

  const [albumList, setAlbumList] = useState<contentListData[]>([])
  
  // const [dirId, setDirId] = useState('c056d136-8409-4b48-9965-49ee216f24202024-05-09T20:37:16.919633749')
  
  useEffect(() => {
    getDirectory(
      currentDir,
      (res) => {
        console.log(res.data.data.contentList)
        setParentDir(res.data.data.parentId)
        setAlbumList(res.data.data.contentList)
      },
      (err) => {
        console.error(err)
      }
    )
  }, [currentDir])


  return (
    <div className="grid grid-cols-3 sm:grid-cols-5 md:grid-cols-6 lg:grid-cols-8 xl:grid-cols-10 2xl:grid-cols-12 px-4 gap-1 overflow-scroll scrollbar-hide ">
      { parentDir!= '' && 
        <div 
          onClick={() => setCurrentDir(parentDir)}
          className="flex flex-col justify-center items-center gap-1"
        >
          <img src={folder} className="w-[82px] h-[65px]" />
          <div className="font-pre-R text-center text-xs">.. /</div>
        </div>
      }
      {albumList!.map((item, idx) => (
        ( item.type === 'DIRECTORY' ?
          <div 
            key={idx} onClick={() => setCurrentDir(item.pk)}
            className="flex flex-col justify-center items-center gap-1"
          >
            <img src={folder} className="w-[82px] h-[65px]" />
            <div className="font-pre-R text-center text-xs">{item.name}</div>
          </div>
          :(
            selectMode ? 
              <SelectModeImg key={idx} item={item} />
            :
              <Link 
                to={`/album/${item.pk}`} state={{url: item.url}}
                key={idx} onClick={() => setCurrentImg(item.pk, item.url)}
                className="flex justify-center items-center"
              >
                {/* <img src={item.url} className="w-[106px] h-[90px] object-cover rounded-lg" /> */}
                <SecureImg />
              </Link>
            
          // </div>
        ))))}
    </div>
  )
};

export default AlbumMain;