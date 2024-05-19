import React, { useState } from "react";
import { motion, AnimatePresence } from "framer-motion"
import trash from '@/assets/DetailImg/trash.png'
import noHeart from '@/assets/DetailImg/noHeart.png'
import heart from '@/assets/DetailImg/heart.png'
import memo from '@/assets/DetailImg/memo.png'
import download from '@/assets/DetailImg/download.png'
import article from '@/assets/DetailImg/article.png'
import ArticleList from "./ArticleList";
import Memo from "./Memo";
import { downloadFile } from "../../api/file";
import { useStore } from "zustand";
import { currentDirStore, prefixStore, updateAlbumStore } from "../../stores/AlbumStore";
import Modal from "../@commons/Modal";
import { DetailImgStore } from "../../stores/DetailImgStore";
import { getDetailImgDataType } from "../../types/AlbumType";
import { useNavigate, useParams } from "react-router-dom";
import { userStore } from "../../stores/UserStore";

import * as P from "../../api/detailImg"
import { delFiles } from "../../api/directory";

interface NavPropsType {
  info: getDetailImgDataType,
  // 부모 함수 호출하여 좋아요 값 업데이트
  updateLikes : (isLike : boolean) => void
}
const FootNav: React.FC<NavPropsType> = ({ info,updateLikes }) => {
  const [isMemo, setIsMemo] = useState<boolean>(false)
  const [isPrefixOpen, setIsPrefixOpen] = useState(false)
  const [isIng, setIsIng] = useState(false)
  const [isDownDone, setIsDownDone] = useState(false)
  const [isDelete, setIsDelete] = useState(false)

  const { prefix } = useStore(prefixStore)
  const { currentId } = useStore(DetailImgStore)

  const { groupKey } = useStore(userStore);
  const { groupPk } = useParams();

  // 사진 정보 가져오기
  const detail = useStore(DetailImgStore)

  const prefixHandler = async () => {
    setIsPrefixOpen(false)
    setIsIng(true)
    await downloadFile(
      groupKey[Number(groupPk)],
      {
        fileId: currentId,
        prefix: prefix
      },
      (res) => {
        const url = window.URL.createObjectURL(new Blob([res.data]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `${prefix}.png`)
        document.body.appendChild(link)
        link.click()
        doneHandler()
      },
      (err) => { 
        setIsPrefixOpen(true)
        setIsIng(false)

        console.error(err)
        alert('오류가 발생했습니다. 다시 시도하십시오.')
      }
    )
  }
    
  const doneHandler = () => {
    setIsIng(false)
    setIsDownDone(true)
    setNeedUpdate()
  }

  // 좋아요 클릭
  const likeClick = () => {
    // 좋아요 여부 체크
    if(info.isLikes){
      likeDelete();
    }else{
      likeCreate();
    }
  }

  const likeCreate = async () => {
    await P.createLike(
      detail.currentImg,
      () => {
        // 좋아요 값 업데이트
        updateLikes(true);
      },
      (err) => {
        console.log(err);
      }
    )
  }

  const likeDelete = async () => {
    await P.deleteLike(
      detail.currentImg,
      () => {
        // 좋아요 값 업데이트
        updateLikes(false);
      },
      (err) => {
        console.log(err);
      }
    )
  }

  const { currentDirId } = useStore(currentDirStore)
  const { setNeedUpdate } = useStore(updateAlbumStore)
  const { currentImg } = useStore(DetailImgStore)
  const navigate = useNavigate()

  const delHandler = async () => {
    await delFiles(
      currentDirId,
      { 
        data: {
          sseKey: groupKey[Number(groupPk)],
          fileIdList: [currentImg]
        }
      },
      () => {
        doneHandler()
        setNeedUpdate()
        navigate(`/album/${groupPk}`)
      },
      (err) => { 
        console.error(err)
        alert('오류가 발생했습니다. 다시 시도하십시오.')
      }
    )
  }

  return (
    <>
      <AnimatePresence>
        { isMemo && (
          <motion.div
            key='isMemo'
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
          >
            <Memo />
          </motion.div>
        )}

        { isPrefixOpen && (
          <Modal
            key='isPrefixOpen'
            modalItems={{ title: '파일명 설정', content: '', modalType: 'input', btn: 2 }}
            onSubmitBtnClick={() => prefixHandler()}
            onCancelBtnClick={() => setIsPrefixOpen(false)}
          />
        )}
        { isIng && (
          <Modal
            key='isIng'
            modalItems={{ content: '다운로드 진행 중입니다', modalType: 'ing', btn: 0 }}
            onSubmitBtnClick={() => prefixHandler()}
            onCancelBtnClick={() => setIsPrefixOpen(false)}
          />
        )}

        { isDownDone && (
          <Modal
            key='isDownDone'
            modalItems={{ content: '다운로드가 완료되었습니다.', modalType: 'done', btn: 1 }}
            onSubmitBtnClick={() => setIsDownDone(false)}
          />
        )}

        { isDelete && (
          <Modal
            key='isDelete'
            modalItems={{ title: '사진을 삭제합니다', content: '삭제된 사진은 휴지통에 2주간 보관되며 기간 내 복구 가능합니다', modalType: 'warn', btn: 2 }}
            onSubmitBtnClick={() => delHandler()}
            onCancelBtnClick={() => setIsDelete(false)}
          />
        )}
      </AnimatePresence>

      <div className="bg-white w-full h-11 flex items-center justify-between px-4 fixed bottom-11">
        <img src={trash} onClick={() => setIsDelete(true)} className="w-6" />
    
        { info.isLikes ? 
          <img src={heart} onClick={likeClick} className="w-6" /> 
          : <img src={noHeart} onClick={likeClick} className="w-6" />
        }

        <img src={memo} onClick={() => setIsMemo(!isMemo)} className="w-6" />

        <img src={download} onClick={() => setIsPrefixOpen(true)} className="w-5" />
      </div>
    </>
  )
}; 

export default FootNav;