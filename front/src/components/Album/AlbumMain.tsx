import React, { useEffect, useState } from "react";
import folder from "@/assets/Album/folder.png"
import heart from "@/assets/Album/heart.png"
import downloaded from "@/assets/Album/downloaded.png"
import backFolder from "@/assets/Album/backFolder.png"
import { Link, useParams } from "react-router-dom";
import { useStore } from "zustand";
import { DetailImgStore } from "../../stores/DetailImgStore";
import { currentDirStore, selectModeStore, updateAlbumStore } from "../../stores/AlbumStore";
import SelectModeImg from "./SelectModeImg";
import { getDirectory } from "../../api/directory";
import { contentListData } from "../../types/AlbumType";
import SecureImg from "./SecureImg";
import { getPartyDetail } from "../../api/party";

const AlbumMain: React.FC = () => {
  const { setCurrentImg } = useStore(DetailImgStore)
  const { selectMode } = useStore(selectModeStore)
  const { needUpdate } = useStore(updateAlbumStore)
  const { currentDirId, setCurrentDirId, setCurrentDirName, parentDirId, setParentDirId, parentDirName } = useStore(currentDirStore)
  const [albumList, setAlbumList] = useState<contentListData[]>([])
  const { groupPk } = useParams();

  useEffect(() => {
    console.log('groupPk', groupPk)
    getPartyDetail(
      Number(groupPk),
      (res) => {
        setCurrentDirId(res.data.data.rootDirId)
        console.log('partyDetail', res.data)

        getDirectory(
          res.data.data.rootDirId,
          (res) => {
            console.log('List', res.data)
            setParentDirId(res.data.data.parentId)
            setAlbumList(res.data.data.contentList)
          },
          (err) => { console.error(err) }
        )
      },
      (err) => { console.error(err) }
    )
  }, [groupPk])

  useEffect(() => {
    getDirectory(
      currentDirId,
      (res) => {
        setParentDirId(res.data.data.parentId)
        setAlbumList(res.data.data.contentList)
        console.log('data', res.data.data)
      },
      (err) => { console.error(err) }
    )
  }, [needUpdate, currentDirId])

  const dirHandler = (id: string, name: string) => {
    setCurrentDirId(id)
    setCurrentDirName(name)
  }

  return (
    <div className="grid grid-cols-3 sm:grid-cols-5 md:grid-cols-6 lg:grid-cols-8 xl:grid-cols-10 2xl:grid-cols-12 px-4 gap-1 overflow-scroll scrollbar-hide ">
      { parentDirId!= '' && 
        <div 
          onClick={() => dirHandler(parentDirId, parentDirName)}
          className="flex flex-col justify-center items-center gap-1"
        >
          <img src={backFolder} className="w-[82px] h-[65px]" />
          <div className="font-pre-R text-center text-xs">.. /</div>
        </div>
      }
      {albumList!.map((item, idx) => (
        ( item.type === 'DIRECTORY' ?
          <div 
            key={idx} onClick={() => dirHandler(item.pk, item.name)}
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
                to={`/album/detail/${item.pk}/${groupPk}`} state={{url: item.url}}
                key={idx} onClick={() => setCurrentImg(item.photoId, item.pk, item.url)}
                className="flex justify-center items-center relative"
              >
                {item.isDownload ? 
                  <img src={downloaded} className="absolute bottom-0 left-0 w-6" />
                  : <></>
                }
                {item.isLikes ? 
                  <div className="absolute bottom-1 right-1">
                    <img className="w-4" src={heart} />
                  </div>
                  : <></>
                }
                <SecureImg url={item.url} />
              </Link>
        ))))}
    </div>
  )
};

export default AlbumMain;