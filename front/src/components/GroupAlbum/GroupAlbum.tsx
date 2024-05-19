import React, { useEffect, useRef, useState} from "react";
import heart from "@/assets/Album/heart.png"
import downloaded from "@/assets/Album/downloaded.png"

import { getGroupDetail } from "../../api/party";

import { Link, useParams } from "react-router-dom";

import { imgGroupStore, selectModeStore, updateAlbumStore } from "../../stores/AlbumStore";
import SecureImg from "../Album/SecureImg";
import { DetailImgStore } from "../../stores/DetailImgStore";
import { useStore } from "zustand";
import SelectModeImg from "../Album/SelectModeImg";
import { contentListData } from "../../types/AlbumType";
const GroupAlbum: React.FC = () => {
  const { setCurrentImg } = useStore(DetailImgStore)
  const { selectMode } = useStore(selectModeStore)
  const { needUpdate } = useStore(updateAlbumStore)
  const { groupPk } = useParams();
  const { type, groupName } = imgGroupStore();
  const [ albumList, setAlbumList ] = useState<contentListData[]>([]);
  const isInitialRender = useRef(true);

  useEffect(() => {
    getImgDetail(type, groupName);
  }, []);

  const getImgDetail = async (type:number, groupName:string) => {
    const key = Number(groupPk)
    await getGroupDetail(type, groupName, key,
      (res) => {
        console.log(res.data.data)
        setAlbumList(res.data.data)
      },
      (err) => {
        console.log(err)
      })
  }

  useEffect(() => {
    if (isInitialRender.current) {
      isInitialRender.current = false;
    } else {

    }
  }, [needUpdate])



  return (
    <div className="grid grid-cols-3 sm:grid-cols-5 md:grid-cols-6 lg:grid-cols-8 xl:grid-cols-10 2xl:grid-cols-12 px-4 gap-1 overflow-scroll scrollbar-hide ">
    {albumList!.map((item, idx) => (
      (
        selectMode ? 
          <SelectModeImg key={idx} item={item} />
        :
          <Link 
            to={`/album/detail/${item.pk}/${groupPk}`} state={{url: item.url}}
            key={idx} onClick={() => setCurrentImg(item.photoId, item.pk, item.url)}
            className="flex justify-center items-center"
          >
            <div className="relative">
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
            </div>
          </Link>
        )))}  
    </div>
  )
};

export default GroupAlbum;