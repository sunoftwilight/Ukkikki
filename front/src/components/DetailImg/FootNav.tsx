import React from "react";
import trash from '@/assets/DetailImg/trash.png'
import noStar from '@/assets/DetailImg/noStar.png'
import star from '@/assets/DetailImg/star.png'
import noHeart from '@/assets/DetailImg/noHeart.png'
import heart from '@/assets/DetailImg/heart.png'
import memo from '@/assets/DetailImg/memo.png'
import option from '@/assets/DetailImg/option.png'

const FootNav: React.FC = () => {
  const isStar: boolean = false
  const isHeart: boolean = true
  const isMemo: boolean = true

  return (
    <div>
      {isMemo && 
        <div>
          
        </div>
      }

      <div className="bg-white w-full h-11 flex items-center justify-between px-4 fixed bottom-0">
        <img src={trash} className="w-6" />
        {isStar ? <img src={star} className="w-6" /> : <img src={noStar} className="w-6" /> }
        {isHeart ? <img src={heart} className="w-6" /> : <img src={noHeart} className="w-6" /> }
        <img src={memo} className="w-6" />
        <img src={option} className="w-6" />
      </div>
    </div>
  )
}; 

export default FootNav;