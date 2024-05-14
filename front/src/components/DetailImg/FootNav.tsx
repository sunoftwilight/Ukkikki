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
import { prefixStore } from "../../stores/AlbumStore";
import Modal from "../@commons/Modal";
import { DetailImgStore } from "../../stores/DetailImgStore";
import { getDetailImgDataType } from "../../types/AlbumType";

interface NavPropsType {
  info: getDetailImgDataType
}
const FootNav: React.FC<NavPropsType> = ({ info }) => {
  const [isHeart, setIsHeart] = useState<boolean>(false)
  const [isArticle, setIsArticle] = useState<boolean>(false)
  const [isMemo, setIsMemo] = useState<boolean>(false)
  const [isPrefixOpen, setIsPrefixOpen] = useState(false)
  const [isIng, setIsIng] = useState(false)
  const [isDownDone, setIsDownDone] = useState(false)

  const { prefix } = useStore(prefixStore)
  const { currentId } = useStore(DetailImgStore)

  const prefixHandler = async () => {
    setIsPrefixOpen(false)
    setIsIng(true)
    await downloadFile(
      'XlD0Bazmy98XN59LnysMn0FExeOA6guSmMsC69j/5RE=',
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
  }

  return (
    <>
      <AnimatePresence>
        { isArticle && (
          <motion.div
            key='isArticle'
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
          >
            <ArticleList />
          </motion.div>
        )}

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
            modalItems={{ title: '파일명 수정', content: '', modalType: 'input', btn: 2 }}
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
      </AnimatePresence>

      <div className="bg-white w-full h-11 flex items-center justify-between px-4 fixed bottom-11">
        <img src={trash} className="w-6" />
    
        { info.isLikes ? 
          <img src={heart} onClick={() => setIsHeart(!isHeart)} className="w-6" /> 
          : <img src={noHeart} onClick={() => setIsHeart(!isHeart)} className="w-6" />
        }

        <img src={memo} onClick={() => setIsMemo(!isMemo)} className="w-6" />

        <img src={download} onClick={() => setIsPrefixOpen(true)} className="w-5" />

        <img src={article} onClick={() => setIsArticle(!isArticle)} className="w-6" />
      </div>
    </>
  )
}; 

export default FootNav;