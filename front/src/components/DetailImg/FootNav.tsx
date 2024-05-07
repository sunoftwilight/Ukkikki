import React, { useState } from "react";
import { motion, AnimatePresence } from "framer-motion"
import trash from '@/assets/DetailImg/trash.png'
import noStar from '@/assets/DetailImg/noStar.png'
import star from '@/assets/DetailImg/star.png'
import noHeart from '@/assets/DetailImg/noHeart.png'
import heart from '@/assets/DetailImg/heart.png'
import memo from '@/assets/DetailImg/memo.png'
import download from '@/assets/DetailImg/download.png'
import article from '@/assets/DetailImg/article.png'
import ArticleList from "./ArticleList";
import Memo from "./Memo";
import { downloadFile } from "../../api/file";

const FootNav: React.FC = () => {
  const [isStar, setIsStar] = useState<boolean>(false)
  const [isHeart, setIsHeart] = useState<boolean>(false)
  const [isArticle, setIsArticle] = useState<boolean>(false)
  const [isMemo, setIsMemo] = useState<boolean>(false)

  const downHandler = () => {
    downloadFile(
      {
        key: '',
        fileId: 0,
        prefix: ''
      },
      (res) => {
        console.log(res)
      },
      (err) => { console.error(err) }
    )
  }

  return (
    <>
      <AnimatePresence>
        { isArticle && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
          >
            <ArticleList />
          </motion.div>
        )}

        { isMemo && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
          >
            <Memo />
          </motion.div>
        )}
      </AnimatePresence>

      <div className="bg-white w-full h-11 flex items-center justify-between px-4 fixed bottom-11">
        <img src={trash} className="w-6" />

        { isStar ? 
          <img src={star} onClick={() => setIsStar(!isStar)} className="w-6" /> 
          : <img src={noStar} onClick={() => setIsStar(!isStar)} className="w-6" />
        }
    
        { isHeart ? 
          <img src={heart} onClick={() => setIsHeart(!isHeart)} className="w-6" /> 
          : <img src={noHeart} onClick={() => setIsHeart(!isHeart)} className="w-6" />
        }

        <img src={memo} onClick={() => setIsMemo(!isMemo)} className="w-6" />

        <img src={download} onClick={() => downHandler()} className="w-5" />

        <img src={article} onClick={() => setIsArticle(!isArticle)} className="w-6" />
      </div>
    </>
  )
}; 

export default FootNav;