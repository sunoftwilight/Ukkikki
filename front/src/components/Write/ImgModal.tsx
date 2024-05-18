import React, { useEffect, useState } from "react";
import { useStore } from "zustand";
import { motion, AnimatePresence } from "framer-motion"

import ModalBackground from "../@commons/ModalBackground";
import { currentDirStore } from "../../stores/AlbumStore";
import { ImgModalProps } from "../../types/ArticleType";
import { ImgListProps } from "../../types/ArticleType";
import { getPartyDetail, getPartyThumb } from "../../api/party";
import { getDirectory } from "../../api/directory";


import backFolder from "@/assets/Album/backFolder.png"
import folder from "@/assets/Album/folder.png"
import check from "@/assets/Feed/yes.png"

import { useParams } from "react-router-dom";
import { userStore } from "../../stores/UserStore";

const ImgModal: React.FC<ImgModalProps> = ({onChoiceDoneBtn, onCancelBtn}) => {
  const isOpen = useState<boolean>(true);
  const { currentDirId, setCurrentDirId, setCurrentDirName, parentDirId, setParentDirId, parentDirName } = useStore(currentDirStore);
  const { groupPk } = useParams();
  const [albumList, setAlbumList] = useState<ImgListProps[]>([]);
  const [ selectImgList, setSelectImgList ] = useState<{photoId: string, src: string}[]>([])
  const keys = useStore(userStore).groupKey;
  useEffect(() => {
    getPartyImg()
  }, [])


  const getPartyImg = async () => {
    const pk = Number(groupPk)
    await getPartyDetail(pk,
      (res) => {
        setCurrentDirId(res.data.data.rootDirId)
        getPartyDir(currentDirId)
      },
      (err) => {
        console.error(err)
      }
    )
  }

  const getPartyDir = async (dirId : string) => {
    await getDirectory(
      dirId, 
      (res) => {
        setParentDirId(res.data.data.parentId)
        const data = res.data.data.contentList.map((item) => {
          return {
            pk: item.pk,
            type: item.type,
            photoId: item.photoId,
            url: item.url,
            isSelect: false,
            name: item.name
          }
        })

        data.forEach((item) => {
          getImg(item.url)
        })

        setAlbumList(data)
      },
      (err) => { console.error(err) }
    )
  }

  const dirHandler = (id: string, name: string) => {
    setCurrentDirId(id)
    setCurrentDirName(name)
  }

  const doneBtnHandler = () => {
    onChoiceDoneBtn(selectImgList)
    onCancelBtn()
  }

  const cancelBtnHandler = () => {
    onCancelBtn()
  }

  const itemClickHandler = (data: ImgListProps) => {
    data.isSelect = !data.isSelect
    setSelectImgList((prevList) => {
      const index = prevList.findIndex((item) => item.photoId === data.pk);
      if (index !== -1) {
        return prevList.filter((item) => item.photoId !== data.pk);
      } else {
        return [...prevList, { photoId: data.pk, src: data.url }];
      }
    });
  }

  const getImg = async (url:string) => {
    const key = keys[Number(groupPk)];
    const opt = {
      "x-amz-server-side-encryption-customer-key": key,
    };
    await getPartyThumb(url, opt,
      () => {},
      (err) => {
        console.log(err)
      }
    )
  }


  
	return (
		<div className="fixed z-10 top-0 left-0 w-full h-full">
			<AnimatePresence>
				{isOpen && (
          <>
            <ModalBackground/>
            <motion.div 
              key='ImgModal'
              className="flex justify-center items-center w-full h-full"
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
            >
              <div className="z-20 w-11/12 h-4/6 bg-white rounded-[15px] p-1 py-5 flex flex-wrap content-between border border-disabled-gray drop-shadow-2xl">
                
                <div key='imgModal' className="grid grid-cols-3 sm:grid-cols-5 md:grid-cols-6 lg:grid-cols-8 xl:grid-cols-10 2xl:grid-cols-12 px-4 gap-1 overflow-scroll scrollbar-hide">
                  { parentDirId!= '' && 
                    <div
                      key={'folder'}
                      onClick={() => dirHandler(parentDirId, parentDirName)}
                      className="flex flex-col justify-center items-center gap-1"
                    >
                      <img src={backFolder} className="w-[82px] h-[65px]" />
                      <div className="font-pre-R text-center text-xs">.. /</div>
                    </div>
                  }
                  {albumList!.map((item) => (
                    ( item.type === 'DIRECTORY' ?
                      <div 
                        key={item.photoId} onClick={() => dirHandler(item.pk, item.name)}
                        className="flex flex-col justify-center items-center gap-1"
                      >
                        <img src={folder} className="w-[82px] h-[65px]" />
                        <div className="font-pre-R text-center text-xs">{item.name}</div>
                      </div>
                      :(
                        <div className="relative">
                          {item.isSelect && (
                            <img src={check} className="z-10 w-10 h-10 object-cover absolute top-1/4 left-1/4"  onClick={() => itemClickHandler(item)}/>
                          )}
                          <img key={item.photoId} src={item.url} className={"w-[90px] h-[80px] object-cover rounded-lg " + (item.isSelect ? 'blur-[1px]' : '')} onClick={() => itemClickHandler(item)}/>

                        </div>
                      )
                    )
                  ))}
                </div>

                <div className="flex gap-4 w-full justify-end">
                  <button 
                    className={`h-[35px] font-gtr-B text-lg text-white rounded-[15px] w-[70px] bg-disabled-gray`} 
                    onClick={() => {cancelBtnHandler()}}
                  >
                    취소
                  </button>
                  <button
                    className={`h-[35px] font-gtr-B text-lg text-white rounded-[15px] w-[70px] bg-main-blue` }
                    onClick={() => {doneBtnHandler()}}
                  >
                    확인
                  </button>
                </div>						
              </div>
            </motion.div>
          </>
      )}
        
			</AnimatePresence>
		</div>
	);
};

export default ImgModal;
